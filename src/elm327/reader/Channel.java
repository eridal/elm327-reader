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
        flush();
    }

    private void write(String message) throws IOException {
        writter.write(message.getBytes());
        writter.write('\r');
        writter.flush();
    }

    private String read() throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        
        for (int b; (b = reader.read()) != -1;) {
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
                reader.skip(reader.available());
            }
        } catch(IOException e) {
            throw Throwables.propagate(e);
        }
    }

    public void send(String command) {
        String result;
        try {
            write(command);
            result = read();
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }

        checkResultMatchesCommand(command, result);
    }
    
    public void send(String command, boolean value) {
        send(String.format("%s%c", command , value ? '1' : '0'));
    }
    
    private void checkResultMatchesCommand(String command, String result) {
        if (!result.startsWith(command)) {
            throw new IllegalStateException();
        }
        String resultCode = result.substring(command.length()).trim(); 
        System.out.println(String.format("%s -> %s", command, resultCode));
    }

    public String read(String command) {
        try {
            write(command);
            return read();
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }
    
    public String read(PID pid) {
        return read(pid.code);
    }
}
