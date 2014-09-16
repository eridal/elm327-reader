package elm327.reader;

public enum Pid {

    CalculatedEngineLoad(
        "04", 1,
        Units.PERCENT.range(0.0, 100.0),
        "A*100/255"
    ),

    EngineCoolantTemperature(
        "05", 1,
        Units.CELCIUS.range(-40.0, 215.0),
        "A-40"
    ),

    ShortTermFuel_Bank_1(
        "06", 1,
        Units.PERCENT.range(-100.0, 99.22),
        "(A-128) * 100/128"
    ),

    LongTermFuel_Bank_1(
        "07", 1,
        Units.PERCENT.range(-100.0, 99.22),
        "(A-128) * 100/128"
    ),

    ShortTermFuel_Bank_2(
        "08", 1,
        Units.PERCENT.range(-100.0, 99.22),
        "(A-128) * 100/128"
    ),

    LongTermFuel_Bank_2(
        "09", 1,
        Units.PERCENT.range(-100.0, 99.22),
        "(A-128) * 100/128"
    ),

    FuelPressure(
        "0A", 1,
        Units.KPA.range(0.0, 765.0),
        "A*3"
    ),

    IntakeManifoldAbsolutePressure(
        "0B", 1,
        Units.KPA.range(0.0, 255.0),
        "A"
    ),


    EngineRPM (
        "0C", 2,
        Units.RPM.range(0.0, 16383.75),
        "((A*256)+B)/4"
    ),

    VehicleSpeed  (
        "0D", 1,
        Units.KM_H.range(0.0, 255.0),
        "A"
    ),

    TimingAdvance (
        "0E", 1,
        Units.DEGREE.range(-64.0, 63.5),
        "(A-128)/2"
    ),

    IntakeAirTemperature (
        "0F", 1,
        Units.CELCIUS.range(-40.0, 215.0),
        "A-40"
    ),

    MAF_AirFlowRate  (
        "10", 2,
        Units.GRAMS_SEC.range(0.0, 655.35),
        "((A*256)+B) / 100"
    ),

    ThrottlePosition  (
        "11", 1,
        Units.DEGREE.range(0.0, 100.0),
        "A*100/255"
    ),

    RunTimeSinceEngineStart(
        "1F", 2,
        Units.SECONDS.range(0, 65535),
        "(A*256)+B"
    ),

    DistanceTraveledWithMalfunctionIndicatorLamp(
        "21", 2,
        Units.KM.range(0.0, 65535.0),
        "(A*256)+B"
    ),

    FuelRailPressure_RelativoToManifoldVacuum(
        "22", 2,
        Units.KPA.range(0.0, 5177.265),
        "((A*256)+B) * 0.079"
    ),

    FuelRailPressure(
        "23", 2,
        Units.KPA.range(0.0, 655350.0),
        "((A*256)+B) * 10"
    ),

    CommandedEGR(
        "2C", 1,
        Units.PERCENT.range(0.0, 100.0),
        "A*100/255"
    ),

    EGR_Error  (
        "2D", 1,
        Units.PERCENT.range(-100.0, 99.22),
        "(A-128) * 100/128"
    ),

    CommandedEvaporativePurge(
        "2E", 1,
        Units.PERCENT.range(0.0, 100.0),
        "A*100/255"
    ),

    FuelLevelInput   (
        "2F", 1,
        Units.PERCENT.range(0.0, 100.0),
        "A*100/255"
    ),

    NumbersOfWarmUpsSinceCodesCleared(
        "30", 1,
        Units.SCALAR.range(0, 255),
        "A"
    ),

    DistanceTraveledSinceCodesCleared  (
        "31", 2,
        Units.KM.range(0.0, 65535.0),
        "(A*256)+B"
    ),

    Evap_SystemVaporPressure(
        "32", 2,
        Units.PA.range(-8192.0, 8192.0),
        "((A*256)+B)/4"
    ),

    BarometricPressure(
        "33", 1,
        Units.KPA.range(0.0, 255.0),
        "A"
    ),

    CatalystTemperature_Bank1_Sensor1(
        "3C", 2,
        Units.CELCIUS.range(-40.0, 6513.5),
        "((A*256)+B)/10 - 40"
    ),

    CatalystTemperature_Bank2_Sensor1(
        "3D", 2,
        Units.CELCIUS.range(-40.0, 6513.5),
        "((A*256)+B)/10 - 40"
    ),

    CatalystTemperature_Bank1_Sensor2(
        "3E", 2,
        Units.CELCIUS.range(-40.0, 6513.5),
        "((A*256)+B)/10 - 40"
    ),

    CatalystTemperature_Bank2_Sensor2(
        "3E", 2,
        Units.CELCIUS.range(-40.0, 6513.5),
        "((A*256)+B)/10 - 40"
    ),

    ControlModuleVoltage (
        "42", 2,
        Units.VOLTS.range(0.0, 65535.0),
        "((A*256)+B)/1000"
    ),

