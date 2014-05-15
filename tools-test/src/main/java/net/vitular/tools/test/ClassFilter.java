/*
 * -----------------------------------------------------------
 * file name  : ClassFilter.java
 * creator    : camry(camry.wu@gmail.com)
 * created    : Sun 19 Sep 2010 02:26:04 PM CST
 * copyright  : (c) 2010 Vitular Inc. All Rights Reserved.
 *
 * modifications:
 *
 * -----------------------------------------------------------
 */
package net.vitular.tools.test;

/**
 * to-do class filter.
 *
 * @author $Author$
 * @version $Revision$
 *          $Date$
 */
public class ClassFilter {

    /**
     * filter formatter.
     */
    private String[] _asFilterFormatter;

    /**
     * constructor.
     * @param filterFormatter    filter formatter
     */
    public ClassFilter(String...filterFormatter) {
        super();
        _asFilterFormatter = filterFormatter;
    } // END: ClassFilter

    /**
     * test whether the classname include filter formatter.
     *
     * @param className
     * @return true if classname include filter formatter
     */
    public boolean apply(final String className) {
        if (className == null) {
            return false;
        }

        if (_asFilterFormatter != null) {
            for (int i = 0; i < _asFilterFormatter.length; i++) {
                if (className.matches(_asFilterFormatter[i])) {
                    return true;
                }
            }
        }

        return false;
    }
} // END: ClassFilter
///:~
