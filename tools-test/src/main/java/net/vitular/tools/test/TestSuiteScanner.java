/*
 * -----------------------------------------------------------
 * file name  : TestSuiteScanner.java
 * creator    : camry(camry.wu@gmail.com)
 * created    : Tue 12 Oct 2010 01:06:07 PM CST
 * copyright  : (c) 2010 Vitular Inc. All Rights Reserved.
 *
 * modifications:
 *
 * -----------------------------------------------------------
 */
package net.vitular.tools.test;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;

import java.lang.reflect.Method;

/**
 * to-do scan classpath to find all of the testable classes,
 * and create TestSuit for them.
 * Example:
 *  .*DAO, .*Helper, .*Facade, .*Action
 *
 * @author $Author$
 * @version $Revision$ $Date$
 */
public class TestSuiteScanner {

    /**
     * class ending.
     */
    public static final String CLASS_ENDING = "} // END: _classname_\n///:~";

    /**
     * class skel.
     */
    private String _sClassSkel;

    /**
     * method skel.
     */
    private String _sMethodSkel;

    /**
     * scan package prefix.
     */
    private String _sPackage;

    /**
     * java source path.
     */
    private String _sSrcPath;

    /**
     * constructor.
     *
     * @param pack      package prefix
     * @param srcpath   java source path
     */
    public TestSuiteScanner(final String pack, final String srcpath) {
        _sPackage = pack;
        _sSrcPath = srcpath;
    }

    /**
     * parse ClassLocation list.
     *
     * @param classes classes
     * @param srcroot src root path
     */
    private void scanClassLocation(final List<ClassLocation> classes, final String srcroot) {
        if (classes != null) {
            for (ClassLocation clz: classes) {
                String cname = clz.getFullName();
                try {
                    Class ori = Class.forName(cname);
                    if (!ori.isInterface()) {

                        String testClassName = clz.getTestSuitName();

                        // if test suite class exists ?
                        Class testClass = null;
                        try {
                            testClass = Class.forName(testClassName);

                            // scan test method
                            scanTestMethods(ori, testClass, clz, srcroot);
                        } catch (ClassNotFoundException cnfe) {
                            // generate test suite class
                            generateTestSuit(ori, clz, srcroot);
                        }
                    }
                } catch (ClassNotFoundException e) {
                    System.out.println("cannot find this class: " + cname);
                }
            }
        }
    } // END: scanClassLocation

    /**
     * generate TestMethod name.
     *
     * @param method origin method
     * @return TestMethod name
     */
    private String generateTestMethodName(final Method method) {
        String mname = method.getName();
        char x = Character.toUpperCase(mname.charAt(0));
        char[] a = {x};
        String b = mname.substring(1);

        StringBuffer paramNames = new StringBuffer();
        Class[] parameterTypes = method.getParameterTypes();
        for (Class param: parameterTypes) {
            String cn = param.getSimpleName();
            if (cn.indexOf('[') != -1) {
                cn = cn.replace("[]", "A");
            }
            paramNames.append('_');
            paramNames.append(cn);
        }

        mname = new String(a) + b + paramNames.toString();
        return mname;
    }

