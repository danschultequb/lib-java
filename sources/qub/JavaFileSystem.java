package qub;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;

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
    public AsyncFunction<Result<Iterable<Root>>> getRootsAsync()
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return asyncRunner.schedule(new Function0<Result<Iterable<Root>>>()
            {
                @Override
                public Result<Iterable<Root>> run()
                {
                    final java.io.File[] javaRoots = java.io.File.listRoots();
                    return Result.<Iterable<Root>>success(Array.fromValues(javaRoots)
                        .map(new Function1<java.io.File, Root>()
                        {
                            @Override
                            public Root run(java.io.File root)
                            {
                                final String rootPathString = root.getAbsolutePath();
                                final String trimmedRootPathString = rootPathString.equals("/") ? rootPathString : rootPathString.substring(0, rootPathString.length() - 1);
                                return JavaFileSystem.this.getRoot(trimmedRootPathString).getValue();
                            }
                        }));
                }
            })
            .thenOn(currentAsyncRunner);
    }

    @Override
    public AsyncFunction<Result<Iterable<FileSystemEntry>>> getFilesAndFoldersAsync(final Path rootedFolderPath)
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();

        AsyncFunction<Result<Iterable<FileSystemEntry>>> result = FileSystemBase.validateRootedFolderPathAsync(rootedFolderPath);
        if (result == null)
        {
            result = asyncRunner.schedule(new Function0<Result<Iterable<FileSystemEntry>>>()
                {
                    @Override
                    public Result<Iterable<FileSystemEntry>> run()
                    {
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

                            result = Result.<Iterable<FileSystemEntry>>success(filesAndFolders);
                        }

                        return result;
                    }
                })
                .thenOn(currentAsyncRunner);
        }
        return result;
    }

    @Override
    public AsyncFunction<Result<Boolean>> folderExistsAsync(final Path rootedFolderPath)
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();

        AsyncFunction<Result<Boolean>> result = FileSystemBase.validateRootedFolderPathAsync(rootedFolderPath);
        if (result == null)
        {
            result = asyncRunner.schedule(new Function0<Result<Boolean>>()
                {
                    @Override
                    public Result<Boolean> run()
                    {
                        final String folderPathString = rootedFolderPath.toString();
                        final java.io.File folderFile = new java.io.File(folderPathString);
                        return Result.success(folderFile.exists() && folderFile.isDirectory());
                    }
                })
                .thenOn(currentAsyncRunner);
        }
        return result;
    }

    @Override
    public AsyncFunction<Result<Folder>> createFolderAsync(final Path rootedFolderPath)
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();

        AsyncFunction<Result<Folder>> result = FileSystemBase.validateRootedFolderPathAsync(rootedFolderPath);
        if (result == null)
        {
            result = asyncRunner.schedule(new Function0<Result<Folder>>()
                {
                    @Override
                    public Result<Folder> run()
                    {
                        Result<Folder> createFolderResult;
                        try
                        {
                            final Path parentFolderPath = rootedFolderPath.getParent();
                            if (parentFolderPath != null)
                            {
                                java.nio.file.Files.createDirectories(java.nio.file.Paths.get(parentFolderPath.toString()));
                            }
                            java.nio.file.Files.createDirectory(java.nio.file.Paths.get(rootedFolderPath.toString()));
                            createFolderResult = Result.success(getFolder(rootedFolderPath).getValue());
                        }
                        catch (java.nio.file.FileAlreadyExistsException e)
                        {
                            createFolderResult = Result.<Folder>done(getFolder(rootedFolderPath).getValue(), new FolderAlreadyExistsException(rootedFolderPath));
                        }
                        catch (java.io.IOException e)
                        {
                            createFolderResult = Result.<Folder>error(e);
                        }
                        return createFolderResult;
                    }
                })
                .thenOn(currentAsyncRunner);
        }

        return result;
    }

    @Override
    public AsyncFunction<Result<Boolean>> deleteFolderAsync(final Path rootedFolderPath)
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();

        AsyncFunction<Result<Boolean>> result = FileSystemBase.validateRootedFolderPathAsync(rootedFolderPath);
        if (result == null)
        {
            result = asyncRunner.schedule(new Function0<Result<Boolean>>()
                {
                    @Override
                    public Result<Boolean> run()
                    {
                        boolean deleteFolderResult = false;
                        Throwable deleteFolderError;

                        final Result<Iterable<FileSystemEntry>> entriesResult = getFilesAndFolders(rootedFolderPath);
                        if (entriesResult.hasError())
                        {
                            deleteFolderError = entriesResult.getError();
                        }
                        else
                        {
                            final List<Throwable> errors = new ArrayList<Throwable>();
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
                })
                .thenOn(currentAsyncRunner);
        }

        return result;
    }

    @Override
    public AsyncFunction<Result<Boolean>> fileExistsAsync(final Path rootedFilePath)
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();

        AsyncFunction<Result<Boolean>> result = FileSystemBase.validateRootedFilePathAsync(rootedFilePath);
        if (result == null)
        {
            result = asyncRunner.schedule(new Function0<Result<Boolean>>()
                {
                    @Override
                    public Result<Boolean> run()
                    {
                        return Result.success(java.nio.file.Files.isRegularFile(java.nio.file.Paths.get(rootedFilePath.toString())));
                    }
                })
                .thenOn(currentAsyncRunner);
        }
        return result;
    }

    @Override
    public AsyncFunction<Result<File>> createFileAsync(final Path rootedFilePath)
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();

        AsyncFunction<Result<File>> result = FileSystemBase.validateRootedFilePathAsync(rootedFilePath);
        if (result == null)
        {
            result = asyncRunner.schedule(new Function0<Result<File>>()
                {
                    @Override
                    public Result<File> run()
                    {
                        Result<File> createFileResult;

                        final Path parentFolderPath = rootedFilePath.getParent();
                        final Result<Folder> createFolderResult = createFolder(parentFolderPath);
                        if (createFolderResult.getValue() == null)
                        {
                            createFileResult = Result.error(createFolderResult.getError());
                        }
                        else
                        {
                            try
                            {
                                java.nio.file.Files.createFile(java.nio.file.Paths.get(rootedFilePath.toString()));
                                createFileResult = Result.success(getFile(rootedFilePath).getValue());
                            }
                            catch (java.nio.file.FileAlreadyExistsException e)
                            {
                                createFileResult = Result.done(getFile(rootedFilePath).getValue(), new FileAlreadyExistsException(rootedFilePath));
                            }
                            catch (java.io.IOException e)
                            {
                                createFileResult = Result.error(e);
                            }
                        }
                        return createFileResult;
                    }
                })
                .thenOn(currentAsyncRunner);
        }

        return result;
    }

    @Override
    public AsyncFunction<Result<Boolean>> deleteFileAsync(final Path rootedFilePath)
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();

        AsyncFunction<Result<Boolean>> result = FileSystemBase.validateRootedFilePathAsync(rootedFilePath);
        if (result == null)
        {
            result = asyncRunner.schedule(new Function0<Result<Boolean>>()
                {
                    @Override
                    public Result<Boolean> run()
                    {
                        Result<Boolean> deleteFileResult;
                        try
                        {
                            java.nio.file.Files.delete(java.nio.file.Paths.get(rootedFilePath.toString()));
                            deleteFileResult = Result.success(true);
                        }
                        catch (java.nio.file.NoSuchFileException e)
                        {
                            deleteFileResult = Result.done(false, new FileNotFoundException(rootedFilePath));
                        }
                        catch (java.io.IOException e)
                        {
                            deleteFileResult = Result.error(e);
                        }
                        return deleteFileResult;
                    }
                })
                .thenOn(currentAsyncRunner);
        }

        return result;
    }

    @Override
    public AsyncFunction<Result<DateTime>> getFileLastModifiedAsync(final Path rootedFilePath)
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();

        AsyncFunction<Result<DateTime>> result = FileSystemBase.validateRootedFilePathAsync(rootedFilePath);
        if (result == null)
        {
            result = asyncRunner.schedule(new Function0<Result<DateTime>>()
                {
                    @Override
                    public Result<DateTime> run()
                    {
                        Result<DateTime> getFileLastModifiedResult;
                        try
                        {
                            final java.nio.file.attribute.FileTime lastModifiedTime = java.nio.file.Files.getLastModifiedTime(java.nio.file.Paths.get(rootedFilePath.toString()));
                            getFileLastModifiedResult = Result.success(DateTime.local(lastModifiedTime.toMillis()));
                        }
                        catch (java.nio.file.NoSuchFileException e)
                        {
                            getFileLastModifiedResult = Result.error(new FileNotFoundException(rootedFilePath));
                        }
                        catch (java.io.IOException e)
                        {
                            getFileLastModifiedResult = Result.error(e);
                        }
                        return getFileLastModifiedResult;
                    }
                })
                .thenOn(currentAsyncRunner);
        }

        return result;
    }

    @Override
    public AsyncFunction<Result<ByteReadStream>> getFileContentByteReadStreamAsync(final Path rootedFilePath)
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();

        AsyncFunction<Result<ByteReadStream>> result = FileSystemBase.validateRootedFilePathAsync(rootedFilePath);
        if (result == null)
        {
            result = asyncRunner.schedule(new Function0<Result<ByteReadStream>>()
                {
                    @Override
                    public Result<ByteReadStream> run()
                    {
                        Result<ByteReadStream> result;
                        try
                        {
                            result = Result.<ByteReadStream>success(
                                new InputStreamToByteReadStream(
                                    java.nio.file.Files.newInputStream(
                                        java.nio.file.Paths.get(rootedFilePath.toString()))));
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
                })
                .thenOn(currentAsyncRunner);
        }

        return result;
    }

    @Override
    public AsyncFunction<Result<ByteWriteStream>> getFileContentByteWriteStreamAsync(final Path rootedFilePath)
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();

        AsyncFunction<Result<ByteWriteStream>> result = FileSystemBase.validateRootedFilePathAsync(rootedFilePath);
        if (result == null)
        {
            result = asyncRunner.schedule(new Function0<Result<ByteWriteStream>>()
                {
                    @Override
                    public Result<ByteWriteStream> run()
                    {
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

                        return Result.<ByteWriteStream>done(outputStream == null ? null : new OutputStreamToByteWriteStream(outputStream), error);
                    }
                })
                .thenOn(currentAsyncRunner);
        }

        return result;
    }
}
