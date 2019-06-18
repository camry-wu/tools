/*
 * -----------------------------------------------------------
 * file name  : AbstractFakerContext.java
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

/**
 * to-do context of Faker.
 *
 * @author $Author$
 * @version $Revision$ $Date$
 */
public abstract class AbstractFakerContext implements IFakerContext {

    /**
     * is debug mode.
     */
    private boolean _bDebug = false;

    /**
     * file faker.
     */
    private IFileFaker _fileFaker;

    /**
     * row faker.
     */
    private IRowFaker _rowFaker;

    /**
     * field faker map.
     */
    private Map<String, IFieldFaker> _fieldFakerMap;

    // getter and setter
    public void setFileFaker(final IFileFaker fileFaker) { _fileFaker = fileFaker; }
    public IFileFaker getFileFaker() { return _fileFaker; }

    public void setRowFaker(final IRowFaker rowFaker) { _rowFaker = rowFaker; }
    public IRowFaker getRowFaker() { return _rowFaker; }

    public Map<String, IFieldFaker> getFieldFakerMap() { return _fieldFakerMap; }

    public boolean isDebug() { return _bDebug; }

    /**
     * constructor.
     * @param debug     if true is debug mode
     */
    public AbstractFakerContext(final boolean debug) {
        super();
        _bDebug = debug;
        _fieldFakerMap = new HashMap<String, IFieldFaker>();
    } // END: AbstractFakerContext

    /**
     * put IFieldFaker into map.
     *
     * @param name
     * @param fieldFaker
     */
    public void addFieldFaker(final String name, final IFieldFaker fieldFaker) {
        _fieldFakerMap.put(name, fieldFaker);
    }

    /**
     * get field faker by name.
     *
     * @param name field name
     * @return IFieldFaker
     */
    public IFieldFaker getFieldFakerByName(final String name) {
        return _fieldFakerMap.get(name);
    }
} // END: AbstractFakerContext
///:~