    /**
     * find if the TestMethod is exists.
     *
     * @param testMethodName    test method name
     * @param testMethodArray   Method array in TestClass
     * @return true or false
     */
    private boolean isExistTestMethod(final String testMethodName, final Method[] testMethodArray) {
        if (testMethodArray != null) {

            String tmp = "test" + testMethodName;
            for (Method m: testMethodArray) {
                if (m.getName().equals(tmp)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * test if the TestSuit include all of the public method of Suit.
     * if one method messing, generate the method and insert into TestSuit file.
     *
     * @param suiteClass            Suit Class
     * @param testClass             Suit Test Class
     * @param suiteClassLocation    Suit Location
     * @param srcroot               src root path
     */
    private void scanTestMethods(final Class suiteClass, final Class testClass, final ClassLocation suiteClassLocation, final String srcroot) {
        String testSuitFileDir = suiteClassLocation.getTestSuitFileDir();
        String testSuitFileName = suiteClassLocation.getTestSuitFileName();

        String sfilepath = srcroot + File.separator + testSuitFileDir;
        File filepath = new File(sfilepath);
        if (!filepath.isDirectory()) {
            filepath.mkdirs();
        }

        File file = new File(filepath, testSuitFileName);
        if (!file.exists() || !file.isFile()) {
            System.out.println(testSuitFileName + " is not exists or is not a regular file! But This Class is found in classpath, Please clean classpath first.");
        } else {
            List<String> missingMethodList = new ArrayList<String>();
            Method[] testMethodArray = testClass.getDeclaredMethods();

            Class[] its = suiteClass.getInterfaces();
            if (its != null && its.length > 0) {
                for (Class it: its) {
                    Method[] methods = it.getMethods();
                    for (Method m: methods) {
                        String mname = generateTestMethodName(m);

                        if (!isExistTestMethod(mname, testMethodArray)) {
                            missingMethodList.add(mname);
                        }
                    }
                }
            } else {
                Method[] methods = suiteClass.getDeclaredMethods();
                for (Method m: methods) {
                    String mname = generateTestMethodName(m);

                    if (!isExistTestMethod(mname, testMethodArray)) {
                        missingMethodList.add(mname);
                    }
                }
            }

            StringBuffer class_body = new StringBuffer();
            if (missingMethodList.size() > 0) {
                String oriCode = readContentFromFile(file.getPath());
                int ending = oriCode.lastIndexOf('}');
                oriCode = oriCode.substring(0, ending);

                class_body.append(oriCode);

                String method_skel = readContentFromFile("test.method.skel");

                for (String mname: missingMethodList) {
                    class_body.append(method_skel.replaceAll("_method_", mname));
                }

                String end = CLASS_ENDING.replaceAll("_classname_", "Test" + suiteClassLocation.getClassName());
                class_body.append(end);

                FileWriter fos = null;
                try {
                    fos = new FileWriter(file, false);
                    fos.write(class_body.toString());
                    fos.flush();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException ie) {
                            ie.printStackTrace();
                        }
                    }
                }
            }
        }
    } // END: scanTestMethods

    /**
     * generate Test Suit.
     *
     * @param suiteClass            Suit Class
     * @param suiteClassLocation    Suit Location
     * @param srcroot               Src root path
     */
    private void generateTestSuit(final Class suiteClass, final ClassLocation suiteClassLocation, final String srcroot) {
        String testSuitFileDir = suiteClassLocation.getTestSuitFileDir();
        String testSuitFileName = suiteClassLocation.getTestSuitFileName();

        String sfilepath = srcroot + File.separator + testSuitFileDir;
        File filepath = new File(sfilepath);
        if (!filepath.isDirectory()) {
            filepath.mkdirs();
        }

        File file = new File(filepath, testSuitFileName);
        if (!file.exists()) {
            String class_skel = readContentFromFile("test.class.skel");
            String method_skel = readContentFromFile("test.method.skel");

            Calendar cal = Calendar.getInstance();
            String year = String.valueOf(cal.get(Calendar.YEAR));
            String month = String.valueOf(cal.get(Calendar.MONTH) + 1);
            String day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));

            String begin = class_skel.replaceAll("_filename_", testSuitFileName)
                .replaceAll("_datetime_", year + "-" + month + "-" + day)
                .replaceAll("_year_", year)
                .replaceAll("_package_", "test." + suiteClassLocation.getPackageName())
                .replaceAll("_classname_", "Test" + suiteClassLocation.getClassName());

            StringBuffer class_body = new StringBuffer(begin);
            Class[] its = suiteClass.getInterfaces();
            if (its != null && its.length > 0) {
                for (Class it: its) {
                    Method[] methods = it.getMethods();
                    for (Method m: methods) {
                        String mname = generateTestMethodName(m);
                        class_body.append(method_skel.replaceAll("_method_", mname));
                    }
                }
            } else {
                Method[] methods = suiteClass.getDeclaredMethods();
                for (Method m: methods) {
                    String mname = generateTestMethodName(m);
                    class_body.append(method_skel.replaceAll("_method_", mname));
                }
            }

            String end = CLASS_ENDING.replaceAll("_classname_", "Test" + suiteClassLocation.getClassName());
            class_body.append(end);

            FileWriter fos = null;
            try {
                fos = new FileWriter(file);
                fos.write(class_body.toString());
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException ie) {
                        ie.printStackTrace();
                    }
                }
            }
        } else {
            if (!file.isFile()) {
                System.out.println(testSuitFileName + " exists, but it is not a regular file!");
            } else {
                System.out.println(testSuitFileName + " exists, please compile it at first!");
            }
        }
    } // END: generateTestSuit

    /**
     * read content from file.
     *
     * @return String file content
     */
    private String readContentFromFile(final String filename) {
        ByteArrayOutputStream baos = null;
        InputStream fis = null;
        File file = new File(filename);

        boolean isSkelFileInCurrentDir = file.exists();

        byte[] buf = new byte[4096];
        int len = -1;
        try {
            baos = new ByteArrayOutputStream();
            if (isSkelFileInCurrentDir) {
                fis = new FileInputStream(file);
            } else {
                fis = getClass().getClassLoader().getResourceAsStream(filename);
            }

            if (fis == null) {
                System.err.println("System Failure: cannot found skel file: " + filename);
                System.exit(1);
            }

            while ((len = fis.read(buf)) != -1) {
                baos.write(buf, 0, len);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return baos.toString();
    }

    /**
     * main function.
     *
     * @param args arguments
     */
    public void execute(final String...filterFormatter) {
        ClassLocator cl = null;
        try {
            cl = new ClassLocator(_sPackage);

            ClassFilter filter = new ClassFilter(filterFormatter);
            cl.setClassFilter(filter);

            List<ClassLocation> classes = cl.getAllClassLocations();
            scanClassLocation(classes, _sSrcPath);

        } catch (ClassNotFoundException e) {
            System.out.println("cannot find the package in classpath: " + _sPackage);
            System.exit(1);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * print ClassLocation list.
     *
     * @param classes
     */
    private void printClassLocation(final List<ClassLocation> classes) {
        if (classes != null) {
            for (ClassLocation clz: classes) {
                System.out.println(clz.getClassName());
            }
        }
    }

    /**
     * main function.
     *
     * @param args arguments
     */
    public static void main(final String[] args) {
        String pack = args[0];
        String srcpath = args[1];

        //ClassFilter filter = new ClassFilter(".*Dao", ".*Helper", ".*Facade", ".*Action");

        TestSuiteScanner scanner = new TestSuiteScanner(pack, srcpath);
        //scanner.execute(".*Dao", ".*Helper", ".*Facade", ".*Action");
        //scanner.execute(".*Dao", ".*Helper", ".*Facade", ".*Mgr");
        scanner.execute(".*ServiceDao.*");
    }
} // END: TestSuiteScanner
///:~
