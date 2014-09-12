package elm327.reader;

import java.util.ArrayList;
import java.util.List;

class Protocol {

    static final Command[] DEFAULT_SETUP = {
        Command.Set.AllToDefaults,
        Command.Do.Reset,
        Command.Disable.LineFeeds,
        Command.Disable.Echo,
        Command.Disable.PrintSpaces,
    };

    /**
     * The current channel
     */
    private final Channel channel;

    public Protocol (Channel channel) {
        this(channel, DEFAULT_SETUP);
    }

    public Protocol (final Channel channel, Command ... setup) {
        this.channel = channel;
        initialize(setup);
    }

    public String[] initialize(Command ... setup) {
        List<String> result = new ArrayList<String>();

        for (Command cmd : setup) {
            result.add(channel.read(cmd));
        }

        return result.toArray(new String[0]);
    }

    public String get(Command.Get command) {
        return channel.read(command);
    }

    public <T> T read(Command.Read<T> command) {
        return channel.read(command);
    }

    public <T> T read(final PID<T> pid) {
        return channel.read(pid);
    }
}
