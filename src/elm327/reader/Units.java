package elm327.reader;

public class Units {

    static final Unit<Integer> SCALAR = new Unit<Integer>("");
    static final Unit<Double> PERCENT = new Unit<Double>("%");
    static final Unit<Double> DEGREE = new Unit<Double>("°");

    static final Unit<Integer> SECONDS = new Unit<Integer>("seconds");
    static final Unit<Integer> MINUTES = new Unit<Integer>("minutes");

    static final Unit<Double> CELCIUS = new Unit<Double>("°C");

    static final Unit<Double> KPA = new Unit<Double>("kPa");
    static final Unit<Double> PA = new Unit<Double>("Pa");

    static final Unit<Double> KM = new Unit<Double>("Km");
    static final Unit<Double> KM_H = new Unit<Double>("Km/h");
    static final Unit<Double> RPM = new Unit<Double>("rpm");

    static final Unit<Double> GRAMS_SEC = new Unit<Double>("grams/sec");
    static final Unit<Double> LIT_H = new Unit<Double>("L/h");


    static final Unit<Double> VOLTS = new Unit<Double>("Volts");

    static final Unit<Double> NM = new Unit<Double>("Nm");
}