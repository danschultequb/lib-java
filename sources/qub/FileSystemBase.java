package qub;

public abstract class FileSystemBase implements FileSystem
{
    @Override
    public boolean rootExists(String rootPath)
    {
        return FileSystemBase.rootExists(this, rootPath);
    }

    @Override
    public boolean rootExists(String rootPath, Action1<String> onError)
    {
        return FileSystemBase.rootExists(this, rootPath, onError);
    }

    @Override
    public boolean rootExists(Path rootPath)
    {
        return FileSystemBase.rootExists(this, rootPath);
    }

    @Override
    public boolean rootExists(Path rootPath, Action1<String> onError)
    {
        return FileSystemBase.rootExists(this, rootPath, onError);
    }

    @Override
    public AsyncFunction<Boolean> rootExistsAsync(String rootPath)
    {
        return FileSystemBase.rootExistsAsync(this, rootPath);
    }

    @Override
    public AsyncFunction<Boolean> rootExistsAsync(String rootPath, Action1<String> onError)
    {
        return FileSystemBase.rootExistsAsync(this, rootPath, onError);
    }

    @Override
    public AsyncFunction<Boolean> rootExistsAsync(Path rootPath)
    {
        return FileSystemBase.rootExistsAsync(this, rootPath);
    }

    @Override
    public AsyncFunction<Boolean> rootExistsAsync(Path rootPath, Action1<String> onError)
    {
        return FileSystemBase.rootExistsAsync(this, rootPath, onError);
    }

    @Override
    public Root getRoot(String rootPath)
    {
        return FileSystemBase.getRoot(this, rootPath);
    }

    @Override
    public Root getRoot(Path rootPath)
    {
        return FileSystemBase.getRoot(this, rootPath);
    }

    @Override
    public Iterable<Root> getRoots()
    {
        return FileSystemBase.getRoots(this);
    }

    @Override
    public AsyncFunction<Iterable<Root>> getRootsAsync()
    {
        return FileSystemBase.getRootsAsync(this);
    }

    @Override
    public AsyncFunction<Iterable<Root>> getRootsAsync(Action1<String> onError)
    {
        return FileSystemBase.getRootsAsync(this, onError);
    }

    @Override
    public Iterable<FileSystemEntry> getFilesAndFolders(String folderPath)
    {
        return FileSystemBase.getFilesAndFolders(this, folderPath);
    }

    @Override
    public Iterable<FileSystemEntry> getFilesAndFolders(String folderPath, Action1<String> onError)
    {
        return FileSystemBase.getFilesAndFolders(this, folderPath, onError);
    }

    @Override
    public Iterable<FileSystemEntry> getFilesAndFolders(Path folderPath)
    {
        return FileSystemBase.getFilesAndFolders(this, folderPath);
    }

    @Override
    public Iterable<FileSystemEntry> getFilesAndFoldersRecursively(String folderPath)
    {
        return FileSystemBase.getFilesAndFoldersRecursively(this, folderPath);
    }

    @Override
    public Iterable<FileSystemEntry> getFilesAndFoldersRecursively(String folderPath, Action1<String> onError)
    {
        return FileSystemBase.getFilesAndFoldersRecursively(this, folderPath, onError);
    }

    @Override
    public Iterable<FileSystemEntry> getFilesAndFoldersRecursively(Path folderPath)
    {
        return FileSystemBase.getFilesAndFoldersRecursively(this, folderPath);
    }

    @Override
    public Iterable<FileSystemEntry> getFilesAndFoldersRecursively(Path folderPath, Action1<String> onError)
    {
        return FileSystemBase.getFilesAndFoldersRecursively(this, folderPath, onError);
    }

    @Override
    public AsyncFunction<Iterable<FileSystemEntry>> getFilesAndFoldersAsync(String folderPath)
    {
        return FileSystemBase.getFilesAndFoldersAsync(this, folderPath);
    }

    @Override
    public AsyncFunction<Iterable<FileSystemEntry>> getFilesAndFoldersAsync(String folderPath, Action1<String> onError)
    {
        return FileSystemBase.getFilesAndFoldersAsync(this, folderPath, onError);
    }

    @Override
    public AsyncFunction<Iterable<FileSystemEntry>> getFilesAndFoldersAsync(Path folderPath)
    {
        return FileSystemBase.getFilesAndFoldersAsync(this, folderPath);
    }

    @Override
    public AsyncFunction<Iterable<FileSystemEntry>> getFilesAndFoldersAsync(Path folderPath, Action1<String> onError)
    {
        return FileSystemBase.getFilesAndFoldersAsync(this, folderPath, onError);
    }

    @Override
    public Iterable<Folder> getFolders(String folderPath)
    {
        return FileSystemBase.getFolders(this, folderPath);
    }

    @Override
    public Iterable<Folder> getFolders(String folderPath, Action1<String> onError)
    {
        return FileSystemBase.getFolders(this, folderPath, onError);
    }

    @Override
    public Iterable<Folder> getFolders(Path folderPath)
    {
        return FileSystemBase.getFolders(this, folderPath);
    }

    @Override
    public Iterable<Folder> getFolders(Path folderPath, Action1<String> onError)
    {
        return FileSystemBase.getFolders(this, folderPath, onError);
    }

    @Override
    public Iterable<Folder> getFoldersRecursively(String folderPath)
    {
        return FileSystemBase.getFoldersRecursively(this, folderPath);
    }

    @Override
    public Iterable<Folder> getFoldersRecursively(String folderPath, Action1<String> onError)
    {
        return FileSystemBase.getFoldersRecursively(this, folderPath, onError);
    }

    @Override
    public Iterable<Folder> getFoldersRecursively(Path folderPath)
    {
        return FileSystemBase.getFoldersRecursively(this, folderPath);
    }

    @Override
    public Iterable<Folder> getFoldersRecursively(Path folderPath, Action1<String> onError)
    {
        return FileSystemBase.getFoldersRecursively(this, folderPath, onError);
    }

    @Override
    public AsyncFunction<Iterable<Folder>> getFoldersAsync(String folderPath)
    {
        return FileSystemBase.getFoldersAsync(this, folderPath);
    }

    @Override
    public AsyncFunction<Iterable<Folder>> getFoldersAsync(String folderPath, Action1<String> onError)
    {
        return FileSystemBase.getFoldersAsync(this, folderPath, onError);
    }

    @Override
    public AsyncFunction<Iterable<Folder>> getFoldersAsync(Path folderPath)
    {
        return FileSystemBase.getFoldersAsync(this, folderPath);
    }

    @Override
    public AsyncFunction<Iterable<Folder>> getFoldersAsync(Path folderPath, Action1<String> onError)
    {
        return FileSystemBase.getFoldersAsync(this, folderPath, onError);
    }

    @Override
    public Iterable<File> getFiles(String folderPath)
    {
        return FileSystemBase.getFiles(this, folderPath);
    }

    @Override
    public Iterable<File> getFiles(String folderPath, Action1<String> onError)
    {
        return FileSystemBase.getFiles(this, folderPath, onError);
    }

    @Override
    public Iterable<File> getFiles(Path folderPath)
    {
        return FileSystemBase.getFiles(this, folderPath);
    }

    @Override
    public Iterable<File> getFiles(Path folderPath, Action1<String> onError)
    {
        return FileSystemBase.getFiles(this, folderPath, onError);
    }

    @Override
    public Iterable<File> getFilesRecursively(String folderPath)
    {
        return FileSystemBase.getFilesRecursively(this, folderPath);
    }

    @Override
    public Iterable<File> getFilesRecursively(String folderPath, Action1<String> onError)
    {
        return FileSystemBase.getFilesRecursively(this, folderPath, onError);
    }

    @Override
    public Iterable<File> getFilesRecursively(Path folderPath)
    {
        return FileSystemBase.getFilesRecursively(this, folderPath);
    }

    @Override
    public Iterable<File> getFilesRecursively(Path folderPath, Action1<String> onError)
    {
        return FileSystemBase.getFilesRecursively(this, folderPath, onError);
    }

    @Override
    public AsyncFunction<Iterable<File>> getFilesAsync(String folderPath)
    {
        return FileSystemBase.getFilesAsync(this, folderPath);
    }

    @Override
    public AsyncFunction<Iterable<File>> getFilesAsync(String folderPath, Action1<String> onError)
    {
        return FileSystemBase.getFilesAsync(this, folderPath, onError);
    }

    @Override
    public AsyncFunction<Iterable<File>> getFilesAsync(Path folderPath)
    {
        return FileSystemBase.getFilesAsync(this, folderPath);
    }

    @Override
    public AsyncFunction<Iterable<File>> getFilesAsync(Path folderPath, Action1<String> onError)
    {
        return FileSystemBase.getFilesAsync(this, folderPath, onError);
    }

    @Override
    public Folder getFolder(String folderPath)
    {
        return FileSystemBase.getFolder(this, folderPath);
    }

    @Override
    public Folder getFolder(Path folderPath)
    {
        return FileSystemBase.getFolder(this, folderPath);
    }

    @Override
    public boolean folderExists(String folderPath)
    {
        return FileSystemBase.folderExists(this, folderPath);
    }

    @Override
    public boolean folderExists(String folderPath, Action1<String> onError)
    {
        return FileSystemBase.folderExists(this, folderPath, onError);
    }

    @Override
    public boolean folderExists(Path folderPath)
    {
        return FileSystemBase.folderExists(this, folderPath);
    }

    @Override
    public AsyncFunction<Boolean> folderExistsAsync(String folderPath)
    {
        return FileSystemBase.folderExistsAsync(this, folderPath);
    }

    @Override
    public AsyncFunction<Boolean> folderExistsAsync(String folderPath, Action1<String> onError)
    {
        return FileSystemBase.folderExistsAsync(this, folderPath, onError);
    }

    @Override
    public AsyncFunction<Boolean> folderExistsAsync(Path folderPath)
    {
        return FileSystemBase.folderExistsAsync(this, folderPath);
    }

    @Override
    public AsyncFunction<Boolean> folderExistsAsync(Path folderPath, Action1<String> onError)
    {
        return FileSystemBase.folderExistsAsync(this, folderPath, onError);
    }

    @Override
    public boolean createFolder(String folderPath)
    {
        return FileSystemBase.createFolder(this, folderPath);
    }

    @Override
    public boolean createFolder(String folderPath, Action1<String> onError)
    {
        return FileSystemBase.createFolder(this, folderPath, onError);
    }

    @Override
    public boolean createFolder(String folderPath, Out<Folder> outputFolder)
    {
        return FileSystemBase.createFolder(this, folderPath, outputFolder);
    }

    @Override
    public boolean createFolder(String folderPath, Out<Folder> outputFolder, Action1<String> onError)
    {
        return FileSystemBase.createFolder(this, folderPath, outputFolder, onError);
    }

    @Override
    public boolean createFolder(Path folderPath)
    {
        return FileSystemBase.createFolder(this, folderPath);
    }

    @Override
    public boolean createFolder(Path folderPath, Action1<String> onError)
    {
        return FileSystemBase.createFolder(this, folderPath, onError);
    }

    @Override
    public boolean createFolder(Path folderPath, Out<Folder> outputFolder)
    {
        return FileSystemBase.createFolder(this, folderPath, outputFolder);
    }

    @Override
    public AsyncFunction<Boolean> createFolderAsync(String folderPath)
    {
        return FileSystemBase.createFolderAsync(this, folderPath);
    }

    @Override
    public AsyncFunction<Boolean> createFolderAsync(String folderPath, Action1<String> onError)
    {
        return FileSystemBase.createFolderAsync(this, folderPath, onError);
    }

    @Override
    public AsyncFunction<Boolean> createFolderAsync(String folderPath, Out<Folder> outputFolder)
    {
        return FileSystemBase.createFolderAsync(this, folderPath, outputFolder);
    }

    @Override
    public AsyncFunction<Boolean> createFolderAsync(String folderPath, Out<Folder> outputFolder, Action1<String> onError)
    {
        return FileSystemBase.createFolderAsync(this, folderPath, outputFolder, onError);
    }

    @Override
    public AsyncFunction<Boolean> createFolderAsync(Path folderPath)
    {
        return FileSystemBase.createFolderAsync(this, folderPath);
    }

    @Override
    public AsyncFunction<Boolean> createFolderAsync(Path folderPath, Action1<String> onError)
    {
        return FileSystemBase.createFolderAsync(this, folderPath, onError);
    }

    @Override
    public AsyncFunction<Boolean> createFolderAsync(Path folderPath, Out<Folder> outputFolder)
    {
        return FileSystemBase.createFolderAsync(this, folderPath, outputFolder);
    }

    @Override
    public AsyncFunction<Boolean> createFolderAsync(Path folderPath, Out<Folder> outputFolder, Action1<String> onError)
    {
        return FileSystemBase.createFolderAsync(this, folderPath, outputFolder, onError);
    }

    @Override
    public boolean deleteFolder(String folderPath)
    {
        return FileSystemBase.deleteFolder(this, folderPath);
    }

    @Override
    public boolean deleteFolder(String folderPath, Action1<String> onError)
    {
        return FileSystemBase.deleteFolder(this, folderPath, onError);
    }

    @Override
    public boolean deleteFolder(Path folderPath)
    {
        return FileSystemBase.deleteFolder(this, folderPath);
    }

    @Override
    public AsyncFunction<Boolean> deleteFolderAsync(String folderPath)
    {
        return FileSystemBase.deleteFolderAsync(this, folderPath);
    }

    @Override
    public AsyncFunction<Boolean> deleteFolderAsync(String folderPath, Action1<String> onError)
    {
        return FileSystemBase.deleteFolderAsync(this, folderPath, onError);
    }

    @Override
    public AsyncFunction<Boolean> deleteFolderAsync(Path folderPath)
    {
        return FileSystemBase.deleteFolderAsync(this, folderPath);
    }

