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

        Message message = command.message();
        String response = channel.send(message);

        try {
            return Results.parse(response, command);
        } catch (Exception e) {
            return Results.createError(command, e);
        }
    }

    public <T> T read(Command<T> command) throws IOException {
        return send(command).data();
    }

}
