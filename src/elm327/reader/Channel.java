package elm327.reader;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class Channel {

    private final InputStream reader;
    private final OutputStream writter;

    public Channel(InputStream reader, OutputStream writter) {
        this.reader = new BufferedInputStream(reader);
        this.writter = writter;
    }

    private void write(String message) throws IOException {
        flush();
        writter.write(message.getBytes());
        writter.write('\r');
        writter.flush();
    }

    private String read() throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        for (;;) {
            if (reader.available() == 0 && buffer.size() > 0) {
                break;
            }
            int b = reader.read();
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

    private void flush() throws IOException {
        while (reader.available() > 0) {
            int b = reader.read();
            if (b == -1) {
                break;
            }
        }
    }

    public String send(Message message) throws IOException {
        write(message.code());
        return read();
    }
}
