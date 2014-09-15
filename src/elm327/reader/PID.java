package elm327.reader;

import com.google.common.base.Function;
import com.google.common.base.Splitter;

class PID<T> implements Command<T> {

    private static final Function<String, byte[]> TO_BYTES = new Function<String, byte[]>() {
        @Override public byte[] apply(String hex) {
            int i = 0;
            int bytesCount = hex.replaceAll("[0-9A-Fa-f]+", "").length() + 1;
            byte[] bytes = new byte[bytesCount];
            for (String h : Splitter.on(" ").split(hex)) {
                assert i <= bytes.length;
                bytes[i++] = Byte.valueOf(h.substring(0, 2), 16);
            }
            return bytes;
        }
    };

    private static final <R> Function<String, R> WITH_BYTES(final Function<byte[], R> fn) {
        return new Function<String, R>() {
            @Override public R apply(String hex) {
                return fn.apply(TO_BYTES.apply(hex));
            }
        };
    }

    private static final Function<String, Double> BANK_CONVESOR = WITH_BYTES(new Function<byte[], Double>() {
        @Override public Double apply(byte[] bytes) {
            return (bytes[0] - 128) * 100 / 128.0;
        }
    });

    private static final Function<String, Double> BANK_CONVESOR_IF_SENSOR = WITH_BYTES(new Function<byte[], Double>() {
        @Override public Double apply(byte[] bytes) {
            if (bytes[1] == 0xFF) {
                return (bytes[1] - 128) * 100 / 128.0;
            }
            return bytes[0] / 200.0;
        }
    });

    private static final Function<String, String> RETURNS_STRING = new Function<String, String>() {
        @Override public String apply(String hex) {
            return hex;
        }
    };

    private static final Function<String, Integer> RETURNS_FIRST_BYTE = WITH_BYTES(new Function<byte[], Integer>() {
        @Override public Integer apply(byte[] bytes) {
            return Integer.valueOf(bytes[0]);
        }
    });

    private static final Function<String, Integer> RETURNS_SHORT = WITH_BYTES(new Function<byte[], Integer>() {
        @Override public Integer apply(byte[] bytes) {
            return (bytes[0] * 256) + bytes[1];
        }
    });

    private static final Function<String, Double> RETURNS_PERCENTAGE = WITH_BYTES(new Function<byte[], Double>() {
        @Override public Double apply(byte[] bytes) {
            return bytes[0] * 100 / 255.0;
        }
    });

    // Includes malfunction indicator lamp (MIL) status and number of DTCs.
    public static final PID<String> MONITOR_STATUS_SINCE_DTC_CLEARED = new PID<String>("0101", "Monitor status since DTCs cleared", "", RETURNS_STRING);
    public static final PID<String> FUEL_SYSTEM_STATUS = new PID<String>("0103","Fuel system status", "", RETURNS_STRING);

    public static final PID<Double> ENGINE_LOAD_VALUE = new PID<Double>("0104", "Calculated engine load value", "%", RETURNS_PERCENTAGE);
    public static final PID<Integer> ENGINE_COOLANT_TEMP = new PID<Integer>("0105", "Engine coolant temperature", "°C", WITH_BYTES(new Function<byte[], Integer>() {
        @Override public Integer apply(byte[] bytes) {
            return bytes[0] - 4;
        }
    }));

    public static final PID<Double> SHORT_TERM_FUEL_BANK_1 = new PID<Double>("0106", "Short term fuel % trim—Bank 1", "%", BANK_CONVESOR);
    public static final PID<Double> LONG_TERM_FUEL_BANK_1  = new PID<Double>("0107", "Long  term fuel % trim—Bank 1", "%", BANK_CONVESOR);
    public static final PID<Double> SHORT_TERM_FUEL_BANK_2 = new PID<Double>("0108", "Short term fuel % trim—Bank 2", "%", BANK_CONVESOR);
    public static final PID<Double> LONG_TERM_FUEL_BANK_2  = new PID<Double>("0109", "Long  term fuel % trim—Bank 2", "%", BANK_CONVESOR);

    public static final PID<Integer> FUEL_PRESSURE = new PID<Integer>("010A", "Fuel Pressure", "kPa", WITH_BYTES(new Function<byte[], Integer>() {
        @Override public Integer apply(byte[] bytes) {
            return bytes[0] * 3;
        }
    }));

    public static final PID<Integer> ENGINE_RPM = new PID<Integer>("010C", "Engine RPM", "rpm", WITH_BYTES(new Function<byte[], Integer>() {
        @Override public Integer apply(byte[] bytes) {
            return ((bytes[1] * 256) + bytes[0]) / 4;
        }
    }));

