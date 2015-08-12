/*
 * -----------------------------------------------------------
 * file name  : ILoginUserService.java
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
public interface ILoginUserService {

    /**
     * load user by username.
     *
     * @param username
     * @return
     * @throws UnknownAccountException
     */
    public LoginUser loadUserByUsername(final String username) throws UnknownAccountException;

    /**
     * save user to db.
     *
     * @param user LoginUser
     * @return user oid
     */
    @Transactional(propagation=Propagation.REQUIRED)
    public Long saveUser(final LoginUser user);
} // END: ILoginUserService
///:~
