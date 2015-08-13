/*
 * -----------------------------------------------------------
 * file name  : IUserSupport.java
 * creator    : camry(camry_camry@sina.com)
 * created    : Thu 13 Aug 2015 01:43:14 PM CST
 *
 * modifications:
 *
 * -----------------------------------------------------------
 */
package net.vitular.tools.common;

/**
 * provide some methods to get authorization user info.
 *
 * @author camry
 * @version $Revision$
 *          $Date$
 */
public interface IUserSupport {

    /**
     * get current login username.
     *
     * @return user name
     */
    public String getCurrentLoginUsername();
} // END: IUserSupport
///:~
