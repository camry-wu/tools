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
        SessionFactory sesfac = (SessionFactory) _context.getBean("sessionFactory");

        Session session = sesfac.openSession();
        session.beginTransaction();
/*
        LoginUser u1 = new LoginUser();
        u1.setUsername("danny");
        u1.setPassword("danny");
        u1.setSalt("salt");
        u1.setLocked(false);
        u1.setVerifyEmail("danny@163.com");
        u1.setVerifyCellPhoneNo("13218033345");
        u1.setIsActive(true);

        //u1.setVersion();
        u1.setLastUpdate(new Date());
        u1.setLastUpdater("admin");

        System.out.println("------ add user --------");
        System.out.println(u1);

        session.save(u1);
*/

        LoginUser u2 = (LoginUser) session.get(LoginUser.class, 3L);

        System.out.println("------ query user --------");
        System.out.println(u2);

        u2.setUsername("camry");
        session.save(u2);

        System.out.println("------ update user --------");
        System.out.println(u2);

        session.getTransaction().commit();
        session.close();

        sesfac.close();
    }
} // END: UserTest
///:~
