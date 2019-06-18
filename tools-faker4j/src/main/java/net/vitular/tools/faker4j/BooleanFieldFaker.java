/*
 * -----------------------------------------------------------
 * file name  : BooleanFieldFaker.java
 * creator    : camry(camry.wu@gmail.com)
 * created    : Mon 04 Feb 2013 01:08:43 PM CST
 * copyright  : (c) 2013 Vitular Inc. All Rights Reserved.
 *
 * modifications:
 *
 * -----------------------------------------------------------
 */
package net.vitular.tools.faker4j;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * to-do Boolean Value Faker of Test Data.
 *
 * @author $Author$
 * @version $Revision$ $Date$
 */
public class BooleanFieldFaker extends AbstractFieldFaker {

    /**
     * format: string, number, boolean.
     * string:      "true" | "false"
     * number:      1 | 0
     * boolean:     true | false
     */
    public static final String FORMAT_STRING    = "string";
    public static final String FORMAT_NUMBER    = "number";
    public static final String FORMAT_BOOLEAN   = "boolean";

    /**
     * field's value.
     */
    private Object _value;

    /**
     * get the generated field value.
     *
     * @return the field value
     */
    public Object getValue() { return _value; }

    /**
     * constructor.
     *
     * @param name          field name
     * @param fakerContext  faker context
     */
    public BooleanFieldFaker(final String name, final IFakerContext fakerContext) {
        super(name, fakerContext);
    }

    /**
     * setup field faker.
     * @param fakerExpression faker expression
     */
    public void initial(final FakerExpression fakerExpression) {
        super.initial(fakerExpression);
    }

    /**
     * generate calculated value.
     *
     * @return the calculated value
     */
    protected Object generateCalculatedValue() {
        throw new IllegalArgumentException("unsupport calculated mode.");
    }

    /**
     * get enum value from options.
     * boolean donot support enum, generate a random value
     *
     * @return the option value
     */
    protected Object generateEnumValue() {
        _logger.warn("unsupport to generate enum value.");
        return generateRandomValue();
    }

    /**
     * get random value.
     *
     * @return the random value
     */
    protected Object generateRandomValue() {

        int length = (int) (Math.random() * getSeed() * 1000) % 6 + 1;
        double mi = Math.pow(10, length);
        double off = Math.random() * 1333;
        int ret = (int) ((((Math.random() * mi) + off)) / 2) % 2;

        _value = new Boolean(ret == 1);

        return _value;
    }

    /**
     * format value to string base on format config.
     *
     * @return the format string value
     */
    public String formatValue() {
        if (_value == null) {
            return "";
        }

        return String.valueOf(_value);
    }

    /**
     * to string.
     *
     * @return field name+value
     */
    public String toString() {
        if (isArray()) {
            return getName() + ":\t" + getArrayValue();
        }

        return getName() + ":\t" + String.valueOf(_value);
    }

    /**
     * dump value to bytes array.
     *
     * @return bytes array
     */
    public byte[] getBytes() {
        byte[] ret = new byte[1];

        if (Boolean.TRUE.equals(_value)) {
            ret[0] = (byte) 1;
        } else {
            ret[0] = (byte) 0;
        }

        return ret;
    }
} // END: BooleanFieldFaker
///:~
