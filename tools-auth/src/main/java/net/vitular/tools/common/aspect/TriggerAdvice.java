/*
 * -----------------------------------------------------------
 * file name  : TriggerAdvice.java
 * creator    : camry(camry_camry@sina.com)
 * created    : Tue 18 Aug 2015 11:42:39 AM CST
 *
 * modifications:
 *
 * -----------------------------------------------------------
 */
package net.vitular.tools.common.aspect;

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import net.vitular.tools.common.annotation.Trigger;

//import net.vitular.tools.auth.AuthorizationUser;
//import net.vitular.tools.common.exception.AccessException;
//import net.vitular.tools.common.exception.FortiException;

/**
 * Advice of Trigger.
 *
 * @author camry
 * @version $Revision$
 *          $Date$
 */
public class TriggerAdvice {

    /**
     * logger.
     */
    protected Log _logger = LogFactory.getLog(getClass());

    /**
     * do trigger.
     *
     * @param joinPoint aspect join point
     * @throws AccessException
     */
    @SuppressWarnings("unchecked")
    public void trigger(final JoinPoint joinPoint) /*throws AccessException*/ {

        Method targetMethod = AspectUtils.getJoinPointMethod(joinPoint);
        if (targetMethod == null) {
            return;
        }

        if (_logger.isDebugEnabled()) {
            _logger.debug(String.format("do trigger for %s.%s.", targetMethod.getDeclaringClass().getName(), targetMethod.getName()));
        }

        Trigger trigger = targetMethod.getAnnotation(Trigger.class);
        if (trigger != null) {
            if (_logger.isDebugEnabled()) {
                _logger.debug(String.format("trigger value: %s", trigger.value()));
            }
        }
    }
} // END: TriggerAdvice
///:~
