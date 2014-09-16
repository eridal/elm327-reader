package elm327.reader;

abstract class Parser<T> {

    public T min;
    public T max;

    Parser(T min, T max) {
        this.min = min;
        this.max = max;
    }

    abstract T parse(String data);
}