package elm327.reader;

import com.google.common.base.Strings;

class Protocol {
    
    /**
     * The current channel 
     */
    Channel channel;

    public Protocol (Channel channel) {
        this.channel = channel;
        configure();
    }
    
    private final String[] CONFIGURE = {
        "ATZ",  // reset
        "ATL0", // disable line feeds
        "ATE0",  // disable echo
    };
    
    private void configure() {
        for (String cmd : CONFIGURE) {
            System.out.println(String.format("%s -> %s", cmd, channel.read(cmd)));
        }
    }

    public String getDeviceName() {
        return channel.read("AT@1");
    }
    
    public String getDeviceIdentifier() {
        return channel.read("AT@2");
    }

    public String getProtocolName() {
        return channel.read("ATDP");
    }
    
    public String getProtocolNumber() {
        return channel.read("ATDPN");
    }

    public String getIgnition() {
        return channel.read("ATIGN");
    }

    public String getVolts() {
        return channel.read("ATRV");
    }

    public String getData() {
        return channel.read("ATRD");
    }

    public String getValue(PID pid) {
        String hex = channel.read(pid.code);
        
        if (Strings.isNullOrEmpty(hex)) {
            return null;
        }

        return String.valueOf(pid.parse(hex));
    }
}
