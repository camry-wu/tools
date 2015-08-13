/*
 * -----------------------------------------------------------
 * file name  : UserTest.java
 * creator    : camry(camry_camry@sina.com)
 * created    : Mon 27 Jul 2015 04:56:44 PM CST
 *
 * modifications:
 *
 * -----------------------------------------------------------
 */
package net.vitular.tools.auth;

import java.util.Date;

import org.junit.Test;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.apache.shiro.authc.UnknownAccountException;

/**
 * test user module.
 *
 * @author camry
 * @version $Revision$
 *          $Date$
 */
public class UserTest {

    /**
     * spring bean factory.
     */
    protected static ApplicationContext _context = null;

    /**
     * default constructor.
     */
    public UserTest() {
        super();
    }

    /**
     * setup.
     */
    @BeforeClass
    public static void setup() {
        if (_context == null) {
            _context = new ClassPathXmlApplicationContext(new String[] {"applicationContext.xml"});
        }
    }

    /**
     * release.
     */
    @After
    public void release() {
    } // END: release

    /**
     * test method for add user.
     */
    @Test
    public void testAddUser() throws Exception {
        IAuthorizationUserService userService = (IAuthorizationUserService) _context.getBean("DBAuthorizationUserService");

        AuthorizationUser au = new AuthorizationUser();
        au.setUsername("danny");
        au.setPassword("danny");
        au.setVerifyEmail("da@163.com");
        au.setVerifyCellPhoneNo("13218033346");

        userService.saveUser(au);
    }

    /**
     * test method for update user.
     */
    @Test
    public void testUpdateUser() throws Exception {
        IAuthorizationUserService userService = (IAuthorizationUserService) _context.getBean("DBAuthorizationUserService");
    }

    /**
     * test method for loadUserByUsername.
     */
    @Test
    public void testLoadUserByUsername() throws Exception {
        IAuthorizationUserService userService = (IAuthorizationUserService) _context.getBean("DBAuthorizationUserService");
        try {
            AuthorizationUser au = (AuthorizationUser) userService.loadUserByUsername("danny");

            System.out.println("------ query user --------");
            System.out.println(au);
        } catch (UnknownAccountException e) {
            System.out.println("donot find danny!");
        }
    }

    /**
     * test method for updatePassword.
     */
    @Test
    public void testUpdatePassword() throws Exception {
        IAuthorizationUserService userService = (IAuthorizationUserService) _context.getBean("DBAuthorizationUserService");
        userService.updatePassword("wuhao", "345");

        AuthorizationUser au = (AuthorizationUser) userService.loadUserByUsername("wuhao");
        System.out.println(au);
    }
} // END: UserTest
///:~
