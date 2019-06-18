/*
 * -----------------------------------------------------------
 * file name  : AbstractRowFaker.java
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * to-do abstract Faker of Test Data Row.
 *
 * @author $Author$
 * @version $Revision$ $Date$
 */
public abstract class AbstractRowFaker implements IRowFaker {

    /**
     * logger.
     */
    protected Log _logger = LogFactory.getLog(getClass());

    /**
     * faker context.
     */
    private IFakerContext _fakerContext;

    /**
     * list of fields' name.
     */
    private List<String> _fieldNameList;

    /**
     * list of fields.
     */
    private List<IFieldFaker> _fieldFakerList;

    // getter and setter
    //protected void setFakerContext(final IFakerContext context) { _fakerContext = context; }
    protected IFakerContext getFakerContext() { return _fakerContext; }

    //protected void setFieldNameList(final List<String> fieldNameList) { _fieldNameList = fieldNameList; }
    protected List<String> getFieldNameList() { return _fieldNameList; }

    //protected void setFieldFakerList(final List<IFieldFaker> fieldFakerList) { _fieldFakerList = fieldFakerList; }
    protected List<IFieldFaker> getFieldFakerList() { return _fieldFakerList; }

    /**
     * constructor.
     *
     * @param fakerContext  faker context
     */
    public AbstractRowFaker(final IFakerContext fakerContext) {
        _fakerContext = fakerContext;

        _fakerContext.setRowFaker(this);

        _fieldNameList = new ArrayList<String> ();
        _fieldFakerList = new ArrayList<IFieldFaker> ();

        // initial all of the fields by name
        String sFieldsArray = _fakerContext.getProperty(FakerConsts.RECORD_FIELDS_ARRAY);
        assert (sFieldsArray != null && !"".equals(sFieldsArray));

        String[] field_array = sFieldsArray.split(FakerConsts.ARRAY_SEPARATOR);
        for (String s: field_array) {
            String fieldName = s.trim();
            _fieldNameList.add(fieldName);

            IFieldFaker fieldFaker = FakerFactory.createFieldFaker(fieldName, fakerContext);

            _fieldFakerList.add(fieldFaker);
        }
    }
} // END: AbstractRowFaker
///:~
