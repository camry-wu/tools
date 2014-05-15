/*
 * -----------------------------------------------------------
 * file name  : Main.java
 * creator    : camry(camry.wu@gmail.com)
 * created    : Mon 04 Feb 2013 01:08:43 PM CST
 * copyright  : (c) 2013 Vitular Inc. All Rights Reserved.
 *
 * modifications:
 *
 * -----------------------------------------------------------
 */
package net.vitular.tools.faker4j;

import java.io.*;
import java.util.Properties;
import java.util.Enumeration;

/**
 * to-do main function for generating Test Data.
 *
 * @author $Author$
 * @version $Revision$ $Date$
 */
public final class Main {

    /**
     * config prop prompt.
     */
    public static final String XML_CONFIG = "-x";
    public static final String PROP_CONFIG = "-p";

    public static final String ARG_DATE_OFFSET = "-d";
    public static final String ARG_ROW_COUNT = "-c";

    /**
     * config file type: xml or properties.
     */
    private static String _sConfigType = null;

    /**
     * config file path.
     */
    private static String _sConfigFilePath = null;

    /**
     * row size.
     */
    private static int _iRowSize = 0;

    /**
     * date offset get from argument.
     */
    private static String _sDateOffset = null;

    /**
     * parse execute arguments.
     *
     * @param args  running arguments
     * @return true if parse success, else faluse
     */
    private static boolean parseArguments(final String[] args) {

        if (args.length <= 0 || (args.length % 2) != 0) {
            return false;
        }

        int i = 0;
        while (i < args.length) {

            if (XML_CONFIG.equals(args[i])) {

                _sConfigType = XML_CONFIG;
                _sConfigFilePath = args[++i];

            } else if (PROP_CONFIG.equals(args[i])) {

                _sConfigType = PROP_CONFIG;
                _sConfigFilePath = args[++i];

            } else if (ARG_DATE_OFFSET.equals(args[i])) {

                _sDateOffset = args[++i];

            } else if (ARG_ROW_COUNT.equals(args[i])) {

                _iRowSize = Integer.parseInt(args[++i]);

            } else {
                return false;
            }

            i++;
        }

        return true;
    }

    /**
     * main function for running data generate.
     *
     * @param args  running arguments
     */
    public static void main(final String[] args) {
        long begin = System.currentTimeMillis();

        if (!parseArguments(args)) {
            usage();
            System.exit(1);
        }

        if (XML_CONFIG.equals(_sConfigType)) {
            // load config from xml file
            System.out.println("unsupport xml config now.");
            usage();

        } else if (PROP_CONFIG.equals(_sConfigType)) {
            // load config from properties file

            Properties props = null;
            try {
                InputStream in = new FileInputStream(_sConfigFilePath);
                props = new Properties();
                props.load(in);

                setGlobalProperties(props);

            } catch (FileNotFoundException fnfe) {
                System.err.println("cannot find properties file: " + _sConfigFilePath);
                System.exit(1);
            } catch (IOException ioe) {
                System.err.println("cannot load properties file: " + _sConfigFilePath);
                System.exit(1);
            }

            String sGlobalPropDebug = props.getProperty(FakerConsts.GLOBAL_PROP_KEY_DEBUG);
            Boolean debug = Boolean.FALSE;
            if (sGlobalPropDebug != null && !"".equals(sGlobalPropDebug)) {
                debug = Boolean.valueOf(sGlobalPropDebug);
            }

            PropFakerContext fakerContext = new PropFakerContext(props, debug.booleanValue());

            IFileFaker fileFaker = FakerFactory.createFileFaker(_iRowSize, fakerContext);
            if (fileFaker == null) {
                System.exit(0);
            }

            // DEBUG
            if (debug.booleanValue()) {
                for (Enumeration names = props.propertyNames(); names.hasMoreElements();) {
                    System.out.println(names.nextElement());
                }

                System.out.println("--------");
                props.list(System.out);
            }

            try {
                fileFaker.generateFile();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                System.exit(1);
            } finally {
                fileFaker.release();
            }

        } else {
            usage();
        }

        long end = System.currentTimeMillis();
        System.out.println(String.format("use time(milliseconds): %d [%d, %d]", (end - begin), begin, end));

        System.exit(0);
    }

    /**
     * set global properties.
     *
     * @param props     config properties
     */
    private static void setGlobalProperties(final Properties props) {

        String sGlobalPropDateOffset = props.getProperty(FakerConsts.GLOBAL_PROP_KEY_DATE_OFFSET);
        String sGlobalPropDateLessThenNow = props.getProperty(FakerConsts.GLOBAL_PROP_KEY_DATELESSTHENNOW);

        if (_sDateOffset != null) {
            props.setProperty(FakerConsts.GLOBAL_PROP_KEY_DATE_OFFSET, _sDateOffset);
        }
    }

    /**
     * print usage string.
     */
    private static void usage() {
        System.out.println(
                "Usage: java net.vitular.tools.faker4j.Main [-x configFilePath | -p configFilePath] [-d dateOffset] [-c rowSize]");
    } // END: usage
} // END: Main
///:~