    @Override
    public AsyncFunction<Boolean> deleteFolderAsync(Path folderPath, Action1<String> onError)
    {
        return FileSystemBase.deleteFolderAsync(this, folderPath, onError);
    }

    @Override
    public File getFile(String filePath)
    {
        return FileSystemBase.getFile(this, filePath);
    }

    @Override
    public File getFile(Path filePath)
    {
        return FileSystemBase.getFile(this, filePath);
    }

    @Override
    public boolean fileExists(String filePath)
    {
        return FileSystemBase.fileExists(this, filePath);
    }

    @Override
    public boolean fileExists(String filePath, Action1<String> onError)
    {
        return FileSystemBase.fileExists(this, filePath, onError);
    }

    @Override
    public boolean fileExists(Path filePath)
    {
        return FileSystemBase.fileExists(this, filePath);
    }

    @Override
    public AsyncFunction<Boolean> fileExistsAsync(String filePath)
    {
        return FileSystemBase.fileExistsAsync(this, filePath);
    }

    @Override
    public AsyncFunction<Boolean> fileExistsAsync(String filePath, Action1<String> onError)
    {
        return FileSystemBase.fileExistsAsync(this, filePath, onError);
    }

    @Override
    public AsyncFunction<Boolean> fileExistsAsync(Path filePath)
    {
        return FileSystemBase.fileExistsAsync(this, filePath);
    }

    @Override
    public AsyncFunction<Boolean> fileExistsAsync(Path filePath, Action1<String> onError)
    {
        return FileSystemBase.fileExistsAsync(this, filePath, onError);
    }

    @Override
    public boolean createFile(String filePath)
    {
        return FileSystemBase.createFile(this, filePath);
    }

    @Override
    public boolean createFile(String filePath, Action1<String> onError)
    {
        return FileSystemBase.createFile(this, filePath, onError);
    }

    @Override
    public boolean createFile(String filePath, byte[] fileContents)
    {
        return FileSystemBase.createFile(this, filePath, fileContents);
    }

    @Override
    public boolean createFile(String filePath, byte[] fileContents, Action1<String> onError)
    {
        return FileSystemBase.createFile(this, filePath, fileContents, onError);
    }

    @Override
    public boolean createFile(String filePath, String fileContents)
    {
        return FileSystemBase.createFile(this, filePath, fileContents);
    }

    @Override
    public boolean createFile(String filePath, String fileContents, Action1<String> onError)
    {
        return FileSystemBase.createFile(this, filePath, fileContents, onError);
    }

    @Override
    public boolean createFile(String filePath, String fileContents, CharacterEncoding encoding)
    {
        return FileSystemBase.createFile(this, filePath, fileContents, encoding);
    }

    @Override
    public boolean createFile(String filePath, String fileContents, CharacterEncoding encoding, Action1<String> onError)
    {
        return FileSystemBase.createFile(this, filePath, fileContents, encoding, onError);
    }

    @Override
    public boolean createFile(String filePath, Out<File> outputFile)
    {
        return FileSystemBase.createFile(this, filePath, outputFile);
    }

    @Override
    public boolean createFile(String filePath, Out<File> outputFile, Action1<String> onError)
    {
        return FileSystemBase.createFile(this, filePath, outputFile, onError);
    }

    @Override
    public boolean createFile(String filePath, byte[] fileContents, Out<File> outputFile)
    {
        return FileSystemBase.createFile(this, filePath, fileContents, outputFile);
    }

    @Override
    public boolean createFile(String filePath, byte[] fileContents, Out<File> outputFile, Action1<String> onError)
    {
        return FileSystemBase.createFile(this, filePath, fileContents, outputFile, onError);
    }

    @Override
    public boolean createFile(String filePath, String fileContents, Out<File> outputFile)
    {
        return FileSystemBase.createFile(this, filePath, fileContents, outputFile);
    }

    @Override
    public boolean createFile(String filePath, String fileContents, Out<File> outputFile, Action1<String> onError)
    {
        return FileSystemBase.createFile(this, filePath, fileContents, outputFile, onError);
    }

    @Override
    public boolean createFile(String filePath, String fileContents, CharacterEncoding encoding, Out<File> outputFile)
    {
        return FileSystemBase.createFile(this, filePath, fileContents, encoding, outputFile);
    }

    @Override
    public boolean createFile(String filePath, String fileContents, CharacterEncoding encoding, Out<File> outputFile, Action1<String> onError)
    {
        return FileSystemBase.createFile(this, filePath, fileContents, encoding, outputFile, onError);
    }

    @Override
    public boolean createFile(Path filePath)
    {
        return FileSystemBase.createFile(this, filePath);
    }

    @Override
    public boolean createFile(Path filePath, Action1<String> onError)
    {
        return FileSystemBase.createFile(this, filePath, onError);
    }

    @Override
    public boolean createFile(Path filePath, byte[] fileContents)
    {
        return FileSystemBase.createFile(this, filePath, fileContents);
    }

    @Override
    public boolean createFile(Path filePath, byte[] fileContents, Action1<String> onError)
    {
        return FileSystemBase.createFile(this, filePath, fileContents, onError);
    }

    @Override
    public boolean createFile(Path filePath, String fileContents)
    {
        return FileSystemBase.createFile(this, filePath, fileContents);
    }

    @Override
    public boolean createFile(Path filePath, String fileContents, Action1<String> onError)
    {
        return FileSystemBase.createFile(this, filePath, fileContents, onError);
    }

    @Override
    public boolean createFile(Path filePath, String fileContents, CharacterEncoding encoding)
    {
        return FileSystemBase.createFile(this, filePath, fileContents, encoding);
    }

    @Override
    public boolean createFile(Path filePath, String fileContents, CharacterEncoding encoding, Action1<String> onError)
    {
        return FileSystemBase.createFile(this, filePath, fileContents, encoding, onError);
    }

    @Override
    public boolean createFile(Path filePath, Out<File> outputFile)
    {
        return FileSystemBase.createFile(this, filePath, outputFile);
    }

    @Override
    public boolean createFile(Path filePath, Out<File> outputFile, Action1<String> onError)
    {
        return FileSystemBase.createFile(this, filePath, outputFile, onError);
    }

    @Override
    public boolean createFile(Path filePath, byte[] fileContents, Out<File> outputFile)
    {
        return FileSystemBase.createFile(this, filePath, fileContents, outputFile);
    }

    @Override
    public boolean createFile(Path filePath, String fileContents, Out<File> outputFile)
    {
        return FileSystemBase.createFile(this, filePath, fileContents, outputFile);
    }

    @Override
    public boolean createFile(Path filePath, String fileContents, Out<File> outputFile, Action1<String> onError)
    {
        return FileSystemBase.createFile(this, filePath, fileContents, outputFile, onError);
    }

    @Override
    public boolean createFile(Path filePath, String fileContents, CharacterEncoding encoding, Out<File> outputFile)
    {
        return FileSystemBase.createFile(this, filePath, fileContents, encoding, outputFile);
    }

    @Override
    public boolean createFile(Path filePath, String fileContents, CharacterEncoding encoding, Out<File> outputFile, Action1<String> onError)
    {
        return FileSystemBase.createFile(this, filePath, fileContents, encoding, outputFile, onError);
    }

    @Override
    public AsyncFunction<Boolean> createFileAsync(String filePath)
    {
        return FileSystemBase.createFileAsync(this, filePath);
    }

    @Override
    public AsyncFunction<Boolean> createFileAsync(String filePath, Action1<String> onError)
    {
        return FileSystemBase.createFileAsync(this, filePath, onError);
    }

    @Override
    public AsyncFunction<Boolean> createFileAsync(String filePath, Out<File> outputFile)
    {
        return FileSystemBase.createFileAsync(this, filePath, outputFile);
    }

    @Override
    public AsyncFunction<Boolean> createFileAsync(String filePath, Out<File> outputFile, Action1<String> onError)
    {
        return FileSystemBase.createFileAsync(this, filePath, outputFile, onError);
    }

    @Override
    public AsyncFunction<Boolean> createFileAsync(Path filePath)
    {
        return FileSystemBase.createFileAsync(this, filePath);
    }

    @Override
    public AsyncFunction<Boolean> createFileAsync(Path filePath, Action1<String> onError)
    {
        return FileSystemBase.createFileAsync(this, filePath, onError);
    }

    @Override
    public AsyncFunction<Boolean> createFileAsync(Path filePath, Out<File> outputFile)
    {
        return FileSystemBase.createFileAsync(this, filePath, outputFile);
    }

    @Override
    public AsyncFunction<Boolean> createFileAsync(Path filePath, Out<File> outputFile, Action1<String> onError)
    {
        return FileSystemBase.createFileAsync(this, filePath, outputFile, onError);
    }

    @Override
    public boolean deleteFile(String filePath)
    {
        return FileSystemBase.deleteFile(this, filePath);
    }

    @Override
    public boolean deleteFile(String filePath, Action1<String> onError)
    {
        return FileSystemBase.deleteFile(this, filePath, onError);
    }

    @Override
    public boolean deleteFile(Path filePath)
    {
        return FileSystemBase.deleteFile(this, filePath);
    }

    @Override
    public AsyncFunction<Boolean> deleteFileAsync(String filePath)
    {
        return FileSystemBase.deleteFileAsync(this, filePath);
    }

    @Override
    public AsyncFunction<Boolean> deleteFileAsync(String filePath, Action1<String> onError)
    {
        return FileSystemBase.deleteFileAsync(this, filePath, onError);
    }

    @Override
    public AsyncFunction<Boolean> deleteFileAsync(Path filePath)
    {
        return FileSystemBase.deleteFileAsync(this, filePath);
    }

    @Override
    public AsyncFunction<Boolean> deleteFileAsync(Path filePath, Action1<String> onError)
    {
        return FileSystemBase.deleteFileAsync(this, filePath, onError);
    }

    @Override
    public DateTime getFileLastModified(String filePath)
    {
        return FileSystemBase.getFileLastModified(this, filePath);
    }

    @Override
    public byte[] getFileContents(String rootedFilePath)
    {
        return FileSystemBase.getFileContents(this, rootedFilePath);
    }

    @Override
    public byte[] getFileContents(String rootedFilePath, Action1<String> onError)
    {
        return FileSystemBase.getFileContents(this, rootedFilePath, onError);
    }

    @Override
    public byte[] getFileContents(Path rootedFilePath)
    {
        return FileSystemBase.getFileContents(this, rootedFilePath);
    }

    @Override
    public byte[] getFileContents(Path rootedFilePath, Action1<String> onError)
    {
        return FileSystemBase.getFileContents(this, rootedFilePath, onError);
    }

    @Override
    public String getFileContentsAsString(String rootedFilePath)
    {
        return FileSystemBase.getFileContentsAsString(this, rootedFilePath);
    }

    @Override
    public String getFileContentsAsString(String rootedFilePath, Action1<String> onError)
    {
        return FileSystemBase.getFileContentsAsString(this, rootedFilePath, onError);
    }

    @Override
    public String getFileContentsAsString(Path rootedFilePath)
    {
        return FileSystemBase.getFileContentsAsString(this, rootedFilePath);
    }

    @Override
    public String getFileContentsAsString(Path rootedFilePath, Action1<String> onError)
    {
        return FileSystemBase.getFileContentsAsString(this, rootedFilePath, onError);
    }

    @Override
    public String getFileContentsAsString(String rootedFilePath, CharacterEncoding encoding)
    {
        return FileSystemBase.getFileContentsAsString(this, rootedFilePath, encoding);
    }

    @Override
    public String getFileContentsAsString(String rootedFilePath, CharacterEncoding encoding, Action1<String> onError)
    {
        return FileSystemBase.getFileContentsAsString(this, rootedFilePath, encoding, onError);
    }

    @Override
    public String getFileContentsAsString(Path rootedFilePath, CharacterEncoding encoding)
    {
        return FileSystemBase.getFileContentsAsString(this, rootedFilePath, encoding);
    }

    @Override
    public String getFileContentsAsString(Path rootedFilePath, CharacterEncoding encoding, Action1<String> onError)
    {
        return FileSystemBase.getFileContentsAsString(this, rootedFilePath, encoding, onError);
    }

    @Override
    public Iterable<byte[]> getFileContentBlocks(String rootedFilePath)
    {
        return FileSystemBase.getFileContentBlocks(this, rootedFilePath);
    }

    @Override
    public Iterable<byte[]> getFileContentBlocks(String rootedFilePath, Action1<String> onError)
    {
        return FileSystemBase.getFileContentBlocks(this, rootedFilePath, onError);
    }

    @Override
    public Iterable<byte[]> getFileContentBlocks(Path rootedFilePath)
    {
        return FileSystemBase.getFileContentBlocks(this, rootedFilePath);
    }

    @Override
    public Iterable<byte[]> getFileContentBlocks(Path rootedFilePath, Action1<String> onError)
    {
        return FileSystemBase.getFileContentBlocks(this, rootedFilePath, onError);
    }

    @Override
    public Iterable<String> getFileContentLines(String rootedFilePath)
    {
        return FileSystemBase.getFileContentLines(this, rootedFilePath);
    }

    @Override
    public Iterable<String> getFileContentLines(String rootedFilePath, Action1<String> onError)
    {
        return FileSystemBase.getFileContentLines(this, rootedFilePath, onError);
    }

    @Override
    public Iterable<String> getFileContentLines(Path rootedFilePath)
    {
        return FileSystemBase.getFileContentLines(this, rootedFilePath);
    }

    @Override
    public Iterable<String> getFileContentLines(Path rootedFilePath, Action1<String> onError)
    {
        return FileSystemBase.getFileContentLines(this, rootedFilePath, onError);
    }

    @Override
    public Iterable<String> getFileContentLines(String rootedFilePath, boolean includeNewLines)
    {
        return FileSystemBase.getFileContentLines(this, rootedFilePath, includeNewLines);
    }

