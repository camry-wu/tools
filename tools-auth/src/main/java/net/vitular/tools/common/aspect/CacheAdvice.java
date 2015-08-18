/*
 * -----------------------------------------------------------
 * file name  : CacheAdvice.java
 * creator    : camry(camry_camry@sina.com)
 * created    : Tue 18 Aug 2015 11:42:39 AM CST
 *
 * modifications:
 *
 * -----------------------------------------------------------
 */
package net.vitular.tools.common.aspect;

import java.lang.reflect.Method;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.aspectj.lang.ProceedingJoinPoint;

import net.vitular.tools.common.annotation.Cacheable;

/**
 * Advice of Cache.
 *
 * @author camry
 * @version $Revision$
 *          $Date$
 */
public class CacheAdvice {

    /**
     * logger.
     */
    protected Log _logger = LogFactory.getLog(getClass());

    /**
     * do cache.
     *
     * @param joinPoint aspect join point
     * @return Object
     * @throws Throwable
     */
    @SuppressWarnings("unchecked")
    public Object cache(final ProceedingJoinPoint joinPoint) throws Throwable {

        Method targetMethod = AspectUtils.getJoinPointMethod(joinPoint);
        if (targetMethod == null) {
            return joinPoint.proceed();
        }

        if (_logger.isDebugEnabled()) {
            _logger.debug(String.format("before do cache for %s.%s.", targetMethod.getDeclaringClass().getName(), targetMethod.getName()));
        }

        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Exception e) {
            throw e;
        }

        if (_logger.isDebugEnabled()) {
            _logger.debug(String.format("after write cache for %s.%s.", targetMethod.getDeclaringClass().getName(), targetMethod.getName()));
        }

        return result;
    }

} // END: CacheAdvice
///:~
