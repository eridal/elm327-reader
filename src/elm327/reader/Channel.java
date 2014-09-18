package elm327.reader;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class Channel {

    private final InputStream reader;
    private final OutputStream writter;

    public Channel(InputStream reader, OutputStream writter) {
        this.reader = new BufferedInputStream(reader);
        this.writter = new BufferedOutputStream(writter);
    }

    private void write(Message message) throws IOException {
        while (reader.available() > 0) {
            if (-1 == reader.read()) {
                break;
            }
        }
        writter.write(message.getBytes());
        writter.write('\r');
        writter.flush();
    }

    private String read() throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        boolean obdsim = false;
        for (;;) {
            if (reader.available() == 0 && buffer.size() > 0) {
                if (obdsim) {
                    break;
                }
            }
            int b = reader.read();
            if (b == -1) {
                break;
            }
            if (b == '>') {
                if (buffer.size() == 0) {
                    obdsim = true;
                    continue;
                } else {
                    break;
                }
            }
            if ((b == '\n' || b == '\r') && buffer.size() == 0) {
                continue;
            }
            buffer.write(b);
        }
        return buffer.toString();
    }

    public String send(Message message) throws IOException {
        write(message);
        String response = read();
        return response.trim();
    }
}
