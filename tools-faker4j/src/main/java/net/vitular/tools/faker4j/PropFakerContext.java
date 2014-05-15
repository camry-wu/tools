/*
 * -----------------------------------------------------------
 * file name  : PropFakerContext.java
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
import java.util.HashMap;
import java.util.Properties;

/**
 * to-do context of Faker, drawing configuration from properties file.
 *
 * @author $Author$
 * @version $Revision$ $Date$
 */
public class PropFakerContext extends AbstractFakerContext {

    /**
     * config properties.
     */
    private Properties _props;

    // getter and setter
    protected void setProperties(final Properties properties) { _props = properties; }
    protected Properties getProperties() { return _props; }

    /**
     * constructor.
     * @param props     faker config
     * @param debug     if true is debug mode
     */
    public PropFakerContext(final Properties props, final boolean debug) {
        super(debug);
        _props = props;
    } // END: PropFakerContext

    /**
     * get faker config property base on property key.
     *
     * @param propertyKey   property key
     * @return property
     */
    public String getProperty(final String propertyKey) {
        return _props.getProperty(propertyKey);
    }

    /**
     * get faker expression of the indicated field.
     *
     * @param fieldName     field name
     * @return faker expression
     */
    public String getFakerFieldExpression(final String fieldName) {
        return _props.getProperty("field." + fieldName);
    }
} // END: PropFakerContext
///:~
