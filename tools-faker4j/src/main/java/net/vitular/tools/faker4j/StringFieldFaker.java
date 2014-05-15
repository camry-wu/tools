/*
 * -----------------------------------------------------------
 * file name  : StringFieldFaker.java
 * creator    : camry(camry.wu@gmail.com)
 * created    : Mon 04 Feb 2013 01:08:43 PM CST
 * copyright  : (c) 2013 Vitular Inc. All Rights Reserved.
 *
 * modifications:
 *
 * -----------------------------------------------------------
 */
package net.vitular.tools.faker4j;

import java.util.Scanner;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * to-do String Value Faker of Test Data .
 *
 * @author $Author$
 * @version $Revision$ $Date$
 */
public class StringFieldFaker extends AbstractFieldFaker {

    /**
     * format: used as fixed bytes size.
     */

    /**
     * limit: length.
     */
    private int _iLength = 6;

    /**
     * field's value.
     */
    private Object _value;

    /**
     * if byte size is fixed, should cut or concat with ' ' or \0.
     * 0 indicate no fixed size.
     */
    private int _iByteSize;

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
    public StringFieldFaker(final String name, final IFakerContext fakerContext) {
        super(name, fakerContext);
    }

    /**
     * setup field faker.
     * @param fakerExpression faker expression
     */
    public void initial(final FakerExpression fakerExpression) {
        super.initial(fakerExpression);

        String len = fakerExpression.getLimit();

        if (len != null && !"".equals(len)) {
            _iLength = Integer.parseInt(len);
        }

        _iByteSize = 0;
        String format = fakerExpression.getFormat();
        if (format != null) {
            Scanner sc = new Scanner(format);
            if (sc.hasNextInt()) {
                _iByteSize = sc.nextInt();
            }
            sc.close();
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
        StringBuffer ret = new StringBuffer("");
        int size = 26;

        for (int i = 0; i < _iLength; i++) {
            double off = Math.random() * 33;
            double mi = Math.pow(10, 2);

            int tmp = (int) (getSeed() * mi + off);
            tmp = tmp % size;
            tmp += 97;
            ret.append((char)tmp);
        }

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
     * dump value to bytes array.
     *
     * @return bytes array
     */
    public byte[] getBytes() {
        // use system encoding
        byte[] ret = null;
        String tmp = (String) _value;

        if (_iByteSize == 0) {
            ret = tmp.getBytes();
        } else {
            ret = new byte[_iByteSize];

            int len = tmp.length();
            int destPos = _iByteSize - len;

            if (len > _iByteSize) {
                len = _iByteSize;
                destPos = 0;
            }

            System.arraycopy(tmp.getBytes(), 0, ret, destPos, len);
        }

        return ret;
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
} // END: StringFieldFaker
///:~
