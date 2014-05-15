/*
 * -----------------------------------------------------------
 * file name  : AbstractFieldFaker.java
 * creator    : camry(camry.wu@gmail.com)
 * created    : Mon 04 Feb 2013 01:08:43 PM CST
 * copyright  : (c) 2013 Vitular Inc. All Rights Reserved.
 *
 * modifications:
 *
 * -----------------------------------------------------------
 */
package net.vitular.tools.faker4j;

import java.net.URLDecoder;
import java.io.UnsupportedEncodingException;

import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Enumeration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * to-do abstract Faker of Test Data Field.
 * priority:
 *      Array       has Array Saperator
 *      Enum        has Options
 *      Random
 *
 * @author $Author$
 * @version $Revision$ $Date$
 */
public abstract class AbstractFieldFaker implements IFieldFaker {

    /**
     * options separator.
     */
    public static final String OPTION_SEPARATOR = ",";

    /**
     * logger.
     */
    protected Log _logger = LogFactory.getLog(getClass());

    /**
     * random seed.
     */
    private double _dSeed;

    /**
     * field name.
     */
    private String _sName;

    /**
     * faker context.
     */
    protected IFakerContext _fakerContext;

    /**
     * faker expression.
     */
    protected FakerExpression _fakerExpression;

    /**
     * related field faker.
     */
    protected IFieldFaker _relatedField;

    /**
     * store array string value.
     */
    private String _sArrayValue;

    /**
     * get faker expression.
     *
     * @return FakerExpression
     */
    public FakerExpression getFakerExpression() { return _fakerExpression; }

    /**
     * test is array.
     */
    private boolean _bIsArray = false;

    // getter and setter
    public String getName() { return _sName; }
    //protected void setName(final String name) { _sName = name; }

    protected double getSeed() { return _dSeed; }
    //protected void setSeed(final double seed) { _dSeed = seed; }

    public boolean isArray() { return _bIsArray; }

    protected String getArrayValue() { return _sArrayValue; }

    /**
     * constructor.
     *
     * @param name          field name
     * @param fakerContext  faker context
     */
    public AbstractFieldFaker(final String name, final IFakerContext fakerContext) {
        _dSeed = Math.random();
        _sName = name;
        _fakerContext = fakerContext;

        _fakerContext.addFieldFaker(name, this);
    }

    /**
     * setup field faker.
     * @param fakerExpression faker expression
     */
    public void initial(final FakerExpression fakerExpression) {
        _fakerExpression = fakerExpression;

        String sArraySeparator = _fakerExpression.getArraySep();
        if (sArraySeparator != null && !"".equals(sArraySeparator)) {
            _bIsArray = true;
        }

        String referFieldName = _fakerExpression.getReferenceFieldName();
        if (_sName.equals(referFieldName)) {
            _relatedField = this;
        } else {
            _relatedField = _fakerContext.getFieldFakerByName(referFieldName);
        }
    }

    /**
     * generate field value.
     *
     * @return the new field value
     */
    public Object next() {

        // value is a array
        // (format to string, seprate by the indicated char)
        if (isArray()) {
            return generateArrayValue();
        }

        return generateOneValue();
    }

    /**
     * generate one value.
     * if is array, need call this function sometimes
     *
     * @return one value
     */
    private Object generateOneValue() {

        Mode mode = _fakerExpression.getMode();

        // mode is calculated
        if (mode == Mode.Calculated) {
            return generateCalculatedValue();
        }

        // mode is options
        if (mode == Mode.OptionList || mode == Mode.OptionFile || mode == Mode.OptionMap) {
            return generateEnumValue();
        }

        // mode is random
        return generateRandomValue();
    }

    /**
     * get random option from options.
     *
     * @param seed  random seed
     * @return one option
     */
    protected String getRandomOption() {

        double ran = Math.random() * 23;

        Mode mode = _fakerExpression.getMode();

        String[] options = null;

        if (mode == Mode.OptionList || mode == Mode.OptionFile) {
            options = _fakerExpression.getOptions();
        } else if (mode == Mode.OptionMap) {
            String relatedValue = _relatedField.formatValue();
            options = _fakerExpression.getRelatedOptions(relatedValue);
        } else {
            throw new IllegalArgumentException("faker expression is not options mode.");
        }

        assert (options != null);
        int len = options.length;
        int idx = (int) (_dSeed * 200 + ran) % len;
        return options[idx];
    }

    /**
     * get array value.
     * (format to string, seprate by the indicated char)
     *
     * array value is not a regular value, donot set into concrete sub faker class.
     *
     * @return the array value
     */
    private String generateArrayValue() {

        int array_size = (int) (Math.random() * 10) % 5 + 1;

        Set<String> set = new HashSet<String> ();

        for (int i = 0; i < array_size; i++) {
            Object one = generateOneValue();
            String item = formatValue();
            set.add(item);
        }

        StringBuffer sb = new StringBuffer();
        array_size = set.size();
        int i = 0;
        for (Iterator<String> x = set.iterator(); x.hasNext();) {
            String item = x.next();
            sb.append(item);

            if (i < array_size - 1) {
                sb.append(_fakerExpression.getArraySep());
            }
            i++;
        }

        _sArrayValue = sb.toString();

        return _sArrayValue;
    }

    /**
     * generate calculated value.
     *
     * @return the calculated value
     */
    protected abstract Object generateCalculatedValue();

    /**
     * get enum value from options.
     *
     * @return the option value
     */
    protected abstract Object generateEnumValue();

    /**
     * get random value.
     *
     * @return the random value
     */
    protected abstract Object generateRandomValue();
} // END: AbstractFieldFaker
///:~
