package qub;

/**
 * A factory that produces ProcessBuilder objects.
 */
public interface ProcessRunner
{
    /**
     * Run the provided executableFile with the provided arguments.
     * @param executableFile The executable file to run.
     * @param arguments The arguments to provide to the executable file.
     * @param workingFolder The folder that the process will be executed from.
     * @param redirectedInputStream The input stream that the new process will use.
     * @param redirectedOutputStream The action that will be invoked each time the process writes to its output stream.
     * @param redirectedErrorStream The action that will be invoked each time the process writes to its error stream.
     * @return The exit code of the process.
     */
    Result<Integer> run(File executableFile, Iterable<String> arguments, Folder workingFolder, ByteReadStream redirectedInputStream, Action1<ByteReadStream> redirectedOutputStream, Action1<ByteReadStream> redirectedErrorStream);
}
