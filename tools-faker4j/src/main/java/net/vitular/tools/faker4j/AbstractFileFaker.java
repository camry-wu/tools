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
     * record type count.
     */
    private int _iRecordTypeCount = 1;

    /**
     * row size.
     */
    private int _aiRowSize[];

    /**
     * total row size.
     */
    private int _iTotalRowSize = 0;

    /**
     * if quiet, donot print out process.
     */
    private boolean _bQuiet = false;

    // getter and setter
    protected void setRowFaker(final IRowFaker rowFaker) { _rowFaker = rowFaker; }
    protected IRowFaker getRowFaker() { return _rowFaker; }

    public void setRecordTypeCount(final int recordTypeCount) { _iRecordTypeCount = recordTypeCount; }
    public int getRecordTypeCount() { return _iRecordTypeCount; }

    public void setRowSize(final int recordType, final int rowSize) {
        _aiRowSize[recordType] = rowSize;
        resetTotalRowSize();
    }
    public int getRowSize(final int recordType) { return _aiRowSize[recordType]; }

    /**
     * reset total row size.
     */
    private void resetTotalRowSize() {
        _iTotalRowSize = 0;
        for (int i = 0; i < _iRecordTypeCount; i++) {
            _iTotalRowSize += _aiRowSize[i];
        }
    }

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

        // record type count
        _iRecordTypeCount = _fakerContext.getIntProperty(FakerConsts.FILE_RECORD_TYPE_COUNT, 1);

        // event count of each record type
        _aiRowSize = new int[_iRecordTypeCount];
        if (rowSize > 0) {
            for (int i = 0; i < _iRecordTypeCount; i++) {
                _aiRowSize[i] = rowSize;
            }
        } else {
            for (int i = 0; i < _iRecordTypeCount; i++) {
                // get row size
                _aiRowSize[i] = _fakerContext.getIntProperty(String.format("%s.%d", FakerConsts.FILE_RECORD_COUNT, i), 1);
            }

        }

        resetTotalRowSize();

        // quiet
        _bQuiet = _fakerContext.getBooleanProperty(FakerConsts.FILE_QUIET, false);
    }

    /**
     * generate file.
     */
    public void generateFile() {

        int percent = (int) _iTotalRowSize / 100;
        if (percent <= 0) {
            percent = 1;
        }

        int outputCount[] = new int[_iRecordTypeCount];
        System.arraycopy(_aiRowSize, 0, outputCount, 0, _iRecordTypeCount);

        int currentType = 0;

        int i = 0;
        int j = 0;
        while (i < _iTotalRowSize) {

            if (outputCount[currentType] <= 0) {
                currentType = (currentType + 1) % _iRecordTypeCount;
                continue;
            }

            Object row = _rowFaker.next(currentType);

            if (((int) (i + 1) % percent) == 0) {
                j++;
                if (j % 10 == 0) {
                    print(j / 10);
                } else {
                    print("=");
                }
            }

            if (!_fakerContext.isDebug()) {
                writeRow(row);
            }

            outputCount[currentType] = outputCount[currentType] - 1;
            currentType = (currentType + 1) % _iRecordTypeCount;
            i++;
        }

        if (!_bQuiet) {
            System.out.println();
        }
    }

    /**
     * generate file in the indicated period time.
     *
     * @param period    unit is milliseconds
     */
    public void generateFile(final long period) {
        if (_fakerContext.isDebug()) {
            return;
        }

        int percent = (int) _iTotalRowSize / 100;
        if (percent <= 0) {
            percent = 1;
        }

        // how many time to make one row?
        long timeToMakeOneRow = (period * 1000000000L) / _iTotalRowSize;
        long waitNano = 0L;

        int outputCount[] = new int[_iRecordTypeCount];
        System.arraycopy(_aiRowSize, 0, outputCount, 0, _iRecordTypeCount);

        int currentType = 0;

        int i = 0;
        int j = 0;
        while (i < _iTotalRowSize) {
            if (outputCount[currentType] <= 0) {
                currentType = (currentType + 1) % _iRecordTypeCount;
                continue;
            }

            long begin = System.nanoTime();

            Object row = _rowFaker.next(currentType);
            writeRow(row);

            if (!_bQuiet) {
                if (((int) (i + 1) % percent) == 0) {
                    j++;
                    if (j % 10 == 0) {
                        print(j / 10);
                    } else {
                        print("=");
                    }
                }
            }

            outputCount[currentType] = outputCount[currentType] - 1;
            currentType = (currentType + 1) % _iRecordTypeCount;
            i++;

            long spend = System.nanoTime() - begin;
            waitNano = timeToMakeOneRow - spend + waitNano;
            if (waitNano > 0) {
                sleepNano(waitNano);
                waitNano = 0;
            }
        }

        if (!_bQuiet) {
            System.out.println();
        }
    }

    /**
     * sleep for nano time.
     *
     * @param nanoTime nano time
     */
    private void sleepNano(final long nanoTime) {
        long begin = System.nanoTime();
        while (true) {
            if (System.nanoTime() - begin >= nanoTime) {
                break;
            }
        }
    }

    /**
     * print something.
     *
     * @param o the output string
     */
    private void print(Object o) {
        if (_bQuiet) {
            return;
        } else {
            System.out.print(String.valueOf(o));
        }
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
     * get the date the file currentd.
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
