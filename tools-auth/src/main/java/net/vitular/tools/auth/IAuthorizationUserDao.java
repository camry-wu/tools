/*
 * -----------------------------------------------------------
 * file name  : IAuthorizationUserDao.java
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
 * authorization user dao interface.
 *
 * @author camry
 * @version $Revision$
 *          $Date$
 */
public interface IAuthorizationUserDao {

    /**
     * get user by oid.
     *
     * @param oid user oid
     * @return AuthorizationUser
    public AuthorizationUser getUserByOid(final Long oid);
     */

    /**
     * save user to db.
     *
     * @param user AuthorizationUser
     * @return user oid
     */
    public Long saveUser(final AuthorizationUser user);

    /**
     * load user by username.
     *
     * @param username  user name
     * @return AuthorizationUser
     */
    public AuthorizationUser loadUserByUsername(final String username);

    /**
     * list user base on the query params.
     *
     * @param queryParams AuthorizationUserQueryParams
     * @return SearchResult
    public SearchResult listUsers(final AuthorizationUserQueryParams queryParams);
     */
}
