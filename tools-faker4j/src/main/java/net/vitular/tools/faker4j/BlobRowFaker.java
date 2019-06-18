/*
 * -----------------------------------------------------------
 * file name  : BlobRowFaker.java
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

import java.nio.ByteBuffer;

/**
 * to-do generate fake row on text plain string.
 *
 * @author $Author$
 * @version $Revision$ $Date$
 */
public class BlobRowFaker extends AbstractRowFaker {

    /**
     * constructor.
     *
     * @param fakerContext  faker context
     */
    public BlobRowFaker(final IFakerContext fakerContext) {
        super(fakerContext);

        initial();
    }

    /**
     * initial row faker.
     */
    private void initial() {
        //_sFormat = getFakerContext().getProperty(FakerConsts.RECORD_FORMAT);
    }

    /**
     * generate row.
     *
     * @return the new row instance
     */
    public Object next() {
        List<IFieldFaker> fieldFakerList = getFieldFakerList();
        assert (fieldFakerList != null && fieldFakerList.size() > 0);

        int byte_count = 0;
        List<byte[]> faker_value_list = new ArrayList<byte[]> (8);
        for (int i = 0, len = fieldFakerList.size(); i < len; i++) {
            IFieldFaker fieldFaker = fieldFakerList.get(i);

            Object value = fieldFaker.next();

            // the generated value may be an array(String type)
            byte[] fieldValue = null;

            if (fieldFaker.isArray()) {
                fieldValue = value.toString().getBytes();
            } else {
                // only format nonArray value
                fieldValue = fieldFaker.getBytes();
            }

            byte_count += fieldValue.length;
            faker_value_list.add(fieldValue);
        }

        ByteBuffer bb = ByteBuffer.allocate(byte_count);
        for (byte[] data: faker_value_list) {
            bb.put(data);
        }
        bb.rewind();

        return bb;
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
} // END: BlobRowFaker
///:~
