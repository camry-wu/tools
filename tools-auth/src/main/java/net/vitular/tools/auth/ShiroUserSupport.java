/*
 * -----------------------------------------------------------
 * file name  : ShiroUserSupport.java
 * creator    : camry(camry_camry@sina.com)
 * created    : Thu 13 Aug 2015 01:52:49 PM CST
 *
 * modifications:
 *
 * -----------------------------------------------------------
 */
package net.vitular.tools.auth;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import net.vitular.tools.common.IUserSupport;

/**
 * a implement of IUserSupport which base on shiro.
 *
 * @author camry
 * @version $Revision$
 *          $Date$
 */
public class ShiroUserSupport implements IUserSupport {

    /**
     * default constructor.
     */
    public ShiroUserSupport() {
        super();
    }

    /**
     * get current login username.
     *
     * @return user name
     */
    public String getCurrentLoginUsername() {
        Subject subject = SecurityUtils.getSubject();
        String username = (subject != null) ? (String) subject.getPrincipal() : null;
        return username;
    }
} // END: ShiroUserSupport
///:~
