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

        System.out.println(sb.toString());
    }

    public static void main( String[] args ) {
        if (args.length <= 0) {
            usage();
            System.exit(1);
        }

        if (args[0].equals("cpustat")) {
            String[] remainArg = new String[args.length - 1];
            System.arraycopy(args, 1, remainArg, 0, args.length - 1);

            CPUStat stat = new CPUStat();
            stat.execute(remainArg);
        } else {
            usage();
            System.exit(1);
        }
    }
}
