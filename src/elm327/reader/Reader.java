package elm327.reader;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class Reader {

    private static final Map<String, Command<?>> PIDS = new TreeMap<String, Command<?>>();
    static {
        PIDS.put("speed", Pids.VehicleSpeed);
        PIDS.put("throttle", Pids.ThrottlePosition);
        PIDS.put("engine.load", Pids.CalculatedEngineLoad);
        PIDS.put("engine.rpm", Pids.EngineRPM);
        PIDS.put("cooleant.temp", Pids.EngineCoolantTemperature);
        PIDS.put("fuel.short.1", Pids.ShortTermFuel_Bank_1);
        PIDS.put("fuel.long.1" , Pids.LongTermFuel_Bank_1);
        PIDS.put("intake.pres", Pids.IntakeManifoldAbsolutePressure);
        PIDS.put("intake.temp", Pids.IntakeAirTemperature);
        PIDS.put("timing", Pids.TimingAdvance);
        PIDS.put("maf.flow", Pids.MAF_AirFlowRate);
    }

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

        for (String key : PIDS.keySet()) {
            out.print(key);
        }

        out.printLineEnd();

        while(true) {

            out.print(new Date().getTime());

            for (Command<?> pid : PIDS.values()) {
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
            if (o != null) {
                print(String.valueOf(o));
            } else {
                print("n");
            }
        }

        public void print(Result<?> r) throws IOException {

            if (r instanceof Results.Error) {
                print('e');
                return;
            }

            if (r instanceof Results.NoData) {
                print('-');
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
            } else {
                out.write('n');
            }
            out.write(',');
        }

        public void printLineEnd() throws IOException {
            out.write('\n');
            out.flush();
        }
    }
}
