/*
 * -----------------------------------------------------------
 * file name  : UserRealm.java
 * creator    : camry(camry_camry@sina.com)
 * created    : Fri 19 Jun 2015 11:01:56 AM CST
 *
 * modifications:
 *
 * -----------------------------------------------------------
 */
package net.vitular.tools.cooking.auth;

import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

/**
 * User Realm.
 *
 * @author camry
 * @version $Revision$
 *          $Date$
 */
public class UserRealm extends AuthorizingRealm {

    /**
     * user details service.
     */
    private UserDetailsService _userDetailsService;

    // spring beans
    public void setUserDetailsService(final UserDetailsService userDetailsService) { _userDetailsService = userDetailsService; }
    public UserDetailsService getUserDetailsService() { return _userDetailsService; }

    /**
     * default constructor.
     */
    public UserRealm() {
        super();
    }

    /**
     * Retrieves the AuthorizationInfo for the given principals from the underlying data store. When returning an instance from this method, you might want to consider using an instance of SimpleAuthorizationInfo, as it is suitable in most cases.
     *
     * @param principals the primary identifying principals of the AuthorizationInfo that should be retrieved.
     * @return the AuthorizationInfo associated with this principals.
     */
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        Set<String> roles = new HashSet<String> ();
        info.setRoles(roles);                               // userService.findRoles(username);

        Set<String> permissions = new HashSet<String> ();
        info.setStringPermissions(permissions);             // userService.findRoles(username);

        return info;
    }

    /**
     * Retrieves authentication data from an implementation-specific datasource (RDBMS, LDAP, etc) for the given authentication token.
     *
     * @param token the authentication token containing the user's principal and credentials.
     * @return an AuthenticationInfo object containing account data resulting from the authentication ONLY if the lookup is successful (i.e. account exists and is valid, etc.)
     * @throws AuthenticationException if there is an error acquiring data or performing realm-specific authentication logic for the specified token
     */
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();

        LoginUser user = _userDetailsService.loadUserByUsername(username);

        if (Boolean.TRUE.equals(user.getLocked())) {
            throw new LockedAccountException(); // account locked
        }

        // call CredentialsMatcher to match password
        // if need, can do this yourself
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(
            user.getUsername(),
            user.getPassword(),
            ByteSource.Util.bytes(user.getCredentialsSalt()),   // salt = username + salt
            getName());

        return info;
    }
} // END: UserRealm
///:~
