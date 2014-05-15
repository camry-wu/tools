/*
 * -----------------------------------------------------------
 * file name  : FakerExpression.java
 * creator    : camry(camry.wu@gmail.com)
 * created    : Mon 30 Mar 2013 01:08:43 PM CST
 * copyright  : (c) 2013 Vitular Inc. All Rights Reserved.
 *
 * modifications:
 *
 * -----------------------------------------------------------
 */
package net.vitular.tools.faker4j;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.StringTokenizer;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * to-do parse faker expression to get faker config.
 * faker expression:
 *      Expression = Type/mode:[calculated|options|random] | Type/mode:&lt;format&gt;[calculated|options|random]
 *      format = ascii
 *      Type = Boolean | Date | IP | MAC | Number | Sequence | String | Composite
 *      mode = c | ol | of | om | r
 *          c(calculated):      field value is calculated base on the reference field's value
 *          ol(options list):   field value is picked from the options list
 *          of(options file):   field value is picked from the options file
 *          om(options map):    field value is picked from options list base on the reference field's value
 *          r(random):          field value is random
 *
 *          n(null):            null option, this mode can be combined with other modes.
 *
 *      calculated = referenceFieldName[+|-]number
 *      options = [arraySepChar]{item1, item2, item3...}
 *                  | [arraySepChar]{filepath}
 *                  | [arraySepChar]referenceFieldName{key1:{item1, item2...}, key2:{item1...}}
 *      random = [arraySepChar](limit)
 *
 *      []: arraySepChar
 *      {}: item list
 *      (): limit
 *      <>: format
 *
 * @author $Author$
 * @version $Revision$ $Date$
 */
public class FakerExpression {
    public static final String delim = "/:<>+-[]{}(),";

    /**
     * faker expression.
     */
    private String _sFakerExpression;

    /**
     * faker type.
     * Boolean, Date, IP, MAC, Number, Sequence, String, Composite
     */
    private String _sFieldType;

    /**
     * mode.
     *  c   : calculated
     *  ol  : options list
     *  of  : options file
     *  om  : options map
     *  r   : random
     *  -----------------------------
     *  n   : can null, can be combined with other modes
     */
    private Mode _mode;

    /**
     * format.
     */
    private String _sFormat = null;

    /**
     * reference field name.
     */
    private String _sReferenceFieldName = null;

    /**
     * calculated variable.
     * +|- variable
     */
    private String _sCalculatedVar = null;

    /**
     * calculated operation.
     * + | -
     */
    private String _sCalculatedOp = null;

    /**
     * array seperator.
     */
    private String _sArraySep = null;

    /**
     * options string: list, filepath or map.
     */
    private String[] _asOptions = null;

    /**
     * the options map base on the related field's value.
     */
    private Map<String, String[]> _relatedOptionsMap = null;

    /**
     * limit config.
     */
    private String _sLimit = null;

    // getter
    public String getFieldType() { return _sFieldType; }

    public Mode getMode() { return _mode; }

    public String getFormat() { return _sFormat; }

    public String getReferenceFieldName() { return _sReferenceFieldName; }

    public String getCalculatedVar() { return _sCalculatedVar; }

    public String getCalculatedOp() { return _sCalculatedOp; }

    public String getArraySep() { return _sArraySep; }

    public String[] getOptions() { return _asOptions; }
    public void setOptions(final String[] opts) { _asOptions = opts; }

    public String[] getRelatedOptions(final String key) {
        if (_relatedOptionsMap == null) {
            return null;
        }

        return _relatedOptionsMap.get(key);
    }

    public String getLimit() { return _sLimit; }

    /**
     * private constructor.
     * @param fakerExpression faker expression
     */
    private FakerExpression(final String fakerExpression) {
        super();
        _sFakerExpression = fakerExpression;
    }

