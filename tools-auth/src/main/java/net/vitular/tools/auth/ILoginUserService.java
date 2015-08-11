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

/**
 * user details service.
 *
 * @author camry
 * @version $Revision$
 *          $Date$
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
    public Long saveUser(final LoginUser user);
} // END: ILoginUserService
///:~
