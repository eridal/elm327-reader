package elm327.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Channels {
    
    public static Channel fromFile(File file) throws FileNotFoundException {
        return new Channel(new FileInputStream(file),
                           new FileOutputStream(file));
    }
    
    public static Channel fromSocket(Socket socket) throws IOException {
        return new Channel(socket.getInputStream(), socket.getOutputStream());
    }
}
