package qub;

/**
 * An implementation of FileSystem that is scoped to a provided folder.
 */
public class FolderFileSystem implements FileSystem
{
    private final FileSystem innerFileSystem;
    private final Path baseFolderPath;

    private FolderFileSystem(FileSystem innerFileSystem, Path baseFolderPath)
    {
        FileSystem.validateRootedFolderPath(baseFolderPath, "baseFolderPath");

        this.innerFileSystem = innerFileSystem;

        Path normalizedBaseFolderPath = baseFolderPath.normalize();
        if (normalizedBaseFolderPath.endsWith("/"))
        {
            final String normalizedBaseFolderPathString = normalizedBaseFolderPath.toString();
            normalizedBaseFolderPath = Path.parse(normalizedBaseFolderPathString.substring(0, normalizedBaseFolderPathString.length() - 1));
        }
        this.baseFolderPath = normalizedBaseFolderPath;
    }

    public static Result<FolderFileSystem> get(FileSystem innerFileSystem, String baseFolderPath)
    {
        FileSystem.validateRootedFolderPath(baseFolderPath, "baseFolderPath");

        return get(innerFileSystem, Path.parse(baseFolderPath));
    }

    public static Result<FolderFileSystem> get(FileSystem innerFileSystem, Path baseFolderPath)
    {
        FileSystem.validateRootedFolderPath(baseFolderPath, "baseFolderPath");

        return Result.success(new FolderFileSystem(innerFileSystem, baseFolderPath));
    }

    public Result<Void> create()
    {
        return innerFileSystem.createFolder(baseFolderPath)
            .then(() -> {});
    }

    public AsyncFunction<Result<Void>> createAsync()
    {
        PreCondition.assertNotNull(getAsyncRunner(), "getAsyncRunner()");

        return getAsyncRunner().scheduleSingle(this::create);
    }

    public Result<Void> delete()
    {
        return innerFileSystem.deleteFolder(baseFolderPath);
    }

    public AsyncFunction<Result<Void>> deleteAsync()
    {
        return innerFileSystem.deleteFolderAsync(baseFolderPath);
    }

    public Result<Boolean> exists()
    {
        return innerFileSystem.folderExists(baseFolderPath);
    }

    public AsyncFunction<Result<Boolean>> existsAsync()
    {
        return innerFileSystem.folderExistsAsync(baseFolderPath);
    }

    public Path getBaseFolderPath()
    {
        return baseFolderPath;
    }

    private Result<Path> getInnerPath(Path outerPath, boolean isFolderPath)
    {
        FileSystem.validateRootedFolderPath(outerPath, "outerPath");

        Result<Path> result = outerPath.resolve();
        if (!result.hasError())
        {
            final Path outerPathRoot = outerPath.getRoot().throwErrorOrGetValue();
            if (!outerPathRoot.equals(Path.parse("/")))
            {
                if (isFolderPath)
                {
                    result = Result.error(new FolderNotFoundException(outerPath));
                }
                else
                {
                    result = Result.error(new FileNotFoundException(outerPath));
                }
            }
            else
            {
                result = outerPath.withoutRoot()
                    .thenResult((Path outerPathWithoutRoot) -> baseFolderPath.resolve(outerPathWithoutRoot))
                    .catchError(NotFoundException.class, () -> baseFolderPath);
            }
        }
        return result;
    }

    private Path getOuterPath(Path innerPath)
    {
        String outerPathString = innerPath.toString().substring(baseFolderPath.toString().length());
        if (outerPathString.isEmpty())
        {
            outerPathString = "/";
        }
        return Path.parse(outerPathString);
    }

    @Override
    public AsyncRunner getAsyncRunner()
    {
        return innerFileSystem.getAsyncRunner();
    }

    @Override
    public Result<Iterable<Root>> getRoots()
    {
        return Result.success(Iterable.create(new Root(this, Path.parse("/"))));
    }

    @Override
    public Result<Iterable<FileSystemEntry>> getFilesAndFolders(Path rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        Iterable<FileSystemEntry> resultEntries = null;
        Throwable resultError = null;

        final Result<Path> innerFolderPath = getInnerPath(rootedFolderPath, true);
        if (innerFolderPath.hasError())
        {
            resultError = innerFolderPath.getError();
        }
        else
        {
            final Result<Iterable<FileSystemEntry>> innerResult = innerFileSystem.getFilesAndFolders(innerFolderPath.getValue());

            final Iterable<FileSystemEntry> innerEntries = innerResult.getValue();
            resultEntries = innerEntries == null ? null : innerEntries.map((FileSystemEntry innerEntry) ->
            {
                final Path outerEntryPath = FolderFileSystem.this.getOuterPath(innerEntry.getPath());
                return innerEntry instanceof File ? new File(FolderFileSystem.this, outerEntryPath) : new Folder(FolderFileSystem.this, outerEntryPath);
            });

            final Throwable innerError = innerResult.getError();
            if (innerError instanceof FolderNotFoundException)
            {
                final Path outerPath = getOuterPath(((FolderNotFoundException)innerError).getFolderPath());
                resultError = new FolderNotFoundException(outerPath);
            }
            else
            {
                resultError = innerError;
            }
        }
        return resultError == null
            ? Result.success(resultEntries)
            : Result.error(resultError);
    }

    @Override
    public Result<Boolean> folderExists(Path rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        final Result<Path> innerPath = getInnerPath(rootedFolderPath, true);
        return innerPath.hasError() ? Result.error(innerPath.getError()) : innerFileSystem.folderExists(innerPath.getValue());
    }

