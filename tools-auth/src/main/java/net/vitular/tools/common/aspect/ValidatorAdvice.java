/*
 * -----------------------------------------------------------
 * file name  : ValidatorAdvice.java
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

import net.vitular.tools.common.annotation.Validator;

//import net.vitular.tools.auth.AuthorizationUser;
//import net.vitular.tools.common.exception.AccessException;
//import net.vitular.tools.common.exception.FortiException;

/**
 * Advice of Validator.
 *
 * @author camry
 * @version $Revision$
 *          $Date$
 */
public class ValidatorAdvice {

    /**
     * logger.
     */
    protected Log _logger = LogFactory.getLog(getClass());

    /**
     * do validator.
     *
     * @param joinPoint aspect join point
     * @throws AccessException
     */
    @SuppressWarnings("unchecked")
    public void validate(final JoinPoint joinPoint) /*throws AccessException*/ {

        Method targetMethod = AspectUtils.getJoinPointMethod(joinPoint);
        if (targetMethod == null) {
            return;
        }

        if (_logger.isDebugEnabled()) {
            _logger.debug(String.format("do validate for %s.%s.", targetMethod.getDeclaringClass().getName(), targetMethod.getName()));
        }

        Validator validator = targetMethod.getAnnotation(Validator.class);
        if (validator != null) {
            if (_logger.isDebugEnabled()) {
                _logger.debug(String.format("validate object: %s", validator.value()));
            }
        }
    }
} // END: ValidatorAdvice
///:~
