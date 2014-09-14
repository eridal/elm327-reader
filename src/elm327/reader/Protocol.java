package elm327.reader;


class Protocol {

    static final Command[] DEFAULT_SETUP = {
        Command.Do.Reset,
        Command.Disable.Echo,
        Command.Disable.LineFeeds,
        Command.Disable.PrintSpaces,
    };

    private final Channel channel;

    public Protocol (Channel channel) {
        this(channel, DEFAULT_SETUP);
    }

    public Protocol (final Channel channel, Command ... setup) {
        this.channel = channel;
        initialize(setup);
    }

    public void initialize(Command ... setup) {
        channel.flush();
        for (Command cmd : setup) {
            channel.send(cmd);
        }
    }

    public String get(Command.Get command) {
        return channel.send(command);
    }

    public boolean send(Command.Send command) {
        return Result.OK.matches(
            channel.send(command)
        );
    }

    public <T> T read(Command.Read<T> command) {
        String result = channel.send(command);
        return command.parse(result);
    }

    public <T> String read(PID<T> pid) {
        return channel.send(pid);
    }
}