    @Override
    public Iterable<String> getFileContentLines(String rootedFilePath, boolean includeNewLines, Action1<String> onError)
    {
        return FileSystemBase.getFileContentLines(this, rootedFilePath, includeNewLines, onError);
    }

    @Override
    public Iterable<String> getFileContentLines(Path rootedFilePath, boolean includeNewLines)
    {
        return FileSystemBase.getFileContentLines(this, rootedFilePath, includeNewLines);
    }

    @Override
    public Iterable<String> getFileContentLines(Path rootedFilePath, boolean includeNewLines, Action1<String> onError)
    {
        return FileSystemBase.getFileContentLines(this, rootedFilePath, includeNewLines, onError);
    }

    @Override
    public Iterable<String> getFileContentLines(String rootedFilePath, CharacterEncoding encoding)
    {
        return FileSystemBase.getFileContentLines(this, rootedFilePath, encoding);
    }

    @Override
    public Iterable<String> getFileContentLines(String rootedFilePath, CharacterEncoding encoding, Action1<String> onError)
    {
        return FileSystemBase.getFileContentLines(this, rootedFilePath, encoding, onError);
    }

    @Override
    public Iterable<String> getFileContentLines(Path rootedFilePath, CharacterEncoding encoding)
    {
        return FileSystemBase.getFileContentLines(this, rootedFilePath, encoding);
    }

    @Override
    public Iterable<String> getFileContentLines(Path rootedFilePath, CharacterEncoding encoding, Action1<String> onError)
    {
        return FileSystemBase.getFileContentLines(this, rootedFilePath, encoding, onError);
    }

    @Override
    public Iterable<String> getFileContentLines(String rootedFilePath, boolean includeNewLines, CharacterEncoding encoding)
    {
        return FileSystemBase.getFileContentLines(this, rootedFilePath, includeNewLines, encoding);
    }

    @Override
    public Iterable<String> getFileContentLines(String rootedFilePath, boolean includeNewLines, CharacterEncoding encoding, Action1<String> onError)
    {
        return FileSystemBase.getFileContentLines(this, rootedFilePath, includeNewLines, encoding, onError);
    }

    @Override
    public Iterable<String> getFileContentLines(Path rootedFilePath, boolean includeNewLines, CharacterEncoding encoding)
    {
        return FileSystemBase.getFileContentLines(this, rootedFilePath, includeNewLines, encoding);
    }

    @Override
    public Iterable<String> getFileContentLines(Path rootedFilePath, boolean includeNewLines, CharacterEncoding encoding, Action1<String> onError)
    {
        return FileSystemBase.getFileContentLines(this, rootedFilePath, includeNewLines, encoding, onError);
    }

    @Override
    public ByteReadStream getFileContentByteReadStream(String rootedFilePath)
    {
        return FileSystemBase.getFileContentByteReadStream(this, rootedFilePath);
    }

    @Override
    public ByteReadStream getFileContentByteReadStream(String rootedFilePath, Action1<String> onError)
    {
        return FileSystemBase.getFileContentByteReadStream(this, rootedFilePath, onError);
    }

    @Override
    public ByteReadStream getFileContentByteReadStream(Path rootedFilePath)
    {
        return FileSystemBase.getFileContentByteReadStream(this, rootedFilePath);
    }

    @Override
    public CharacterReadStream getFileContentCharacterReadStream(String rootedFilePath)
    {
        return FileSystemBase.getFileContentCharacterReadStream(this, rootedFilePath);
    }

    @Override
    public CharacterReadStream getFileContentCharacterReadStream(String rootedFilePath, Action1<String> onError)
    {
        return FileSystemBase.getFileContentCharacterReadStream(this, rootedFilePath, onError);
    }

    @Override
    public CharacterReadStream getFileContentCharacterReadStream(Path rootedFilePath)
    {
        return FileSystemBase.getFileContentCharacterReadStream(this, rootedFilePath);
    }

    @Override
    public CharacterReadStream getFileContentCharacterReadStream(Path rootedFilePath, Action1<String> onError)
    {
        return FileSystemBase.getFileContentCharacterReadStream(this, rootedFilePath, onError);
    }

    @Override
    public boolean setFileContents(String rootedFilePath, byte[] fileContents)
    {
        return FileSystemBase.setFileContents(this, rootedFilePath, fileContents);
    }

    @Override
    public boolean setFileContents(String rootedFilePath, byte[] fileContents, Action1<String> onError)
    {
        return FileSystemBase.setFileContents(this, rootedFilePath, fileContents, onError);
    }

    @Override
    public boolean setFileContents(Path rootedFilePath, byte[] fileContents)
    {
        return FileSystemBase.setFileContents(this, rootedFilePath, fileContents);
    }

    @Override
    public boolean setFileContents(String rootedFilePath, String fileContents)
    {
        return FileSystemBase.setFileContents(this, rootedFilePath, fileContents);
    }

    @Override
    public boolean setFileContents(String rootedFilePath, String fileContents, Action1<String> onError)
    {
        return FileSystemBase.setFileContents(this, rootedFilePath, fileContents, onError);
    }

    @Override
    public boolean setFileContents(Path rootedFilePath, String fileContents)
    {
        return FileSystemBase.setFileContents(this, rootedFilePath, fileContents);
    }

    @Override
    public boolean setFileContents(Path rootedFilePath, String fileContents, Action1<String> onError)
    {
        return FileSystemBase.setFileContents(this, rootedFilePath, fileContents, onError);
    }

    @Override
    public boolean setFileContents(String rootedFilePath, String fileContents, CharacterEncoding encoding)
    {
        return FileSystemBase.setFileContents(this, rootedFilePath, fileContents, encoding);
    }

    @Override
    public boolean setFileContents(String rootedFilePath, String fileContents, CharacterEncoding encoding, Action1<String> onError)
    {
        return FileSystemBase.setFileContents(this, rootedFilePath, fileContents, encoding, onError);
    }

    @Override
    public boolean setFileContents(Path rootedFilePath, String fileContents, CharacterEncoding encoding)
    {
        return FileSystemBase.setFileContents(this, rootedFilePath, fileContents, encoding);
    }

    @Override
    public boolean setFileContents(Path rootedFilePath, String fileContents, CharacterEncoding encoding, Action1<String> onError)
    {
        return FileSystemBase.setFileContents(this, rootedFilePath, fileContents, encoding, onError);
    }

    /**
     * Get whether or not a Root exists in this FileSystem with the provided path.
     * @param rootPath The path to the Root.
     * @return Whether or not a Root exists in this FileSystem with the provided path.
     */
    public static boolean rootExists(FileSystem fileSystem, String rootPath)
    {
        return fileSystem.rootExists(Path.parse(rootPath));
    }

    /**
     * Get whether or not a Root exists in this FileSystem with the provided path.
     * @param rootPath The path to the Root.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not a Root exists in this FileSystem with the provided path.
     */
    public static boolean rootExists(FileSystem fileSystem, String rootPath, Action1<String> onError)
    {
        return fileSystem.rootExists(Path.parse(rootPath), onError);
    }

    /**
     * Get whether or not a Root exists in this FileSystem with the provided path.
     * @param rootPath The path to the Root.
     * @return Whether or not a Root exists in this FileSystem with the provided path.
     */
    public static boolean rootExists(FileSystem fileSystem, Path rootPath)
    {
        return fileSystem.rootExists(rootPath, null);
    }

    /**
     * Get whether or not a Root exists in this FileSystem with the provided path.
     * @param rootPath The path to the Root.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not a Root exists in this FileSystem with the provided path.
     */
    public static boolean rootExists(FileSystem fileSystem, Path rootPath, Action1<String> onError)
    {
        boolean result = false;
        if (rootPath != null && rootPath.isRooted())
        {
            final Path onlyRootPath = rootPath.getRoot();
            result = fileSystem.getRoots(onError).contains(new Function1<Root, Boolean>()
            {
                @Override
                public Boolean run(Root root)
                {
                    return root.getPath().equals(onlyRootPath);
                }
            });
        }
        return result;
    }

    /**
     * Get whether or not a Root exists in this FileSystem with the provided path.
     * @param rootPath The path to the Root.
     * @return Whether or not a Root exists in this FileSystem with the provided path.
     */
    public static AsyncFunction<Boolean> rootExistsAsync(FileSystem fileSystem, String rootPath)
    {
        return fileSystem.rootExistsAsync(Path.parse(rootPath));
    }

    /**
     * Get whether or not a Root exists in this FileSystem with the provided path.
     * @param rootPath The path to the Root.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not a Root exists in this FileSystem with the provided path.
     */
    public static AsyncFunction<Boolean> rootExistsAsync(FileSystem fileSystem, String rootPath, Action1<String> onError)
    {
        return fileSystem.rootExistsAsync(Path.parse(rootPath), onError);
    }

    /**
     * Get whether or not a Root exists in this FileSystem with the provided path.
     * @param rootPath The path to the Root.
     * @return Whether or not a Root exists in this FileSystem with the provided path.
     */
    public static AsyncFunction<Boolean> rootExistsAsync(FileSystem fileSystem, Path rootPath)
    {
        return fileSystem.rootExistsAsync(rootPath, null);
    }

    /**
     * Get whether or not a Root exists in this FileSystem with the provided path.
     * @param rootPath The path to the Root.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not a Root exists in this FileSystem with the provided path.
     */
    public static AsyncFunction<Boolean> rootExistsAsync(final FileSystem fileSystem, final Path rootPath, final Action1<String> onError)
    {
        return async(fileSystem, new Function0<Boolean>()
        {
            @Override
            public Boolean run()
            {
                return fileSystem.rootExists(rootPath, onError);
            }
        });
    }

    /**
     * Get a reference to a Root with the provided path. The returned Root may or may not exist.
     * @param rootPath The path to the Root to return.
     * @return The Root with the provided path.
     */
    public static Root getRoot(FileSystem fileSystem, String rootPath)
    {
        return fileSystem.getRoot(Path.parse(rootPath));
    }

    /**
     * Get a reference to a Root with the provided Path. The returned Root may or may not exist.
     * @param rootPath The Path to the Root to return.
     * @return The Root with the provided Path.
     */
    public static Root getRoot(FileSystem fileSystem, Path rootPath)
    {
        return new Root(fileSystem, rootPath);
    }

    /**
     * Get the roots of this FileSystem.
     * @return The roots of this FileSystem.
     */
    public static Iterable<Root> getRoots(FileSystem fileSystem)
    {
        return fileSystem.getRoots(null);
    }

    /**
     * Get the roots of this FileSystem.
     * @return The roots of this FileSystem.
     */
    public static AsyncFunction<Iterable<Root>> getRootsAsync(FileSystem fileSystem)
    {
        return fileSystem.getRootsAsync(null);
    }

    /**
     * Get the roots of this FileSystem.
     * @param onError The action that will be called when an error occurs.
     * @return The roots of this FileSystem.
     */
    public static AsyncFunction<Iterable<Root>> getRootsAsync(final FileSystem fileSystem, final Action1<String> onError)
    {
        return async(fileSystem, new Function0<Iterable<Root>>()
        {
            @Override
            public Iterable<Root> run()
            {
                return fileSystem.getRoots(onError);
            }
        });
    }

    /**
     * Get the files and folders (entries) at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The files and folders (entries) at the provided folder path.
     */
    public static Iterable<FileSystemEntry> getFilesAndFolders(FileSystem fileSystem, String folderPath)
    {
        return fileSystem.getFilesAndFolders(Path.parse(folderPath));
    }

    /**
     * Get the files and folders (entries) at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @param onError The action that will be called when an error occurs.
     * @return The files and folders (entries) at the provided folder path.
     */
    public static Iterable<FileSystemEntry> getFilesAndFolders(FileSystem fileSystem, String folderPath, Action1<String> onError)
    {
        return fileSystem.getFilesAndFolders(Path.parse(folderPath), onError);
    }

    /**
     * Get the files and folders (entries) at the provided folder Path.
     * @param folderPath The Path to the folder (Root or Folder).
     * @return The files and folders (entries) at the provided folder Path.
     */
    public static Iterable<FileSystemEntry> getFilesAndFolders(FileSystem fileSystem, Path folderPath)
    {
        return fileSystem.getFilesAndFolders(folderPath, null);
    }

    /**
     * Get the files and folders (entries) at the provided folder path and its subfolders.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The files and folders (entries) at the provided folder path and its subfolders.
     */
    public static Iterable<FileSystemEntry> getFilesAndFoldersRecursively(FileSystem fileSystem, String folderPath)
    {
        return fileSystem.getFilesAndFoldersRecursively(Path.parse(folderPath));
    }

    /**
     * Get the files and folders (entries) at the provided folder path and its subfolders.
     * @param folderPath The path to the folder (Root or Folder).
     * @param onError The action that will be called when an error occurs.
     * @return The files and folders (entries) at the provided folder path and its subfolders.
     */
    public static Iterable<FileSystemEntry> getFilesAndFoldersRecursively(FileSystem fileSystem, String folderPath, Action1<String> onError)
    {
        return fileSystem.getFilesAndFoldersRecursively(Path.parse(folderPath), onError);
    }

    /**
     * Get the files and folders (entries) at the provided folder path and its subfolders.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The files and folders (entries) at the provided folder path and its subfolders.
     */
    public static Iterable<FileSystemEntry> getFilesAndFoldersRecursively(FileSystem fileSystem, Path folderPath)
    {
        return fileSystem.getFilesAndFoldersRecursively(folderPath, null);
    }

