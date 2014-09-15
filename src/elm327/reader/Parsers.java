package elm327.reader;

import com.google.common.base.Function;
import com.google.common.base.Functions;

interface Parsers {

    Function<String, String> identity = Functions.<String>identity();

    Function<String, Integer> DISTANCE = null;

    Function<String, Integer> POSITION = new Function<String, Integer>() {
        @Override public Integer apply(String data) {
            return 1; // FIXME: parse data
        }
    };
}
