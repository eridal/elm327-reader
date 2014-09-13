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

    private void write(Command c) throws IOException {
        String message = c.toCommandString();
        writter.write(message.getBytes());
        writter.write('\r');
        writter.flush();
    }

    private String read() throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        for (int b;;) {
            if (reader.available() == 0 && buffer.size() > 0) {
                break;
            }
            b = reader.read();
            if (b == -1) {
                break;
            }
            if (b == '>') {
                if (buffer.size() == 0) {
                    continue;
                } else {
                    break;
                }
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

    private String writeAndRead(Command cmd) {
        try {
            write(cmd);
            return read();
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    private static String extractCommand(Command cmd, String result) {
        String message = cmd.toCommandString();
        if (result.startsWith(message)) {
            return result.substring(message.length());
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

    public String send(Command cmd) {
        String result = writeAndRead(cmd);
        return handleResult(
            extractCommand(cmd, result)
        );
    }
}
