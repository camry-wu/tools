package net.vitular.tools.stat;

/**
 * Hello world!
 *
 */
public class App {

    public static void usage() {
        StringBuilder sb = new StringBuilder();
        sb.append("java -jar stat.jar <method> <options>\n");
        sb.append("method   options:\n");
        sb.append("-------------------------------------\n");
        sb.append("cpustat  [interval] [output file path]\n");
        sb.append("pstat    <pid> [interval] [output file path]\n");
        sb.append("tstat    <pid> <tid> [interval] [output file path]\n");

        System.out.println(sb.toString());
    }

    public static void main( String[] args ) {
        if (args.length <= 0) {
            usage();
            System.exit(1);
        }

        String[] remainArg = new String[args.length - 1];
        System.arraycopy(args, 1, remainArg, 0, args.length - 1);

        if (args[0].equals("cpustat")) {
            CPUStat stat = new CPUStat();
            stat.statCPU(remainArg);
        } else if (args[0].equals("pstat")) {
            CPUStat stat = new CPUStat();
            stat.statProcess(remainArg);
        } else if (args[0].equals("tstat")) {
            CPUStat stat = new CPUStat();
            stat.statThread(remainArg);
        } else {
            usage();
            System.exit(1);
        }
    }
}