    AbsoluteLoadValue(
        "43", 2,
        Units.PERCENT.range(0.0, 25700.0),
        "((A*256)+B)*100/255"
    ),

    CommandEquivalenceRratio  (
        "44", 2,
        Units.SCALAR.range(0, 2),
        "((A*256)+B)/32768"
    ),

    RelativeThrottlePosition (
        "45", 1,
        Units.PERCENT.range(0.0, 100.0),
        "A*100/255"
    ),

    AmbientAirTemperature(
        "46", 1,
        Units.CELCIUS.range(-40.0, 215.0),
        "A-40"
    ),

    AbsoluteThrottlePosition_B   (
        "47", 1,
        Units.PERCENT.range(0.0, 100.0),
        "A*100/255"
    ),

    AbsoluteThrottlePosition_C   (
        "48", 1,
        Units.PERCENT.range(0.0, 100.0),
        "A*100/255"
    ),

    AcceleratorPedalPosition_D   (
        "49", 1,
        Units.PERCENT.range(0.0, 100.0),
        "A*100/255"
    ),

    AcceleratorPedalPosition_E   (
        "4A", 1,
        Units.PERCENT.range(0.0, 100.0),
        "A*100/255"
    ),

    AcceleratorPedalPosition_F   (
        "4B", 1,
        Units.PERCENT.range(0.0, 100.0),
        "A*100/255"
    ),

    CommandedThrottleActuator(
        "4C", 1,
        Units.PERCENT.range(0.0, 100.0),
        "A*100/255"
    ),

    TimeRunWithMIL_on   (
        "4D", 2,
        Units.MINUTES.range(0, 65535),
        "(A*256)+B"
    ),

    TimeSinceTroubleCodesCleared(
        "4E", 2,
        Units.MINUTES.range(0, 65535),
        "(A*256)+B"
    ),

    EthanolFuel(
        "52", 1,
        Units.PERCENT.range(0.0, 100.0),
        "A*100/255"
    ),

    AbsoluteEvapSystemVaporPressure(
        "53", 2,
        Units.KPA.range(0.0, 327.675),
        "((A*256)+B)/200"
    ),

    EvapSystemVaporPressure(
        "54", 2,
        Units.PA.range(-32767.0, 32768.0),
        "((A*256)+B)-32767"
    ),

    ShortTermSecondaryOxygenSensorTrim_Bank_1_And_Bank_3(
        "55", 2,
        Units.PERCENT.range(-100.0, 99.22),
        "(A-128)*100/128"
    ),

    LongTermSecondaryOxygenSensorTrim_Bank_1_And_Bank_3(
        "56", 2,
        Units.PERCENT.range(-100.0, 99.22),
        "(A-128)*100/128"
    ),

    ShortTermSecondaryOxygenSensorTrim_Bank_2_And_Bank_4(
        "57", 2,
        Units.PERCENT.range(-100.0, 99.22),
        "(A-128)*100/128"
    ),

    LongTermSecondaryOxygenSensorTrim__Bank_2_And_Bank_4(
        "58", 2,
        Units.PERCENT.range(-100.0, 99.22),
        "(A-128)*100/128"
    ),

    FuelRailPressure_Absolute(
        "59", 2,
        Units.KPA.range(0.0, 655350.0),
        "((A*256)+B) * 10"
    ),

    RelativeAcceleratorPedalPosition(
        "5A", 1,
        Units.PERCENT.range(0.0, 100.0),
        "A*100/255"
    ),

    HybridBatteryPackRemainingLife(
        "5B", 1,
        Units.PERCENT.range(0.0, 100.0),
        "A*100/255"
    ),

    EngineOilTemperature(
        "5C", 1,
        Units.CELCIUS.range(-40.0, 210.0),
        "A - 40"
    ),

    FuelInjectionTiming(
        "5D", 2,
        Units.DEGREE.range(-210.00, 301.992),
        "(((A*256)+B)-26,880)/128"
    ),

    EngineFuelRate(
        "5E", 2,
        Units.LIT_H.range(0.0, 3212.75),
        "((A*256)+B)*0.05"
    ),

    DriversDemandEngine_PercentTorque(
        "61", 1,
        Units.PERCENT.range(-125.0, 125.0),
        "A-125"
    ),

    ActualEngine_PercentTorque (
        "62", 1,
        Units.PERCENT.range(-125.0, 125.0),
        "A-125"
    ),

    EngineReferenceTorque(
        "63", 2,
        Units.NM.range(0.0, 65535.0),
        "A*256+B"
    ),

    EnginePercentTorqueData (
        "64", 5,
        Units.PERCENT.range(-125.0, 125.0),
        "A-125 Idle\n" +
        "B-125 Engine point 1\n" +
        "C-125 Engine point 2\n" +
        "D-125 Engine point 3\n" +
        "E-125 Engine point 4\n"
    );

    public final String code;
    public final Range<?> range;

    private Pid(String code, int byteSize,
                Range<?> range,
                String formula) {

        this.code = code;
        this.range = range;
    }
}