    public static final PID<Integer> VEHICLE_SPEED = new PID<Integer>("010D", "Vehicle speed", "km/h", RETURNS_FIRST_BYTE);

    public static final PID<Double> TIMING_ADVANCE = new PID<Double>("010E", "Timing advance", "°", WITH_BYTES(new Function<byte[], Double>() {
        @Override public Double apply(byte[] bytes) {
            return (bytes[0] - 128) / 2.0;
        }
    }));
    public static final PID<Integer> INTAKE_AIR_TEMPERATURE = new PID<Integer>("010F", "Intake air temperature", "°C", WITH_BYTES(new Function<byte[], Integer>() {
        @Override public Integer apply(byte[] bytes) {
            return bytes[0] - 40;
        }
    }));
    public static final PID<Double> AIR_FLOW_RATE = new PID<Double>("0110", "MAF air flow rate", "grams/sec", WITH_BYTES(new Function<byte[], Double>() {
        @Override public Double apply(byte[] bytes) {
            return ((bytes[0] * 256) + bytes[1]) / 100.0;
        }
    }));

    public static final PID<Double> THROTTLE_POSITION = new PID<Double>("0111", "Oxygen sensors present", "%", RETURNS_PERCENTAGE);

    public static final PID<boolean[]> OXYGEN_SENSORS_PRESENT = new PID<boolean[]>("0113", "Oxygen sensors present", "", WITH_BYTES(new Function<byte[], boolean[]>() {
        @Override public boolean[] apply(byte[] bytes) {
            boolean[] result = new boolean[8];
            byte b = bytes[0];
            for (int i=0; i < 8; i++) {
                int mask = 1 << (8 - i - 1);
                result[i] = (b & mask) != 0;
            }
            return result;
        }
    }));

    public static final PID<Double> BANK_1_SENSOR_1 = new PID<Double>("0114", "Bank 1, Sensor 1: Oxygen sensor voltage, Short term fuel trim", "% volt", BANK_CONVESOR_IF_SENSOR);
    public static final PID<Double> BANK_1_SENSOR_2 = new PID<Double>("0115", "Bank 1, Sensor 2: Oxygen sensor voltage, Short term fuel trim", "% volt", BANK_CONVESOR_IF_SENSOR);
    public static final PID<Double> BANK_1_SENSOR_3 = new PID<Double>("0116", "Bank 1, Sensor 1: Oxygen sensor voltage, Short term fuel trim", "% volt", BANK_CONVESOR_IF_SENSOR);
    public static final PID<Double> BANK_1_SENSOR_4 = new PID<Double>("0117", "Bank 1, Sensor 2: Oxygen sensor voltage, Short term fuel trim", "% volt", BANK_CONVESOR_IF_SENSOR);

    public static final PID<Double> BANK_2_SENSOR_1 = new PID<Double>("0118", "Bank 1, Sensor 1: Oxygen sensor voltage, Short term fuel trim", "% volt", BANK_CONVESOR_IF_SENSOR);
    public static final PID<Double> BANK_2_SENSOR_2 = new PID<Double>("0119", "Bank 1, Sensor 2: Oxygen sensor voltage, Short term fuel trim", "% volt", BANK_CONVESOR_IF_SENSOR);
    public static final PID<Double> BANK_2_SENSOR_3 = new PID<Double>("011A", "Bank 1, Sensor 1: Oxygen sensor voltage, Short term fuel trim", "% volt", BANK_CONVESOR_IF_SENSOR);
    public static final PID<Double> BANK_2_SENSOR_4 = new PID<Double>("011B", "Bank 1, Sensor 2: Oxygen sensor voltage, Short term fuel trim", "% volt", BANK_CONVESOR_IF_SENSOR);

    public static final PID<String> ODB_STANDARD = new PID<String>("011C", "OBD standards this vehicle conforms to", "", RETURNS_STRING);

    public static final PID<Integer> RUNTIME_SINCE_ENGINE_START = new PID<Integer>("011F", "Run time since engine start", "s", RETURNS_SHORT);



    public final String code;
    public final String desc;
    public final String unit;
    public final Function<String, T> converter;

    private PID(String code, String desc) {
        this(code, desc, null);
    }

    private PID(String code, String desc, String unit) {
        this(code, desc, null, null);
    }

    private PID(String code, String desc, String unit, Function<String, T> converter) {
        this.code = code;
        this.desc = desc;
        this.unit = unit;
        this.converter = converter;
    }

    @Override public T parse(String hex) {
        return converter.apply(hex);
    }

    @Override public String toMessage() {
        return code;
    }
}
