package qub;

public class FakeTCPClientTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(FakeTCPClient.class, () ->
        {
            runner.test("basic read and write operations", (Test test) ->
            {
                final InMemoryByteReadStream readStream = new InMemoryByteReadStream(new byte[] { 0, 1, 2, 3, 4, 5 }, test.getMainAsyncRunner()).endOfStream();
                final InMemoryByteWriteStream writeStream = new InMemoryByteWriteStream();

                final IPv4Address localIPAddress = IPv4Address.parse("1.2.3.4");
                final int localPort = 8080;
                final IPv4Address remoteIPAddress = IPv4Address.parse("10.20.30.40");
                final int remotePort = 8081;

                final Result<TCPClient> tcpClientResult = FakeTCPClient.create(localIPAddress, localPort, remoteIPAddress, remotePort, readStream, writeStream, test.getMainAsyncRunner());
                test.assertSuccess(tcpClientResult);

                try (final TCPClient tcpClient = tcpClientResult.getValue())
                {
                    final Result<byte[]> readBytes = tcpClient.readAllBytes();
                    test.assertSuccess(new byte[] { 0, 1, 2, 3, 4, 5 }, readBytes);

                    tcpClient.write(new byte[] { 10, 20, 30 });
                    test.assertEqual(new byte[] { 10, 20, 30 }, writeStream.getBytes());
                }

                test.assertTrue(tcpClientResult.getValue().isDisposed());
                test.assertTrue(readStream.isDisposed());
                test.assertTrue(writeStream.isDisposed());
            });
        });
    }
}
