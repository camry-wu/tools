/*
 * -----------------------------------------------------------
 * file name  : RenameTask.java
 * creator    : camry(camry_camry@sina.com)
 * created    : Thu 09 Jun 2016 10:55:50 AM CST
 * copyright  : (c) 2016 Vitular Inc. All Rights Reserved.
 *
 * modifications:
 *
 * -----------------------------------------------------------
 */
package net.vitular.tools.rename;

/**
 * 修改制定原始字符串为目的字符串.
 * 在指定目录中搜索指定文件名后缀的文件，逐个修改
 *
 * @author $Author$
 * @version $Revision$ $Date$
 */
public class RenameTask extends AbstractTask {

    /**
     * default constructor.
     */
    public RenameTask() {
        super();
    }

    /**
     * 根据参数执行命令.
     * rename   <from> <to> <dir> <fileType>
     *
     * @param args 输入参数
     */
    public void execute(final String[] args) {
        String from = null;
        String to = null;
        String dir = null;
        String fileTypeListFile = null;

        if (args.length == 4) {
            from = args[0];
            to = args[1];
            dir = args[2];
            fileTypeListFile = args[3];
        } else {
            System.err.println("命令格式：rename   <from> <to> <dir> <fileType>");
            return;
        }

        //execute(key, fromFile);
    }
} // END: RenameTask
///:~
