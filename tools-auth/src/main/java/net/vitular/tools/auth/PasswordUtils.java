/*
 * -----------------------------------------------------------
 * file name  : PasswordUtils.java
 * creator    : camry(camry_camry@sina.com)
 * created    : Mon 10 Aug 2015 05:35:40 PM CST
 *
 * modifications:
 *
 * -----------------------------------------------------------
 */
package net.vitular.tools.auth;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.util.ByteSource;

/**
 * password utils.
 *
 * @author camry
 * @version $Revision$
 *          $Date$
 */
public final class PasswordUtils {

    /**
     * algorithm name.
     */
    private final static String _sAlgorithmName = "md5";

    /**
     * hash iterations.
     */
    private final static int _iHashIterations = 2;

    /**
     * random number generator.
     */
    private final static RandomNumberGenerator _randomNumberGenerator = new SecureRandomNumberGenerator();

    /**
     * default constructor.
     */
    public PasswordUtils() {
        super();
    }

    /**
     * encrypt user password.
     *
     * @param user          Login User
     * @param password      user's password
     */
    public static void encryptPassword(final LoginUser user, final String password) {
        user.setSalt(_randomNumberGenerator.nextBytes().toHex());

        String newPassword = new SimpleHash(
            _sAlgorithmName,
            password,
            ByteSource.Util.bytes(user.getCredentialsSalt()),
            _iHashIterations).toHex();

        user.setPassword(newPassword);
    }
} // END: PasswordUtils
///:~
