package qub;

import java.io.IOException;
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
    public AsyncFunction<Result<Iterable<Root>>> getRoots()
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
    public AsyncFunction<Result<Iterable<FileSystemEntry>>> getFilesAndFolders(final Path rootedFolderPath)
    {
        return asyncFunction(this, new Function1<AsyncRunner, AsyncFunction<Result<Iterable<FileSystemEntry>>>>()
        {
            @Override
            public AsyncFunction<Result<Iterable<FileSystemEntry>>> run(final AsyncRunner asyncRunner)
            {
                return folderExists(rootedFolderPath)
                    .thenAsyncFunction(new Function1<Result<Boolean>, AsyncFunction<Result<Iterable<FileSystemEntry>>>>()
                    {
                        @Override
                        public AsyncFunction<Result<Iterable<FileSystemEntry>>> run(Result<Boolean> folderExistsResult)
                        {
                            AsyncFunction<Result<Iterable<FileSystemEntry>>> getFilesAndFoldersResult;
                            if (folderExistsResult.hasError())
                            {
                                getFilesAndFoldersResult = Async.error(asyncRunner, folderExistsResult.getError());
                            }
                            else if (!folderExistsResult.getValue())
                            {
                                getFilesAndFoldersResult = Async.error(asyncRunner, new FolderNotFoundException(rootedFolderPath));
                            }
                            else
                            {
                                getFilesAndFoldersResult = asyncRunner.schedule(new Function0<Result<Iterable<FileSystemEntry>>>()
                                {
                                    @Override
                                    public Result<Iterable<FileSystemEntry>> run()
                                    {
                                        Array<FileSystemEntry> filesAndFolders;

                                        final java.io.File containerFile = new java.io.File(rootedFolderPath.toString());
                                        final java.io.File[] containerEntryFiles = containerFile.listFiles();
                                        if (containerEntryFiles == null || containerEntryFiles.length == 0)
                                        {
                                            filesAndFolders = new Array<>(0);
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
                                        }

                                        return Result.<Iterable<FileSystemEntry>>success(filesAndFolders);
                                    }
                                });
                            }
                            return getFilesAndFoldersResult;
                        }
                    });
            }
        });
    }

    @Override
    public AsyncFunction<Result<Boolean>> folderExists(final Path rootedFolderPath)
    {
        return asyncFunction(this, new Function1<AsyncRunner, AsyncFunction<Result<Boolean>>>()
        {
            @Override
            public AsyncFunction<Result<Boolean>> run(AsyncRunner asyncRunner)
            {
                return rootExists(rootedFolderPath)
                    .then(new Function1<Result<Boolean>, Result<Boolean>>()
                    {
                        @Override
                        public Result<Boolean> run(Result<Boolean> rootExistsResult)
                        {
                            Result<Boolean> folderExistsResult;
                            if (rootExistsResult.hasError() || !rootExistsResult.getValue())
                            {
                                folderExistsResult = rootExistsResult;
                            }
                            else
                            {
                                final String folderPathString = rootedFolderPath.toString();
                                final java.io.File folderFile = new java.io.File(folderPathString);
                                folderExistsResult = Result.success(folderFile.exists() && folderFile.isDirectory());
                            }
                            return folderExistsResult;
                        }
                    });
            }
        });
    }

    @Override
    public AsyncFunction<Result<Folder>> createFolder(final Path rootedFolderPath)
    {
        AsyncFunction<Result<Folder>> result;
        if (rootedFolderPath == null)
        {
            result = Async.error(getAsyncRunner(), new IllegalArgumentException("rootedFolderPath cannot be null."));
        }
        else if (!rootedFolderPath.isRooted())
        {
            result = Async.error(getAsyncRunner(), new IllegalArgumentException("rootedFolderPath must be rooted."));
        }
        else
        {
            result = asyncFunction(this, new Function1<AsyncRunner, AsyncFunction<Result<Folder>>>()
            {
                @Override
                public AsyncFunction<Result<Folder>> run(final AsyncRunner asyncRunner)
                {
                    return rootExists(rootedFolderPath)
                        .thenAsyncFunction(new Function1<Result<Boolean>, AsyncFunction<Result<Folder>>>()
                        {
                            @Override
                            public AsyncFunction<Result<Folder>> run(Result<Boolean> rootExistsResult)
                            {
                                AsyncFunction<Result<Folder>> createFolderResult;
                                if (rootExistsResult.hasError())
                                {
                                    createFolderResult = Async.error(asyncRunner, rootExistsResult.getError());
                                }
                                else if (!rootExistsResult.getValue())
                                {
                                    createFolderResult = Async.error(asyncRunner, new RootNotFoundException(rootedFolderPath.getRoot()));
                                }
                                else
                                {
                                    try
                                    {
                                        java.nio.file.Files.createDirectories(java.nio.file.Paths.get(rootedFolderPath.toString()));
                                        createFolderResult = Async.success(asyncRunner, getFolder(rootedFolderPath).getValue());
                                    }
                                    catch (java.nio.file.FileAlreadyExistsException e)
                                    {
                                        createFolderResult = Async.error(asyncRunner, new FolderAlreadyExistsException(rootedFolderPath));
                                    }
                                    catch (java.io.IOException e)
                                    {
                                        createFolderResult = Async.error(asyncRunner, e);
                                    }
                                }
                                return createFolderResult;
                            }
                        });
                }
            });
        }

        return result;
    }

    @Override
    public AsyncFunction<Result<Boolean>> deleteFolder(final Path rootedFolderPath)
    {
        return asyncFunction(this, new Function1<AsyncRunner, AsyncFunction<Result<Boolean>>>()
        {
            @Override
            public AsyncFunction<Result<Boolean>> run(final AsyncRunner asyncRunner)
            {
                return getFilesAndFolders(rootedFolderPath)
                    .thenAsyncFunction(new Function1<Result<Iterable<FileSystemEntry>>, AsyncFunction<Result<Boolean>>>()
                    {
                        @Override
                        public AsyncFunction<Result<Boolean>> run(final Result<Iterable<FileSystemEntry>> getFilesAndFoldersResult)
                        {
                            AsyncFunction<Result<Boolean>> deleteFolderResult;

                            if (getFilesAndFoldersResult.hasError())
                            {
                                deleteFolderResult = asyncRunner.schedule(new Function0<Result<Boolean>>()
                                {
                                    @Override
                                    public Result<Boolean> run()
                                    {
                                        return Result.error(getFilesAndFoldersResult.getError());
                                    }
                                });
                            }
                            else
                            {
                                final List<AsyncFunction<Result<Boolean>>> tasks = new ArrayList<>();

                                for (final FileSystemEntry entry : getFilesAndFoldersResult.getValue())
                                {
                                    if (entry instanceof File)
                                    {
                                        tasks.add(deleteFile(entry.getPath()));
                                    }
                                    else
                                    {
                                        tasks.add(deleteFolder(entry.getPath()));
                                    }
                                }

                                tasks.add(asyncRunner.schedule(new Function0<Result<Boolean>>()
                                {
                                    @Override
                                    public Result<Boolean> run()
                                    {
                                        Result<Boolean> deleteFolderResult;
                                        try
                                        {
                                            java.nio.file.Files.delete(java.nio.file.Paths.get(rootedFolderPath.toString()));
                                            deleteFolderResult = Result.success(true);
                                        }
                                        catch (java.io.IOException e)
                                        {
                                            deleteFolderResult = Result.error(e);
                                        }
                                        return deleteFolderResult;
                                    }
                                }));

                                deleteFolderResult = Async.whenAll(asyncRunner, tasks);
                            }

                            return deleteFolderResult;
                        }
                    });
            }
        });
    }

    @Override
    public AsyncFunction<Result<Boolean>> fileExists(final Path rootedFilePath)
    {
        return async(this, new Function1<AsyncRunner, Result<Boolean>>()
        {
            @Override
            public Result<Boolean> run(AsyncRunner asyncRunner)
            {
                return Result.success(java.nio.file.Files.exists(java.nio.file.Paths.get(rootedFilePath.toString())));
            }
        });
    }

    @Override
    public AsyncFunction<Result<File>> createFile(final Path rootedFilePath)
    {
        return asyncFunction(this, new Function1<AsyncRunner, AsyncFunction<Result<File>>>()
        {
            @Override
            public AsyncFunction<Result<File>> run(final AsyncRunner asyncRunner)
            {
                final Path parentFolderPath = rootedFilePath.getParent();
                return createFolder(parentFolderPath)
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
