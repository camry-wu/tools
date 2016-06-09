/*
 * -----------------------------------------------------------
 * file name  : AspectUtils.java
 * creator    : wuhao(wuhao@fortinet.com)
 * created    : Tue 18 Aug 2015 09:57:57 AM CST
 *
 * modifications:
 *
 * -----------------------------------------------------------
 */
package net.vitular.tools.common.aspect;

import java.lang.reflect.Method;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;

/**
 * Advice Utils.
 *
 * @author wuhao
 * @version $Revision$
 *          $Date$
 */
public final class AspectUtils {

    /**
     * get declared method base on the JoinPoint.
     *
     * @param joinPoint aspect join point
     * @return Method
     */
    public static Method getJoinPointMethod(final JoinPoint joinPoint) {

        Object[] args = joinPoint.getArgs();
        Class[] argsClass = new Class[args.length];

        for (int i = 0; i < args.length; i++) {
            argsClass[i] = (args[i] == null) ? null : args[i].getClass();
        }

        Signature sig = joinPoint.getSignature();
        Class targetClass = sig.getDeclaringType();
        Method targetMethod = null;
        try {
            targetMethod = targetClass.getMethod(sig.getName(), argsClass);
        } catch(NoSuchMethodException e) {
            String msg = String.format("cannot find method: %s in %s.", sig.getName(), targetClass.getName());
            System.err.println(msg);
        }

        return targetMethod;
    }
} // END: AspectUtils
///:~
