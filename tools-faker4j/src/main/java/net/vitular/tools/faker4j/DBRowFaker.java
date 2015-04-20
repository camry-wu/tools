/*
 * -----------------------------------------------------------
 * file name  : DBRowFaker.java
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

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;

/**
 * to-do Faker One Test Row and save to DB.
 *
 * @author $Author$
 * @version $Revision$ $Date$
 */
public class DBRowFaker extends AbstractRowFaker {

    /**
     * class of row.
     */
    private Class _rowClass;

    /**
     * constructor.
     *
     * @param fakerContext  faker context
     */
    public DBRowFaker(final IFakerContext fakerContext) {
        super(fakerContext);

        initial();
    }

    /**
     * initial row faker.
     */
    private void initial() {
        IFakerContext ctx = getFakerContext();

        String sRowClassName = ctx.getProperty("row.className");

        try {
            _rowClass = Class.forName(sRowClassName);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("cannot find class: " + sRowClassName, e);
        }
    }

    /**
     * generate row.
     *
     * @return the new row instance
     */
    public Object next() {
        List<IFieldFaker> fieldFakerList = getFieldFakerList();
        assert (fieldFakerList != null && fieldFakerList.size() > 0);

        Object row = null;
        try {
            row = _rowClass.newInstance();
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("cannot create class: " + _rowClass.getName(), e);
        } catch (InstantiationException e) {
            throw new IllegalArgumentException("cannot create class: " + _rowClass.getName(), e);
        }

        if (_logger.isDebugEnabled()) {
            _logger.debug(String.format("------ Generate: %s ----------", _rowClass.getName()));
        }

        for (IFieldFaker fieldFaker: fieldFakerList) {
            Object obj = fieldFaker.next();
            _logger.debug(fieldFaker.toString());

            String fieldName = fieldFaker.getName();

            try {
                Class valueClz = PropertyUtils.getPropertyType(row, fieldName);

                if (valueClz == null) {
                    String err = String.format("cannot find prop[%s] from object[%s]: ",
                            fieldName, _rowClass.getName());
                    throw new IllegalArgumentException(err);
                }

                Object convertedValue = ConvertUtils.convert(obj, valueClz);

                PropertyUtils.setProperty(row, fieldName, convertedValue);
            } catch (IllegalAccessException e) {
                String err = String.format("cannot set prop[%s] to object[%s]: ",
                        fieldName, _rowClass.getName());
                throw new IllegalArgumentException(err, e);
            } catch (InvocationTargetException e) {
                String err = String.format("cannot set prop[%s] to object[%s]: ",
                        fieldName, _rowClass.getName());
                throw new IllegalArgumentException(err, e);
            } catch (NoSuchMethodException e) {
                String err = String.format("cannot set prop[%s] to object[%s]: ",
                        fieldName, _rowClass.getName());
                throw new IllegalArgumentException(err, e);
            }
        }

        _logger.debug("generated row: " + row);

        return row;
    }

    /**
     * generate row.
     *
     * @return the new row instance
     */
    public Object next(final int recordType) {
        return next();
    }

    ///////// private methods ///////////
} // END: DBRowFaker
///:~
