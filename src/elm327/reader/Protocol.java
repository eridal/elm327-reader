package elm327.reader;


class Protocol {
    
    /**
     * The current channel 
     */
    Channel channel;

    public Protocol (Channel channel) {
        this.channel = channel;
        configure();
    }
    
    private void configure() {
        channel.send("ATZ");       // reset
        channel.send("ATSP3");     // set ISO 9141-2 protocol
        channel.send("ATL", false);// disable line feeds
        channel.send("ATE", false);// disable echo
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
        return channel.read(pid.code);
    }
}
