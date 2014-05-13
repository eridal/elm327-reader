package elm327.reader.runner;

import java.io.IOException;
import java.net.Socket;

import com.google.common.base.Throwables;

import elm327.reader.Channel;

public class TcpRunner implements Runner {

        @Override public String params() {
            return "<host> <port>";
        }

        @Override public Channel connect(String[] args) throws IOException {
            Socket socket = createSocket(args[0], Integer.parseInt(args[1], 10));
            return new Channel(socket.getInputStream(), socket.getOutputStream());
        }

        private Socket createSocket(String host, int port) {
            try {
                return new Socket(host, port);
            } catch (IOException e) {
                throw Throwables.propagate(e);
            }
        }
    }
