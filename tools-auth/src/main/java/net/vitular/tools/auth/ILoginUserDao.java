/*
 * -----------------------------------------------------------
 * file name  : ILoginUserDao.java
 * creator    : camry(camry_camry@sina.com)
 * created    : Fri 12 Jun 2015 12:17:59 PM CST
 *
 * modifications:
 *
 * -----------------------------------------------------------
 */
package net.vitular.tools.auth;

import java.util.List;

import net.vitular.tools.common.dao.SearchResult;

/**
 * login user dao interface.
 *
 * @author camry
 * @version $Revision$
 *          $Date$
 */
public interface ILoginUserDao {

    /**
     * get user by oid.
     *
     * @param oid user oid
     * @return LoginUser
    public LoginUser getUserByOid(final Long oid);
     */

    /**
     * save user to db.
     *
     * @param user LoginUser
     * @return user oid
     */
    public Long saveUser(final LoginUser user);

    /**
     * load user by username.
     *
     * @param username  user name
     * @return LoginUser
     */
    public LoginUser loadUserByUsername(final String username);

    /**
     * list user base on the query params.
     *
     * @param queryParams LoginUserQueryParams
     * @return SearchResult
    public SearchResult listUsers(final LoginUserQueryParams queryParams);
     */
}
