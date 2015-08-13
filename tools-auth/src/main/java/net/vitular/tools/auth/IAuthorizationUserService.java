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
    public AuthorizationUser loadUserByUsername(final String username) throws UnknownAccountException;

    /**
     * save user to db.
     *
     * @param user      AuthorizationUser
     * @return user oid
     */
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
} // END: IAuthorizationUserService
///:~
