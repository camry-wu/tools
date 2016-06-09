/*
 * -----------------------------------------------------------
 * file name  : PickwordTask.java
 * creator    : camry(camry_camry@sina.com)
 * created    : Thu 09 Jun 2016 10:11:50 AM CST
 * copyright  : (c) 2016 Vitular Inc. All Rights Reserved.
 *
 * modifications:
 *
 * -----------------------------------------------------------
 */
package net.vitular.tools.rename;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringWriter;
import java.io.IOException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * 从一个 grep 得到的文件中将包含关键字（忽略大小写）的单词挑出.
 * 一个单词为字母数字串，可带有下划线。
 * 并列出包含此关键字的文件后缀名列表。
 *
 * @author wuhao
 * @version $Revision$
 *          $Date$
 */
public class PickwordTask extends AbstractTask {

    /**
     * 文件扩展名列表.
     */
    private Set<String> _filenameExtensionSet;

    /**
     * default constructor.
     */
    public PickwordTask() {
        super();
        _filenameExtensionSet = new HashSet<String> ();
    }

    /**
     * pick words which contains the key ignorecase.
     *
     * @param key       查询关键字
     * @param fromFile  扫描文件
     */
    private void execute(final String key, final String fromFile) {
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(fromFile));

            String line = null;
            while ((line = br.readLine()) != null) {
                String extension = pickFilenameExtension(line);
                if (extension != null) {
                    _filenameExtensionSet.add(extension);
                }
            }

            // print all the filename extensions
            System.out.println("---- print all the filename extensions ----");
            for (String s: _filenameExtensionSet) {
                System.out.println(s);
            }
        } catch (IOException ioe) {
            System.err.println(String.format("cannot read file: %s", fromFile));
            System.exit(1);
            return;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
    } // END: execute

    /**
     * 从 grep 输出的一行数据中挑出文件名的扩展名.
     *
     * @param line grep line
     * @return filename extension
     */
    private String pickFilenameExtension(final String line) {
        // 查找 ..../...:
        int x = line.indexOf(':');
        if (x == -1) {
            return null;
        }

        int y = line.lastIndexOf('.', x);
        if (y == -1) {
            return line.substring(0, x);
        } else {
            return line.substring(y + 1, x);
        }
    }

    /**
     * 根据参数执行命令.
     * pickword <key> <fromFile>
     *
     * @param args 输入参数
     */
    public void execute(final String[] args) {
        String key = null;
        String fromFile = null;

        if (args.length == 2) {
            key = args[0];
            fromFile = args[1];
        } else {
            System.err.println("命令格式：pickword <key> <fromFile>");
            return;
        }

        execute(key, fromFile);
    }
} // END: PickwordTask
///:~
