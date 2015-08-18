/*
 * -----------------------------------------------------------
 * file name  : OperationPermissionAdvice.java
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

// import org.springframework.core.annotation.Order;

import net.vitular.tools.common.annotation.OperationPermission;

//import net.vitular.tools.auth.AuthorizationUser;
//import net.vitular.tools.common.exception.AccessException;
//import net.vitular.tools.common.exception.FortiException;

/**
 * Advice of OperationPermission.
 *
 * @author camry
 * @version $Revision$
 *          $Date$
 */
public class OperationPermissionAdvice {

    /**
     * logger.
     */
    protected Log _logger = LogFactory.getLog(getClass());

    /**
     * check user permission.
     *
     * @param joinPoint aspect join point
     * @throws AccessException
     */
    @SuppressWarnings("unchecked")
    public void check(final JoinPoint joinPoint) /*throws AccessException*/ {

        Method targetMethod = AspectUtils.getJoinPointMethod(joinPoint);
        if (targetMethod == null) {
            return;
        }

        if (_logger.isDebugEnabled()) {
            _logger.debug(String.format("check permission for %s.%s.", targetMethod.getDeclaringClass().getName(), targetMethod.getName()));
        }

        OperationPermission neededPermissions = targetMethod.getAnnotation(OperationPermission.class);
        if (neededPermissions != null) {
            if (_logger.isDebugEnabled()) {
                _logger.debug(String.format("need permission: %s", java.util.Arrays.toString(neededPermissions.permissions())));
            }
        }
    }

/*
    @SuppressWarnings("unchecked")
    @Around(value="execution(* com..bizservice.*Facade.*(..))")
    public Object check(ProceedingJoinPoint pjp) throws Throwable
    {
        if( logger.isDebugEnabled() )
            logger.debug("checking operation permission...");

        if(pjp.getArgs()==null)
        {
            return pjp.proceed();
        }

        Object[] args = pjp.getArgs();
        Class[] argsClass = new Class[args.length];

        for (int i = 0; i < args.length; i++)
        {
            argsClass[i] = (args[i]==null)? null : args[i].getClass();
        }

        Signature sig = pjp.getSignature();
        Class targetClass = sig.getDeclaringType();
        Method targetMethod = null;
        try
        {
            targetMethod = targetClass.getMethod(sig.getName(), argsClass);
        }
        catch(Exception e)
        {
            return pjp.proceed();
        }

        OperationPermission neededPermissions = targetMethod.getAnnotation(OperationPermission.class);
        //No operation permission needed.
        if(neededPermissions==null)
        {
            return pjp.proceed();
        }
        if(args.length==0 || !(args[0] instanceof IUserInfo))
        {
            throw new FortiException("No user instance parameter, which is needed when check permission!", "access.error.operationPermission.noUserObject");
        }
        IUserInfo user = (IUserInfo)args[0];

        boolean checkPass = false;

        for(String neededPermissionItem:neededPermissions.permissionItems())
        {
            if( user.ownPermissionItem(neededPermissionItem) )
            {
                checkPass = true;
                break;
            }
        }
        //=========================================================

        if(!checkPass)
        {
            throw new AccessException("the current user '" + user.getUserID() + "' does not has operation permission to do this operation!", "access.error.operationPermission.noPermission");
        }
        return pjp.proceed();
    }

    public void printPjpInfo(ProceedingJoinPoint pjp)
    {
        logger.debug("pjp.getArgs()=" + pjp.getArgs());
        Object[] args = pjp.getArgs();
        logger.debug(args.length);
        for(int i=0; i<args.length; i++)
        {
            logger.info("arg[" + i + "]" + args[i]);
        }

        logger.debug("pjp.getClass()=" + pjp.getClass());
        logger.debug("pjp.getKind()=" + pjp.getKind());
        logger.debug("pjp.getSignature()=" + pjp.getSignature());
        logger.debug("pjp.getSourceLocation()=" + pjp.getSourceLocation());
        logger.debug("pjp.getStaticPart()=" + pjp.getStaticPart());
        logger.debug("pjp.getTarget()=" + pjp.getTarget());
        logger.debug("pjp.getThis()=" + pjp.getThis());

        logger.debug("................");
        Signature sig = pjp.getSignature();

        logger.debug("sig.getDeclaringTypeName()=" + sig.getDeclaringTypeName());
        logger.debug("sig.getModifiers()=" + sig.getModifiers());
        logger.debug("sig.getName()=" + sig.getName());
        logger.debug("sig.getClass()=" + sig.getClass());
        logger.debug("sig.getDeclaringType()=" + sig.getDeclaringType());
    }
        */
} // END: OperationPermissionAdvice
