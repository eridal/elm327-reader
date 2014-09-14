package elm327.reader;

import java.io.IOException;

class Protocol {

    static final Command<?>[] DEFAULT_SETUP = {
        Commands.Send.Reset,
        Commands.Disable.Echo,
        Commands.Disable.LineFeeds,
        Commands.Disable.PrintSpaces,
    };

    private final Channel channel;

    public Protocol (Channel channel) throws InitializationException {
        this(channel, DEFAULT_SETUP);
    }

    public Protocol (final Channel channel, Command<?> ... setup) throws InitializationException {
        this.channel = channel;
        initialize(setup);
    }

    public void initialize(Command<?> ... setup) throws InitializationException {
        for (Command<?> cmd : setup) {
            Result<?> result = send(cmd);
            if (result instanceof Result.Error) {
                Result.Error<?> resultError = (Result.Error<?>) result;
                throw new InitializationException(resultError.getError());
            }
        }
    }

    public <T> Result<T> send(Command<T> command) {
        String message = command.toMessage();
        String response;
        try {
            response = channel.send(message);
        } catch (IOException e) {
            return new Result.Error<T>(command, e);
        }
        return ResultParser.parse(response, command);
    }

    public <T> T read(Command<T> command) {
        Result<T> result = send(command);
        return result.getData();
    }

    @SuppressWarnings("serial")
    class InitializationException extends Exception {
        InitializationException(Exception e) {
            super(e);
        }
    }
}
