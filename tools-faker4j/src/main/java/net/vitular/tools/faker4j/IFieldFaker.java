/*
 * -----------------------------------------------------------
 * file name  : IFieldFaker.java
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
 * to-do interface of one field faker.
 *
 * @author $Author$
 * @version $Revision$ $Date$
 */
public interface IFieldFaker {

    /**
     * setup field faker.
     * @param fakerExpression faker expression
     */
    public void initial(final FakerExpression fakerExpression);

    /**
     * get faker expression.
     *
     * @return FakerExpression
     */
    public FakerExpression getFakerExpression();

    /**
     * generate field value.
     *
     * @return the field value
     */
    public Object next();

    /**
     * get field name.
     *
     * @return field name
     */
    public String getName();

    /**
     * if this field's value is an array.
     *
     * @return true if field is array
     */
    public boolean isArray();

    /**
     * get the generated field value.
     *
     * @return the field value
     */
    public Object getValue();

    /**
     * format value to string base on format config.
     *
     * @return the format string value
     */
    public String formatValue();

    /**
     * dump value to bytes array.
     *
     * @return bytes array
     */
    public byte[] getBytes();
} // END: IFieldFaker
///:~
