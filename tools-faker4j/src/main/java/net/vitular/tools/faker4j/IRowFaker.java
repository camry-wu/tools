/*
 * -----------------------------------------------------------
 * file name  : IRowFaker.java
 * creator    : camry(camry.wu@gmail.com)
 * created    : Mon 04 Feb 2013 01:08:43 PM CST
 * copyright  : (c) 2013 Vitular Inc. All Rights Reserved.
 *
 * modifications:
 *
 * -----------------------------------------------------------
 */
package net.vitular.tools.faker4j;

/**
 * to-do interface of one row faker.
 *
 * @author $Author$
 * @version $Revision$ $Date$
 */
public interface IRowFaker {

    /**
     * generate row.
     *
     * @return the new row instance
     */
    public Object next();
} // END: IRowFaker
///:~
