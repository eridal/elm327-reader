package elm327.reader.runner;

import java.io.IOException;

import elm327.reader.Channel;

public interface Runner {

	public String params();
	Channel connect(String[] params) throws IOException;
}
