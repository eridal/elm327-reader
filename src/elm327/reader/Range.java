package elm327.reader;

class Range<T> {

    public final Unit<T> unit;
    public final T min;
    public final T max;

    public Range(Unit<T> unit, T min, T max) {
        this.unit = unit;
        this.min = min;
        this.max = max;
    }
}
