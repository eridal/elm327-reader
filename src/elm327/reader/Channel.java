package elm327.reader;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.google.common.base.Strings;
import com.google.common.base.Throwables;

class Channel {

    private final InputStream reader;
    private final OutputStream writter;

    public Channel(InputStream reader, OutputStream writter) {
        this.reader = new BufferedInputStream(reader);
        this.writter = writter;
        flush();
    }

    private void write(String message) throws IOException {
        writter.write(message.getBytes());
        writter.write('\r');
        writter.flush();
    }

    private String read() throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int b;
        while ((b = reader.read()) != -1) {
            if (b == '>') {
                break;
            }
            buffer.write(b);
        }
        return buffer.toString();
    }

    private void flush() {
        try {
            while (reader.available() > 0) {
                int b = reader.read();
                if (b == -1) {
                    break;
                }
            }
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    private String writeAndRead(String command) {
        String result;
        try {
            write(command);
            result = read();
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }

        return handleResult(
            extractCommand(command, result)
        );
    }

    private static String extractCommand(String command, String result) {
        if (result.startsWith(command)) {
            return result.substring(command.length());
        }
        return result;

    }

    private static String handleResult(String result) {
        if (Strings.isNullOrEmpty(result) ||
            Strings.isNullOrEmpty(result = result.trim())) {
            return null;
        }
        if (Result.UNKNOWN.matches(result)) {
            return null;
        }
        if (Result.NO_DATA.matches(result)) {
            return null;
        }
        return result;
    }

    public String read(Command c) {
        return writeAndRead(c.toString());
    }

    public <T> T read(Command.Read<T> c) {
        final String result = writeAndRead(c.toString());
        return c.parse(result);
    }

    public <T> T read(PID<T> pid) {
        final String result = writeAndRead(pid.toString());
        return pid.parse(result);
    }

    public boolean send(Command c) {
        return Result.OK.matches(read(c));
    }
}
