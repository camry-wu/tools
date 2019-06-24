/*
 * -----------------------------------------------------------
 * file name  : CompositeFieldFaker.java
 * creator    : camry(camry.wu@gmail.com)
 * created    : Mon 04 Feb 2013 01:08:43 PM CST
 * copyright  : (c) 2013 Vitular Inc. All Rights Reserved.
 *
 * modifications:
 *
 * -----------------------------------------------------------
 */
package net.vitular.tools.faker4j;

import java.text.MessageFormat;
import java.util.List;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * to-do Composite field value Faker of Test Data .
 * each subfield's name is fieldName.index ( index is 1,2,3,4...)
 * concatenates all the field's value one by one
 *
 * if no subfields, return null
 *
 * @author $Author$
 * @version $Revision$ $Date$
 */
public class CompositeFieldFaker extends AbstractFieldFaker {

    /**
     * limit: subfield size.
     */
    private int _iFieldsSize = 0;

    /**
     * list of fields.
     */
    private List<IFieldFaker> _fieldFakerList;

    /**
     * message format.
     */
    private MessageFormat _msgFormat = null;

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
    public CompositeFieldFaker(final String name, final IFakerContext fakerContext) {
        super(name, fakerContext);

        _fieldFakerList = new ArrayList<IFieldFaker> ();
    }

    /**
     * setup field faker.
     * @param fakerExpression faker expression
     */
    public void initial(final FakerExpression fakerExpression) {
        super.initial(fakerExpression);

        String len = fakerExpression.getLimit();

        if (len != null && !"".equals(len)) {
            _iFieldsSize = Integer.parseInt(len);
        }

        for (int i = 0; i < _iFieldsSize; i++) {
            String fieldName = getName() + "." + i;

            IFieldFaker fieldFaker = FakerFactory.createFieldFaker(fieldName, _fakerContext);
            _fieldFakerList.add(fieldFaker);
        }

        String format = fakerExpression.getFormat();
        if (format != null && !format.equals("")) {
            _msgFormat = new MessageFormat(format);
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

        List<String> value_list = new ArrayList<String>();
        for (int i = 0; i < _iFieldsSize; i++) {
            IFieldFaker fieldFaker = _fieldFakerList.get(i);

            Object value = fieldFaker.next();

            // the generated value may be an array(String type)
            String fieldValue = null;

            if (value instanceof String) {
                fieldValue = (String) value;
            } else {
                fieldValue = fieldFaker.formatValue();   // only format nonArray value
            }

            // concatenates field value
            ret.append(fieldValue);
            value_list.add(fieldValue);
        }

        if (_msgFormat != null) {
            _value = _msgFormat.format(value_list.toArray());
        } else {
            _value = ret.toString();
        }

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
} // END: CompositeFieldFaker
///:~
