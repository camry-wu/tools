/*
 * -----------------------------------------------------------
 * file name  : LoginUser.java
 * creator    : camry(camry_camry@sina.com)
 * created    : Wed 17 Jun 2015 04:59:37 PM CST
 *
 * modifications:
 *
 * -----------------------------------------------------------
 */
package net.vitular.tools.cooking.auth;

/**
 * LoginUser domain.
 *
 * @author wuhao
 * @version $Revision$
 *          $Date$
 */
public class LoginUser {

    /**
     * username.
     */
    private String _sUsername;

    /**
     * password.
     */
    private String _sPassword;

    // getter and setter
    public void setUsername(final String username) { _sUsername = username; }
    public String getUsername() { return _sUsername; }

    public void setPassword(final String password) { _sPassword = password; }
    public String getPassword() { return _sPassword; }

    /**
     * default constructor.
     */
    public LoginUser() {
        super();
    }

    /**
     * constructor.
     * @param user  username
     * @param pass  password
     */
    public LoginUser(final String user, final String pass) {
        super();
        _sUsername = user;
        _sPassword = pass;
    }
} // END: LoginUser
///:~
