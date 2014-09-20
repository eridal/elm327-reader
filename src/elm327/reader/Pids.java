package elm327.reader;


public class Pids {

    private Pids() {}

    static final Pid<Double> CalculatedEngineLoad = new Pid<Double>("04", Unit.PERCENT, Parsers.PERCENT);
    static final Pid<Integer> EngineCoolantTemperature = new Pid<Integer>("05", Unit.CELCIUS, Parsers.TEMPERATURE);

    static final Pid<Double> ShortTermFuel_Bank_1 = new Pid<Double>("06", Unit.PERCENT, Parsers.PERCENT_TRIM);
    static final Pid<Double> LongTermFuel_Bank_1 = new Pid<Double>("07", Unit.PERCENT, Parsers.PERCENT_TRIM);
    static final Pid<Double> ShortTermFuel_Bank_2 = new Pid<Double>("08", Unit.PERCENT, Parsers.PERCENT_TRIM);
    static final Pid<Double> LongTermFuel_Bank_2 = new Pid<Double>("09", Unit.PERCENT, Parsers.PERCENT_TRIM);

    static final Pid<Integer> FuelPressure = new Pid<Integer>("0A", Unit.KPA, Parsers.KPA_GAUGE);

    static final Pid<Integer> IntakeManifoldAbsolutePressure = new Pid<Integer>("0B", Unit.KPA, Parsers.KPA_GAUGE);

    static final Pid<Double> EngineRPM = new Pid<Double>("0C", Unit.RPM, Parsers.RPM);

    static final Pid<Integer> VehicleSpeed = new Pid<Integer>("DC", Unit.KM_H, Parsers.FIRST_BYTE);
    static final Pid<Double> TimingAdvance = new Pid<Double>("0E", Unit.DEGREE, new Parser<Double>(-64.0, 63.5) {
        @Override public Double parse(String data) {
            short[] bytes = Parsers.toBytes(data, 1);
            return (bytes[0] - 128) / 2.0;
        };
    });

    static final Pid<Integer> IntakeAirTemperature = new Pid<Integer>("0F", Unit.CELCIUS, Parsers.TEMPERATURE);

    static final Pid<Double> MAF_AirFlowRate = new Pid<Double>("10", Unit.GRAMS_SEC, new Parser<Double>(0.0, 655.35) {
        @Override public Double parse(String data) {
            return Parsers.TO_SHORT.parse(data) / 100.0;
        }
    });

    static final Pid<Double> ThrottlePosition = new Pid<Double>("11", Unit.PERCENT, Parsers.PERCENT);
    static final Pid<Integer> RunTimeSinceEngineStart = new Pid<Integer>("1F", Unit.SECONDS, Parsers.TO_SHORT);
    static final Pid<Integer> DistanceTraveledWithMalFunctionIndicatorLamp = new Pid<Integer>("21", Unit.KM, Parsers.TO_SHORT);

    static final Pid<Double> FuelRailPressure_RelativoToManifoldVacuum = new Pid<Double>("22", Unit.KPA, new Parser<Double>(0.0, 5177.265) {
        @Override public Double parse(String data) {
            return Parsers.TO_SHORT.parse(data) * 0.079;
        }
    });

    static final Pid<Integer> FuelRailPressure = new Pid<Integer>("23", Unit.KPA, Parsers.TO_SHORT_TIMES_10);

    static final Pid<Double> EGR_Error = new Pid<Double>("2D", Unit.PERCENT, Parsers.PERCENT_TRIM);

    static final Pid<Double> CommandedEGR = new Pid<Double>("2C", Unit.PERCENT, Parsers.PERCENT_TRIM);
    static final Pid<Double> CommandedEvaporativePurge = new Pid<Double>("2E", Unit.PERCENT, Parsers.PERCENT);

    static final Pid<Integer> NumbersOfWarmUpsSinceCodesCleared = new Pid<Integer>("30", Unit.SCALAR, Parsers.FIRST_BYTE);

    static final Pid<Integer> DistanceTraveledSinceCodesCleared = new Pid<Integer>("31", Unit.KM, Parsers.TO_SHORT);

    static final Pid<Double> Evap_SystemVaporPressure = new Pid<Double>("32", Unit.PA, new Parser<Double>(-8192.0, 8192.0) {
        @Override public Double parse(String data) {
            return Parsers.TO_SHORT.parse(data) / 4.0;
        };
    });

    static final Pid<Integer> BarometricPressure = new Pid<Integer>("33", Unit.KPA, Parsers.FIRST_BYTE);

    static final Pid<Double> CatalystTemperature_Bank1_Sensor1 = new Pid<Double>("3C", Unit.CELCIUS, Parsers.TEMPERATURE_CATALYST);
    static final Pid<Double> CatalystTemperature_Bank2_Sensor1 = new Pid<Double>("3D", Unit.CELCIUS, Parsers.TEMPERATURE_CATALYST);
    static final Pid<Double> CatalystTemperature_Bank1_Sensor2 = new Pid<Double>("3E", Unit.CELCIUS, Parsers.TEMPERATURE_CATALYST);
    static final Pid<Double> CatalystTemperature_Bank2_Sensor2 = new Pid<Double>("3F", Unit.CELCIUS, Parsers.TEMPERATURE_CATALYST);

