/*
 * -----------------------------------------------------------
 * file name  : ClassLocation.java
 * creator    : camry(camry.wu@gmail.com)
 * created    : Sun 19 Sep 2010 09:59:57 AM CST
 * copyright  : (c) 2010 Vitular Inc. All Rights Reserved.
 *
 * modifications:
 *
 * -----------------------------------------------------------
 */
package net.vitular.tools.test;

import java.io.File;
import java.net.URL;

/**
 * to-do Class information.
 *
 * @author $Author$
 * @version $Revision$
 *          $Date$
 */
public class ClassLocation {

    /**
     * package name.
     */
    private String _sPackageName;

    /**
     * class name.
     */
    private String _sClassName;

    /**
     * package name + class name.
     */
    private String _sFullName;

    /**
     * TestSuit Class full name for this class.
     */
    private String _sTestSuitName;

    /**
     * class url.
     */
    private URL _classURL;

    /**
     * class loader.
     */
    private ClassLoader _classLoader;

    // ---------------------------------------------------
    // getter and setter
    // ---------------------------------------------------
    public String getPackageName() { return _sPackageName; }

    public String getClassName() { return _sClassName; }

    public void setFullName(final String fullName) {
        _sFullName = fullName;
    }

    public String getFullName() { return _sFullName; }

    public String getTestSuitName() { return _sTestSuitName; }

    public URL getUrl() {
        return _classURL;
    }

    public void setUrl(URL url) {
        _classURL = url;
    }

    public ClassLoader getClassLoader() {
        return _classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        _classLoader = classLoader;
    }

    /**
     * constructor.
     *
     * @param classLoader   class loader
     * @param fullName      full class name
     * @param url           class url
     */
    public ClassLocation(ClassLoader classLoader, String fullName, URL url) {
        _sFullName = fullName;
        _classURL = url;
        _classLoader = classLoader;

        if (_sFullName != null) {
            int dot = _sFullName.lastIndexOf('.');
            if (dot != -1) {
                _sPackageName = _sFullName.substring(0, dot);
                _sClassName = _sFullName.substring(dot + 1);

                _sTestSuitName = String.format("%s%s%s%s", "test.", _sPackageName, ".Test", _sClassName);
            } else {
                _sPackageName = null;
                _sClassName = _sFullName;

                _sTestSuitName = String.format("%s%s%s", "test.", "Test", _sClassName);
            }
        }
    }

    /**
     * get TestSuit class file relative path.
     *
     * @return test suit class file path
     */
    public String getTestSuitFileDir() {
        String s = _sPackageName.replaceAll("\\.", File.separator);
        return "test" + File.separator + s;
    }

    /**
     * get TestSuit class file relative path.
     *
     * @return test suit class file path
     */
    public String getTestSuitFileName() {
        return String.format("%s%s%s", "Test", _sClassName, ".java");
    }

    @Override
    public int hashCode() {
        return (_sClassName == null) ? 0 : _sClassName.hashCode();
    }

    public boolean equals(ClassLocation classLocation) {
        if (classLocation == null)
            return false;

        return (_sClassName.equals(classLocation._sClassName));
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ClassLocation)
            return equals((ClassLocation) o);
        else
            return false;
    }
} // END: ClassLocation
///:~
