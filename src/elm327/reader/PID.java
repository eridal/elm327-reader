package elm327.reader;

enum PID {

    PIDS_SUPPORT_FROM_01_TO_20      ("0100", "PIDs supported [01 - 20]"),
    PIDS_SUPPORT_FROM_21_TO_40      ("0120", "PIDs supported [21 - 40]"),
    // Includes malfunction indicator lamp (MIL) status and number of DTCs.
    MONITOR_STATUS_SINCE_DTC_CLEARED("0101", "Monitor status since DTCs cleared"),
    FUEL_SYSTEM_STATUS              ("0103", "Fuel system status"),
    ENGINE_LOAD_VALUE               ("0104", "Calculated engine load value"),
    ENGINE_COOLANT_TEMP             ("0105", "Engine coolant temperature"),
    SHORT_TERM_FUEL                 ("0106", "Short term fuel % trim—Bank 1"),
    LONG_TERM_FUEL                  ("0107", "Long term fuel % trim—Bank 1"),
    ENGINE_RPM                      ("010C", "Engine RPM"),
    VEHICLE_SPEED                   ("010D", "Vehicle speed"),
    TIMING_ADVANCE                  ("010E", "Timing advance"),
    INTAKE_AIR_TEMPERATURE          ("010F", "Intake air temperature"),
    AIR_FLOW_RATE                   ("0110", "MAF air flow rate"),
    THROTTLE_POSITION               ("0111", "Oxygen sensors present"),
    OXYGEN_SENSORS_PRESENT          ("0113", "Oxygen sensors present"),
    BANK_1_SENSOR_1                 ("0114", "Bank 1, Sensor 1: Oxygen sensor voltage, Short term fuel trim"),
    BANK_1_SENSOR_2                 ("0115", "Bank 1, Sensor 2: Oxygen sensor voltage, Short term fuel trim"),
    ODB_STANDARD                    ("011C", "OBD standards this vehicle conforms to"),
    DISTANCE_TRAVELED_WITH_LAMP_ON  ("0121", "Distance traveled with malfunction indicator lamp (MIL) on");

    public final String code;
    public final String desc;

    PID(String code) {
        this(code, null);
    }
    
    PID (String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
