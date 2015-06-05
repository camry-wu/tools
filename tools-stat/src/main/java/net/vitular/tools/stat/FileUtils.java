/*
 * -----------------------------------------------------------
 * file name  : FileUtils.java
 * creator    : camry(camry_camry@sina.com)
 * created    : Tue 02 Jun 2015 11:22:48 AM CST
 *
 * modifications:
 *
 * -----------------------------------------------------------
 */
package net.vitular.tools.stat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * file utils.
 *
 * @author wuhao
 * @version $Revision$
 *          $Date$
 */
public class FileUtils {

    /**
     * default constructor.
     */
    public FileUtils() {
        super();
    }

    /**
     * read all content from file to one string.
     *
     * @param filename file name
     * @return file content
     */
    public static String file2string(final String filename) throws IOException {
        StringWriter sw = new StringWriter();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(filename));

            String line = null;
            while ((line = br.readLine()) != null) {
                sw.write(line);
                sw.write('\n');
            }
        } finally {
            if (br != null) {
                br.close();
            }
        }

        return sw.toString();
    }

    /**
     * grep file.
     *
     * @param pattern   grep pattern
     * @param filename  file name
     * @return
     */
    public static List<String> grepFile(final Pattern pattern, final String filename) throws IOException {
        List<String> ret = new ArrayList<String> ();

        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(filename));

            String line = null;
            while ((line = br.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    ret.add(matcher.group(1));
                }
            }
        } finally {
            if (br != null) {
                br.close();
            }
        }

        return ret;
    }
} // END: FileUtils
///:~
