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
     * format string.
     */
    private String _sFormat;

    /**
     * message format.
     */
    private MessageFormat _msgFormat;

    /**
     * constructor.
     *
     * @param fakerContext  faker context
     */
    public TextPlainRowFaker(final IFakerContext fakerContext) {
        super(fakerContext);

        initial();
    }

    /**
     * initial row faker.
     */
    private void initial() {
        _sFormat = getFakerContext().getProperty(FakerConsts.RECORD_FORMAT);

        _msgFormat = new MessageFormat(_sFormat);
    }

    /**
     * generate row.
     *
     * @return the new row instance
     */
    public Object next() {
        List<IFieldFaker> fieldFakerList = getFieldFakerList();
        assert (fieldFakerList != null && fieldFakerList.size() > 0);

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

        return _msgFormat.format(faker_value_list.toArray());
    }

    ///////// private methods ///////////
} // END: TextPlainRowFaker
///:~
