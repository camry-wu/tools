/*
 * -----------------------------------------------------------
 * file name  : ClassLocator.java
 * creator    : camry(camry.wu@gmail.com)
 * created    : Sun 19 Sep 2010 10:16:31 AM CST
 * copyright  : (c) 2010 Vitular Inc. All Rights Reserved.
 *
 * modifications:
 *
 * -----------------------------------------------------------
 */
package net.vitular.tools.test;

import java.io.File;
import java.io.IOException;

import java.net.URL;
import java.net.JarURLConnection;

import java.util.List;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

//import org.densebrain.annex.util.JavaUtility;

/**
 * to-do travel class in classpath.
 *
 * @author $Author$
 * @version $Revision$
 *          $Date$
 */
public class ClassLocator {

    /**
     * package names.
     */
    private String[] _asPackageNames;

    /**
     * class loader.
     */
    private ClassLoader _classLoader;

    /**
     * package name.
     */
    private String _sPackageName;

    /**
     * class locations.
     */
    private List<ClassLocation> _classLocations = new LinkedList<ClassLocation>();

    /**
     * class filter.
     */
    private ClassFilter _classFilter;

    public void setClassFilter(final ClassFilter filter) { _classFilter = filter; }

    /**
     * constructor.
     *
     * @param packageNames      package names
     * @throws ClassNotFoundException
     */
    public ClassLocator(String...packageNames) throws ClassNotFoundException {
        this(Thread.currentThread().getContextClassLoader(), packageNames);
    }

    /**
     * constructor.
     *
     * @param classLoader       class loader
     * @param packageNames      package names
     * @throws ClassNotFoundException
     */
    public ClassLocator(ClassLoader classLoader, String...packageNames) throws ClassNotFoundException {
        _classLoader = classLoader;
        _asPackageNames = packageNames;

        if (classLoader == null)			
            throw new ClassNotFoundException("Can't get class loader.");
    }

    /**
     * travel classpath to locate class.
     *
     * @return ClassLocation List
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public List<ClassLocation> getAllClassLocations() throws ClassNotFoundException, IOException {
        synchronized(this) {
            _classLocations.clear();

            for (String packageName : _asPackageNames) {
                _sPackageName = packageName;

                String path = packageName.replace('.', '/');
                Enumeration<URL> resources = _classLoader.getResources(path);
                if (resources == null || !resources.hasMoreElements()) {
                    throw new ClassNotFoundException("No resource for " + path);
                }

                while (resources.hasMoreElements()) {
                    URL resource = resources.nextElement();
                    if (resource.getProtocol().equalsIgnoreCase("FILE")) {
                        loadDirectory(resource);
                    } else if (resource.getProtocol().equalsIgnoreCase("JAR")) {
                        loadJar(resource);
                    } else {
                        throw new ClassNotFoundException("Unknown protocol on class resource: " + resource.toExternalForm());
                    }
                }
            }

            return _classLocations;
        }
    }

    /**
     * load class from jar.
     *
     * @param resource      class url
     * @throws IOException
     */
    private void loadJar(URL resource) throws IOException {
        JarURLConnection conn = (JarURLConnection) resource.openConnection();
        JarFile jarFile = conn.getJarFile();
        Enumeration<JarEntry> entries = jarFile.entries();
        String packagePath = _sPackageName.replace('.', '/');

        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if ((entry.getName().startsWith(packagePath) || entry.getName().startsWith("WEB-INF/classes/" + packagePath))
                            && entry.getName().endsWith(".class")) {
                //URL url = new URL("jar:" + new URL("file", null, JavaUtility.slashify(jarFile.getName(), false)).toExternalForm()
                //                  + "!/" + entry.getName());
                URL url = null;//new URL("jar:!/" + entry.getName());

                String className = entry.getName();
                if (className.startsWith("/"))
                        className = className.substring(1);
                className = className.replace('/', '.');

                className = className.substring(0, className.length() - ".class".length());				
                if (className.indexOf('$') != -1) {
                    continue;
                }

                if (_classFilter != null && !_classFilter.apply(className)) {
                    continue;
                }

                ClassLocation classLocation = new ClassLocation(_classLoader, className, url);
                addClassLocation(classLocation);
            }
        }
    }

    /**
     * load class from directory.
     *
     * @param resource      package directory
     * @throws IOException
     */
    private void loadDirectory(URL resource) throws IOException {
        loadDirectory(_sPackageName, resource.getFile());
    }

    /**
     * load class from directory.
     *
     * @param packageName   package name
     * @param fullPath      package full path
     * @throws IOException
     */
    private void loadDirectory(String packageName, String fullPath) throws IOException {
        File directory = new File(fullPath);
        if (!directory.isDirectory())
            throw new IOException("Invalid directory " + directory.getAbsolutePath());

        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory())
                loadDirectory(packageName + '.' + file.getName(), file.getAbsolutePath());
            else if (file.getName().endsWith(".class")) {
                String simpleName = file.getName();
                simpleName = simpleName.substring(0, simpleName.length() - ".class".length());
                if (simpleName.indexOf('$') != -1) {
                    continue;
                }
                String className = String.format("%s.%s", packageName, simpleName);

                if (_classFilter != null && !_classFilter.apply(className)) {
                    continue;
                }

                ClassLocation location = new ClassLocation(_classLoader, className, new URL("file", null, file.getAbsolutePath()));
                addClassLocation(location);
            }
        }
    }

    /**
     * add ClassLocation.
     *
     * @param classLocation new ClassLocation
     * @throws IOException
     */
    private void addClassLocation(ClassLocation classLocation) throws IOException {
        if (_classLocations.contains(classLocation)) {
            throw new IOException("Duplicate location found for: " + classLocation.getClassName());
        }

        _classLocations.add(classLocation);
    }
} // END: ClassLocator
///:~
