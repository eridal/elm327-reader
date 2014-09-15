package elm327.reader;

import java.util.Arrays;

class Parsers {

    private Parsers() { }

    static byte[] toBytes(String data) {
        return toBytes(data, 20);
    }

    static byte[] toBytes(String data, int maxSize) {

        assert maxSize > 0;

        byte[] bytes = new byte[maxSize];

        int b = 0;
        int index = 0;
        int length = data.length();

        for (; b < maxSize && index < length; index++) {

            if (data.charAt(index) == ' ') {
                continue;
            }

            String hex = data.substring(index, 2);
            bytes[b++] = Byte.valueOf(hex, 16);
        }

        if (b < maxSize) {
            return Arrays.copyOf(bytes, b);
        } else {
            return bytes;
        }
    };

    static int distance(String data) {
        byte[] bytes = toBytes(data, 2);
        return (bytes[0] * 256) + bytes[1];
    };

    static double position(String data) {
        byte[] bytes = toBytes(data, 1);
        return (bytes[0] * 100) / 255.0;
    };

    static int speed(String data) {
        byte[] bytes = toBytes(data, 1);
        return bytes[0];
    };
}
