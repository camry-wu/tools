/*
 * -----------------------------------------------------------
 * file name  : FakerConsts.java
 * creator    : camry(camry.wu@gmail.com)
 * created    : Sun 17 Mar 2013 01:08:43 PM CST
 * copyright  : (c) 2013 Vitular Inc. All Rights Reserved.
 *
 * modifications:
 *
 * -----------------------------------------------------------
 */
package net.vitular.tools.faker4j;

/**
 * to-do Constants of Faker.
 *
 * @author $Author$
 * @version $Revision$ $Date$
 */
public interface FakerConsts {

    /**
     * date format: number (unit is second).
     */
    public static final String DATE_FORMAT_SECOND = "SECOND";

    /**
     * date format: number (unit is millisecond).
     */
    public static final String DATE_FORMAT_MILLIS = "MILLIS";

    /**
     * global property key.
     */
    public static final String GLOBAL_PROP_KEY_DEBUG            = "global.debug";
    public static final String GLOBAL_PROP_KEY_DATE_OFFSET      = "global.dateOffset";
    public static final String GLOBAL_PROP_KEY_DATELESSTHENNOW  = "global.dateLessThenNow";

    /**
     * file property key.
     */
    public static final String FILE_FAKER                       = "file.faker";
    public static final String FILE_RECORD_COUNT                = "file.record.count";
    public static final String FILE_OUTPUT_PATH                 = "file.output.path";
    public static final String FILE_HEADER                      = "file.header";
    public static final String FILE_FOOTER                      = "file.footer";

    /**
     * record property key.
     */
    public static final String RECORD_FIELDS_ARRAY              = "record.fields.array";
    public static final String RECORD_FORMAT                    = "record.format";

    /**
     * record fields array separator.
     */
    public static final String ARRAY_SEPARATOR = ",";
} // END: FakerConsts
///:~
