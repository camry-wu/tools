/*
 * -----------------------------------------------------------
 * file name  : IAuthorizationUserService.java
 * creator    : camry(camry_camry@sina.com)
 * created    : Fri 19 Jun 2015 04:22:06 PM CST
 *
 * modifications:
 *
 * -----------------------------------------------------------
 */
package net.vitular.tools.auth;

import org.apache.shiro.authc.UnknownAccountException;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import net.vitular.tools.common.annotation.Cacheable;
import net.vitular.tools.common.annotation.OperationAudit;
import net.vitular.tools.common.annotation.OperationPermission;
import net.vitular.tools.common.annotation.Trigger;
import net.vitular.tools.common.annotation.Validator;

/**
 * user details service.
 *
 * @author camry
 * @version $Revision$
 *          $Date$
 *
 *  transactional desc
 *  -------------------
 *  @Transactional:
 *      propagation:    REQUIRED, NOT_SUPPORTED, REQUIRED_NEW, MANDATORY, NEVER, SUPPORTS
 *      isolation:      READ_UNCOMMITTED, READ_COMMITTED, REPEATABLE_READ, SERIALIZABLE
 *      readOnly
 *      timeout
 *      rollbackFor
 *      rollbackForClassName
 *      noRollbackFor
 *      noRollbackForClassName
 */
public interface IAuthorizationUserService {

    /**
     * load user by username.
     *
     * @param username
     * @return
     * @throws UnknownAccountException
     */
    @Transactional(propagation=Propagation.REQUIRED)
    @OperationPermission(permissions={"p1", "p2"})
    @Cacheable(cacheName="c1")
    @Validator("v1")
    @OperationAudit(auditeeVarName="a1")
    @Trigger("t1")
    public AuthorizationUser loadUserByUsername(final String username) throws UnknownAccountException;

    /**
     * save or update user.
     *
     * @param user      AuthorizationUser
     * @return user oid
     */
    // @Permission(permission chain)
    // @Trigger(trigger chain)
    // @Validator(validator chain)
    // @Audit
    // @Cache(clear)
    @Transactional(propagation=Propagation.REQUIRED)
    public Long saveUser(final AuthorizationUser user);

    /**
     * change user password.
     *
     * @param username  user name
     * @param password  password
     * @return user oid
     */
    @Transactional(propagation=Propagation.REQUIRED)
    public Long updatePassword(final String username, final String password);

    /**
     * delete user.
     *
     * @param user      AuthorizationUser
     */
    @Transactional(propagation=Propagation.REQUIRED)
    public void deleteUser(final AuthorizationUser user);

    // public Long validate(final AuthorizationUser user);
    // public Long invalidate(final AuthorizationUser user);

    // public Long lock(final AuthorizationUser user);
    // public Long unlock(final AuthorizationUser user);
} // END: IAuthorizationUserService
///:~
