/*
 * -----------------------------------------------------------
 * file name  : MACFieldFaker.java
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
 * to-do MAC Value Faker of Test Data .
 *
 * @author $Author$
 * @version $Revision$ $Date$
 */
public class MACFieldFaker extends AbstractFieldFaker {

    /**
     * format: ?.
     */

    /**
     * separator: ':' or '-' ...
     */
    private String _sSeparator = ":";

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
    public MACFieldFaker(final String name, final IFakerContext fakerContext) {
        super(name, fakerContext);
    }

    /**
     * setup field faker.
     * @param fakerExpression faker expression
     */
    public void initial(final FakerExpression fakerExpression) {
        super.initial(fakerExpression);

        String sep = fakerExpression.getFormat();

        if (sep != null && !"".equals(sep)) {
            _sSeparator = sep;
        }
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
     *
     * @return the option value
     */
    protected Object generateEnumValue() {
        String option = getRandomOption();
        _value = option;

        return _value;
    }

    /**
     * get random value.
     *
     * @return the random value
     */
    protected Object generateRandomValue() {
        int sd = (int) (getSeed() * 1000000000);
        int p1 = (int) Math.abs(sd >> 24);
        int p2 = (int) Math.abs((sd << 8) >> 24);
        int p3 = (int) Math.abs((sd << 16) >> 24);
        int p4 = (int) Math.abs((sd << 24) >> 24);
        int p5 = (int) Math.abs((sd << 10) >> 24);
        int p6 = (int) Math.abs((sd << 20) >> 24);

        String sp1 = convert2Hex(p1);
        String sp2 = convert2Hex(p2);
        String sp3 = convert2Hex(p3);
        String sp4 = convert2Hex(p4);
        String sp5 = convert2Hex(p5);
        String sp6 = convert2Hex(p6);

        StringBuffer ret = new StringBuffer();
        ret.append(sp1).append(_sSeparator).append(sp2).append(_sSeparator).append(sp3)
            .append(_sSeparator).append(sp4).append(_sSeparator).append(sp5)
            .append(_sSeparator).append(sp6);

        _value = ret.toString();

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
        // use system encoding
        byte[] ret = String.valueOf(_value).getBytes();

        return ret;
    }

    ////// private methods ////////

    /**
     * convert int to hex string.
     *
     * @param isrc
     * @return
     */
    private String convert2Hex(final int isrc) {
        if (isrc == 0) {
            return "00";
        }

        String ret = Integer.toHexString(isrc);
        if (ret.length() < 2) {
            ret = "0" + ret;
        }

        return ret;
    }
} // END: MACFieldFaker
///:~
