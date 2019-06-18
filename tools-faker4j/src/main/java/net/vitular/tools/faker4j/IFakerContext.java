/*
 * -----------------------------------------------------------
 * file name  : IFakerContext.java
 * creator    : camry(camry.wu@gmail.com)
 * created    : Sun Mar 17 20:41:56 2013
 * copyright  : (c) 2013 Vitular Inc. All Rights Reserved.
 *
 * modifications:
 *
 * -----------------------------------------------------------
 */
package net.vitular.tools.faker4j;

import java.util.Map;

/**
 * to-do context of Faker.
 *
 * @author $Author$
 * @version $Revision$ $Date$
 */
public interface IFakerContext {

    /**
     * if debug is true, donot write rows to file.
     *
     * @return true or false
     */
    public boolean isDebug();

    /**
     * get file faker.
     *
     * @return IFileFaker
     */
    public IFileFaker getFileFaker();

    /**
     * set file faker.
     *
     * @param fileFaker IFileFaker
     */
    public void setFileFaker(IFileFaker fileFaker);

    /**
     * get row faker.
     *
     * @return IRowFaker
     */
    public IRowFaker getRowFaker();

    /**
     * set row faker.
     *
     * @param rowFaker IRowFaker
     */
    public void setRowFaker(final IRowFaker rowFaker);

    /**
     * get field faker map.
     *
     * @return field faker map
     */
    public Map<String, IFieldFaker> getFieldFakerMap();

    /**
     * add IFieldFaker into map.
     *
     * @param name
     * @param fieldFaker
     */
    public void addFieldFaker(final String name, final IFieldFaker fieldFaker);

    /**
     * get field faker by name.
     *
     * @param name field name
     * @return IFieldFaker
     */
    public IFieldFaker getFieldFakerByName(final String name);

    /**
     * get faker config property base on property key.
     *
     * @param propertyKey   property key
     * @return property
     */
    public String getProperty(final String propertyKey);

    /**
     * get faker config int property base on property key.
     *
     * @param propertyKey   property key
     * @return property int value
     */
    public int getIntProperty(final String propertyKey, final int defaultValue);

    /**
     * get faker config long property base on property key.
     *
     * @param propertyKey   property key
     * @return property long value
     */
    public long getLongProperty(final String propertyKey, final long defaultValue);

    /**
     * get faker config boolean property base on property key.
     *
     * @param propertyKey   property key
     * @return property boolean value
     */
    public boolean getBooleanProperty(final String propertyKey, final boolean defaultValue);

    /**
     * get faker expression of the field.
     *
     * @param fieldName     field name
     * @return faker expression
     */
    public String getFakerFieldExpression(final String fieldName);
} // END: FakerContext
///:~
