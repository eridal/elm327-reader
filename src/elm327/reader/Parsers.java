package elm327.reader;

import java.util.Arrays;

import com.google.common.base.Function;

interface Parsers {

    final Function<String, byte[]> BYTES = new Function<String, byte[]>() {
        @Override public byte[] apply(String data) {

            byte[] bytes = new byte[20];

            int index = 0;
            int length = data.length();

            for (int b = 0; index < length; index++) {

                if (data.charAt(index) == ' ') {
                    continue;
                }

                String hex = data.substring(index, 2);
                bytes[b++] = Byte.valueOf(hex, 16);
            }

            return Arrays.copyOf(bytes, index);
        }
    };

    final Function<String, Integer> DISTANCE = new Function<String, Integer>() {
        @Override public Integer apply(String data) {
            byte[] bytes = BYTES.apply(data);
            return (bytes[0] * 256) + bytes[1];
        }
    };

    final Function<String, Double> POSITION = new Function<String, Double>() {
        @Override public Double apply(String data) {
            byte[] bytes = BYTES.apply(data);
            return (bytes[0] * 100) / 255.0;
        }
    };
}