    @Override
    public Result<Folder> createFolder(Path rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        Result<Folder> result;
        final Result<Path> innerPath = getInnerPath(rootedFolderPath, true);
        if (innerPath.hasError())
        {
            result = Result.error(innerPath.getError());
        }
        else
        {
            final Result<Folder> innerResult = innerFileSystem.createFolder(innerPath.getValue());

            final Folder createdFolder = innerResult.getValue();
            final Folder resultFolder = (createdFolder == null ? null : new Folder(FolderFileSystem.this, getOuterPath(createdFolder.getPath())));

            final Throwable error = innerResult.getError();
            Throwable resultError;
            if (error instanceof FolderAlreadyExistsException)
            {
                final Path outerPath = getOuterPath(((FolderAlreadyExistsException)error).getFolderPath());
                resultError = new FolderAlreadyExistsException(outerPath);
            }
            else
            {
                resultError = error;
            }

            result = resultError == null
                ? Result.success(resultFolder)
                : Result.error(resultError);
        }

        return result;
    }

    @Override
    public Result<Void> deleteFolder(Path rootedFolderPath)
    {
        FileSystem.validateRootedFolderPath(rootedFolderPath);

        Result<Void> result;
        final Result<Path> innerFolderPath = getInnerPath(rootedFolderPath, true);
        if (innerFolderPath.hasError())
        {
            result = Result.error(innerFolderPath.getError());
        }
        else if (innerFolderPath.getValue().equals(baseFolderPath))
        {
            result = Result.error(new IllegalArgumentException("Cannot delete a root folder (" + rootedFolderPath.resolve().getValue() + ")."));
        }
        else
        {
            result = innerFileSystem.deleteFolder(innerFolderPath.getValue());
            if (result.getError() instanceof FolderNotFoundException)
            {
                final Path outerFolderPath = getOuterPath(((FolderNotFoundException)result.getError()).getFolderPath());
                result = Result.error(new FolderNotFoundException(outerFolderPath));
            }
        }

        return result;
    }

    @Override
    public Result<Boolean> fileExists(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        Result<Boolean> result;

        final Result<Path> innerFilePath = getInnerPath(rootedFilePath, false);
        if (innerFilePath.hasError())
        {
            result = Result.error(innerFilePath.getError());
        }
        else
        {
            result = innerFileSystem.fileExists(innerFilePath.getValue());
        }

        return result;
    }

    @Override
    public Result<File> createFile(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        Result<File> result;
        final Result<Path> innerFilePath = getInnerPath(rootedFilePath, false);
        if (innerFilePath.hasError())
        {
            result = Result.error(innerFilePath.getError());
        }
        else
        {
            final Result<File> innerResult = innerFileSystem.createFile(innerFilePath.getValue());

            final File createdFile = innerResult.getValue() == null ? null : new File(this, getOuterPath(innerResult.getValue().getPath()));
            final Throwable error = innerResult.getError() instanceof FileAlreadyExistsException ? new FileAlreadyExistsException(rootedFilePath) : innerResult.getError();
            result = error == null
                ? Result.success(createdFile)
                : Result.error(error);
        }

        return result;
    }

    @Override
    public Result<Void> deleteFile(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        Result<Void> result;
        final Result<Path> innerFilePath = getInnerPath(rootedFilePath, false);
        if (innerFilePath.hasError())
        {
            result = Result.error(innerFilePath.getError());
        }
        else
        {
            result = innerFileSystem.deleteFile(innerFilePath.getValue());
            if (result.getError() instanceof FileNotFoundException)
            {
                result = Result.error(new FileNotFoundException(getOuterPath(innerFilePath.getValue())));
            }
        }

        return result;
    }

    @Override
    public Result<DateTime> getFileLastModified(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        Result<DateTime> result;
        final Result<Path> innerFilePath = getInnerPath(rootedFilePath, false);
        if (innerFilePath.hasError())
        {
            result = Result.error(innerFilePath.getError());
        }
        else
        {
            result = innerFileSystem.getFileLastModified(innerFilePath.getValue());
            if (result.getError() instanceof FileNotFoundException)
            {
                result = Result.error(new FileNotFoundException(getOuterPath(innerFilePath.getValue())));
            }
        }

        return result;
    }

    @Override
    public Result<ByteReadStream> getFileContentByteReadStream(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        Result<ByteReadStream> result;
        final Result<Path> innerFilePath = getInnerPath(rootedFilePath, false);
        if (innerFilePath.hasError())
        {
            result = Result.error(innerFilePath.getError());
        }
        else
        {
            result = innerFileSystem.getFileContentByteReadStream(innerFilePath.getValue());
            if (result.getError() instanceof FileNotFoundException)
            {
                result = Result.error(new FileNotFoundException(getOuterPath(innerFilePath.getValue())));
            }
        }

        return result;
    }

    @Override
    public Result<ByteWriteStream> getFileContentByteWriteStream(Path rootedFilePath)
    {
        FileSystem.validateRootedFilePath(rootedFilePath);

        Result<ByteWriteStream> result;
        final Result<Path> innerFilePath = getInnerPath(rootedFilePath, false);
        if (innerFilePath.hasError())
        {
            result = Result.error(innerFilePath.getError());
        }
        else
        {
            result = innerFileSystem.getFileContentByteWriteStream(innerFilePath.getValue());
        }

        return result;
    }
}