    /**
     * Get the files and folders (entries) at the provided folder path and its subfolders.
     * @param folderPath The path to the folder (Root or Folder).
     * @param onError The action that will be called when an error occurs.
     * @return The files and folders (entries) at the provided folder path and its subfolders.
     */
    public static Iterable<FileSystemEntry> getFilesAndFoldersRecursively(FileSystem fileSystem, Path folderPath, Action1<String> onError)
    {
        List<FileSystemEntry> result = new ArrayList<>();

        final Folder folder = fileSystem.getFolder(folderPath);
        if (folder != null && folder.exists())
        {
            final Queue<Folder> foldersToVisit = new SingleLinkListQueue<>();
            foldersToVisit.enqueue(folder);

            while (foldersToVisit.any())
            {
                final Folder currentFolder = foldersToVisit.dequeue();
                final Iterable<FileSystemEntry> currentFolderEntries = currentFolder.getFilesAndFolders(onError);
                for (final FileSystemEntry entry : currentFolderEntries)
                {
                    result.add(entry);

                    if (entry instanceof Folder)
                    {
                        foldersToVisit.enqueue((Folder)entry);
                    }
                }
            }
        }

        return result;
    }

    /**
     * Get the files and folders (entries) at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The files and folders (entries) at the provided folder path.
     */
    public static AsyncFunction<Iterable<FileSystemEntry>> getFilesAndFoldersAsync(FileSystem fileSystem, String folderPath)
    {
        return fileSystem.getFilesAndFoldersAsync(Path.parse(folderPath));
    }

    /**
     * Get the files and folders (entries) at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @param onError The action that will be called when an error occurs.
     * @return The files and folders (entries) at the provided folder path.
     */
    public static AsyncFunction<Iterable<FileSystemEntry>> getFilesAndFoldersAsync(FileSystem fileSystem, String folderPath, Action1<String> onError)
    {
        return fileSystem.getFilesAndFoldersAsync(Path.parse(folderPath), onError);
    }

    /**
     * Get the files and folders (entries) at the provided folder Path.
     * @param folderPath The Path to the folder (Root or Folder).
     * @return The files and folders (entries) at the provided folder Path.
     */
    public static AsyncFunction<Iterable<FileSystemEntry>> getFilesAndFoldersAsync(FileSystem fileSystem, Path folderPath)
    {
        return fileSystem.getFilesAndFoldersAsync(folderPath, null);
    }

    /**
     * Get the files and folders (entries) at the provided folder Path.
     * @param folderPath The Path to the folder (Root or Folder).
     * @param onError The action that will be called when an error occurs.
     * @return The files and folders (entries) at the provided folder Path.
     */
    public static AsyncFunction<Iterable<FileSystemEntry>> getFilesAndFoldersAsync(final FileSystem fileSystem, final Path folderPath, final Action1<String> onError)
    {
        return async(fileSystem, new Function0<Iterable<FileSystemEntry>>()
        {
            @Override
            public Iterable<FileSystemEntry> run()
            {
                return fileSystem.getFilesAndFolders(folderPath, onError);
            }
        });
    }

    /**
     * Get the folders at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The folders at the provided container path.
     */
    public static Iterable<Folder> getFolders(FileSystem fileSystem, String folderPath)
    {
        return fileSystem.getFolders(Path.parse(folderPath));
    }

    /**
     * Get the folders at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @param onError The action that will be called when an error occurs.
     * @return The folders at the provided container path.
     */
    public static Iterable<Folder> getFolders(FileSystem fileSystem, String folderPath, Action1<String> onError)
    {
        return fileSystem.getFolders(Path.parse(folderPath), onError);
    }

    /**
     * Get the folders at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The folders at the provided container path.
     */
    public static Iterable<Folder> getFolders(FileSystem fileSystem, Path folderPath)
    {
        return fileSystem.getFolders(folderPath, null);
    }

    /**
     * Get the folders at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @param onError The action that will be called when an error occurs.
     * @return The folders at the provided container path.
     */
    public static Iterable<Folder> getFolders(FileSystem fileSystem, Path folderPath, Action1<String> onError)
    {
        return fileSystem.getFilesAndFolders(folderPath, onError).instanceOf(Folder.class);
    }

    /**
     * Get the folders at the provided folder path and its subfolders.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The folders at the provided container path and its subfolders.
     */
    public static Iterable<Folder> getFoldersRecursively(FileSystem fileSystem, String folderPath)
    {
        return fileSystem.getFoldersRecursively(Path.parse(folderPath));
    }

    /**
     * Get the folders at the provided folder path and its subfolders.
     * @param folderPath The path to the folder (Root or Folder).
     * @param onError The action that will be called when an error occurs.
     * @return The folders at the provided container path and its subfolders.
     */
    public static Iterable<Folder> getFoldersRecursively(FileSystem fileSystem, String folderPath, Action1<String> onError)
    {
        return fileSystem.getFoldersRecursively(Path.parse(folderPath), onError);
    }

    /**
     * Get the folders at the provided folder path and its subfolders.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The folders at the provided container path and its subfolders.
     */
    public static Iterable<Folder> getFoldersRecursively(FileSystem fileSystem, Path folderPath)
    {
        return fileSystem.getFoldersRecursively(folderPath, null);
    }

    /**
     * Get the folders at the provided folder path and its subfolders.
     * @param folderPath The path to the folder (Root or Folder).
     * @param onError The action that will be called when an error occurs.
     * @return The folders at the provided container path and its subfolders.
     */
    public static Iterable<Folder> getFoldersRecursively(FileSystem fileSystem, Path folderPath, Action1<String> onError)
    {
        return fileSystem.getFilesAndFoldersRecursively(folderPath, onError).instanceOf(Folder.class);
    }

    /**
     * Get the folders at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The folders at the provided container path.
     */
    public static AsyncFunction<Iterable<Folder>> getFoldersAsync(FileSystem fileSystem, String folderPath)
    {
        return fileSystem.getFoldersAsync(Path.parse(folderPath));
    }

    /**
     * Get the folders at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @param onError The action that will be called when an error occurs.
     * @return The folders at the provided container path.
     */
    public static AsyncFunction<Iterable<Folder>> getFoldersAsync(FileSystem fileSystem, String folderPath, Action1<String> onError)
    {
        return fileSystem.getFoldersAsync(Path.parse(folderPath), onError);
    }

    /**
     * Get the folders at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The folders at the provided container path.
     */
    public static AsyncFunction<Iterable<Folder>> getFoldersAsync(FileSystem fileSystem, Path folderPath)
    {
        return fileSystem.getFoldersAsync(folderPath, null);
    }

    /**
     * Get the folders at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @param onError The action that will be called when an error occurs.
     * @return The folders at the provided container path.
     */
    public static AsyncFunction<Iterable<Folder>> getFoldersAsync(final FileSystem fileSystem, final Path folderPath, final Action1<String> onError)
    {
        return async(fileSystem, new Function0<Iterable<Folder>>()
        {
            @Override
            public Iterable<Folder> run()
            {
                return fileSystem.getFolders(folderPath, onError);
            }
        });
    }

    /**
     * Get the files at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The files at the provided container path.
     */
    public static Iterable<File> getFiles(FileSystem fileSystem, String folderPath)
    {
        return fileSystem.getFiles(Path.parse(folderPath));
    }

    /**
     * Get the files at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @param onError The action that will be called when an error occurs.
     * @return The files at the provided container path.
     */
    public static Iterable<File> getFiles(FileSystem fileSystem, String folderPath, Action1<String> onError)
    {
        return fileSystem.getFiles(Path.parse(folderPath), onError);
    }

    /**
     * Get the files at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The files at the provided container path.
     */
    public static Iterable<File> getFiles(FileSystem fileSystem, Path folderPath)
    {
        return fileSystem.getFiles(folderPath, null);
    }

    /**
     * Get the files at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @param onError The action that will be called when an error occurs.
     * @return The files at the provided container path.
     */
    public static Iterable<File> getFiles(FileSystem fileSystem, Path folderPath, Action1<String> onError)
    {
        return fileSystem.getFilesAndFolders(folderPath, onError).instanceOf(File.class);
    }

    /**
     * Get the files at the provided folder path and each of the subfolders recursively.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The files at the provided container path and its subfolders.
     */
    public static Iterable<File> getFilesRecursively(FileSystem fileSystem, String folderPath)
    {
        return fileSystem.getFilesRecursively(Path.parse(folderPath));
    }

    /**
     * Get the files at the provided folder path and each of the subfolders recursively.
     * @param folderPath The path to the folder (Root or Folder).
     * @param onError The action that will be called when an error occurs.
     * @return The files at the provided container path and its subfolders.
     */
    public static Iterable<File> getFilesRecursively(FileSystem fileSystem, String folderPath, Action1<String> onError)
    {
        return fileSystem.getFilesRecursively(Path.parse(folderPath), onError);
    }

    /**
     * Get the files at the provided folder path and each of the subfolders recursively.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The files at the provided container path and its subfolders.
     */
    public static Iterable<File> getFilesRecursively(FileSystem fileSystem, Path folderPath)
    {
        return fileSystem.getFilesRecursively(folderPath, null);
    }

    /**
     * Get the files at the provided folder path and each of the subfolders recursively.
     * @param folderPath The path to the folder (Root or Folder).
     * @param onError The action that will be called when an error occurs.
     * @return The files at the provided container path and its subfolders.
     */
    public static Iterable<File> getFilesRecursively(FileSystem fileSystem, Path folderPath, Action1<String> onError)
    {
        return fileSystem.getFilesAndFoldersRecursively(folderPath, onError).instanceOf(File.class);
    }

    /**
     * Get the files at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The files at the provided container path.
     */
    public static AsyncFunction<Iterable<File>> getFilesAsync(FileSystem fileSystem, String folderPath)
    {
        return fileSystem.getFilesAsync(Path.parse(folderPath), null);
    }

    /**
     * Get the files at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @param onError The action that will be called when an error occurs.
     * @return The files at the provided container path.
     */
    public static AsyncFunction<Iterable<File>> getFilesAsync(FileSystem fileSystem, String folderPath, Action1<String> onError)
    {
        return fileSystem.getFilesAsync(Path.parse(folderPath), onError);
    }

    /**
     * Get the files at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @return The files at the provided container path.
     */
    public static AsyncFunction<Iterable<File>> getFilesAsync(FileSystem fileSystem, Path folderPath)
    {
        return fileSystem.getFilesAsync(folderPath, null);
    }

    /**
     * Get the files at the provided folder path.
     * @param folderPath The path to the folder (Root or Folder).
     * @param onError The action that will be called when an error occurs.
     * @return The files at the provided container path.
     */
    public static AsyncFunction<Iterable<File>> getFilesAsync(final FileSystem fileSystem, final Path folderPath, final Action1<String> onError)
    {
        return async(fileSystem, new Function0<Iterable<File>>()
        {
            @Override
            public Iterable<File> run()
            {
                return fileSystem.getFiles(folderPath, onError);
            }
        });
    }

    /**
     * Get a reference to the Folder at the provided folderPath.
     * @param folderPath The path to the folder.
     * @return A reference to the Folder at the provided folderPath.
     */
    public static Folder getFolder(FileSystem fileSystem, String folderPath)
    {
        return fileSystem.getFolder(Path.parse(folderPath));
    }

    /**
     * Get a reference to the Folder at the provided folderPath.
     * @param folderPath The path to the folder.
     * @return A reference to the Folder at the provided folderPath.
     */
    public static Folder getFolder(FileSystem fileSystem, Path folderPath)
    {
        return folderPath == null || !folderPath.isRooted() ? null : new Folder(fileSystem, folderPath);
    }

    /**
     * Get whether or not a Folder exists in this FileSystem with the provided path.
     * @param folderPath The path to the Folder.
     * @return Whether or not a Folder exists in this FileSystem with the provided path.
     */
    public static boolean folderExists(FileSystem fileSystem, String folderPath)
    {
        return fileSystem.folderExists(Path.parse(folderPath));
    }

    /**
     * Get whether or not a Folder exists in this FileSystem with the provided path.
     * @param folderPath The path to the Folder.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not a Folder exists in this FileSystem with the provided path.
     */
    public static boolean folderExists(FileSystem fileSystem, String folderPath, Action1<String> onError)
    {
        return fileSystem.folderExists(Path.parse(folderPath), onError);
    }

    /**
     * Get whether or not a Folder exists in this FileSystem with the provided path.
     * @param folderPath The path to the Folder.
     * @return Whether or not a Folder exists in this FileSystem with the provided path.
     */
    public static boolean folderExists(FileSystem fileSystem, Path folderPath)
    {
        return fileSystem.folderExists(folderPath, null);
    }

    /**
     * Get whether or not a Folder exists in this FileSystem with the provided path.
     * @param folderPath The path to the Folder.
     * @return Whether or not a Folder exists in this FileSystem with the provided path.
     */
    public static AsyncFunction<Boolean> folderExistsAsync(FileSystem fileSystem, String folderPath)
    {
        return fileSystem.folderExistsAsync(Path.parse(folderPath));
    }

    /**
     * Get whether or not a Folder exists in this FileSystem with the provided path.
     * @param folderPath The path to the Folder.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not a Folder exists in this FileSystem with the provided path.
     */
    public static AsyncFunction<Boolean> folderExistsAsync(FileSystem fileSystem, String folderPath, Action1<String> onError)
    {
        return fileSystem.folderExistsAsync(Path.parse(folderPath), onError);
    }

    /**
     * Get whether or not a Folder exists in this FileSystem with the provided path.
     * @param folderPath The path to the Folder.
     * @return Whether or not a Folder exists in this FileSystem with the provided path.
     */
    public static AsyncFunction<Boolean> folderExistsAsync(FileSystem fileSystem, Path folderPath)
    {
        return fileSystem.folderExistsAsync(folderPath, null);
    }

