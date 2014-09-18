package elm327.reader;

import java.io.IOException;

@SuppressWarnings("serial")
public class ProtocolException extends IOException {

    public ProtocolException(String message) {
        super(message);
    }

}
