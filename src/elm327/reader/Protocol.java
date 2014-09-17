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
        initialize(setup);
    }

    public void initialize(Command<?> ... setup) throws IOException {
        for (Command<?> cmd : setup) {
            send(cmd);
        }
    }

    public <T> Result<T> send(Command<T> command) throws IOException {
        String response = channel.send(command.message());
        try {
            return ResultParser.parse(response, command);
        } catch (Exception e) {
            return new Result.Error<T>(command, e);
        }
    }

    public <T> T read(Command<T> command) throws IOException {
        return send(command).data();
    }

}
