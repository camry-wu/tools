/*
 * -----------------------------------------------------------
 * file name  : IFileFaker.java
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
 * to-do interface of one file faker.
 *
 * @author $Author$
 * @version $Revision$ $Date$
 */
public interface IFileFaker {

    /**
     * initial faker.
     */
    public void initial();

    /**
     * get faker context.
     */
    public IFakerContext getFakerContext();

    /**
     * generate file.
     */
    public void generateFile();

    /**
     * release resources.
     */
    public void release();
} // END: IFileFaker
///:~
