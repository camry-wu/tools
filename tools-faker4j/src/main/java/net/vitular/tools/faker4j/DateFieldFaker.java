/*
 * -----------------------------------------------------------
 * file name  : DateFieldFaker.java
 * creator    : camry(camry.wu@gmail.com)
 * created    : Mon 04 Feb 2013 01:08:43 PM CST
 * copyright  : (c) 2013 Vitular Inc. All Rights Reserved.
 *
 * modifications:
 *
 * -----------------------------------------------------------
 */
package net.vitular.tools.faker4j;

import java.nio.ByteBuffer;

import java.util.Date;
import java.util.Calendar;

import java.text.SimpleDateFormat;
import java.text.ParseException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * to-do Date Value Faker of Test Data .
 *
 * @author $Author$
 * @version $Revision$ $Date$
 */
public class DateFieldFaker extends AbstractFieldFaker {

    /**
     * date format mode.
     * 1: second value
     * 2: millisecond value
     * 3: use indicated format string
     */
    public static final int FORMAT_MODE_SECOND = 1;
    public static final int FORMAT_MODE_MILLIS = 2;
    public static final int FORMAT_MODE_STRING = 3;

    /**
     * date format of the calculated initial value.
     */
    public static final String INITIAL_VALUE_FORMAT = "yyyyMMdd HHmmss";

    /**
     * date format of the fixed value.
     */
    public static final String FIX_VALUE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * initial date as current time.
     */
    public static final String NOW = "NOW";

    /**
     * date format mode.
     */
    private int _iFormatMode;

    /**
     * date format.
     */
    private SimpleDateFormat _dateFormat = null;

    /**
     * date offset.
     */
    private int _iDateOffset = 0;

    /**
     * is date less then now.
     */
    private boolean _bIsDateLessThenNow = true;

    /**
     * 1 is later, -1 is early.
     */
    private int _iIsLater = 1;

    /**
     * how much early or later.
     * calculate: value = related +|- pv/pb;
     */
    private int _iStep = 0;
    private int _iPercentageValue = 0;
    private int _iPercentageBase = 0;

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
    public DateFieldFaker(final String name, final IFakerContext fakerContext) {
        super(name, fakerContext);
    }

    /**
     * setup field faker.
     * @param fakerExpression faker expression
     */
    public void initial(final FakerExpression fakerExpression) {
        super.initial(fakerExpression);

        String sOffset = _fakerContext.getProperty(FakerConsts.GLOBAL_PROP_KEY_DATE_OFFSET);
        String sDateLessThenNow = _fakerContext.getProperty(FakerConsts.GLOBAL_PROP_KEY_DATELESSTHENNOW);

        if (sOffset != null && !"".equals(sOffset)) {
            _iDateOffset = Integer.parseInt(sOffset);
        }

        if (sDateLessThenNow != null && !"".equals(sDateLessThenNow)) {
            _bIsDateLessThenNow = Boolean.valueOf(sDateLessThenNow).booleanValue();
        }

        // later or early then which field
        _iIsLater = ("+".equals(fakerExpression.getCalculatedOp())) ? 1 : -1;

        String showmuch = fakerExpression.getCalculatedVar();   // example: 1/60(initial long value)
        if (showmuch != null && !"".equals(showmuch)) {
            // use calculate mode, if self increment, need initial value
            int tmp = showmuch.indexOf('(');
            if (tmp != -1) {
                int tmp2 = showmuch.indexOf(')', tmp);

                // the initial value maybe long or 'yyyyMMdd HHmmss'
                // because the calculated variable cannot include '-'
                //
                String initialValue = showmuch.substring(tmp + 1, tmp2);

                if (initialValue.length() >= 15) {                      // yyyyMMdd HHmmss
                    SimpleDateFormat sdf = new SimpleDateFormat(INITIAL_VALUE_FORMAT);
                    try {
                        _value = sdf.parse(initialValue);
                    } catch (ParseException e) {
                        _value = getDateBefore1Day();
                    }
                } else if (initialValue.length() >= 13) {               // long
                    long ltmp = Long.parseLong(initialValue);
                    _value = new Date(ltmp);
                } else if (initialValue.equalsIgnoreCase(NOW)) {        // NOW
                    _value = new Date();
                } else {                                                // others, not defined
                    _value = getDateBefore1Day();
                }

                showmuch = showmuch.substring(0, tmp);
            }

            tmp = showmuch.indexOf('/');
            if (tmp != -1) {
                _iPercentageValue = Integer.parseInt(showmuch.substring(0, tmp));
                _iPercentageBase = Integer.parseInt(showmuch.substring(tmp + 1));
            } else {
                _iPercentageValue = Integer.parseInt(showmuch);
                _iPercentageBase = 1;
            }
        }

        String format = fakerExpression.getFormat();

        if (FakerConsts.DATE_FORMAT_SECOND.equalsIgnoreCase(format)) {
            _iFormatMode = FORMAT_MODE_SECOND;
            _dateFormat = new SimpleDateFormat(FIX_VALUE_FORMAT);

        } else if (FakerConsts.DATE_FORMAT_MILLIS.equalsIgnoreCase(format)) {
            _iFormatMode = FORMAT_MODE_MILLIS;
            _dateFormat = new SimpleDateFormat(FIX_VALUE_FORMAT);

        } else {
            _iFormatMode = FORMAT_MODE_STRING;

            if (format == null || "".equals(format)) {
                _dateFormat = new SimpleDateFormat(FIX_VALUE_FORMAT);
            } else {
                _dateFormat = new SimpleDateFormat(format);
            }
        }
    }

