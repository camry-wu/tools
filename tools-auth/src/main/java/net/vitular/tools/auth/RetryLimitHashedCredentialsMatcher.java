/*
 * -----------------------------------------------------------
 * file name  : RetryLimitHashedCredentialsMatcher.java
 * creator    : camry(camry_camry@sina.com)
 * created    : Fri 19 Jun 2015 12:03:54 PM CST
 *
 * modifications:
 *
 * -----------------------------------------------------------
 */
package net.vitular.tools.auth;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;

/**
 * hashed credentials matcher.
 *
 * @author camry
 * @version $Revision$
 *          $Date$
 */
public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {

    /**
     * logger.
     */
    protected Log _logger = LogFactory.getLog(getClass());

    private EhCacheManager  _cacheManager;

    /**
     * constructor.
     * @param cacheManager cache manager
     */
    public RetryLimitHashedCredentialsMatcher(final EhCacheManager cacheManager) {
        super();
        _cacheManager = cacheManager;
    }

    /**
     * This implementation first hashes the token's credentials, potentially using a salt if the info argument is a SaltedAuthenticationInfo. It then compares the hash against the AuthenticationInfo's already-hashed credentials. This method returns true if those two values are equal, false otherwise.
     *
     * @param token the AuthenticationToken submitted during the authentication attempt.
     * @param info the AuthenticationInfo stored in the system matching the token principal
     * @return true if the provided token credentials hash match to the stored account credentials hash, false otherwise
     */
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        if (_logger.isDebugEnabled()) {
            _logger.debug("call doCredentialsMatch..");
        }

        String username = (String) token.getPrincipal();
        /*
        // retry count + 1
        Element element = passwordRetryCache.get(username);
        if (element == null) {
            element = new Element(username, new AtomicInteger(0));
            passwordRetryCache.put(element);
        } else {
            AtomicInteger retryCount = (AtomicInteger) element.getObjectValue();
            if (retryCount.incrementAndGet() > 10) {
                throw new ExcessiveAttemptsException();
            }
        }*/

        boolean matches = super.doCredentialsMatch(token, info);
        /*if (matches) {
            passwordRetryCache.remove(username);
        }*/

        return matches;
    }
} // END: RetryLimitHashedCredentialsMatcher
///:~
