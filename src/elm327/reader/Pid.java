package elm327.reader;

public enum Pid {

    DistanceWithMalfuncionOff(
        "21", 1,
        "Distance traveled with malfunction indicator lamp (MIL) on",
        0f, 65.535, "km"
    );


    public final String code;
    public final String desc;
    public final String unit;

    private Pid(String code, int byteSize,
                String desc,
                double min, double max, String unit) {

        this.code = code;
        this.desc = desc;
        this.unit = unit;
    }
}