    /**
     * generate calculated value base on the related field's value.
     *
     * @return the calculated value
     */
    protected Object generateCalculatedValue() {
        if (_relatedField == null) {
            return new Date();                          // if no related field find, return Now
        }

        Object relatedDate = _relatedField.getValue();
        if (!(relatedDate instanceof Date)) {
            throw new IllegalArgumentException("The related field's value isnot Date.");
        }
        long datetime = ((Date) relatedDate).getTime();

        int step = 0;
        _iStep = _iStep + _iPercentageValue;
        if (_iStep >= _iPercentageBase) {
            _iStep = 0;
            step = _iPercentageValue;
        }

        datetime += step * _iIsLater;

        _value = new Date(datetime);

        return _value;
    }

    /**
     * get enum value from options.
     *
     * @return the option value
     */
    protected Object generateEnumValue() {

        String option = getRandomOption();

        try {
            _value = _dateFormat.parse(option);
        } catch (ParseException e) {
            throw new IllegalArgumentException("cannot format this date option: " + option, e);
        }

        return _value;
    }

    /**
     * get random value.
     *
     * @return the random value
     */
    protected Object generateRandomValue() {

        long datetime = getRandomDateTime();

        _value = new Date(datetime);

        return _value;
    }

    /**
     * generate a random datetime.
     *
     * @return random datetime
     */
    private long getRandomDateTime() {
        long c = System.currentTimeMillis();
        double ran = Math.random();
        double fu1 = Math.random() * 10;
        if (fu1 > 4) {
            fu1 = -1;
        } else {
            fu1 = 1;
        }
        double fu2 = Math.random() * 10;
        if (fu2 > 4) {
            fu2 = 1;
        } else {
            fu2 = -1;
        }

        long offset = (long) (getSeed() * fu1 * 6 * 60 * 60 * 1000);
        long offset2 = (long) (ran * fu2 * 6 * 60 * 60 * 1000);

        long ret = c + offset + offset2;
        ret += (long) _iDateOffset * 24 * 60 * 60 * 1000;

        long max = getDayEndTime();
        if (_bIsDateLessThenNow) {
            max = System.currentTimeMillis();
            max += (long) _iDateOffset * 24 * 60 * 60 * 1000;
        }

        if (ret > max) {
            long len = ret - max;
            long x = len / (60 * 60 * 1000);
            if (len % (60 * 60 * 1000) != 0) {
                x += 1;
            }
            ret -= x * 60 * 60 * 1000;
        }

        return ret;
    }

    /**
     * get end time of one day.
     *
     * @return the end time of one day
     */
    private long getDayEndTime() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);

        long ret = c.getTime().getTime();
        ret += (long) _iDateOffset * 24 * 60 * 60 * 1000;

        return ret;
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

        String ret = null;
        Date date = (Date) _value;

        switch (_iFormatMode) {
            case FORMAT_MODE_SECOND:
            {
                long ldate = date.getTime();
                ret = String.valueOf(ldate / 1000);
                break;
            }
            case FORMAT_MODE_MILLIS:
            {
                long ldate = date.getTime();
                ret = String.valueOf(ldate);
                break;
            }
            case FORMAT_MODE_STRING:
            default:
                ret = _dateFormat.format(date);
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

    /**
     * dump value to bytes array.
     *
     * @return bytes array
     */
    public byte[] getBytes() {
        Date date = (Date) _value;
        long time = date.getTime();

        byte[] ret = ByteBuffer.allocate(8).putLong(time).array();

        return ret;
    }

    /**
     * get date before one day base on now.
     *
     * @return the date in yesterday
     */
    public static Date getDateBefore1Day() {
        long now = System.currentTimeMillis();
        return new Date(now - 24 * 60 * 60 * 1000);
    }
} // END: DateFieldFaker
///:~
