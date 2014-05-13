package elm327.reader.runner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import elm327.reader.Channel;

public class FileRunner implements Runner {

    @Override public String params() {
        return "<path>";
    }

    @Override public Channel connect(String[] params) throws IOException {
        File file = new File(params[0]);
        return new Channel(new FileInputStream(file),
                           new FileOutputStream(file));
    }
}
