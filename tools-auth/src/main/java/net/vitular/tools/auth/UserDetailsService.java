/*
 * -----------------------------------------------------------
 * file name  : UserDetailsService.java
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
public interface UserDetailsService {

    /**
     * load user by username.
     *
     * @param username
     * @return
     * @throws UnknownAccountException
     */
    public LoginUser loadUserByUsername(final String username) throws UnknownAccountException;
} // END: UserDetailsService
///:~
