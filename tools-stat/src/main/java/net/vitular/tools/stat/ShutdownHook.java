/*
 * -----------------------------------------------------------
 * file name  : ShutdownHook.java
 * creator    : camry(camry_camry@sina.com)
 * created    : Wed 03 Jun 2015 10:16:22 AM CST
 *
 * modifications:
 *
 * -----------------------------------------------------------
 */
package net.vitular.tools.stat;

/**
 * shutdown hook, release resource and print goodby word.
 *
 * @author wuhao
 * @version $Revision$
 *          $Date$
 */
public class ShutdownHook extends Thread {

    private Releaseable[] _releaseObj;

    public ShutdownHook(final Releaseable ... releaseObj) {
        _releaseObj = releaseObj;
    }

    public void run() {

        for (Releaseable relobj: _releaseObj) {
            relobj.release();
        }

        System.out.println("release resources.");
    }

    /**
     * default constructor.
     */
    public ShutdownHook() {
        super();
    }
} // END: ShutdownHook
///:~
