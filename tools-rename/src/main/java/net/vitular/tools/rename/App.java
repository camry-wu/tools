package net.vitular.tools.rename;

/**
 * rename some word in an directory.
 */
public class App {

    public static void usage() {
        StringBuilder sb = new StringBuilder();
        sb.append("java -jar rename.jar <method> <options>\n");
        sb.append("method   options:\n");
        sb.append("-------------------------------------\n");
        sb.append("pickword <key> <fromFile>            -- 从给定 grep 文件中分析出所有包含给定字串的单词\n");
        sb.append("                                     -- 并给出包含此关键字的文件后缀名列表\n");
        sb.append("rename   <from> <to> <dir> <fileType>-- 将目录中所有文本文件中的 fromName 改为 toName\n");

        System.out.println(sb.toString());
    }

    public static void main( String[] args ) {
        if (args.length <= 0) {
            usage();
            System.exit(1);
        }

        String[] remainArg = new String[args.length - 1];
        System.arraycopy(args, 1, remainArg, 0, args.length - 1);

        if (args[0].equals("pickword")) {
            PickwordTask cmd = new PickwordTask();
            cmd.execute(remainArg);
        } else if (args[0].equals("rename")) {
            RenameTask cmd = new RenameTask();
            cmd.execute(remainArg);
        } else {
            usage();
            System.exit(1);
        }
    }
}
