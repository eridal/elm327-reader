package elm327.reader;

public enum Unit {
    SCALAR(""),
    PERCENT("%"),
    DEGREE("°"),
    SECONDS("seconds"),
    MINUTES("minutes"),
    CELCIUS("°C"),
    KPA("kPa"),
    PA("Pa"),
    KM("Km"),
    KM_H("Km/h"),
    RPM("rpm"),
    GRAMS_SEC("grams/sec"),
    LIT_H("L/h"),
    VOLTS("Volts"),
    NM("Nm");

    public final String symbol;

    private Unit(String symbol) {
        this.symbol = symbol;
    }
}
