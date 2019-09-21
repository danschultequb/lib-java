package qub;

/**
 * A ProcessRunner that invokes external processes.
 */
public class RealProcessRunner implements ProcessRunner
{
    private final AsyncRunner parallelAsyncRunner;

    public RealProcessRunner(AsyncRunner parallelAsyncRunner)
    {
        PreCondition.assertNotNull(parallelAsyncRunner, "parallelAsyncRunner");

        this.parallelAsyncRunner = parallelAsyncRunner;
    }

    @Override
    public Result<Integer> run(File executableFile, Iterable<String> arguments, Folder workingFolder, ByteReadStream redirectedInputStream, Action1<ByteReadStream> redirectOutputAction, Action1<ByteReadStream> redirectErrorAction)
    {
        PreCondition.assertNotNull(executableFile, "executableFile");
        PreCondition.assertNotNull(arguments, "arguments");
        PreCondition.assertNotNull(workingFolder, "workingFolder");

        final java.lang.ProcessBuilder builder = new java.lang.ProcessBuilder(executableFile.getPath().toString());

        if (arguments.any())
        {
            for (final String argument : arguments)
            {
                builder.command().add(argument);
            }
        }

        builder.directory(new java.io.File(workingFolder.getPath().toString()));

        if (redirectedInputStream != null)
        {
            builder.redirectInput();
        }

        if (redirectOutputAction != null)
        {
            builder.redirectOutput();
        }

        if (redirectErrorAction != null)
        {
            builder.redirectError();
        }

        Result<Integer> result;
        try
        {
            final java.lang.Process process = builder.start();

            final Result<Void> inputAction = redirectedInputStream == null
                ? Result.success()
                : parallelAsyncRunner.schedule(() ->
                    {
                        final OutputStreamToByteWriteStream processInputStream = new OutputStreamToByteWriteStream(process.getOutputStream(), true);
                        processInputStream.writeAllBytes(redirectedInputStream).catchError().await();
                    });

            final Result<Void> outputAction = redirectOutputAction == null
                ? Result.success()
                : parallelAsyncRunner.schedule(() -> redirectOutputAction.run(new InputStreamToByteReadStream(process.getInputStream())));

            final Result<Void> errorAction = redirectErrorAction == null
                ? Result.success()
                : parallelAsyncRunner.schedule(() -> redirectErrorAction.run(new InputStreamToByteReadStream(process.getErrorStream())));

            result = parallelAsyncRunner.schedule(() ->
                {
                    final int processResult;
                    try
                    {
                        processResult = process.waitFor();

                        inputAction.await();
                        outputAction.await();
                        errorAction.await();
                    }
                    catch (Throwable e)
                    {
                        throw Exceptions.asRuntime(e);
                    }

                    return processResult;
                });
        }
        catch (Throwable error)
        {
            result = Result.error(error);
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }
}