    static final Pid<Double> ControlModuleVoltage = new Pid<Double>("42", Unit.VOLTS, new Parser<Double>(0.0, 65535.0) {
        @Override Double parse(String data) {
            return Parsers.TO_SHORT.parse(data) / 1000.0;
        };
    });

    static final Pid<Double> AbsoluteLoadValue = new Pid<Double>("43", Unit.PERCENT, new Parser<Double>(0.0, 25700.0) {
        @Override Double parse(String data) {
            return Parsers.TO_SHORT.parse(data) * 100 / 255.0;
        };
    });

    static final Pid<Double> CommandEquivalenceRratio = new Pid<Double>("44", Unit.PERCENT, new Parser<Double>(0.0, 2.0) {
        @Override Double parse(String data) {
            return Parsers.TO_SHORT.parse(data) / 32768.0;
        };
    });

    static final Pid<Integer> AmbientAirTemperature = new Pid<Integer>("46", Unit.CELCIUS, Parsers.TEMPERATURE);

    static final Pid<Integer> TimeRunWithMIL_ON = new Pid<Integer>("4D", Unit.MINUTES, Parsers.TO_SHORT);
    static final Pid<Integer> TimeSinceTroubleCodesCleared = new Pid<Integer>("4E", Unit.MINUTES, Parsers.TO_SHORT);

    static final Pid<Double> AbsoluteEvapSystemVaporPressure = new Pid<Double>("53", Unit.KPA, new Parser<Double>(0.0, 327.675) {
        @Override Double parse(String data) {
            return Parsers.TO_SHORT.parse(data) / 200.0;
        };
    });

    static final Pid<Integer> EvapSystemVaporPressure = new Pid<Integer>("54", Unit.PA, new Parser<Integer>(-32767, 32768) {
        @Override Integer parse(String data) {
            return Parsers.TO_SHORT.parse(data) - 32767;
        };
    });

    static final Pid<Double> ShortTermSecondaryOxygenSensorTrim_Bank_1 = new Pid<Double>("55", Unit.PERCENT, Parsers.PERCENT_TRIM);
    static final Pid<Double> ShortTermSecondaryOxygenSensorTrim_Bank_3 = new Pid<Double>("55", Unit.PERCENT, Parsers.PERCENT_TRIM_B);

    static final Pid<Double> LongTermSecondaryOxygenSensorTrim_Bank_1 = new Pid<Double>("56", Unit.PERCENT, Parsers.PERCENT_TRIM);
    static final Pid<Double> LongTermSecondaryOxygenSensorTrim_Bank_3 = new Pid<Double>("56", Unit.PERCENT, Parsers.PERCENT_TRIM_B);

    static final Pid<Double> ShortTermSecondaryOxygenSensorTrim_Bank_2 = new Pid<Double>("57", Unit.PERCENT, Parsers.PERCENT_TRIM);
    static final Pid<Double> ShortTermSecondaryOxygenSensorTrim_Bank_4 = new Pid<Double>("57", Unit.PERCENT, Parsers.PERCENT_TRIM_B);

    static final Pid<Double> LongTermSecondaryOxygenSensorTrim_Bank_2 = new Pid<Double>("58", Unit.PERCENT, Parsers.PERCENT_TRIM);
    static final Pid<Double> LongTermSecondaryOxygenSensorTrim_Bank_4 = new Pid<Double>("58", Unit.PERCENT, Parsers.PERCENT_TRIM_B);

    static final Pid<Integer> FuelRailPressure_Absolute = new Pid<Integer>("59", Unit.KPA, Parsers.TO_SHORT_TIMES_10);

    static final Pid<Integer> EngineOilTemperature = new Pid<Integer>("5C", Unit.CELCIUS, Parsers.TEMPERATURE);

    static final Pid<Double> FuelInjectionTiming = new Pid<Double>("5D", Unit.DEGREE, new Parser<Double>(-210.00, 301.992) {
        @Override Double parse(String data) {
            return (Parsers.TO_SHORT.parse(data) - 26880) / 128.0;
        }
    });

    static final Pid<Double> EngineFuelRate = new Pid<Double>("5E", Unit.LIT_H, new Parser<Double>(0.0, 3212.75) {
        @Override Double parse(String data) {
            return Parsers.TO_SHORT.parse(data) * 0.05;
        }
    });

    static final Pid<Integer> DriversDemandEngine_PercentTorque = new Pid<Integer>("61", Unit.PERCENT, Parsers.TORQUE);
    static final Pid<Integer> ActualEngine_PercentTorque = new Pid<Integer>("62", Unit.PERCENT, Parsers.TORQUE);

    static final Pid<Integer> EngineReferenceTorque = new Pid<Integer>("63", Unit.NM, Parsers.TO_SHORT);
}