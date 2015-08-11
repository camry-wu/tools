/*
 * -----------------------------------------------------------
 * file name  : DBLoginUserService.java
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

import org.springframework.dao.DataAccessException;

import org.apache.shiro.authc.UnknownAccountException;

/**
 * DB user details service.
 *
 * @author camry
 * @version $Revision$
 *          $Date$
 */
public class DBLoginUserService implements ILoginUserService {

    /**
     * logger.
     */
    protected Log _logger = LogFactory.getLog(getClass());

    /**
     * login user dao.
     */
    private ILoginUserDao _loginUserDao;

    public void setLoginUserDao(final ILoginUserDao loginUserDao) { _loginUserDao = loginUserDao; }
    public ILoginUserDao getLoginUserDao() { return _loginUserDao; }

    /**
     * default constructor.
     */
    public DBLoginUserService() {
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
        LoginUser user = _loginUserDao.loadUserByUsername(username);
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
     * @param user LoginUser
     * @return user oid
     */
    public Long saveUser(final LoginUser user) {
        return _loginUserDao.saveUser(user);
    }
} // END: DBLoginUserService
///:~
