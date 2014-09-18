package elm327.reader;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

public class Reader {

    private static final Command<?> PIDS[] = {
        Pids.CalculatedEngineLoad,
        Pids.EngineCoolantTemperature,
        Pids.ShortTermFuel_Bank_1,
        Pids.LongTermFuel_Bank_1,
        Pids.ShortTermFuel_Bank_2,
        Pids.LongTermFuel_Bank_2,
        Pids.FuelPressure,
        Pids.IntakeManifoldAbsolutePressure,
        Pids.EngineRPM,
        Pids.VehicleSpeed,
        Pids.TimingAdvance,
        Pids.IntakeAirTemperature,
        Pids.MAF_AirFlowRate,
        Pids.ThrottlePosition,
        Pids.RunTimeSinceEngineStart,
        Pids.DistanceTraveledWithMalFunctionIndicatorLamp,
        Pids.FuelRailPressure_RelativoToManifoldVacuum,
        Pids.FuelRailPressure,
        Pids.EGR_Error,
        Pids.CommandedEGR,
        Pids.CommandedEvaporativePurge,
        Pids.NumbersOfWarmUpsSinceCodesCleared,
        Pids.DistanceTraveledSinceCodesCleared,
        Pids.Evap_SystemVaporPressure,
        Pids.BarometricPressure,
        Pids.CatalystTemperature_Bank1_Sensor1,
        Pids.CatalystTemperature_Bank2_Sensor1,
        Pids.CatalystTemperature_Bank1_Sensor2,
        Pids.CatalystTemperature_Bank2_Sensor2,
        Pids.ControlModuleVoltage,
        Pids.AbsoluteLoadValue,
        Pids.CommandEquivalenceRratio,
        Pids.AmbientAirTemperature,
        Pids.TimeRunWithMIL_ON,
        Pids.TimeSinceTroubleCodesCleared,
        Pids.AbsoluteEvapSystemVaporPressure,
        Pids.EvapSystemVaporPressure,
        Pids.ShortTermSecondaryOxygenSensorTrim_Bank_1,
        Pids.ShortTermSecondaryOxygenSensorTrim_Bank_3,
        Pids.LongTermSecondaryOxygenSensorTrim_Bank_1,
        Pids.LongTermSecondaryOxygenSensorTrim_Bank_3,
        Pids.ShortTermSecondaryOxygenSensorTrim_Bank_2,
        Pids.ShortTermSecondaryOxygenSensorTrim_Bank_4,
        Pids.LongTermSecondaryOxygenSensorTrim_Bank_2,
        Pids.LongTermSecondaryOxygenSensorTrim_Bank_4,
        Pids.FuelRailPressure_Absolute,
        Pids.EngineOilTemperature,
        Pids.FuelInjectionTiming,
        Pids.EngineFuelRate,
        Pids.DriversDemandEngine_PercentTorque,
        Pids.ActualEngine_PercentTorque,
        Pids.EngineReferenceTorque,
    };

    public static void main(String[] args) {

        if (args.length == 0) {
            System.err.println("USAGE: Reader <serial>");
            return;
        }

        try {
            read(new File(args[0]));
        } catch (Exception e) {
            System.err.println(e);
            return;
        }
    }

    private static void read(File serial) throws IOException {

        Writter out = new Writter(System.out);
        Protocol obd = new Protocol(Channels.fromFile(serial));

        out.print("time");

        for (Command<?> pid : PIDS) {
            Message message = pid.message();
            out.print(message);
        }

        out.printLineEnd();

        while(true) {

            out.print(new Date().getTime());

            for (Command<?> pid : PIDS) {
                Result<?> result = obd.send(pid);
                out.print(result);
            }

            out.printLineEnd();

        }
    }

    static class Writter {

        final OutputStream out;

        Writter(OutputStream out) {
            this.out = new BufferedOutputStream(out);
        }

        public void print(Object o) throws IOException {
            print(String.valueOf(o));
        }

        public void print(Message m) throws IOException {
            print(m.code);
        }

        public void print(Result<?> r) throws IOException {

            if (r instanceof Results.Error) {
                print('e');
                return;
            }

            if (r instanceof Results.Unknown) {
                print('?');
                return;
            }

            print(r.data());
        }

        public void print(String s) throws IOException {
            if (null != s) {
                out.write(s.getBytes());
            }
            out.write(',');
        }

        public void printLineEnd() throws IOException {
            out.write('\n');
            out.flush();
        }
    }
}
