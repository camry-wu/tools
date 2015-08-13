/*
 * -----------------------------------------------------------
 * file name  : DBAuthorizationUserService.java
 * creator    : camry(camry_camry@sina.com)
 * created    : Fri 12 Jun 2015 12:17:59 PM CST
 *
 * modifications:
 *
 * -----------------------------------------------------------
 */
package net.vitular.tools.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

//import org.springframework.dao.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.apache.shiro.authc.UnknownAccountException;

/**
 * DB user details service.
 *
 * @author camry
 * @version $Revision$
 *          $Date$
 */
public class DBAuthorizationUserService implements IAuthorizationUserService {

    /**
     * logger.
     */
    protected Log _logger = LogFactory.getLog(getClass());

    /**
     * authorization user dao.
     */
    @Autowired(required=true)
    @Qualifier("authorizationUserDao")
    private IAuthorizationUserDao _authorizationUserDao;

    /**
     * default constructor.
     */
    public DBAuthorizationUserService() {
        super();
    }

    /**
     * load user by username.
     *
     * @param username
     * @return
     * @throws UnknownAccountException
     */
    public AuthorizationUser loadUserByUsername(String username) throws UnknownAccountException {
        AuthorizationUser user = _authorizationUserDao.loadUserByUsername(username);
        /*
        user.setSalt("123");
        PasswordHelper.encryptPassword(user);
        System.out.println("pwd:" + user.getPassword());
        */

        return user;
    }

    /**
     * save user to db.
     *
     * @param user AuthorizationUser
     * @return user oid
     */
    public Long saveUser(final AuthorizationUser user) {
        return _authorizationUserDao.saveUser(user);
    }
} // END: DBAuthorizationUserService
///:~