    /**
     * Get whether or not a Folder exists in this FileSystem with the provided path.
     * @param folderPath The path to the Folder.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not a Folder exists in this FileSystem with the provided path.
     */
    public static AsyncFunction<Boolean> folderExistsAsync(final FileSystem fileSystem, final Path folderPath, final Action1<String> onError)
    {
        return async(fileSystem, new Function0<Boolean>()
        {
            @Override
            public Boolean run()
            {
                return fileSystem.folderExists(folderPath, onError);
            }
        });
    }

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param folderPath The path to the folder to create.
     * @return Whether or not this function created the folder.
     */
    public static boolean createFolder(FileSystem fileSystem, String folderPath)
    {
        return fileSystem.createFolder(Path.parse(folderPath));
    }

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param folderPath The path to the folder to create.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the folder.
     */
    public static boolean createFolder(FileSystem fileSystem, String folderPath, Action1<String> onError)
    {
        return fileSystem.createFolder(Path.parse(folderPath), onError);
    }

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param folderPath The path to the folder to create.
     * @param outputFolder The output parameter where the folder that was created or the folder that
     *                     already existed will be placed.
     * @return Whether or not this function created the folder.
     */
    public static boolean createFolder(FileSystem fileSystem, String folderPath, Out<Folder> outputFolder)
    {
        return fileSystem.createFolder(Path.parse(folderPath), outputFolder);
    }

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param folderPath The path to the folder to create.
     * @param outputFolder The output parameter where the folder that was created or the folder that
     *                     already existed will be placed.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the folder.
     */
    public static boolean createFolder(FileSystem fileSystem, String folderPath, Out<Folder> outputFolder, Action1<String> onError)
    {
        return fileSystem.createFolder(Path.parse(folderPath), outputFolder, onError);
    }

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param folderPath The path to the folder to create.
     * @return Whether or not this function created the folder.
     */
    public static boolean createFolder(FileSystem fileSystem, Path folderPath)
    {
        return fileSystem.createFolder(folderPath, null, null);
    }

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param folderPath The path to the folder to create.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the folder.
     */
    public static boolean createFolder(FileSystem fileSystem, Path folderPath, Action1<String> onError)
    {
        return fileSystem.createFolder(folderPath, null, onError);
    }

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param folderPath The path to the folder to create.
     * @param outputFolder The output parameter where the folder that was created or the folder that
     *                     already existed will be placed.
     * @return Whether or not this function created the folder.
     */
    public static boolean createFolder(FileSystem fileSystem, Path folderPath, Out<Folder> outputFolder)
    {
        return fileSystem.createFolder(folderPath, outputFolder, null);
    }

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param folderPath The path to the folder to create.
     * @return Whether or not this function created the folder.
     */
    public static AsyncFunction<Boolean> createFolderAsync(FileSystem fileSystem, String folderPath)
    {
        return fileSystem.createFolderAsync(Path.parse(folderPath));
    }

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param folderPath The path to the folder to create.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the folder.
     */
    public static AsyncFunction<Boolean> createFolderAsync(FileSystem fileSystem, String folderPath, Action1<String> onError)
    {
        return fileSystem.createFolderAsync(Path.parse(folderPath), onError);
    }

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param folderPath The path to the folder to create.
     * @param outputFolder The output parameter where the folder that was created or the folder that
     *                     already existed will be placed.
     * @return Whether or not this function created the folder.
     */
    public static AsyncFunction<Boolean> createFolderAsync(FileSystem fileSystem, String folderPath, Out<Folder> outputFolder)
    {
        return fileSystem.createFolderAsync(Path.parse(folderPath), outputFolder);
    }

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param folderPath The path to the folder to create.
     * @param outputFolder The output parameter where the folder that was created or the folder that
     *                     already existed will be placed.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the folder.
     */
    public static AsyncFunction<Boolean> createFolderAsync(FileSystem fileSystem, String folderPath, Out<Folder> outputFolder, Action1<String> onError)
    {
        return fileSystem.createFolderAsync(Path.parse(folderPath), outputFolder, onError);
    }

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param folderPath The path to the folder to create.
     * @return Whether or not this function created the folder.
     */
    public static AsyncFunction<Boolean> createFolderAsync(FileSystem fileSystem, Path folderPath)
    {
        return fileSystem.createFolderAsync(folderPath, null, null);
    }

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param folderPath The path to the folder to create.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the folder.
     */
    public static AsyncFunction<Boolean> createFolderAsync(FileSystem fileSystem, Path folderPath, Action1<String> onError)
    {
        return fileSystem.createFolderAsync(folderPath, null, onError);
    }

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param folderPath The path to the folder to create.
     * @param outputFolder The output parameter where the folder that was created or the folder that
     *                     already existed will be placed.
     * @return Whether or not this function created the folder.
     */
    public static AsyncFunction<Boolean> createFolderAsync(FileSystem fileSystem, Path folderPath, Out<Folder> outputFolder)
    {
        return fileSystem.createFolderAsync(folderPath, outputFolder, null);
    }

    /**
     * Create a folder at the provided path and return whether or not this function created the
     * folder.
     * @param folderPath The path to the folder to create.
     * @param outputFolder The output parameter where the folder that was created or the folder that
     *                     already existed will be placed.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the folder.
     */
    public static AsyncFunction<Boolean> createFolderAsync(final FileSystem fileSystem, final Path folderPath, final Out<Folder> outputFolder, final Action1<String> onError)
    {
        return async(fileSystem, new Function0<Boolean>()
        {
            @Override
            public Boolean run()
            {
                return fileSystem.createFolder(folderPath, outputFolder, onError);
            }
        });
    }

    /**
     * Delete the folder at the provided path and return whether this function deleted the folder.
     * @param folderPath The path to the folder to delete.
     * @return Whether or not this function deleted the folder.
     */
    public static boolean deleteFolder(FileSystem fileSystem, String folderPath)
    {
        return fileSystem.deleteFolder(Path.parse(folderPath));
    }

    /**
     * Delete the folder at the provided path and return whether this function deleted the folder.
     * @param folderPath The path to the folder to delete.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function deleted the folder.
     */
    public static boolean deleteFolder(FileSystem fileSystem, String folderPath, Action1<String> onError)
    {
        return fileSystem.deleteFolder(Path.parse(folderPath), onError);
    }

    /**
     * Delete the folder at the provided path and return whether this function deleted the folder.
     * @param folderPath The path to the folder to delete.
     * @return Whether or not this function deleted the folder.
     */
    public static boolean deleteFolder(FileSystem fileSystem, Path folderPath)
    {
        return fileSystem.deleteFolder(folderPath, null);
    }

    /**
     * Delete the folder at the provided path and return whether this function deleted the folder.
     * @param folderPath The path to the folder to delete.
     * @return Whether or not this function deleted the folder.
     */
    public static AsyncFunction<Boolean> deleteFolderAsync(FileSystem fileSystem, String folderPath)
    {
        return fileSystem.deleteFolderAsync(Path.parse(folderPath));
    }

    /**
     * Delete the folder at the provided path and return whether this function deleted the folder.
     * @param folderPath The path to the folder to delete.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function deleted the folder.
     */
    public static AsyncFunction<Boolean> deleteFolderAsync(FileSystem fileSystem, String folderPath, Action1<String> onError)
    {
        return fileSystem.deleteFolderAsync(Path.parse(folderPath), onError);
    }

    /**
     * Delete the folder at the provided path and return whether this function deleted the folder.
     * @param folderPath The path to the folder to delete.
     * @return Whether or not this function deleted the folder.
     */
    public static AsyncFunction<Boolean> deleteFolderAsync(FileSystem fileSystem, Path folderPath)
    {
        return fileSystem.deleteFolderAsync(folderPath, null);
    }

    /**
     * Delete the folder at the provided path and return whether this function deleted the folder.
     * @param folderPath The path to the folder to delete.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function deleted the folder.
     */
    public static AsyncFunction<Boolean> deleteFolderAsync(final FileSystem fileSystem, final Path folderPath, final Action1<String> onError)
    {
        return async(fileSystem, new Function0<Boolean>()
        {
            @Override
            public Boolean run()
            {
                return fileSystem.deleteFolder(folderPath, onError);
            }
        });
    }

    /**
     * Get a reference to the File at the provided folderPath.
     * @param filePath The path to the file.
     * @return A reference to the File at the provided filePath.
     */
    public static File getFile(FileSystem fileSystem, String filePath)
    {
        return fileSystem.getFile(Path.parse(filePath));
    }

    /**
     * Get a reference to the File at the provided folderPath.
     * @param filePath The path to the file.
     * @return A reference to the File at the provided filePath.
     */
    public static File getFile(FileSystem fileSystem, Path filePath)
    {
        return filePath == null || !filePath.isRooted() || filePath.endsWith("/") || filePath.endsWith("\\") ? null : new File(fileSystem, filePath);
    }

    /**
     * Get whether or not a File exists in this FileSystem with the provided path.
     * @param filePath The path to the File.
     * @return Whether or not a File exists in this FileSystem with the provided path.
     */
    public static boolean fileExists(FileSystem fileSystem, String filePath)
    {
        return fileSystem.fileExists(Path.parse(filePath));
    }

    /**
     * Get whether or not a File exists in this FileSystem with the provided path.
     * @param filePath The path to the File.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not a File exists in this FileSystem with the provided path.
     */
    public static boolean fileExists(FileSystem fileSystem, String filePath, Action1<String> onError)
    {
        return fileSystem.fileExists(Path.parse(filePath), onError);
    }

    /**
     * Get whether or not a File exists in this FileSystem with the provided path.
     * @param filePath The path to the File.
     * @return Whether or not a File exists in this FileSystem with the provided path.
     */
    public static boolean fileExists(FileSystem fileSystem, Path filePath)
    {
        return fileSystem.fileExists(filePath, null);
    }

    /**
     * Get whether or not a File exists in this FileSystem with the provided path.
     * @param filePath The path to the File.
     * @return Whether or not a File exists in this FileSystem with the provided path.
     */
    public static AsyncFunction<Boolean> fileExistsAsync(FileSystem fileSystem, String filePath)
    {
        return fileSystem.fileExistsAsync(Path.parse(filePath));
    }

    /**
     * Get whether or not a File exists in this FileSystem with the provided path.
     * @param filePath The path to the File.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not a File exists in this FileSystem with the provided path.
     */
    public static AsyncFunction<Boolean> fileExistsAsync(FileSystem fileSystem, String filePath, Action1<String> onError)
    {
        return fileSystem.fileExistsAsync(Path.parse(filePath), onError);
    }

    /**
     * Get whether or not a File exists in this FileSystem with the provided path.
     * @param filePath The path to the File.
     * @return Whether or not a File exists in this FileSystem with the provided path.
     */
    public static AsyncFunction<Boolean> fileExistsAsync(FileSystem fileSystem, Path filePath)
    {
        return fileSystem.fileExistsAsync(filePath, null);
    }

    /**
     * Get whether or not a File exists in this FileSystem with the provided path.
     * @param filePath The path to the File.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not a File exists in this FileSystem with the provided path.
     */
    public static AsyncFunction<Boolean> fileExistsAsync(final FileSystem fileSystem, final Path filePath, final Action1<String> onError)
    {
        return async(fileSystem, new Function0<Boolean>()
        {
            @Override
            public Boolean run()
            {
                return fileSystem.fileExists(filePath, onError);
            }
        });
    }

    /**
     * Create a file at the provided path and return whether or not this function created the file.
     * @param filePath The path to the file to create.
     * @return Whether or not this function created the file.
     */
    public static boolean createFile(FileSystem fileSystem, String filePath)
    {
        return fileSystem.createFile(Path.parse(filePath));
    }

    /**
     * Create a file at the provided path and return whether or not this function created the file.
     * @param filePath The path to the file to create.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the file.
     */
    public static boolean createFile(FileSystem fileSystem, String filePath, Action1<String> onError)
    {
        return fileSystem.createFile(Path.parse(filePath), onError);
    }

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @return Whether or not this function created the file.
     */
    public static boolean createFile(FileSystem fileSystem, String filePath, byte[] fileContents)
    {
        return fileSystem.createFile(Path.parse(filePath), fileContents);
    }

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the file.
     */
    public static boolean createFile(FileSystem fileSystem, String filePath, byte[] fileContents, Action1<String> onError)
    {
        return fileSystem.createFile(Path.parse(filePath), fileContents, onError);
    }

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @return Whether or not this function created the file.
     */
    public static boolean createFile(FileSystem fileSystem, String filePath, String fileContents)
    {
        return fileSystem.createFile(Path.parse(filePath), fileContents);
    }

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the file.
     */
    public static boolean createFile(FileSystem fileSystem, String filePath, String fileContents, Action1<String> onError)
    {
        return fileSystem.createFile(Path.parse(filePath), fileContents, onError);
    }

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @param encoding The CharacterEncoding to use to convert the provided fileContents to bytes.
     * @return Whether or not this function created the file.
     */
    public static boolean createFile(FileSystem fileSystem, String filePath, String fileContents, CharacterEncoding encoding)
    {
        return fileSystem.createFile(Path.parse(filePath), fileContents, encoding);
    }

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @param encoding The CharacterEncoding to use to convert the provided fileContents to bytes.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the file.
     */
    public static boolean createFile(FileSystem fileSystem, String filePath, String fileContents, CharacterEncoding encoding, Action1<String> onError)
    {
        return fileSystem.createFile(Path.parse(filePath), fileContents, encoding, onError);
    }

    /**
     * Create a file at the provided path and return whether or not this function created the file.
     * @param filePath The path to the file to create.
     * @param outputFile The output parameter where the file that was created or the file that
     *                     already existed will be placed.
     * @return Whether or not this function created the file.
     */
    public static boolean createFile(FileSystem fileSystem, String filePath, Out<File> outputFile)
    {
        return fileSystem.createFile(Path.parse(filePath), outputFile);
    }

