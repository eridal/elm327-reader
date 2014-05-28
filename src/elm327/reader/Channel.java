package elm327.reader;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.google.common.base.Strings;
import com.google.common.base.Throwables;

class Channel {

    private InputStream reader;
    private OutputStream writter;
    
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
        StringBuffer buffer = new StringBuffer();
        try {
            while (reader.available() > 0) {
                int b = reader.read();
                if (b != -1) {
                    buffer.append(b);
                } else {
                    break;
                }
            }
        } catch(IOException e) {
            throw Throwables.propagate(e);
        }
        if (buffer.length() > 0) {
            System.out.println(String.format("discarded: %s", buffer.toString()));
        }
    }

    private String extractCommand(String command, String result) {
        if (result.startsWith(command)) {
            return result.substring(command.length());
        }
        return result;
        
    }

    private String handleResult(String result) {
        if (Strings.isNullOrEmpty(result)) {
            return null;
        }
        if (Result.UNKNOWN.matches(result)) {
            return null;
        }
        if (Result.NO_DATA.matches(result)) {
            return null;
        }
        return result.trim();
    }

    public String read(String command) {
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
    
    public boolean send(String command) {
        return Result.OK.matches(read(command));
    }
    
    public boolean send(String command, boolean value) {
        return send(String.format("%s%c", command , value ? '1' : '0'));
    }
}
