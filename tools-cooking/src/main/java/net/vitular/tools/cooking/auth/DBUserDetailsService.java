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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * DB user details service.
 *
 * @author wuhao
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
     * @throws UsernameNotFoundException
     */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = null;

        List<GrantedAuthority> authlist = new ArrayList<GrantedAuthority> ();

        authlist.add(new SimpleGrantedAuthority("ROLE_USER"));

        if (_logger.isDebugEnabled()) {
            _logger.debug("call loadUserByUsername()...");
        }

        if ("camry".equals(username)) {
            authlist.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        user = new User(username, "202cb962ac59075b964b07152d234b70", true, true, true, true, authlist);
        //user = new User(username, "123", true, true, true, true, authlist);

        return user;
    }
} // END: DBUserDetailsService
///:~
