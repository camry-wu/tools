/*
 * -----------------------------------------------------------
 * file name  : PK.java
 * creator    : camry(camry_camry@sina.com)
 * created    : Fri 05 Jun 2015 02:05:27 PM CST
 *
 * modifications:
 *
 * -----------------------------------------------------------
 */
package net.vitular.tools.stat;

import java.lang.reflect.Method;

/**
 * Processer Killer.
 *
 * @author wuhao
 * @version $Revision$
 *          $Date$
 */
public class PK {
    private static Method DESTROY_PROCESS = null;

    static {
        try {
            Class clz = Class.forName("java.lang.UNIXProcess");
            DESTROY_PROCESS = clz.getDeclaredMethod("destroyProcess", new Class[] {int.class});
            DESTROY_PROCESS.setAccessible(true);
        } catch (Exception e) {
        }
    }

    public static void killProcess(final int pid) {
        try {
            DESTROY_PROCESS.invoke(null, new Object[]{new Integer(pid)});
        } catch (Exception e) {
            throw new RuntimeException("Could not kill process, id: " + pid, e);
        }
    }

    public static void main(final String[] args) {
        System.out.println(PK.DESTROY_PROCESS);
    }
} // END: PK
///:~