    /**
     * Create a file at the provided path and return whether or not this function created the file.
     * @param filePath The path to the file to create.
     * @param outputFile The output parameter where the file that was created or the file that
     *                     already existed will be placed.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the file.
     */
    public static boolean createFile(FileSystem fileSystem, String filePath, Out<File> outputFile, Action1<String> onError)
    {
        return fileSystem.createFile(Path.parse(filePath), outputFile, onError);
    }

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @param outputFile The output parameter where the file that was created or the file that
     *                     already existed will be placed.
     * @return Whether or not this function created the file.
     */
    public static boolean createFile(FileSystem fileSystem, String filePath, byte[] fileContents, Out<File> outputFile)
    {
        return fileSystem.createFile(Path.parse(filePath), fileContents, outputFile);
    }

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @param outputFile The output parameter where the file that was created or the file that
     *                     already existed will be placed.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the file.
     */
    public static boolean createFile(FileSystem fileSystem, String filePath, byte[] fileContents, Out<File> outputFile, Action1<String> onError)
    {
        return fileSystem.createFile(Path.parse(filePath), fileContents, outputFile, onError);
    }

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @param outputFile The output parameter where the file that was created or the file that
     *                     already existed will be placed.
     * @return Whether or not this function created the file.
     */
    public static boolean createFile(FileSystem fileSystem, String filePath, String fileContents, Out<File> outputFile)
    {
        return fileSystem.createFile(Path.parse(filePath), fileContents, outputFile);
    }

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @param outputFile The output parameter where the file that was created or the file that
     *                     already existed will be placed.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the file.
     */
    public static boolean createFile(FileSystem fileSystem, String filePath, String fileContents, Out<File> outputFile, Action1<String> onError)
    {
        return fileSystem.createFile(Path.parse(filePath), fileContents, outputFile, onError);
    }

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @param encoding The CharacterEncoding to use to convert the provided fileContents to bytes.
     * @param outputFile The output parameter where the file that was created or the file that
     *                     already existed will be placed.
     * @return Whether or not this function created the file.
     */
    public static boolean createFile(FileSystem fileSystem, String filePath, String fileContents, CharacterEncoding encoding, Out<File> outputFile)
    {
        return fileSystem.createFile(Path.parse(filePath), fileContents, encoding, outputFile);
    }

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @param encoding The CharacterEncoding to use to convert the provided fileContents to bytes.
     * @param outputFile The output parameter where the file that was created or the file that
     *                     already existed will be placed.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the file.
     */
    public static boolean createFile(FileSystem fileSystem, String filePath, String fileContents, CharacterEncoding encoding, Out<File> outputFile, Action1<String> onError)
    {
        return fileSystem.createFile(Path.parse(filePath), fileContents, encoding, outputFile, onError);
    }

    /**
     * Create a file at the provided path and return whether or not this function created the file.
     * @param filePath The path to the file to create.
     * @return Whether or not this function created the file.
     */
    public static boolean createFile(FileSystem fileSystem, Path filePath)
    {
        return fileSystem.createFile(filePath, (byte[])null);
    }

    /**
     * Create a file at the provided path and return whether or not this function created the file.
     * @param filePath The path to the file to create.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the file.
     */
    public static boolean createFile(FileSystem fileSystem, Path filePath, Action1<String> onError)
    {
        return fileSystem.createFile(filePath, (byte[])null, onError);
    }

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @return Whether or not this function created the file.
     */
    public static boolean createFile(FileSystem fileSystem, Path filePath, byte[] fileContents)
    {
        return fileSystem.createFile(filePath, fileContents, (Out<File>)null);
    }

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the file.
     */
    public static boolean createFile(FileSystem fileSystem, Path filePath, byte[] fileContents, Action1<String> onError)
    {
        return fileSystem.createFile(filePath, fileContents, null, onError);
    }

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @return Whether or not this function created the file.
     */
    public static boolean createFile(FileSystem fileSystem, Path filePath, String fileContents)
    {
        return fileSystem.createFile(filePath, fileContents, CharacterEncoding.UTF_8, (Out<File>)null);
    }

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the file.
     */
    public static boolean createFile(FileSystem fileSystem, Path filePath, String fileContents, Action1<String> onError)
    {
        return fileSystem.createFile(filePath, fileContents, CharacterEncoding.UTF_8, null, onError);
    }

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @param encoding The CharacterEncoding to use to convert the provided fileContents to bytes.
     * @return Whether or not this function created the file.
     */
    public static boolean createFile(FileSystem fileSystem, Path filePath, String fileContents, CharacterEncoding encoding)
    {
        return fileSystem.createFile(filePath, fileContents, encoding, (Out<File>)null);
    }

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @param encoding The CharacterEncoding to use to convert the provided fileContents to bytes.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the file.
     */
    public static boolean createFile(FileSystem fileSystem, Path filePath, String fileContents, CharacterEncoding encoding, Action1<String> onError)
    {
        return fileSystem.createFile(filePath, fileContents, encoding, null, onError);
    }

    /**
     * Create a file at the provided path and return whether or not this function created the
     * file.
     * @param filePath The path to the file to create.
     * @param outputFile The output parameter where the file that was created or the file that
     *                     already existed will be placed.
     * @return Whether or not this function created the file.
     */
    public static boolean createFile(FileSystem fileSystem, Path filePath, Out<File> outputFile)
    {
        return fileSystem.createFile(filePath, (byte[])null, outputFile);
    }

    /**
     * Create a file at the provided path and return whether or not this function created the
     * file.
     * @param filePath The path to the file to create.
     * @param outputFile The output parameter where the file that was created or the file that
     *                     already existed will be placed.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the file.
     */
    public static boolean createFile(FileSystem fileSystem, Path filePath, Out<File> outputFile, Action1<String> onError)
    {
        return fileSystem.createFile(filePath, (byte[])null, outputFile, onError);
    }

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @param outputFile The output parameter where the file that was created or the file that
     *                     already existed will be placed.
     * @return Whether or not this function created the file.
     */
    public static boolean createFile(FileSystem fileSystem, Path filePath, byte[] fileContents, Out<File> outputFile)
    {
        return fileSystem.createFile(filePath, fileContents, outputFile, null);
    }

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @param outputFile The output parameter where the file that was created or the file that
     *                     already existed will be placed.
     * @return Whether or not this function created the file.
     */
    public static boolean createFile(FileSystem fileSystem, Path filePath, String fileContents, Out<File> outputFile)
    {
        return fileSystem.createFile(filePath, fileContents, CharacterEncoding.UTF_8, outputFile);
    }

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @param outputFile The output parameter where the file that was created or the file that
     *                     already existed will be placed.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the file.
     */
    public static boolean createFile(FileSystem fileSystem, Path filePath, String fileContents, Out<File> outputFile, Action1<String> onError)
    {
        return fileSystem.createFile(filePath, fileContents, CharacterEncoding.UTF_8, outputFile, onError);
    }

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @param encoding The CharacterEncoding to use to convert the provided fileContents to bytes.
     * @param outputFile The output parameter where the file that was created or the file that
     *                     already existed will be placed.
     * @return Whether or not this function created the file.
     */
    public static boolean createFile(FileSystem fileSystem, Path filePath, String fileContents, CharacterEncoding encoding, Out<File> outputFile)
    {
        final byte[] fileContentsBytes = encoding == null ? null : encoding.encode(fileContents);
        return fileSystem.createFile(filePath, fileContentsBytes, outputFile);
    }

    /**
     * Create a file at the provided path with the provided fileContents and return whether or not
     * this function created the file.
     * @param filePath The path to the file to create.
     * @param fileContents The contents to put in the file if the file didn't exist before this
     *                     function.
     * @param encoding The CharacterEncoding to use to convert the provided fileContents to bytes.
     * @param outputFile The output parameter where the file that was created or the file that
     *                     already existed will be placed.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the file.
     */
    public static boolean createFile(FileSystem fileSystem, Path filePath, String fileContents, CharacterEncoding encoding, Out<File> outputFile, Action1<String> onError)
    {
        final byte[] fileContentsBytes = encoding == null ? null : encoding.encode(fileContents);
        return fileSystem.createFile(filePath, fileContentsBytes, outputFile, onError);
    }

    /**
     * Create a file at the provided path and return whether or not this function created the file.
     * @param filePath The path to the file to create.
     * @return Whether or not this function created the file.
     */
    public static AsyncFunction<Boolean> createFileAsync(FileSystem fileSystem, String filePath)
    {
        return fileSystem.createFileAsync(Path.parse(filePath));
    }

    /**
     * Create a file at the provided path and return whether or not this function created the file.
     * @param filePath The path to the file to create.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the file.
     */
    public static AsyncFunction<Boolean> createFileAsync(FileSystem fileSystem, String filePath, Action1<String> onError)
    {
        return fileSystem.createFileAsync(Path.parse(filePath), onError);
    }

    /**
     * Create a file at the provided path and return whether or not this function created the file.
     * @param filePath The path to the file to create.
     * @param outputFile The output parameter where the file that was created or the file that
     *                     already existed will be placed.
     * @return Whether or not this function created the file.
     */
    public static AsyncFunction<Boolean> createFileAsync(FileSystem fileSystem, String filePath, Out<File> outputFile)
    {
        return fileSystem.createFileAsync(Path.parse(filePath), outputFile);
    }

    /**
     * Create a file at the provided path and return whether or not this function created the file.
     * @param filePath The path to the file to create.
     * @param outputFile The output parameter where the file that was created or the file that
     *                     already existed will be placed.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the file.
     */
    public static AsyncFunction<Boolean> createFileAsync(FileSystem fileSystem, String filePath, Out<File> outputFile, Action1<String> onError)
    {
        return fileSystem.createFileAsync(Path.parse(filePath), outputFile, onError);
    }

    /**
     * Create a file at the provided path and return whether or not this function created the file.
     * @param filePath The path to the file to create.
     * @return Whether or not this function created the file.
     */
    public static AsyncFunction<Boolean> createFileAsync(FileSystem fileSystem, Path filePath)
    {
        return fileSystem.createFileAsync(filePath, (Out<File>)null);
    }

    /**
     * Create a file at the provided path and return whether or not this function created the file.
     * @param filePath The path to the file to create.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the file.
     */
    public static AsyncFunction<Boolean> createFileAsync(FileSystem fileSystem, Path filePath, Action1<String> onError)
    {
        return fileSystem.createFileAsync(filePath, null, onError);
    }

    /**
     * Create a file at the provided path and return whether or not this function created the
     * file.
     * @param filePath The path to the file to create.
     * @param outputFile The output parameter where the file that was created or the file that
     *                     already existed will be placed.
     * @return Whether or not this function created the file.
     */
    public static AsyncFunction<Boolean> createFileAsync(FileSystem fileSystem, Path filePath, Out<File> outputFile)
    {
        return fileSystem.createFileAsync(filePath, outputFile, null);
    }

    /**
     * Create a file at the provided path and return whether or not this function created the
     * file.
     * @param filePath The path to the file to create.
     * @param outputFile The output parameter where the file that was created or the file that
     *                     already existed will be placed.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function created the file.
     */
    public static AsyncFunction<Boolean> createFileAsync(final FileSystem fileSystem, final Path filePath, final Out<File> outputFile, final Action1<String> onError)
    {
        return async(fileSystem, new Function0<Boolean>()
        {
            @Override
            public Boolean run()
            {
                return fileSystem.createFile(filePath, outputFile, onError);
            }
        });
    }

    /**
     * Delete the file at the provided path and return whether this function deleted the file.
     * @param filePath The path to the file to delete.
     * @return Whether or not this function deleted the file.
     */
    public static boolean deleteFile(FileSystem fileSystem, String filePath)
    {
        return fileSystem.deleteFile(Path.parse(filePath));
    }

    /**
     * Delete the file at the provided path and return whether this function deleted the file.
     * @param filePath The path to the file to delete.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function deleted the file.
     */
    public static boolean deleteFile(FileSystem fileSystem, String filePath, Action1<String> onError)
    {
        return fileSystem.deleteFile(Path.parse(filePath), onError);
    }

    /**
     * Delete the file at the provided path and return whether this function deleted the file.
     * @param filePath The path to the file to delete.
     * @return Whether or not this function deleted the file.
     */
    public static boolean deleteFile(FileSystem fileSystem, Path filePath)
    {
        return fileSystem.deleteFile(filePath, null);
    }

    /**
     * Delete the file at the provided path and return whether this function deleted the file.
     * @param filePath The path to the file to delete.
     * @return Whether or not this function deleted the file.
     */
    public static AsyncFunction<Boolean> deleteFileAsync(FileSystem fileSystem, String filePath)
    {
        return fileSystem.deleteFileAsync(Path.parse(filePath));
    }

    /**
     * Delete the file at the provided path and return whether this function deleted the file.
     * @param filePath The path to the file to delete.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function deleted the file.
     */
    public static AsyncFunction<Boolean> deleteFileAsync(FileSystem fileSystem, String filePath, Action1<String> onError)
    {
        return fileSystem.deleteFileAsync(Path.parse(filePath), onError);
    }

    /**
     * Delete the file at the provided path and return whether this function deleted the file.
     * @param filePath The path to the file to delete.
     * @return Whether or not this function deleted the file.
     */
    public static AsyncFunction<Boolean> deleteFileAsync(FileSystem fileSystem, Path filePath)
    {
        return fileSystem.deleteFileAsync(filePath, null);
    }

    /**
     * Delete the file at the provided path and return whether this function deleted the file.
     * @param filePath The path to the file to delete.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not this function deleted the file.
     */
    public static AsyncFunction<Boolean> deleteFileAsync(final FileSystem fileSystem, final Path filePath, final Action1<String> onError)
    {
        return async(fileSystem, new Function0<Boolean>()
        {
            @Override
            public Boolean run()
            {
                return fileSystem.deleteFile(filePath, onError);
            }
        });
    }

    /**
     * Get the date and time of the most recent modification of the given file, or null if the file
     * doesn't exist.
     * @param filePath The path to the file.
     * @return The date and time of the most recent modification of the given file, or null if the
     * file doesn't exist.
     */
    public static DateTime getFileLastModified(FileSystem fileSystem, String filePath)
    {
        return fileSystem.getFileLastModified(Path.parse(filePath));
    }

