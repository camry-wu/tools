/*
 * -----------------------------------------------------------
 * file name  : OperationAudit.java
 * creator    : camry(camry_camry@sina.com)
 * created    : Sat 15 Aug 2015 16:00:39 PM CST
 *
 * modifications:
 *
 * -----------------------------------------------------------
 */
package net.vitular.tools.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//import net.vitular.tools.common.domain.AuditPOJO;
//import net.vitular.tools.common.domain.IAuditPOJO;

/**
 * Audit Information for Audit Aspect.
 *
 * @author camry
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OperationAudit {
    // we can get Auditee object base on this name in the method variables.
    // if the var is null:
    //      donot save audit, and report warning
    //
    // if the var is String Or Number:
    //      it is auditee oid
    //      Auditee name is get from register: package -> auditee module name.
    //      package = method.getDeclaringClass().getPackage().getName()
    //
    // if the var is Object:
    //      get obj name base on the obj's class
    //      the Object must provide IAuditPOJO.getAuditeeModuleName()
    //
    // if the var is a sub object:
    //      Parent-ModuleName + Sub-MouleName is auditee name
    //      the Object must provide IAuditPOJO.getAuditeeModuleName()
    //
    // the auditee name be stored in db should be a resource key!
    public String auditeeVarName();

    // AuditeeOid
    // if auditee is object, it must provide IAuditPOJO.getOid()
    // if operation is create, the oid of auditee is null,
    // set auditee.oid after insert the auditee

    // Operation Type
    // New, Edit, Delete, Import, Export, Validate, Invalidate, Approve, Reject...
    // if method is saveOrUpdate, how to test it is New or Update?
    // if oid is 0 or null, the op type is New?
    public int operationType() default 0;

    // If it is not 0, then we should set it as operation type when the auditee's OID is null
    //public int insertOperationType() default 0;

    // Operation Parameter Property Key
    //public String operationParProp() default "";

    // Operation Parameter's variable fields of Auditee, it is only valid when operationParProp is not null
    //public String[] operationParVarFields() default {};

    // AuditPojo Class Name, if it is null then use default AuditPOJO
    // Class<? extends IAuditPOJO> auditPOJOClass() default AuditPOJO.class;
}
