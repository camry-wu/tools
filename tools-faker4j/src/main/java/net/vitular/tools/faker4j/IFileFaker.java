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
     * generate file in the indicated period time.
     *
     * @param period    unit is milliseconds
     */
    public void generateFile(final long period);

    /**
     * release resources.
     */
    public void release();

    /**
     * get record type count.
     *
     * @return record type count
     */
    public int getRecordTypeCount();

    /**
     * set row size for the indicated record type.
     *
     * @param recordType    record type
     * @param rowSize       row size
     */
    public void setRowSize(final int recordType, final int rowSize);

    /**
     * get row size for the indicated record type.
     *
     * @param recordType    record type
     * @return row size of the record type
     */
    public int getRowSize(final int recordType);
} // END: IFileFaker
///:~
