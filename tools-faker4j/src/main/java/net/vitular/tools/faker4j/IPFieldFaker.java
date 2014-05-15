/*
 * -----------------------------------------------------------
 * file name  : IPFieldFaker.java
 * creator    : camry(camry.wu@gmail.com)
 * created    : Mon 04 Feb 2013 01:08:43 PM CST
 * copyright  : (c) 2013 Vitular Inc. All Rights Reserved.
 *
 * modifications:
 *
 * -----------------------------------------------------------
 */
package net.vitular.tools.faker4j;

import java.nio.ByteBuffer;

import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * to-do IP Value Faker of Test Data .
 *
 * @author $Author$
 * @version $Revision$ $Date$
 */
public class IPFieldFaker extends AbstractFieldFaker {

    /**
     * the max ip value.
     */
    public static final long IPMAX = 0X00000000FFFFFFFFL;

    /**
     * format: string(default) or number.
     */
    public static final String FORMAT_STRING            = "string";
    public static final String FORMAT_LONG              = "long";
    public static final String FORMAT_REVERSE_LONG      = "reverseLong";
    public static final String FORMAT_INTEGER           = "int";
    public static final String FORMAT_REVERSE_INTEGER   = "reverseInt";

    /**
     * limit: ip mask.
     */
    private String _sMask;

    /**
     * the min ip under the mask.
     */
    private long _lMaskMinIP;

    /**
     * the ip count under the mask.
     */
    private long _lMaskIPCount;

    /**
     * format.
     */
    private String _sFormat;

    /**
     * if format is reverseNumber, reverse the low and high bits.
     */
    private boolean _bReverse = false;;

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
    public IPFieldFaker(final String name, final IFakerContext fakerContext) {
        super(name, fakerContext);
    }

