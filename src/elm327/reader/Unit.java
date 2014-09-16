package elm327.reader;

public class Unit<T> {

    public final String symbol;

    Unit(String symbol) {
        this.symbol = symbol;
    }

    public Range<T> range(T min, T max) {
        return new Range<T>(this, min, max);
    }
}
