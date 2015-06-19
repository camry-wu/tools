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

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.util.ByteSource;

/**
 * LoginUser domain.
 *
 * @author camry
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

    /**
     * is locked.
     */
    private Boolean _bLocked = false;

    /**
     * salt = username + salt.
     */
    private String _sSalt;

    // getter and setter
    public void setUsername(final String username) { _sUsername = username; }
    public String getUsername() { return _sUsername; }

    public void setPassword(final String password) { _sPassword = password; }
    public String getPassword() { return _sPassword; }

    public void setLocked(final Boolean locked) { _bLocked = locked; }
    public Boolean getLocked() { return _bLocked; }

    public void setSalt(final String salt) { _sSalt = salt; }
    public String getSalt() { return _sSalt; }

    public String getCredentialsSalt() { return _sUsername + _sSalt; }

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

/**
 * password helper.
 *
 * @author camry
 * @version
 */
class PasswordHelper {
    private final static String algorithmName = "md5";
    private final static int hashIterations = 2;
    private final static RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();

    public static void encryptPassword(final LoginUser user) {
        user.setSalt(randomNumberGenerator.nextBytes().toHex());

        String newPassword = new SimpleHash(
            algorithmName,
            user.getPassword(),
            ByteSource.Util.bytes(user.getCredentialsSalt()),
            hashIterations).toHex();

        user.setPassword(newPassword);
    }
}
///:~
