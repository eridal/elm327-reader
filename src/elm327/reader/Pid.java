package elm327.reader;

public enum Pid {

    DistanceWithMalfuncionOff(
        "21", 1,
        "Distance traveled with malfunction indicator lamp (MIL) on",
        0, 65535, "km"
    ),

    ThrottlePosition(
        "11", 1,
        "Throttle position",
        0, 100, "%"
    );

    public final String code;
    public final String desc;
    public final String unit;

    private Pid(String code, int byteSize,
                String desc,
                int min, int max, String unit) {

        this.code = code;
        this.desc = desc;
        this.unit = unit;
    }
}
