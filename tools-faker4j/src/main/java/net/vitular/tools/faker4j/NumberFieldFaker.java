/*
 * -----------------------------------------------------------
 * file name  : NumberFieldFaker.java
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
 * to-do Number Value Faker of Test Data .
 *
 * @author $Author$
 * @version $Revision$ $Date$
 */
public class NumberFieldFaker extends AbstractFieldFaker {

    /**
     * format: string, number.
     */
    public static final String FORMAT_STRING = "string";
    public static final String FORMAT_NUMBER = "number";

    /**
     * max and min.
     */
    private Long _lMin = null;
    private Long _lMax = null;

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
    public NumberFieldFaker(final String name, final IFakerContext fakerContext) {
        super(name, fakerContext);
    }

    /**
     * setup field faker.
     * @param fakerExpression faker expression
     */
    public void initial(final FakerExpression fakerExpression) {
        super.initial(fakerExpression);

        String limit = fakerExpression.getLimit();

        if (limit != null && limit.length() > 0) {
            String splitChars = "\\.\\.";
            String[] tokens = limit.split(splitChars);
            if (tokens == null || tokens.length < 2) {
                throw new IllegalArgumentException(String.format("faker expression error: wrong number limit [%s]", limit));
            }

            String smin = tokens[0].trim();
            String smax = tokens[1].trim();

            if (smin != null && !"".equals(smin)) {
                _lMin = Long.valueOf(smin);
                if (_lMin <= 0) {
                    throw new IllegalArgumentException(String.format("faker expression error: the min value should bigger then 0 [%s]", limit));
                }
            }

            if (smax != null && !"".equals(smax)) {
                _lMax = Long.valueOf(smax);
            }
        }

        _iByteSize = 8;
        String format = fakerExpression.getFormat();
        if (format != null) {
            Scanner sc = new Scanner(format);
            if (sc.hasNextInt()) {
                _iByteSize = sc.nextInt();
            }
            sc.close();
        }

        //_logger.debug(String.format("number generate[%s] limit: min[%d] max[%d].", getName(), _lMin, _lMax));
    }

    /**
     * generate calculated value.
     *
     * @return the calculated value
     */
    protected Object generateCalculatedValue() {
        assert(_relatedField != null);

        Object relatedNumber = _relatedField.getValue();
        if (!(relatedNumber instanceof Long)) {
            throw new IllegalArgumentException("The related field's value isnot Long.");
        }
        long base = ((Long) relatedNumber).longValue();

        long lHowMuch = 0L;
        String showmuch = _fakerExpression.getCalculatedVar();
        if (showmuch != null && !"".equals(showmuch)) {
            lHowMuch = Long.parseLong(showmuch);
        }

        if ("+".equals(_fakerExpression.getCalculatedOp())) {
            base += lHowMuch;
        } else {
            base -= lHowMuch;
        }

        _value = new Long(base);

        return _value;
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

        int length = (int) (Math.random() * getSeed() * 1000) % 6 + 1;
        double mi = Math.pow(10, length);
        double off = Math.random() * 1333;
        long ret = (long) ((Math.random() * mi) + off);

        if (_lMin != null) {
            long min = _lMin.longValue();

            if (ret <= min) {
                int p = ((min - ret) % min) == 0 ? 0 : 1;
                ret += (((min - ret) / min) + p) * min;
            }
        }

        if (_lMax != null) {
            long max = _lMax.longValue();

            if (ret > max) {
                int p = ((ret - max) % max) == 0 ? 0 : 1;
                ret -= (((ret - max) / max) + p) * max;
            }

            if (_lMin != null) {
                long min = _lMin.longValue();
                if (ret < min) {
                    ret = (max - min) * ret / min + min;
                }
            }
        }

        _value = new Long(ret);

        return _value;
    }

    /**
     * format value to string base on format config.
     *
     * @return the format string value
     */
    public String formatValue() {
        // String format = _fakerExpression.getFormat();

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
} // END: NumberFieldFaker
///:~
