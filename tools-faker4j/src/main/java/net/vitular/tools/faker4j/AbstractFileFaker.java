/*
 * -----------------------------------------------------------
 * file name  : AbstractFileFaker.java
 * creator    : camry(camry.wu@gmail.com)
 * created    : Mon 04 Feb 2013 01:08:43 PM CST
 * copyright  : (c) 2013 Vitular Inc. All Rights Reserved.
 *
 * modifications:
 *
 * -----------------------------------------------------------
 */
package net.vitular.tools.faker4j;

import java.util.Date;

import java.text.SimpleDateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * to-do abstract Faker of Test Data File.
 *
 * @author $Author$
 * @version $Revision$ $Date$
 */
public abstract class AbstractFileFaker implements IFileFaker {

    /**
     * logger.
     */
    protected Log _logger = LogFactory.getLog(getClass());

    /**
     * row faker.
     */
    private IRowFaker _rowFaker;

    /**
     * faker context.
     */
    private IFakerContext _fakerContext;

    /**
     * row size.
     */
    private int _iRowSize;

    // getter and setter
    protected void setRowFaker(final IRowFaker rowFaker) { _rowFaker = rowFaker; }
    protected IRowFaker getRowFaker() { return _rowFaker; }

    public void setRowSize(final int rowSize) { _iRowSize = rowSize; }
    public int getRowSize() { return _iRowSize; }

    /**
     * get faker context.
     */
    public IFakerContext getFakerContext() { return _fakerContext; }

    /**
     * constructor.
     *
     * @param rowSize       row size, get from command argument
     * @param fakerContext  faker context
     */
    public AbstractFileFaker(final int rowSize, final IFakerContext fakerContext) {

        _fakerContext = fakerContext;

        _fakerContext.setFileFaker(this);

        if (rowSize > 0) {
            _iRowSize = rowSize;
        } else {
            // get row size
            String sRowSize = _fakerContext.getProperty(FakerConsts.FILE_RECORD_COUNT);
            _iRowSize = Integer.parseInt(sRowSize);
        }
    }

    /**
     * generate file.
     */
    public void generateFile() {

        int percent = (int) _iRowSize / 100;
        if (percent <= 0) {
            percent = 1;
        }

        int j = 0;
        for (int i = 0; i < _iRowSize; i++) {
            Object row = _rowFaker.next();

            if (((int) (i + 1) % percent) == 0) {
                j++;
                if (j % 10 == 0) {
                    System.out.print(j / 10);
                } else {
                    System.out.print("=");
                }
            }

            if (!_fakerContext.isDebug()) {
                writeRow(row);
            }
        }
        System.out.println();
    }

    /**
     * make output file path.
     *
     * dest url maybe include {date format}.
     *
     * @param sDestURL the config dest url
     * @return output file path
     */
    protected String makeOutputFilePath(final String sDestURL) {
        assert (sDestURL != null && !"".equals(sDestURL));

        // donot find '{', return destURL
        int begin = sDestURL.indexOf("{");
        if (begin == -1) {
            return sDestURL;
        }

        StringBuffer ret = new StringBuffer(sDestURL.substring(0, begin));

        int end = sDestURL.indexOf("}");

        String sFormat = sDestURL.substring(begin + 1, end);

        long ldate = getFileGeneratedDate();

        if (FakerConsts.DATE_FORMAT_SECOND.equalsIgnoreCase(sFormat)) {
            ret.append(ldate / 1000);
        } else if (FakerConsts.DATE_FORMAT_MILLIS.equalsIgnoreCase(sFormat)) {
            ret.append(ldate);
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat(sFormat);
            ret.append(sdf.format(new Date(ldate)));
        }

        ret.append(sDestURL.substring(end + 1));

        return ret.toString();
    }

    /**
     * get the date the file generated.
     *
     * @param props     config properties
     * @return datetime long value
     */
    protected long getFileGeneratedDate() {
        long dateoff = 0l;
        String sOffset = _fakerContext.getProperty(FakerConsts.GLOBAL_PROP_KEY_DATE_OFFSET);
        if (sOffset != null && !"".equals(sOffset)) {
            dateoff = Long.parseLong(sOffset);
        }

        long lrecvtime = (new Date()).getTime();
        lrecvtime += dateoff * 24 * 60 * 60 * 1000;

        return lrecvtime;
    }

    /**
     * save one row to DB or write to file.
     *
     * @param row    the row instance or one row line(string)
     */
    protected abstract void writeRow(final Object row);
} // END: AbstractFileFaker
///:~