    /**
     * parse the expression.
     */
    private void parse() {
        int colon_idx = _sFakerExpression.indexOf(":");
        int slash_idx = _sFakerExpression.indexOf("/");

        // no config
        if (colon_idx == -1) {
            if (slash_idx == -1) {
                _sFieldType = _sFakerExpression;
                _mode = Mode.Random;
                return;
            }

            _sFieldType = _sFakerExpression.substring(0, slash_idx).trim();
            _mode = Mode.getInstance(_sFakerExpression.substring(slash_idx + 1).trim());

            return;
        }

        // has config
        assert(slash_idx > 0);
        _sFieldType = _sFakerExpression.substring(0, slash_idx).trim();
        _mode = Mode.getInstance(_sFakerExpression.substring(slash_idx + 1, colon_idx).trim());

        String config = _sFakerExpression.substring(colon_idx + 1).trim();

        // get format string from expression
        config = parseFormatString(config);

        // parse config base on mode
        if (_mode == Mode.Calculated || _mode == Mode.NCalculated) {
            String splitChars = "[+-]";
            String[] tokens = config.split(splitChars);
            if (tokens == null || tokens.length < 2) {
                throw new IllegalArgumentException(String.format("faker expression error: wrong mode(c) [%s]", _sFakerExpression));
            }

            _sReferenceFieldName = tokens[0].trim();
            _sCalculatedVar = tokens[1].trim();

            _sCalculatedOp = "-";
            if (config.indexOf("+") != -1) {
                _sCalculatedOp = "+";
            }

        } else if (_mode == Mode.OptionList || _mode == Mode.NOptionList) {
            // parse array sep char
            config = parseArraySepChar(config);

            String splitChars = "[{,}]";
            String[] tokens = config.split(splitChars);
            if (tokens == null || tokens.length < 1) {
                throw new IllegalArgumentException(String.format("faker expression error: wrong mode(ol) [%s]", _sFakerExpression));
            }

            List<String> oplist = new ArrayList<String> (8);
            for (int i = 0, len = tokens.length; i < len; i++) {
                String tmp = tokens[i].trim();
                if (tmp.length() > 0) {
                    oplist.add(tmp);
                }
            }

            _asOptions = oplist.toArray(new String[0]);

        } else if (_mode == Mode.OptionFile || _mode == Mode.NOptionFile) {
            // parse array sep char
            config = parseArraySepChar(config);

            String splitChars = "[{}]";
            String[] tokens = config.split(splitChars);
            if (tokens == null || tokens.length < 1) {
                throw new IllegalArgumentException(String.format("faker expression error: wrong mode(of) [%s]", _sFakerExpression));
            }

            String filepath = null;
            for (int i = 0; i < tokens.length; i++) {
                filepath = tokens[i];
                if (filepath.length() > 0) {
                    break;
                }
            }

            // read options from the file
            List<String> list = new ArrayList<String> (8);
            BufferedReader fr = null;
            try {
                fr = new BufferedReader(new FileReader(new File(filepath)));
                String line = null;
                while ((line = fr.readLine()) != null) {
                    list.add(line.trim());
                }
            } catch (IOException ioe) {
                throw new IllegalArgumentException(String.format("faker expression error: wrong mode(of) [%s]", _sFakerExpression), ioe);
            } finally {
                if (fr != null) {
                    try {
                        fr.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            _asOptions = list.toArray(new String[0]);

        } else if (_mode == Mode.OptionMap || _mode == Mode.NOptionMap) {
            // parse array sep char
            config = parseArraySepChar(config);

            String splitChars = "[{,}]";
            String[] tokens = config.split(splitChars);
            if (tokens == null || tokens.length < 3) {
                throw new IllegalArgumentException(String.format("faker expression error: wrong mode(om) [%s]", _sFakerExpression));
            }

            _relatedOptionsMap = new HashMap<String, String[]> ();

            _sReferenceFieldName = tokens[0].trim();
            String key = null;
            List<String> oplist = new ArrayList<String> (8);

            for (int i = 0; i < tokens.length; i++) {

                String tmp = tokens[i].trim();
                int x = tmp.indexOf(":");

                if (x != -1) {           // this is a key
                    if (key != null) {                  // is not the first key, store options
                        String[] ops = oplist.toArray(new String[0]);
                        _relatedOptionsMap.put(key, ops);
                    }

                    key = tmp.substring(0, x).trim();
                    oplist.clear();
                } else {                // this is a options
                    if (tmp.length() > 0)
                        oplist.add(tmp);
                }
            }

            // put the last key-options
            if (oplist.size() > 0 && key != null) {
                String[] ops = oplist.toArray(new String[0]);
                _relatedOptionsMap.put(key, ops);
            }

        } else if (_mode == Mode.Random || _mode == Mode.NRandom) {
            // parse array sep char
            config = parseArraySepChar(config);

            String splitChars = "[()]";
            String[] tokens = config.split(splitChars);
            if (tokens == null || tokens.length < 1) {
                throw new IllegalArgumentException(String.format("faker expression error: wrong mode(r) [%s]", _sFakerExpression));
            }

            for (int i = 0; i < tokens.length; i++) {
                _sLimit = tokens[i];
                if (_sLimit.length() > 0) {
                    break;
                }
            }

        } else {
            throw new IllegalArgumentException("unsupport mode of expression: " + _sFakerExpression);
        }
    }

    /**
     * get format string from the config expression.
     *
     * @param expression    config expression
     * @return the rest expression string
     */
    private String parseFormatString(final String expression) {
        String config = expression;

        int lt_idx = expression.indexOf("<");
        if (lt_idx != 0) {
            lt_idx = -1;
        }

        int gt_idx = -1;
        if (lt_idx == 0) {
            gt_idx = expression.indexOf(">");

            if (gt_idx == -1) {
                throw new IllegalArgumentException("faker expression error: unmach <format string>.");
            }

            _sFormat = expression.substring(lt_idx + 1, gt_idx).trim();

            config = expression.substring(gt_idx + 1).trim();
        }

        return config;
    }

    /**
     * get array separate char from the config expression.
     *
     * @param expression    config expression
     * @return the rest expression string
     */
    private String parseArraySepChar(final String expression) {
        String config = expression;

        int lt_idx = expression.indexOf("[");
        if (lt_idx != 0) {
            lt_idx = -1;
        }

        int gt_idx = -1;
        if (lt_idx == 0) {
            gt_idx = expression.indexOf("]");

            if (gt_idx == -1) {
                throw new IllegalArgumentException("faker expression error: unmach [array separate char].");
            }

            _sArraySep = expression.substring(lt_idx + 1, gt_idx).trim();

            config = expression.substring(gt_idx + 1).trim();
        }

        return config;
    }

    /**
     * create a instance of FakerExpression and parse expression to get faker config.
     *
     * @param fakerExpression faker expression
     * @return FakerExpression instance
     */
    public static FakerExpression getInstance(final String fakerExpression) {
        FakerExpression expression = new FakerExpression(fakerExpression);
        expression.parse();
        return expression;
    }

    /**
     * to string.
     *
     * @return faker expression.
     */
    public String toString() {
        return _sFakerExpression;
    }
} // END: FakerExpression

/**
 * Mode of Faker Expression.
 *
 * @author wuhao(wuhao@fortinet.com)
 * @version $Revision$ $Date$
 */
class Mode {
    // int mode = 11111111 11111111 11111111 11111111;
    // first 8 bits: special mode flag
    //      1: can null or not
    // second 8 bits: calculated mode flag
    // third 8 bits: options mode flag
    //      1: options list
    //      2: options file
    //      3: options map
    // forth 8 bits: random mode flag

    /**
     * mode name.
     */
    private final String _sName;

    /**
     * indicated the field value can by null or not.
     * default is false.
     */
    private final boolean _bCanNull;

    /**
     * constructor.
     *
     * @param name      mode name
     * @param canNull   can null or not
     */
    private Mode(final String name, final boolean canNull) {
        _sName = name;
        _bCanNull = canNull;
    }

    /**
     * return mode name.
     *
     * @return mode name
     */
    public String toString() {
        return _sName;
    }

    /**
     * mode enum instance.
     */
    public static final Mode Calculated = new Mode("c", false);
    public static final Mode OptionList = new Mode("ol", false);
    public static final Mode OptionFile = new Mode("of", false);
    public static final Mode OptionMap = new Mode("om", false);
    public static final Mode Random = new Mode("r", false);

    public static final Mode NCalculated = new Mode("c", true);
    public static final Mode NOptionList = new Mode("ol", true);
    public static final Mode NOptionFile = new Mode("of", true);
    public static final Mode NOptionMap = new Mode("om", true);
    public static final Mode NRandom = new Mode("r", true);

    /**
     * get the enum instance base on name.
     *
     * @param name mode name
     * @return one Mode enum instance
     */
    public static Mode getInstance(final String name) {
        String tmpname = name;
        boolean cannull = false;
        int n_index = tmpname.indexOf('n');
        if (n_index != -1) {
            cannull = true;
            tmpname = name.replaceAll("n", "");
        }

        if ("c".equalsIgnoreCase(tmpname)) {
            if (cannull)
                return NCalculated;
            else
                return Calculated;
        } else if ("ol".equalsIgnoreCase(tmpname)) {
            if (cannull)
                return NOptionList;
            else
                return OptionList;
        } else if ("of".equalsIgnoreCase(tmpname)) {
            if (cannull)
                return NOptionFile;
            else
                return OptionFile;
        } else if ("om".equalsIgnoreCase(tmpname)) {
            if (cannull)
                return NOptionMap;
            else
                return OptionMap;
        } else {
            if (cannull)
                return NRandom;
            else
                return Random;
        }
    }
} // END: Mode
///:~
