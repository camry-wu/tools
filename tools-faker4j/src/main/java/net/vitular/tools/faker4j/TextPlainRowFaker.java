/*
 * -----------------------------------------------------------
 * file name  : TextPlainRowFaker.java
 * creator    : camry(camry.wu@gmail.com)
 * created    : Mon 04 Feb 2013 01:08:43 PM CST
 * copyright  : (c) 2013 Vitular Inc. All Rights Reserved.
 *
 * modifications:
 *
 * -----------------------------------------------------------
 */
package net.vitular.tools.faker4j;

import java.util.List;
import java.util.ArrayList;

import java.text.MessageFormat;

/**
 * to-do generate fake row on text plain string.
 *
 * @author $Author$
 * @version $Revision$ $Date$
 */
public class TextPlainRowFaker extends AbstractRowFaker {

    /**
     * record type count.
     */
    private int _iRecordTypeCount = 1;

    /**
     * format string.
     */
    private String[] _asFormat;

    /**
     * message format.
     */
    private MessageFormat[] _aMsgFormat;

    /**
     * constructor.
     *
     * @param fakerContext      faker context
     * @param recordTypeCount   record type count
     */
    public TextPlainRowFaker(final IFakerContext fakerContext, final int recordTypeCount) {
        super(fakerContext);

        _iRecordTypeCount = recordTypeCount;
        initial();
    }

    /**
     * initial row faker.
     */
    private void initial() {
        List<IFieldFaker> fieldFakerList = getFieldFakerList();
        assert (fieldFakerList != null && fieldFakerList.size() > 0);

        _asFormat = new String[_iRecordTypeCount];
        _aMsgFormat = new MessageFormat[_iRecordTypeCount];

        for (int i = 0; i < _iRecordTypeCount; i++) {
            _asFormat[i] = getFakerContext().getProperty(String.format("%s.%d", FakerConsts.RECORD_FORMAT, i));
            _aMsgFormat[i] = new MessageFormat(_asFormat[i]);
        }

    }

    /**
     * generate row.
     *
     * @return the new row instance
     */
    public Object next() {

        List<String> faker_value_list = generateFieldList();
        return _aMsgFormat[0].format(faker_value_list.toArray());
    }

    /**
     * generate row.
     *
     * @return the new row instance
     */
    public Object next(final int recordType) {
        List<String> faker_value_list = generateFieldList();
        return _aMsgFormat[recordType].format(faker_value_list.toArray());
    }

    ///////// private methods ///////////

    /**
     * generate field value list.
     *
     * @return value list
     */
    private List<String> generateFieldList() {
        List<IFieldFaker> fieldFakerList = getFieldFakerList();

        List<String> faker_value_list = new ArrayList<String> (8);
        for (int i = 0, len = fieldFakerList.size(); i < len; i++) {
            IFieldFaker fieldFaker = fieldFakerList.get(i);

            Object value = fieldFaker.next();

            //_logger.debug(fieldFaker.toString());

            // the generated value may be an array(String type)
            String fieldValue = null;

            if (fieldFaker.isArray()) {
                fieldValue = value.toString();
            } else {
                // only format nonArray value
                fieldValue = fieldFaker.formatValue();
            }

            faker_value_list.add(fieldValue);
        }

        return faker_value_list;
    }
} // END: TextPlainRowFaker
///:~
