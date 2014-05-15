/*
 * -----------------------------------------------------------
 * file name  : SequenceFieldFaker.java
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

import java.nio.ByteBuffer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * to-do Sequence Value Faker of Test Data .
 *
 * @author $Author$
 * @version $Revision$ $Date$
 */
public class SequenceFieldFaker extends AbstractFieldFaker {

    /**
     * step.
     */
    private long _lStep = 1;

    /**
     * field's value.
     */
    private Object _value;

    /**
     * the bytes size.
     * Long:    8
     * Int:     4
     * Short:   2
     * Byte:    1
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
    public SequenceFieldFaker(final String name, final IFakerContext fakerContext) {
        super(name, fakerContext);
    }

    /**
     * setup field faker.
     * @param fakerExpression faker expression
     */
    public void initial(final FakerExpression fakerExpression) {
        super.initial(fakerExpression);

        String limit = fakerExpression.getLimit();

        String splitChars = ",";
        String[] tokens = limit.split(splitChars);
        if (tokens == null || tokens.length < 2) {
            throw new IllegalArgumentException(String.format("faker expression error: wrong sequence limit [%s]", limit));
        }

        String sStartWith = tokens[0].trim();
        String sStep = tokens[1].trim();

        if (sStartWith != null && !"".equals(sStartWith)) {
            _value = Long.valueOf(sStartWith);
        } else {
            _value = new Long(0L);
        }

        if (sStep != null && !"".equals(sStep)) {
            _lStep = Integer.parseInt(sStep);
        }

        String format = fakerExpression.getFormat();
        Scanner sc = new Scanner(format);
        if (sc.hasNextInt()) {
            _iByteSize = sc.nextInt();
        }
        sc.close();
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
        _value = Long.valueOf(option);

        return _value;
    }

    /**
     * get random value.
     *
     * @return the random value
     */
    protected Object generateRandomValue() {
        long ret = ((Long) _value).longValue();
        ret += _lStep;

        _value = new Long(ret);

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
        long v = ((Long) _value).longValue();

        byte[] ret = null;

        ByteBuffer bb = ByteBuffer.allocate(_iByteSize);

        switch (_iByteSize) {
            case 1:
                ret = bb.put((byte) v).array();
                break;
            case 2:
                ret = bb.putShort((short) v).array();
                break;
            case 4:
                ret = bb.putInt((int) v).array();
                break;
            case 8:
            default:
                ret = bb.putLong(v).array();
                break;
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
} // END: SequenceFieldFaker
///:~