    /**
     * Read the contents of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedFilePath, then null will be returned.
     * @param rootedFilePath The rooted file path of the file to readByte.
     * @return The contents of the file at the provided rootedFilePath, or null if no file exists at
     * the provided rootedFilePath.
     */
    public static byte[] getFileContents(FileSystem fileSystem, String rootedFilePath)
    {
        return fileSystem.getFileContents(Path.parse(rootedFilePath));
    }

    /**
     * Read the contents of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedFilePath, then null will be returned.
     * @param rootedFilePath The rooted file path of the file to readByte.
     * @param onError The action that will be called when an error occurs.
     * @return The contents of the file at the provided rootedFilePath, or null if no file exists at
     * the provided rootedFilePath.
     */
    public static byte[] getFileContents(FileSystem fileSystem, String rootedFilePath, Action1<String> onError)
    {
        return fileSystem.getFileContents(Path.parse(rootedFilePath), onError);
    }

    /**
     * Read the contents of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedFilePath, then null will be returned.
     * @param rootedFilePath The rooted file path of the file to readByte.
     * @return The contents of the file at the provided rootedFilePath, or null if no file exists at
     * the provided rootedFilePath.
     */
    public static byte[] getFileContents(FileSystem fileSystem, Path rootedFilePath)
    {
        return fileSystem.getFileContents(rootedFilePath, null);
    }

    /**
     * Read the contents of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedFilePath, then null will be returned.
     * @param rootedFilePath The rooted file path of the file to readByte.
     * @param onError The action that will be called when an error occurs.
     * @return The contents of the file at the provided rootedFilePath, or null if no file exists at
     * the provided rootedFilePath.
     */
    public static byte[] getFileContents(FileSystem fileSystem, Path rootedFilePath, Action1<String> onError)
    {
        return Array.merge(fileSystem.getFileContentBlocks(rootedFilePath, onError));
    }

    /**
     * Read the contents of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedFilePath, then null will be returned.
     * @param rootedFilePath The rooted file path of the file to readByte.
     * @return The contents of the file at the provided rootedFilePath, or null if no file exists at
     * the provided rootedFilePath.
     */
    public static String getFileContentsAsString(FileSystem fileSystem, String rootedFilePath)
    {
        return fileSystem.getFileContentsAsString(Path.parse(rootedFilePath));
    }

    /**
     * Read the contents of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedFilePath, then null will be returned.
     * @param rootedFilePath The rooted file path of the file to readByte.
     * @param onError The action that will be called when an error occurs.
     * @return The contents of the file at the provided rootedFilePath, or null if no file exists at
     * the provided rootedFilePath.
     */
    public static String getFileContentsAsString(FileSystem fileSystem, String rootedFilePath, Action1<String> onError)
    {
        return fileSystem.getFileContentsAsString(Path.parse(rootedFilePath), onError);
    }

    /**
     * Read the contents of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedFilePath, then null will be returned.
     * @param rootedFilePath The rooted file path of the file to readByte.
     * @return The contents of the file at the provided rootedFilePath, or null if no file exists at
     * the provided rootedFilePath.
     */
    public static String getFileContentsAsString(FileSystem fileSystem, Path rootedFilePath)
    {
        return fileSystem.getFileContentsAsString(rootedFilePath, CharacterEncoding.UTF_8);
    }

    /**
     * Read the contents of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedFilePath, then null will be returned.
     * @param rootedFilePath The rooted file path of the file to readByte.
     * @param onError The action that will be called when an error occurs.
     * @return The contents of the file at the provided rootedFilePath, or null if no file exists at
     * the provided rootedFilePath.
     */
    public static String getFileContentsAsString(FileSystem fileSystem, Path rootedFilePath, Action1<String> onError)
    {
        return fileSystem.getFileContentsAsString(rootedFilePath, CharacterEncoding.UTF_8, onError);
    }

    /**
     * Read the contents of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedFilePath, then null will be returned.
     * @param rootedFilePath The rooted file path of the file to readByte.
     * @return The contents of the file at the provided rootedFilePath, or null if no file exists at
     * the provided rootedFilePath.
     */
    public static String getFileContentsAsString(FileSystem fileSystem, String rootedFilePath, CharacterEncoding encoding)
    {
        return fileSystem.getFileContentsAsString(Path.parse(rootedFilePath), encoding);
    }

    /**
     * Read the contents of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedFilePath, then null will be returned.
     * @param rootedFilePath The rooted file path of the file to readByte.
     * @param onError The action that will be called when an error occurs.
     * @return The contents of the file at the provided rootedFilePath, or null if no file exists at
     * the provided rootedFilePath.
     */
    public static String getFileContentsAsString(FileSystem fileSystem, String rootedFilePath, CharacterEncoding encoding, Action1<String> onError)
    {
        return fileSystem.getFileContentsAsString(Path.parse(rootedFilePath), encoding, onError);
    }

    /**
     * Read the contents of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedFilePath, then null will be returned.
     * @param rootedFilePath The rooted file path of the file to readByte.
     * @return The contents of the file at the provided rootedFilePath, or null if no file exists at
     * the provided rootedFilePath.
     */
    public static String getFileContentsAsString(FileSystem fileSystem, Path rootedFilePath, CharacterEncoding encoding)
    {
        return fileSystem.getFileContentsAsString(rootedFilePath, encoding, null);
    }

    /**
     * Read the contents of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedFilePath, then null will be returned.
     * @param rootedFilePath The rooted file path of the file to readByte.
     * @param onError The action that will be called when an error occurs.
     * @return The contents of the file at the provided rootedFilePath, or null if no file exists at
     * the provided rootedFilePath.
     */
    public static String getFileContentsAsString(FileSystem fileSystem, Path rootedFilePath, CharacterEncoding encoding, Action1<String> onError)
    {
        String result = null;
        if (encoding != null)
        {
            final byte[] fileContents = fileSystem.getFileContents(rootedFilePath, onError);
            result = encoding.decode(fileContents);
        }
        return result;
    }

    /**
     * Read the contents of the file at the provided rootedFilePath as a sequence of byte[]. If no
     * file exists at the provided rootedFilePath, then null will be returned.
     * @param rootedFilePath The rooted file path of the file to readByte.
     * @return The contents of the file at the provided rootedFilePath as a sequence of byte[], or
     * null if no file exists at the provided rootedFilePath.
     */
    public static Iterable<byte[]> getFileContentBlocks(FileSystem fileSystem, String rootedFilePath)
    {
        return fileSystem.getFileContentBlocks(Path.parse(rootedFilePath));
    }

    /**
     * Read the contents of the file at the provided rootedFilePath as a sequence of byte[]. If no
     * file exists at the provided rootedFilePath, then null will be returned.
     * @param rootedFilePath The rooted file path of the file to readByte.
     * @param onError The action that will be called when an error occurs.
     * @return The contents of the file at the provided rootedFilePath as a sequence of byte[], or
     * null if no file exists at the provided rootedFilePath.
     */
    public static Iterable<byte[]> getFileContentBlocks(FileSystem fileSystem, String rootedFilePath, Action1<String> onError)
    {
        return fileSystem.getFileContentBlocks(Path.parse(rootedFilePath), onError);
    }

    /**
     * Read the contents of the file at the provided rootedFilePath as a sequence of byte[]. If no
     * file exists at the provided rootedFilePath, then null will be returned.
     * @param rootedFilePath The rooted file path of the file to readByte.
     * @return The contents of the file at the provided rootedFilePath as a sequence of byte[], or
     * null if no file exists at the provided rootedFilePath.
     */
    public static Iterable<byte[]> getFileContentBlocks(FileSystem fileSystem, Path rootedFilePath)
    {
        return fileSystem.getFileContentBlocks(rootedFilePath, null);
    }

    /**
     * Read the contents of the file at the provided rootedFilePath as a sequence of byte[]. If no
     * file exists at the provided rootedFilePath, then null will be returned.
     * @param rootedFilePath The rooted file path of the file to readByte.
     * @param onError The action that will be called when an error occurs.
     * @return The contents of the file at the provided rootedFilePath as a sequence of byte[], or
     * null if no file exists at the provided rootedFilePath.
     */
    public static Iterable<byte[]> getFileContentBlocks(FileSystem fileSystem, Path rootedFilePath, Action1<String> onError)
    {
        List<byte[]> result = null;

        try (final ByteReadStream fileByteReadStream = fileSystem.getFileContentByteReadStream(rootedFilePath, onError))
        {
            if (fileByteReadStream != null)
            {
                result = new ArrayList<>();

                final byte[] buffer = new byte[1024];
                int bytesRead;
                do
                {
                    bytesRead = fileByteReadStream.readBytes(buffer);
                    if (bytesRead > 0)
                    {
                        final byte[] byteBlock = Array.clone(buffer, 0, bytesRead);
                        result.add(byteBlock);
                    }
                }
                while (bytesRead >= 0);
            }
        }

        return result;
    }

    /**
     * Get the content lines of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedfilePath, then null will be returned.
     * @param rootedFilePath The path to the File.
     * @return The content lines of the file, or null if no file exists at the provided
     * rootedFilePath.
     */
    public static Iterable<String> getFileContentLines(FileSystem fileSystem, String rootedFilePath)
    {
        return fileSystem.getFileContentLines(Path.parse(rootedFilePath));
    }

    /**
     * Get the content lines of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedfilePath, then null will be returned.
     * @param rootedFilePath The path to the File.
     * @param onError The action that will be called when an error occurs.
     * @return The content lines of the file, or null if no file exists at the provided
     * rootedFilePath.
     */
    public static Iterable<String> getFileContentLines(FileSystem fileSystem, String rootedFilePath, Action1<String> onError)
    {
        return fileSystem.getFileContentLines(Path.parse(rootedFilePath), onError);
    }

    /**
     * Get the content lines of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedfilePath, then null will be returned.
     * @param rootedFilePath The path to the File.
     * @return The content lines of the file, or null if no file exists at the provided
     * rootedFilePath.
     */
    public static Iterable<String> getFileContentLines(FileSystem fileSystem, Path rootedFilePath)
    {
        return fileSystem.getFileContentLines(rootedFilePath, true);
    }

    /**
     * Get the content lines of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedfilePath, then null will be returned.
     * @param rootedFilePath The path to the File.
     * @param onError The action that will be called when an error occurs.
     * @return The content lines of the file, or null if no file exists at the provided
     * rootedFilePath.
     */
    public static Iterable<String> getFileContentLines(FileSystem fileSystem, Path rootedFilePath, Action1<String> onError)
    {
        return fileSystem.getFileContentLines(rootedFilePath, true, onError);
    }

    /**
     * Get the content lines of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedfilePath, then null will be returned.
     * @param rootedFilePath The path to the File.
     * @param includeNewLines Whether or not each line will include its terminating newline
     *                        character(s).
     * @return The content lines of the file, or null if no file exists at the provided
     * rootedFilePath.
     */
    public static Iterable<String> getFileContentLines(FileSystem fileSystem, String rootedFilePath, boolean includeNewLines)
    {
        return fileSystem.getFileContentLines(Path.parse(rootedFilePath), includeNewLines);
    }

    /**
     * Get the content lines of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedfilePath, then null will be returned.
     * @param rootedFilePath The path to the File.
     * @param includeNewLines Whether or not each line will include its terminating newline
     *                        character(s).
     * @param onError The action that will be called when an error occurs.
     * @return The content lines of the file, or null if no file exists at the provided
     * rootedFilePath.
     */
    public static Iterable<String> getFileContentLines(FileSystem fileSystem, String rootedFilePath, boolean includeNewLines, Action1<String> onError)
    {
        return fileSystem.getFileContentLines(Path.parse(rootedFilePath), includeNewLines, onError);
    }

    /**
     * Get the content lines of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedfilePath, then null will be returned.
     * @param rootedFilePath The path to the File.
     * @param includeNewLines Whether or not each line will include its terminating newline
     *                        character(s).
     * @return The content lines of the file, or null if no file exists at the provided
     * rootedFilePath.
     */
    public static Iterable<String> getFileContentLines(FileSystem fileSystem, Path rootedFilePath, boolean includeNewLines)
    {
        return fileSystem.getFileContentLines(rootedFilePath, includeNewLines, CharacterEncoding.UTF_8);
    }

    /**
     * Get the content lines of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedfilePath, then null will be returned.
     * @param rootedFilePath The path to the File.
     * @param includeNewLines Whether or not each line will include its terminating newline
     *                        character(s).
     * @param onError The action that will be called when an error occurs.
     * @return The content lines of the file, or null if no file exists at the provided
     * rootedFilePath.
     */
    public static Iterable<String> getFileContentLines(FileSystem fileSystem, Path rootedFilePath, boolean includeNewLines, Action1<String> onError)
    {
        return fileSystem.getFileContentLines(rootedFilePath, includeNewLines, CharacterEncoding.UTF_8, onError);
    }

    /**
     * Get the content lines of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedfilePath, then null will be returned.
     * @param rootedFilePath The path to the File.
     * @param encoding The CharacterEncoding to use to convert the file's contents to Strings.
     * @return The content lines of the file, or null if no file exists at the provided
     * rootedFilePath.
     */
    public static Iterable<String> getFileContentLines(FileSystem fileSystem, String rootedFilePath, CharacterEncoding encoding)
    {
        return fileSystem.getFileContentLines(Path.parse(rootedFilePath), encoding);
    }

    /**
     * Get the content lines of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedfilePath, then null will be returned.
     * @param rootedFilePath The path to the File.
     * @param encoding The CharacterEncoding to use to convert the file's contents to Strings.
     * @param onError The action that will be called when an error occurs.
     * @return The content lines of the file, or null if no file exists at the provided
     * rootedFilePath.
     */
    public static Iterable<String> getFileContentLines(FileSystem fileSystem, String rootedFilePath, CharacterEncoding encoding, Action1<String> onError)
    {
        return fileSystem.getFileContentLines(Path.parse(rootedFilePath), encoding, onError);
    }

