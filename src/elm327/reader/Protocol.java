package elm327.reader;

import com.google.common.base.Function;
import com.google.common.base.Strings;

class Protocol {
    
    /**
     * The current channel 
     */
    Channel channel;

    public Protocol (Channel channel) {
        this.channel = channel;
        
        final String[] CONFIGURE = {
            "ATZ",  // reset
            "ATL0", // disable line feeds
            "ATE0",  // disable echo
        };

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

    public <T> T getValue(final PID<T> pid) {
        return getValue(pid.code, new Function<String, T>() {
            @Override public T apply(String hex) {
                return pid.parse(hex);
            }
        });
    }
    
    private <T> T getValue(String code, Function<String, T> parse) {
        String hex = channel.read(code);
        
        if (Strings.isNullOrEmpty(hex)) {
            return null;
        }

        return parse.apply(hex);
    }
    
    private static final Function<String, boolean[]> PARSE_BOOLEANS = new Function<String, boolean[]>() {
        @Override public boolean[] apply(String hex) {
            
            final int FLAG_COUNT = 32;
            final int BYTE_COUNT = FLAG_COUNT / 8;
            
            boolean[] flags = new boolean[FLAG_COUNT];
            hex = hex.replace(" ", "");
            for (int i = 0; i < BYTE_COUNT; i++) {
                int byteValue = Integer.valueOf(hex.substring(i, i+1), 16); 
                for (int b = 0; b < 4; b++) {
                    int index = (i * 4) + 3 - b;
                    int mask = 1 << b;
                    flags[index] = (byteValue & mask) == mask;
                }
            }
            return flags;
        }
    };
    
    public boolean[] getPIDSupport() {
        boolean[] pids_01_to_20 = getValue("0100", PARSE_BOOLEANS);
        boolean[] pids_21_to_40 = getValue("0120", PARSE_BOOLEANS);
        
        boolean[] result = new boolean[pids_01_to_20.length + pids_21_to_40.length];
        System.arraycopy(pids_01_to_20, 0, result, 0, pids_01_to_20.length);
        System.arraycopy(pids_21_to_40, 0, result, pids_01_to_20.length, pids_21_to_40.length);
        
        return result;
    }
}
