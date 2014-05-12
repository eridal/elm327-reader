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
	
	public String send(String message) {
		try {
			write(message);
			Thread.yield();
			return read();
		} catch (IOException e) {
			throw Throwables.propagate(e);
		}
	}

	private void write(String message) throws IOException {
		writter.write(String.format("%s\r", message).getBytes());
		writter.flush();
	} 
	
	private String read() throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();		
		for (int b; (b = reader.read()) != -1; ) {
			if (b == '>') break;
			buffer.write(b);
		}
		return new String(buffer.toByteArray()).trim();
	}
}
