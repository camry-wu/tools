/*
 * -----------------------------------------------------------
 * file name  : MockUserSupport.java
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
 * a mock object to implement IUserSupport.
 *
 * @author camry
 * @version $Revision$
 *          $Date$
 */
public class MockUserSupport implements IUserSupport {

    /**
     * default constructor.
     */
    public MockUserSupport() {
        super();
    }

    /**
     * get current login username.
     *
     * @return user name
     */
    public String getCurrentLoginUsername() {
        return null;
    }
} // END: MockUserSupport
///:~
