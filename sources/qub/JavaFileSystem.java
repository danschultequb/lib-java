package qub;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * A FileSystem implementation that interacts with a typical Windows, Linux, or MacOS device.
 */
public class JavaFileSystem extends FileSystemBase
{
    private final AsyncRunner asyncRunner;

    JavaFileSystem(AsyncRunner asyncRunner)
    {
        this.asyncRunner = asyncRunner;
    }

    @Override
    public AsyncRunner getAsyncRunner()
    {
        return asyncRunner;
    }

    @Override
    public Result<Iterable<Root>> getRoots()
    {
        final Iterable<java.io.File> javaRoots = Array.fromValues(java.io.File.listRoots());
        return Result.success(javaRoots.map((java.io.File root) ->
        {
            final String rootPathString = root.getAbsolutePath();
            final String trimmedRootPathString = rootPathString.equals("/") ? rootPathString : rootPathString.substring(0, rootPathString.length() - 1);
            return JavaFileSystem.this.getRoot(trimmedRootPathString).getValue();
        }));
    }

    @Override
    public Result<Iterable<FileSystemEntry>> getFilesAndFolders(Path rootedFolderPath)
    {
        validateRootedFolderPath(rootedFolderPath);

        Result<Iterable<FileSystemEntry>> result;
        Array<FileSystemEntry> filesAndFolders;

        final java.io.File containerFile = new java.io.File(rootedFolderPath.toString());
        final java.io.File[] containerEntryFiles = containerFile.listFiles();
        if (containerEntryFiles == null)
        {
            result = Result.error(new FolderNotFoundException(rootedFolderPath));
        }
        else
        {
            final List<Folder> folders = new ArrayList<>();
            final List<File> files = new ArrayList<>();
            for (final java.io.File containerEntryFile : containerEntryFiles)
            {
                final String containerEntryPathString = containerEntryFile.getAbsolutePath();
                final Path containerEntryPath = Path.parse(containerEntryPathString).normalize();
                if (containerEntryFile.isFile())
                {
                    files.add(getFile(containerEntryPath).getValue());
                }
                else if (containerEntryFile.isDirectory())
                {
                    folders.add(getFolder(containerEntryPath).getValue());
                }
            }

            filesAndFolders = new Array<>(containerEntryFiles.length);
            final int foldersCount = folders.getCount();
            for (int i = 0; i < foldersCount; ++i)
            {
                filesAndFolders.set(i, folders.get(i));
            }
            for (int i = 0; i < files.getCount(); ++i)
            {
                filesAndFolders.set(i + foldersCount, files.get(i));
            }

            result = Result.success(filesAndFolders);
        }
        return result;
    }

    @Override
    public Result<Boolean> folderExists(Path rootedFolderPath)
    {
        validateRootedFolderPath(rootedFolderPath);

        final String folderPathString = rootedFolderPath.toString();
        final java.io.File folderFile = new java.io.File(folderPathString);
        return Result.success(folderFile.exists() && folderFile.isDirectory());
    }

    @Override
    public Result<Folder> createFolder(Path rootedFolderPath)
    {
        validateRootedFolderPath(rootedFolderPath);

        Result<Folder> result;
        try
        {
            final Path parentFolderPath = rootedFolderPath.getParent();
            if (parentFolderPath != null)
            {
                java.nio.file.Files.createDirectories(java.nio.file.Paths.get(parentFolderPath.toString()));
            }
            java.nio.file.Files.createDirectory(java.nio.file.Paths.get(rootedFolderPath.toString()));
            result = Result.success(getFolder(rootedFolderPath).getValue());
        }
        catch (java.nio.file.FileAlreadyExistsException e)
        {
            result = Result.done(getFolder(rootedFolderPath).getValue(), new FolderAlreadyExistsException(rootedFolderPath));
        }
        catch (java.io.IOException e)
        {
            result = Result.error(e);
        }

        return result;
    }

    @Override
    public Result<Boolean> deleteFolder(Path rootedFolderPath)
    {
        validateRootedFolderPath(rootedFolderPath);

        boolean deleteFolderResult = false;
        Throwable deleteFolderError;

        final Result<Iterable<FileSystemEntry>> entriesResult = getFilesAndFolders(rootedFolderPath);
        if (entriesResult.hasError())
        {
            deleteFolderError = entriesResult.getError();
        }
        else
        {
            final List<Throwable> errors = new ArrayList<>();
            for (final FileSystemEntry entry : entriesResult.getValue())
            {
                final Result<Boolean> deleteEntryResult = entry.delete();
                if (deleteEntryResult.hasError())
                {
                    final Throwable error = deleteEntryResult.getError();
                    if (error instanceof ErrorIterable)
                    {
                        errors.addAll((ErrorIterable)error);
                    }
                    else
                    {
                        errors.add(error);
                    }
                }
            }

            try
            {
                Files.delete(Paths.get(rootedFolderPath.toString()));
                deleteFolderResult = true;
            }
            catch (java.io.FileNotFoundException e)
            {
                errors.add(new FolderNotFoundException(rootedFolderPath));
            }
            catch (Throwable error)
            {
                errors.add(error);
            }

            deleteFolderError = ErrorIterable.from(errors);
        }

        return Result.done(deleteFolderResult, deleteFolderError);
    }