    /**
     * setup field faker.
     * @param fakerExpression faker expression
     */
    public void initial(final FakerExpression fakerExpression) {
        super.initial(fakerExpression);

        String smask = fakerExpression.getLimit();

        if (smask != null && !"".equals(smask)) {
            _sMask = smask;

            int idx = _sMask.indexOf("/");
            String sbase = _sMask.substring(0, idx);
            String smasksit = _sMask.substring(idx + 1);
            int masksit = Integer.parseInt(smasksit);
            masksit = 32 - masksit;

            assert(masksit < 32 && masksit > 0);

            _lMaskMinIP = ip2long(sbase);

            long max = 0XFFFFFFFFFFFFFFFFL;
            long mask = max << masksit;
            mask = mask & IPMAX;

            _lMaskMinIP = _lMaskMinIP & mask;

            long lmaskmax = 1;
            for (int i = 0; i < masksit - 1; i++) {
                lmaskmax= (lmaskmax << 1) + 1;
            }

            long lMaskMaxIP = _lMaskMinIP | lmaskmax;
            _lMaskIPCount = lMaskMaxIP - _lMaskMinIP;
        }

        _sFormat = fakerExpression.getFormat();

        if (FORMAT_REVERSE_LONG.equals(_sFormat) || FORMAT_REVERSE_INTEGER.equals(_sFormat)) {        // Reverse Number (long)
            _bReverse = true;
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

        // random IP format is X.X.X.X
        _value = new Long(ip2long(option));

        return _value;
    }

    /**
     * get random value.
     *
     * @return the random value
     */
    protected Object generateRandomValue() {
        if (_sMask != null && !"".equals(_sMask)) {
            _value = new Long(generateIPUnderMask());

            return _value;
        }

        _value = new Long(generateRandomIPv4());
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

        String ret = null;

        if (FORMAT_LONG.equals(_sFormat)) {                         // Number (long)
            ret = String.valueOf(_value);
        } else if (FORMAT_REVERSE_LONG.equals(_sFormat)) {          // Reverse Number (long)
            ret = String.valueOf(reverse(((Long) _value).longValue()));
        } else if (FORMAT_INTEGER.equals(_sFormat)) {               // Number (int)
            int value = ((Long) _value).intValue();
            ret = String.valueOf(value);
        } else if (FORMAT_REVERSE_INTEGER.equals(_sFormat)) {       // Reverse Number (int)
            long reverse = reverse(((Long) _value).longValue());
            int value = (int) reverse;
            ret = String.valueOf(value);
        } else {                                                    // String (default)
            ret = long2ip(((Long) _value).longValue());
        }

        return ret;
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

        return getName() + ":\t" + long2ip(((Long) _value).longValue());
    }

    /**
     * dump value to bytes array.
     *
     * @return bytes array
     */
    public byte[] getBytes() {
        long lip = ((Long) _value).longValue();
        int iip = (int) lip;

        byte[] ret = ByteBuffer.allocate(4).putInt(iip).array();

        return ret;
    }

    ////// private methods ////////

    /**
     * generate random ip v4.
     *
     * @return random string ip
     */
    private long generateRandomIPv4() {
        long ip = (long) (Math.random() * IPMAX);
        return ip;
    }

    /**
     * generate IP unser mask.
     *
     * @return IP string
     */
    private long generateIPUnderMask() {

        long ip = (long) (Math.random() * IPMAX);
        ip = ip % _lMaskIPCount;
        if (ip == 0L) {
            ip = 1L;
        }
        ip += _lMaskMinIP;

        return ip;
    }

    /**
     * convert long ip to string ip.
     *
     * @param ip long ip
     * @return string ip
     */
    private static String long2ip(final long ip) {
        StringBuffer sb = new StringBuffer();

        long h1 = ((0X00000000FF000000L & ip) >> 24);
        long h2 = ((0X0000000000FF0000L & ip) >> 16);
        long h3 = ((0X000000000000FF00L & ip) >> 8);
        long h4 = (0X00000000000000FFL & ip);

        sb.append(h1).append('.').append(h2).append('.').append(h3).append('.').append(h4);

        return sb.toString();
    }

    /**
     * convert ip to long.
     *
     * @param ip string ip
     * @return long ip
     */
    private static long ip2long(final String ip) {
        StringTokenizer st = new StringTokenizer(ip, ".");

        long i;
        long j = 0;
        long ret = 0;

        while (st.hasMoreTokens()) {
            j++;
            String tmp = st.nextToken();
            i = Long.parseLong(tmp);

            if (j < 4l) {
                ret += i << (4l - j) * 8l;
            } else {
                ret += i;
            }
        }

        //String str = Long.toBinaryString(ret);

        return ret;
    }

    /**
     * reverse int ip from .
     *
     * the original ip is low-high mode:
     * FF FF FF FF
     * 4  3  2  1
     *
     * FortiSIEM need reverse it to high-low mode:
     * FF FF FF FF
     * 1  2  3  4
     *
     * @param ip    int ip
     * @return event type
     */
    private long reverse(final long ip) {
        if (ip == 0) {
            return 0;
        }

        long site1 = ip & 0xFF;
        long site2 = (ip >>> 8) & 0xFF;
        long site3 = (ip >>> 16) & 0xFF;
        long site4 = (ip >>> 24) & 0xFF;

        long ret = site1 * 16777216 + site2 * 65536 + site3 * 256 + site4;
        return ret;
    }

    /**
     * test.
     *
     * @param args arguments
     */
    public static void main(final String[] args) {
        int masksit = 8;
        if (args.length > 0) {
            masksit = 32 - Integer.parseInt(args[0]);
        }

        long max = 0XFFFFFFFFFFFFFFFFL;
        long mask = max << masksit;
        mask = mask & IPMAX;

        long lbase = ip2long("172.22.13.250");
        lbase = lbase & mask;

        long lmaskmax = 1;
        for (int i = 0; i < masksit - 1; i++) {
            lmaskmax= (lmaskmax << 1) + 1;
        }

        long lbasemax = lbase | lmaskmax;

        long ip = (long) (Math.random() * IPMAX);
        ip = ip % (lbasemax - lbase);
        if (ip == 0L) {
            ip = 1L;
        }
        ip += lbase;

        System.out.println(Long.toBinaryString(mask));
        System.out.println(Long.toBinaryString(lmaskmax));
        System.out.println(Long.toBinaryString(lbase));
        System.out.println(Long.toBinaryString(lbasemax));
        System.out.println(Long.toBinaryString(ip));
        System.out.println(lbase);
        System.out.println(lbasemax);
        System.out.println(long2ip(ip));
    }
} // END: IPFieldFaker
///:~
