/*
 * -----------------------------------------------------------
 * file name  : IgnoreTestSuite.java
 * authors    : camry(camry.wu@gmail.com)
 * created    : 2010-10-12
 * copyright  : (c) 2010 Vitular Inc. All Rights Reserved.
 *
 * modifications:
 *
 * -----------------------------------------------------------
 */
package net.vitular.tools.test;

import java.lang.annotation.Target;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

/**
 * to-do if the test method is ignore.
 *
 * @author $Author$
 * @version $Revision$
 *          $Date$
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreTestSuite {
    public String[] value();
} // END: IgnoreTestSuite
///:~
