package elm327.reader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.google.common.base.Throwables;

public class Channel {

    private InputStream reader;
    private OutputStream writter;

    public Channel(InputStream reader, OutputStream writter) {
        this.reader = reader;
        this.writter = writter;
    }

    private void write(String message) throws IOException {
        writter.write(String.format("%s\r", message).getBytes());
        writter.flush();
    }

    private String read() throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        while (true) {

            int b = reader.read();

            if (b == -1 ||
                b == '>') {
                break;
            }

            buffer.write(b);
        }

        return buffer.toString().trim();
    }

    private void flush() throws IOException {
        while (reader.available() > 0) {
            reader.read();
        }
    }

    public void send(String message) {
        try {
            write(message);
            flush();
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    public void setBoolean(String message, boolean value) {
        try {
            write(message + translate(value));
            flush();
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    public String getString(String message) {
        try {
            write(message);
            return read();
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    private char translate (boolean value) {
        return value ? '1' : '0';
    }
}
