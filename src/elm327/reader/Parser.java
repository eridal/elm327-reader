package elm327.reader;


abstract class Parser<T extends Comparable<T>> {

    public T min;
    public T max;

    Parser(T min, T max) {
        this.min = min;
        this.max = max;
    }

    abstract T parse(String data);

    public T valueOf(String data) throws NumberFormatException {

        final T result = parse(data);

        if (null != result) {

            int cmin = min.compareTo(result);
            int cmax = max.compareTo(result);

            if (0 < cmin || cmax < 0) {
                throw new NumberFormatException(
                    String.format("value (%s) is out of range: [%s - %s]", result, min, max)
                );
            }
        }

        return result;
    }
}