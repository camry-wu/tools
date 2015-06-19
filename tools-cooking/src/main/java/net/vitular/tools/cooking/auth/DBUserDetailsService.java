/*
 * -----------------------------------------------------------
 * file name  : DBUserDetailsService.java
 * creator    : camry(camry_camry@sina.com)
 * created    : Fri 12 Jun 2015 12:17:59 PM CST
 *
 * modifications:
 *
 * -----------------------------------------------------------
 */
package net.vitular.tools.cooking.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.dao.DataAccessException;

import org.apache.shiro.authc.UnknownAccountException;

/**
 * DB user details service.
 *
 * @author camry
 * @version $Revision$
 *          $Date$
 */
public class DBUserDetailsService implements UserDetailsService {

    /**
     * logger.
     */
    protected Log _logger = LogFactory.getLog(getClass());

    /**
     * default constructor.
     */
    public DBUserDetailsService() {
        super();
    }

    /**
     * load user by username.
     *
     * @param username
     * @return
     * @throws UnknownAccountException
     */
    public LoginUser loadUserByUsername(String username) throws UnknownAccountException {
        return new LoginUser(username, "");
    }
} // END: DBUserDetailsService
///:~
