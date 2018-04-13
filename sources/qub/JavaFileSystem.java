package qub;

import java.io.IOException;
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
        return async(this, new Function1<AsyncRunner, Result<Iterable<Root>>>()
        {
            @Override
            public Result<Iterable<Root>> run(AsyncRunner asyncRunner)
            {
                return Result.<Iterable<Root>>success(Array.fromValues(java.io.File.listRoots())
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
        });
    }

    @Override
    public AsyncFunction<Result<Iterable<FileSystemEntry>>> getFilesAndFoldersAsync(final Path rootedFolderPath)
    {
        final AsyncRunner currentAsyncRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        final AsyncRunner fileSystemAsyncRunner = getAsyncRunner();

        AsyncFunction<Result<Iterable<FileSystemEntry>>> result;
        if (rootedFolderPath == null)
        {
            result = Async.error(currentAsyncRunner, new IllegalArgumentException("rootedFolderPath cannot be null."));
        }
        else if (!rootedFolderPath.isRooted())
        {
            result = Async.error(currentAsyncRunner, new IllegalArgumentException("rootedFolderPath must be rooted."));
        }
        else
        {
            result = fileSystemAsyncRunner.schedule(new Function0<Result<Iterable<FileSystemEntry>>>()
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
        AsyncFunction<Result<Boolean>> result;
        if (rootedFolderPath == null)
        {
            result = Async.error(currentAsyncRunner, new IllegalArgumentException("rootedFolderPath cannot be null."));
        }
        else if (!rootedFolderPath.isRooted())
        {
            result = Async.error(currentAsyncRunner, new IllegalArgumentException("rootedFolderPath must be rooted."));
        }
        else
        {
            final AsyncRunner fileSystemAsyncRunner = getAsyncRunner();
            result = fileSystemAsyncRunner.schedule(new Function0<Result<Boolean>>()
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
        AsyncFunction<Result<Folder>> result;
        if (rootedFolderPath == null)
        {
            result = Async.error(currentAsyncRunner, new IllegalArgumentException("rootedFolderPath cannot be null."));
        }
        else if (!rootedFolderPath.isRooted())
        {
            result = Async.error(currentAsyncRunner, new IllegalArgumentException("rootedFolderPath must be rooted."));
        }
        else
        {
            final AsyncRunner fileSystemAsyncRunner = getAsyncRunner();
            result = fileSystemAsyncRunner.schedule(new Function0<Result<Folder>>()
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

        AsyncFunction<Result<Boolean>> result;
        if (rootedFolderPath == null)
        {
            result = Async.error(currentAsyncRunner, new IllegalArgumentException("rootedFolderPath cannot be null."));
        }
        else if (!rootedFolderPath.isRooted())
        {
            result = Async.error(currentAsyncRunner, new IllegalArgumentException("rootedFolderPath must be rooted."));
        }
        else
        {
            final AsyncRunner fileSystemAsyncRunner = getAsyncRunner();
            result = fileSystemAsyncRunner.schedule(new Function0<Result<Boolean>>()
                {
                    @Override
                    public Result<Boolean> run()
                    {
                        boolean deleteFolderResult = false;
                        Throwable deleteFolderError = null;

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
        AsyncFunction<Result<Boolean>> result;
        if (rootedFilePath == null)
        {
            result = Async.error(currentAsyncRunner, new IllegalArgumentException("rootedFilePath cannot be null."));
        }
        else if (!rootedFilePath.isRooted())
        {
            result = Async.error(currentAsyncRunner, new IllegalArgumentException("rootedFilePath must be rooted."));
        }
        else if (rootedFilePath.endsWith("/"))
        {
            result = Async.error(currentAsyncRunner, new IllegalArgumentException("rootedFilePath cannot end with '/'."));
        }
        else if (rootedFilePath.endsWith("\\"))
        {
            result = Async.error(currentAsyncRunner, new IllegalArgumentException("rootedFilePath cannot end with '\\'."));
        }
        else
        {
            final AsyncRunner fileSystemAsyncRunner = getAsyncRunner();
            result = fileSystemAsyncRunner.schedule(new Function0<Result<Boolean>>()
            {
                @Override
                public Result<Boolean> run()
                {
                    java.nio.file.Files.getAttribute()
                    return Result.success(java.nio.file.Files.exists(java.nio.file.Paths.get(rootedFilePath.toString())));
                }
            });
        }
        return result;
    }

    @Override
    public AsyncFunction<Result<File>> createFileAsync(final Path rootedFilePath)
    {
        return asyncFunction(this, new Function1<AsyncRunner, AsyncFunction<Result<File>>>()
        {
            @Override
            public AsyncFunction<Result<File>> run(final AsyncRunner asyncRunner)
            {
                final Path parentFolderPath = rootedFilePath.getParent();
                return createFolderAsync(parentFolderPath)
                    .then(new Function1<Result<Folder>, Result<File>>()
                    {
                        @Override
                        public Result<File> run(Result<Folder> createFolderResult)
                        {
                            Result<File> createFileResult;
                            if (createFolderResult.hasError() && !(createFolderResult.getError() instanceof FolderAlreadyExistsException))
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
                                    createFileResult = Result.error(new FileAlreadyExistsException(rootedFilePath));
                                }
                                catch (java.io.IOException e)
                                {
                                    createFileResult = Result.error(e);
                                }
                            }
                            return createFileResult;
                        }
                    });
            }
        });
    }

    @Override
    public AsyncFunction<Result<Boolean>> deleteFile(final Path rootedFilePath)
    {
        return asyncFunction(this, new Function1<AsyncRunner, AsyncFunction<Result<Boolean>>>()
        {
            @Override
            public AsyncFunction<Result<Boolean>> run(final AsyncRunner asyncRunner)
            {
                return rootExists(rootedFilePath)
                    .then(new Function1<Result<Boolean>, Result<Boolean>>()
                    {
                        @Override
                        public Result<Boolean> run(Result<Boolean> rootExistsResult)
                        {
                            Result<Boolean> deleteFileResult;
                            if (rootExistsResult.hasError())
                            {
                                deleteFileResult = Result.error(rootExistsResult.getError());
                            }
                            else if (!rootExistsResult.getValue())
                            {
                                deleteFileResult = Result.error(new RootNotFoundException(rootedFilePath.getRoot()));
                            }
                            else
                            {
                                try
                                {
                                    java.nio.file.Files.delete(java.nio.file.Paths.get(rootedFilePath.toString()));
                                    deleteFileResult = Result.success(true);
                                }
                                catch (java.nio.file.NoSuchFileException e)
                                {
                                    deleteFileResult = Result.error(new FileNotFoundException(rootedFilePath));
                                }
                                catch (java.io.IOException e)
                                {
                                    deleteFileResult = Result.error(e);
                                }
                            }
                            return deleteFileResult;
                        }
                    });
            }
        });
    }

    @Override
    public AsyncFunction<Result<DateTime>> getFileLastModified(final Path rootedFilePath)
    {
        return async(this, new Function1<AsyncRunner, Result<DateTime>>()
        {
            @Override
            public Result<DateTime> run(AsyncRunner asyncRunner)
            {
                Result<DateTime> getFileLastModifiedResult;
                try
                {
                    final java.nio.file.attribute.FileTime lastModifiedTime = java.nio.file.Files.getLastModifiedTime(java.nio.file.Paths.get(rootedFilePath.toString()));
                    getFileLastModifiedResult = Result.success(DateTime.local(lastModifiedTime.toMillis()));
                }
                catch (java.io.IOException e)
                {
                    getFileLastModifiedResult = Result.error(e);
                }
                return getFileLastModifiedResult;
            }
        });
    }

    @Override
    public AsyncFunction<Result<ByteReadStream>> getFileContentByteReadStream(final Path rootedFilePath)
    {
        return async(this, new Function1<AsyncRunner, Result<ByteReadStream>>()
        {
            @Override
            public Result<ByteReadStream> run(AsyncRunner arg1)
            {
                Result<ByteReadStream> result;
                try
                {
                    result = Result.<ByteReadStream>success(
                        new InputStreamToByteReadStream(
                            java.nio.file.Files.newInputStream(
                                java.nio.file.Paths.get(rootedFilePath.toString()))));
                }
                catch (java.io.IOException e)
                {
                    result = Result.error(e);
                }
                return result;
            }
        });
    }

    @Override
    public AsyncFunction<Result<ByteWriteStream>> getFileContentByteWriteStream(final Path rootedFilePath)
    {
        return async(this, new Function1<AsyncRunner, Result<ByteWriteStream>>()
        {
            @Override
            public Result<ByteWriteStream> run(AsyncRunner arg1)
            {
                Result<ByteWriteStream> result;
                try
                {
                    result = Result.<ByteWriteStream>success(
                        new OutputStreamToByteWriteStream(
                            java.nio.file.Files.newOutputStream(
                                java.nio.file.Paths.get(rootedFilePath.toString()),
                                StandardOpenOption.CREATE)));
                }
                catch (IOException e)
                {
                    result = Result.error(e);
                }
                return result;
            }
        });
    }
}
