package elm327.reader;

import java.io.IOException;

class Protocol {

    static final Command<?>[] DEFAULT_SETUP = {
        Commands.Send.FullReset,
        Commands.Configure.Disable_Echo,
        Commands.Configure.Disable_LineFeeds,
        Commands.Configure.Disable_PrintSpaces,
    };

    private final Channel channel;

    public Protocol (Channel channel) throws IOException {
        this(channel, DEFAULT_SETUP);
    }

    public Protocol (final Channel channel, Command<?> ... setup) throws IOException {
        this.channel = channel;

        for (Command<?> cmd : setup) {
            Result<?> result  = send(cmd);
            if (result.isError()) {
                throw new ProtocolException(
                    String.format("Init command error: %s", cmd.message())
                );
            }
        }
    }

    public <T> Result<T> send(Command<T> command) throws IOException {
        Result<T> result;
        Message message = command.message();

        do {
            String response = channel.send(message);
            result = ResultParser.parse(response, command);
        } while (result instanceof Results.RetryCommand);

        if (null == result) {
            result = new Results.NoData<T>(command);
        }

        return result;
    }

    public <T> T read(Command<T> command) throws IOException {
        return send(command).data();
    }

}
