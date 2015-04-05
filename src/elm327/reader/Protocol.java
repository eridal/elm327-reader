package elm327.reader;

import java.io.IOException;
import java.net.Socket;

public class Protocol {

    private final Channel channel;

    public Protocol (final Socket socket) throws IOException {
        this(Channels.fromSocket(socket));
    }

    public Protocol (final Channel channel) throws IOException {
        this.channel = channel;
        configure();
    }

    private void configure() throws IOException {
        read(Commands.Send.FullReset);
        read(Commands.Configure.Disable_Echo);
        read(Commands.Configure.Disable_LineFeeds);
        read(Commands.Configure.Disable_PrintSpaces);
        // 0100
        // 0120
        // 0140
    }

    public <T> Result<T> send(Command<T> command) throws IOException {
        Result<T> result;
        Message message = command.message();

        do {
            String response = channel.send(message);
            result = ResultParser.parse(response, command);
        } while (result instanceof Results.RetryCommand);

        if (null == result) {
            return new Results.NoData<T>(command);
        } else {
            return result;
        }
    }

    public <T> T read(Command<T> command) throws IOException {
        return send(command).data();
    }
}