    @Override
    public Result<Boolean> fileExists(Path rootedFilePath)
    {
        validateRootedFilePath(rootedFilePath);

        return Result.success(java.nio.file.Files.isRegularFile(java.nio.file.Paths.get(rootedFilePath.toString())));
    }

    @Override
    public Result<File> createFile(Path rootedFilePath)
    {
        validateRootedFilePath(rootedFilePath);

        Result<File> result;
        final Path parentFolderPath = rootedFilePath.getParent();
        final Result<Folder> createFolderResult = createFolder(parentFolderPath);
        if (createFolderResult.getValue() == null)
        {
            result = Result.error(createFolderResult.getError());
        }
        else
        {
            try
            {
                java.nio.file.Files.createFile(java.nio.file.Paths.get(rootedFilePath.toString()));
                result = Result.success(getFile(rootedFilePath).getValue());
            }
            catch (java.nio.file.FileAlreadyExistsException e)
            {
                result = Result.done(getFile(rootedFilePath).getValue(), new FileAlreadyExistsException(rootedFilePath));
            }
            catch (java.io.IOException e)
            {
                result = Result.error(e);
            }
        }

        return result;
    }

    @Override
    public Result<Boolean> deleteFile(Path rootedFilePath)
    {
        validateRootedFilePath(rootedFilePath);

        Result<Boolean> result;
        try
        {
            java.nio.file.Files.delete(java.nio.file.Paths.get(rootedFilePath.toString()));
            result = Result.success(true);
        }
        catch (java.nio.file.NoSuchFileException e)
        {
            result = Result.done(false, new FileNotFoundException(rootedFilePath));
        }
        catch (java.io.IOException e)
        {
            result = Result.error(e);
        }

        return result;
    }

    @Override
    public Result<DateTime> getFileLastModified(Path rootedFilePath)
    {
        validateRootedFilePath(rootedFilePath);

        Result<DateTime> result;
        try
        {
            final java.nio.file.attribute.FileTime lastModifiedTime = java.nio.file.Files.getLastModifiedTime(java.nio.file.Paths.get(rootedFilePath.toString()));
            result = Result.success(DateTime.local(lastModifiedTime.toMillis()));
        }
        catch (java.nio.file.NoSuchFileException e)
        {
            result = Result.error(new FileNotFoundException(rootedFilePath));
        }
        catch (java.io.IOException e)
        {
            result = Result.error(e);
        }

        return result;
    }

    @Override
    public Result<ByteReadStream> getFileContentByteReadStream(Path rootedFilePath)
    {
        validateRootedFilePath(rootedFilePath);
        Result<ByteReadStream> result;
        try
        {
            final InputStream fileContentsInputStream = java.nio.file.Files.newInputStream(
                java.nio.file.Paths.get(rootedFilePath.toString()));
            result = Result.success(new InputStreamToByteReadStream(fileContentsInputStream, asyncRunner));
        }
        catch (java.nio.file.NoSuchFileException e)
        {
            result = Result.error(new FileNotFoundException(rootedFilePath));
        }
        catch (java.io.IOException e)
        {
            result = Result.error(e);
        }

        return result;
    }

    @Override
    public Result<ByteWriteStream> getFileContentByteWriteStream(Path rootedFilePath)
    {
        validateRootedFilePath(rootedFilePath);

        OutputStream outputStream = null;
        Throwable error = null;
        try
        {
            outputStream =
                java.nio.file.Files.newOutputStream(
                    java.nio.file.Paths.get(rootedFilePath.toString()),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        }
        catch (java.nio.file.NoSuchFileException e)
        {
            final Result<Folder> createParentFolderResult = createFolder(rootedFilePath.getParent());
            if (createParentFolderResult.getValue() != null)
            {
                try
                {
                    outputStream =
                        Files.newOutputStream(
                            Paths.get(rootedFilePath.toString()),
                            StandardOpenOption.CREATE,
                            StandardOpenOption.TRUNCATE_EXISTING);
                }
                catch (IOException e1)
                {
                    error = e1;
                }
            }
        }
        catch (IOException e)
        {
            error = e;
        }

        return Result.done(outputStream == null ? null : new OutputStreamToByteWriteStream(outputStream), error);
    }
}
