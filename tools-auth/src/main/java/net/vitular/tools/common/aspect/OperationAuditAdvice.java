/*
 * -----------------------------------------------------------
 * file name  : OperationAuditAdvice.java
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

import net.vitular.tools.common.annotation.OperationAudit;
/*
import com.framework.IFactory;
import com.framework.exception.FortiException;

import com.framework.util.AspectUtils;
import com.framework.util.StringUtils;
import com.framework.util.IProperty;

import com.framework.access.domain.IUserInfo;
import com.framework.audit.helper.IAuditHelper;
import com.framework.audit.domain.IAuditee;
*/
/**
 * Advice of OperationAudit.
 *
 * @author camry
 * @version $Revision$
 *          $Date$
 */
public class OperationAuditAdvice {

    /**
     * logger.
     */
    protected Log _logger = LogFactory.getLog(getClass());

    /**
     * write user operation audit.
     *
     * @param joinPoint aspect join point
     * @return Object
     * @throws Throwable
     */
    @SuppressWarnings("unchecked")
    public Object audit(final ProceedingJoinPoint joinPoint) throws Throwable {

        Method targetMethod = AspectUtils.getJoinPointMethod(joinPoint);
        if (targetMethod == null) {
            return joinPoint.proceed();
        }

        if (_logger.isDebugEnabled()) {
            _logger.debug(String.format("before write audit for %s.%s.", targetMethod.getDeclaringClass().getName(), targetMethod.getName()));
        }

        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Exception e) {
            throw e;
        }

        if (_logger.isDebugEnabled()) {
            _logger.debug(String.format("after write audit for %s.%s.", targetMethod.getDeclaringClass().getName(), targetMethod.getName()));
        }

        return result;
    }

/*
    private IAuditHelper auditHelper;
    private IFactory factory;

    public Object logUserOperation(ProceedingJoinPoint joinPoint) throws Throwable
    {
        Audit annoAudit = (Audit)AspectUtils.getAnnotationObj(joinPoint, Audit.class);

        if( annoAudit == null )
        {
            return joinPoint.proceed();
        }

        Object[] aobjArgs = joinPoint.getArgs();

        if( aobjArgs != null )
        {
            IUserInfo userInfo = null;
            IAuditee auditeeObj = null;
            Long longAuditeeOid = null;
            int iAuditeeType;
            int iOperationType;
            String sOperationParProp;
            Locale locale = null;
            IProperty proAppMsg = null;

            // UserInfo
            userInfo = (IUserInfo) AspectUtils.getParameterValue(aobjArgs, IUserInfo.class);
            if( userInfo == null )
                throw new FortiException("The method should have parameter user:" + joinPoint.getSignature().getName());

            // Auditee Type
            iAuditeeType = annoAudit.auditeeType();
            // Operation Type
            iOperationType = annoAudit.operationType();

            // Auditee and Auditee OID
            if( aobjArgs[annoAudit.auditeeParIdx()] != null )
            {
                if( aobjArgs[annoAudit.auditeeParIdx()] instanceof IAuditee )
                {
                    auditeeObj = (IAuditee) aobjArgs[annoAudit.auditeeParIdx()];

                    if( auditeeObj != null )
                    {
                        longAuditeeOid = auditeeObj.getOid();
                        if( longAuditeeOid == null || longAuditeeOid.equals(0L)  )
                        {
                            if( annoAudit.insertOperationType() != 0 )
                            {
                                iOperationType = annoAudit.insertOperationType();
                            }
                            else
                            {
                                throw new FortiException("The auditee should have property oid:" + joinPoint.getSignature().getName());
                            }
                        }
                    }
                    else
                    {
                        throw new FortiException("The method should have parameter auditee:" + joinPoint.getSignature().getName());
                    }
                }
                else
                {
                    longAuditeeOid = (Long) aobjArgs[annoAudit.auditeeParIdx()];

                    if( longAuditeeOid == null || longAuditeeOid.equals(0L) )
                    {
                        throw new FortiException("The method should have parameter oid:" + joinPoint.getSignature().getName());
                    }
                }
            }
            else
            {
                throw new FortiException("The method should have parameter auditee:" + joinPoint.getSignature().getName());
            }

            // operationParProp
            sOperationParProp = annoAudit.operationParProp();
            if( !StringUtils.isEmptyAfterTrim(sOperationParProp) )
            {
                // Locale
                locale = (Locale) AspectUtils.getParameterValue(aobjArgs, Locale.class);
                // property
                proAppMsg = factory.getAppMessageResources();
            }
            else
                sOperationParProp = null;

            // Proceed the target method
            try
            {
                // Proceed
                Object objRet = joinPoint.proceed();

                // Log user operation
                if( longAuditeeOid == null || longAuditeeOid.equals(0L)  )
                {
                    longAuditeeOid = auditeeObj.getOid();
                }

                if( longAuditeeOid != null && !longAuditeeOid.equals(0L) )
                {
                    auditHelper.logUserOperation(longAuditeeOid, userInfo, iAuditeeType, iOperationType,
                            sOperationParProp, auditeeObj, annoAudit.operationParVarFields(), annoAudit.auditeeAsOperationPar(),
                            annoAudit.auditPOJOClass(), proAppMsg, locale);
                }
                else
                {
                    throw new FortiException("The auditee should have property oid:" + joinPoint.getSignature().getName());
                }

                return objRet;
            }
            catch( Exception e )
            {
                throw e;
            }
        }
        else
        {
            throw new FortiException("The method should has arguments which meet the Audit's principal:" + joinPoint.getSignature().getName());
        }
    }
*/
    /*===================================================================*
     *                      Spring Beans                                 *
     *===================================================================*/
/*
    public IAuditHelper getAuditHelper()
    {
        return auditHelper;
    }

    public void setAuditHelper(IAuditHelper auditHelper)
    {
        this.auditHelper = auditHelper;
    }

    public IFactory getFactory()
    {
        return factory;
    }

    public void setFactory(IFactory factory)
    {
        this.factory = factory;
    }
*/
}