    /**
     * Get the content lines of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedfilePath, then null will be returned.
     * @param rootedFilePath The path to the File.
     * @param encoding The CharacterEncoding to use to convert the file's contents to Strings.
     * @return The content lines of the file, or null if no file exists at the provided
     * rootedFilePath.
     */
    public static Iterable<String> getFileContentLines(FileSystem fileSystem, Path rootedFilePath, CharacterEncoding encoding)
    {
        return fileSystem.getFileContentLines(rootedFilePath, true, encoding);
    }

    /**
     * Get the content lines of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedfilePath, then null will be returned.
     * @param rootedFilePath The path to the File.
     * @param encoding The CharacterEncoding to use to convert the file's contents to Strings.
     * @param onError The action that will be called when an error occurs.
     * @return The content lines of the file, or null if no file exists at the provided
     * rootedFilePath.
     */
    public static Iterable<String> getFileContentLines(FileSystem fileSystem, Path rootedFilePath, CharacterEncoding encoding, Action1<String> onError)
    {
        return fileSystem.getFileContentLines(rootedFilePath, true, encoding, onError);
    }

    /**
     * Get the content lines of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedfilePath, then null will be returned.
     * @param rootedFilePath The path to the File.
     * @param includeNewLines Whether or not each line will include its terminating newline
     *                        character(s).
     * @param encoding The CharacterEncoding to use to convert the file's contents to Strings.
     * @return The content lines of the file, or null if no file exists at the provided
     * rootedFilePath.
     */
    public static Iterable<String> getFileContentLines(FileSystem fileSystem, String rootedFilePath, boolean includeNewLines, CharacterEncoding encoding)
    {
        return fileSystem.getFileContentLines(Path.parse(rootedFilePath), includeNewLines, encoding);
    }

    /**
     * Get the content lines of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedfilePath, then null will be returned.
     * @param rootedFilePath The path to the File.
     * @param includeNewLines Whether or not each line will include its terminating newline
     *                        character(s).
     * @param encoding The CharacterEncoding to use to convert the file's contents to Strings.
     * @param onError The action that will be called when an error occurs.
     * @return The content lines of the file, or null if no file exists at the provided
     * rootedFilePath.
     */
    public static Iterable<String> getFileContentLines(FileSystem fileSystem, String rootedFilePath, boolean includeNewLines, CharacterEncoding encoding, Action1<String> onError)
    {
        return fileSystem.getFileContentLines(Path.parse(rootedFilePath), includeNewLines, encoding, onError);
    }

    /**
     * Get the content lines of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedfilePath, then null will be returned.
     * @param rootedFilePath The path to the File.
     * @param includeNewLines Whether or not each line will include its terminating newline
     *                        character(s).
     * @param encoding The CharacterEncoding to use to convert the file's contents to Strings.
     * @return The content lines of the file, or null if no file exists at the provided
     * rootedFilePath.
     */
    public static Iterable<String> getFileContentLines(FileSystem fileSystem, Path rootedFilePath, boolean includeNewLines, CharacterEncoding encoding)
    {
        return fileSystem.getFileContentLines(rootedFilePath, includeNewLines, encoding, null);
    }

    /**
     * Get the content lines of the file at the provided rootedFilePath. If no file exists at the
     * provided rootedfilePath, then null will be returned.
     * @param rootedFilePath The path to the File.
     * @param includeNewLines Whether or not each line will include its terminating newline
     *                        character(s).
     * @param encoding The CharacterEncoding to use to convert the file's contents to Strings.
     * @param onError The action that will be called when an error occurs.
     * @return The content lines of the file, or null if no file exists at the provided
     * rootedFilePath.
     */
    public static Iterable<String> getFileContentLines(FileSystem fileSystem, Path rootedFilePath, boolean includeNewLines, CharacterEncoding encoding, Action1<String> onError)
    {
        Iterable<String> result = null;

        final String fileContents = fileSystem.getFileContentsAsString(rootedFilePath, encoding, onError);
        if (fileContents != null)
        {
            final List<String> lines = new ArrayList<>();
            final int fileContentsLength = fileContents.length();
            int lineStartIndex = 0;

            while (lineStartIndex < fileContentsLength)
            {
                final int newLineCharacterIndex = fileContents.indexOf('\n', lineStartIndex);
                if (newLineCharacterIndex < 0)
                {
                    lines.add(fileContents.substring(lineStartIndex));
                    lineStartIndex = fileContentsLength;
                }
                else
                {
                    String line = fileContents.substring(lineStartIndex, newLineCharacterIndex + 1);
                    if (!includeNewLines)
                    {
                        final int newLineWidth = line.endsWith("\r\n") ? 2 : 1;
                        line = line.substring(0, line.length() - newLineWidth);
                    }
                    lines.add(line);
                    lineStartIndex = newLineCharacterIndex + 1;
                }
            }

            result = lines;
        }

        return result;
    }

    /**
     * Get a ByteReadStream to the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @return A ByteReadStream to the contents of the file.
     */
    public static ByteReadStream getFileContentByteReadStream(FileSystem fileSystem, String rootedFilePath)
    {
        return fileSystem.getFileContentByteReadStream(Path.parse(rootedFilePath));
    }

    /**
     * Get a ByteReadStream to the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @param onError The action that will be called when an error occurs.
     * @return A ByteReadStream to the contents of the file.
     */
    public static ByteReadStream getFileContentByteReadStream(FileSystem fileSystem, String rootedFilePath, Action1<String> onError)
    {
        return fileSystem.getFileContentByteReadStream(Path.parse(rootedFilePath), onError);
    }

    /**
     * Get a ByteReadStream to the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @return A ByteReadStream to the contents of the file.
     */
    public static ByteReadStream getFileContentByteReadStream(FileSystem fileSystem, Path rootedFilePath)
    {
        return fileSystem.getFileContentByteReadStream(rootedFilePath, null);
    }

    /**
     * Get a CharacterReadStream to the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @return A CharacterReadStream to the contents of the file.
     */
    public static CharacterReadStream getFileContentCharacterReadStream(FileSystem fileSystem, String rootedFilePath)
    {
        return fileSystem.getFileContentCharacterReadStream(Path.parse(rootedFilePath));
    }

    /**
     * Get a CharacterReadStream to the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @param onError The action that will be called when an error occurs.
     * @return A CharacterReadStream to the contents of the file.
     */
    public static CharacterReadStream getFileContentCharacterReadStream(FileSystem fileSystem, String rootedFilePath, Action1<String> onError)
    {
        return fileSystem.getFileContentCharacterReadStream(Path.parse(rootedFilePath), onError);
    }

    /**
     * Get a CharacterReadStream to the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @return A CharacterReadStream to the contents of the file.
     */
    public static CharacterReadStream getFileContentCharacterReadStream(FileSystem fileSystem, Path rootedFilePath)
    {
        return fileSystem.getFileContentCharacterReadStream(rootedFilePath, null);
    }

    /**
     * Get a CharacterReadStream to the file at the provided rootedFilePath.
     * @param rootedFilePath The rooted file path to the file.
     * @param onError The action that will be called when an error occurs.
     * @return A CharacterReadStream to the contents of the file.
     */
    public static CharacterReadStream getFileContentCharacterReadStream(FileSystem fileSystem, Path rootedFilePath, Action1<String> onError)
    {
        final ByteReadStream contentByteReadStream = fileSystem.getFileContentByteReadStream(rootedFilePath, onError);
        return contentByteReadStream == null ? null : contentByteReadStream.asCharacterReadStream();
    }

    /**
     * Set the contents of the file at the provided rootedFilePath to the provided byte array. If
     * the file doesn't exist, then it will be created.
     * @param rootedFilePath The path to the file to set the contents for.
     * @param fileContents The contents to set.
     * @return Whether or not the file's contents were set.
     */
    public static boolean setFileContents(FileSystem fileSystem, String rootedFilePath, byte[] fileContents)
    {
        return fileSystem.setFileContents(Path.parse(rootedFilePath), fileContents);
    }

    /**
     * Set the contents of the file at the provided rootedFilePath to the provided byte array. If
     * the file doesn't exist, then it will be created.
     * @param rootedFilePath The path to the file to set the contents for.
     * @param fileContents The contents to set.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not the file's contents were set.
     */
    public static boolean setFileContents(FileSystem fileSystem, String rootedFilePath, byte[] fileContents, Action1<String> onError)
    {
        return fileSystem.setFileContents(Path.parse(rootedFilePath), fileContents, onError);
    }

    /**
     * Set the contents of the file at the provided rootedFilePath to the provided byte array. If
     * the file doesn't exist, then it will be created.
     * @param rootedFilePath The path to the file to set the contents for.
     * @param fileContents The contents to set.
     * @return Whether or not the file's contents were set.
     */
    public static boolean setFileContents(FileSystem fileSystem, Path rootedFilePath, byte[] fileContents)
    {
        return fileSystem.setFileContents(rootedFilePath, fileContents, null);
    }

    /**
     * Set the contents of the file at the provided rootedFilePath to the provided String. If the
     * file doesn't exist, then it will be created.
     * @param rootedFilePath The path to the file to set the contents for.
     * @param fileContents The contents to set.
     * @return Whether or not the file's contents were set.
     */
    public static boolean setFileContents(FileSystem fileSystem, String rootedFilePath, String fileContents)
    {
        return fileSystem.setFileContents(Path.parse(rootedFilePath), fileContents);
    }

    /**
     * Set the contents of the file at the provided rootedFilePath to the provided String. If the
     * file doesn't exist, then it will be created.
     * @param rootedFilePath The path to the file to set the contents for.
     * @param fileContents The contents to set.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not the file's contents were set.
     */
    public static boolean setFileContents(FileSystem fileSystem, String rootedFilePath, String fileContents, Action1<String> onError)
    {
        return fileSystem.setFileContents(Path.parse(rootedFilePath), fileContents, onError);
    }

    /**
     * Set the contents of the file at the provided rootedFilePath to the provided String. If the
     * file doesn't exist, then it will be created.
     * @param rootedFilePath The path to the file to set the contents for.
     * @param fileContents The contents to set.
     * @return Whether or not the file's contents were set.
     */
    public static boolean setFileContents(FileSystem fileSystem, Path rootedFilePath, String fileContents)
    {
        return fileSystem.setFileContents(rootedFilePath, fileContents, (Action1<String>)null);
    }

    /**
     * Set the contents of the file at the provided rootedFilePath to the provided String. If the
     * file doesn't exist, then it will be created.
     * @param rootedFilePath The path to the file to set the contents for.
     * @param fileContents The contents to set.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not the file's contents were set.
     */
    public static boolean setFileContents(FileSystem fileSystem, Path rootedFilePath, String fileContents, Action1<String> onError)
    {
        return fileSystem.setFileContents(rootedFilePath, fileContents, CharacterEncoding.UTF_8, onError);
    }

    /**
     * Set the contents of the file at the provided rootedFilePath to the provided String. If the
     * file doesn't exist, then it will be created.
     * @param rootedFilePath The path to the file to set the contents for.
     * @param fileContents The contents to set.
     * @param encoding The CharacterEncoding to use to convert the provided fileContents to bytes.
     * @return Whether or not the file's contents were set.
     */
    public static boolean setFileContents(FileSystem fileSystem, String rootedFilePath, String fileContents, CharacterEncoding encoding)
    {
        return fileSystem.setFileContents(rootedFilePath, encoding == null ? null : encoding.encode(fileContents));
    }

    /**
     * Set the contents of the file at the provided rootedFilePath to the provided String. If the
     * file doesn't exist, then it will be created.
     * @param rootedFilePath The path to the file to set the contents for.
     * @param fileContents The contents to set.
     * @param encoding The CharacterEncoding to use to convert the provided fileContents to bytes.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not the file's contents were set.
     */
    public static boolean setFileContents(FileSystem fileSystem, String rootedFilePath, String fileContents, CharacterEncoding encoding, Action1<String> onError)
    {
        return fileSystem.setFileContents(Path.parse(rootedFilePath), fileContents, encoding, onError);
    }

    /**
     * Set the contents of the file at the provided rootedFilePath to the provided String. If the
     * file doesn't exist, then it will be created.
     * @param rootedFilePath The path to the file to set the contents for.
     * @param fileContents The contents to set.
     * @param encoding The CharacterEncoding to use to convert the provided fileContents to bytes.
     * @return Whether or not the file's contents were set.
     */
    public static boolean setFileContents(FileSystem fileSystem, Path rootedFilePath, String fileContents, CharacterEncoding encoding)
    {
        return fileSystem.setFileContents(rootedFilePath, fileContents, encoding, null);
    }

    /**
     * Set the contents of the file at the provided rootedFilePath to the provided String. If the
     * file doesn't exist, then it will be created.
     * @param rootedFilePath The path to the file to set the contents for.
     * @param fileContents The contents to set.
     * @param encoding The CharacterEncoding to use to convert the provided fileContents to bytes.
     * @param onError The action that will be called when an error occurs.
     * @return Whether or not the file's contents were set.
     */
    public static boolean setFileContents(FileSystem fileSystem, Path rootedFilePath, String fileContents, CharacterEncoding encoding, Action1<String> onError)
    {
        return fileSystem.setFileContents(rootedFilePath, encoding == null ? null : encoding.encode(fileContents), onError);
    }

    private static <T> AsyncFunction<T> async(FileSystem fileSystem, Function0<T> function)
    {
        final AsyncRunner currentRunner = AsyncRunnerRegistry.getCurrentThreadAsyncRunner();
        return fileSystem.getAsyncRunner()
            .schedule(function)
            .thenOn(currentRunner);
    }
}
