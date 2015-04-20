/*
 * -----------------------------------------------------------
 * file name  : TextPlainFileFaker.java
 * creator    : camry(camry.wu@gmail.com)
 * created    : Mon 04 Feb 2013 01:08:43 PM CST
 * copyright  : (c) 2013 Vitular Inc. All Rights Reserved.
 *
 * modifications:
 *
 * -----------------------------------------------------------
 */
package net.vitular.tools.faker4j;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

/**
 * to-do Faker Test Data file (more then one rows) and save rows to text/plain file.
 *
 * @author $Author$
 * @version $Revision$ $Date$
 */
public class TextPlainFileFaker extends AbstractFileFaker {

    /**
     * output stream.
     */
    private BufferedWriter _fileOutputStream = null;

    /**
     * header.
     */
    private String _sHeader;

    /**
     * footer.
     */
    private String _sFooter;

    /**
     * constructor.
     *
     * @param rowSize       row size, get from command argument
     * @param fakerContext  faker context
     */
    public TextPlainFileFaker(final int rowSize, final IFakerContext fakerContext) {
        super(rowSize, fakerContext);
    }

    /**
     * initial faker.
     */
    public void initial() {
        IFakerContext ctx = getFakerContext();

        // initial file
        String sDestURL = ctx.getProperty(FakerConsts.FILE_OUTPUT_PATH);
        _sHeader = ctx.getProperty(FakerConsts.FILE_HEADER);
        _sFooter = ctx.getProperty(FakerConsts.FILE_FOOTER);

        String filepath = makeOutputFilePath(sDestURL);

        if (!ctx.isDebug()) {
            try {
                _fileOutputStream = new BufferedWriter(new FileWriter(filepath, true));

                if (_sHeader != null && !"".equals(_sHeader)) {
                    writeLine(_sHeader);
                }
            } catch (FileNotFoundException e) {
                String err = String.format("cannot open file: %s [base on file_output_path: %s].", filepath, sDestURL);
                throw new IllegalArgumentException(err, e);
            } catch (IOException e) {
                String err = String.format("cannot find path: %s [base on file_output_path: %s].", filepath, sDestURL);
                throw new IllegalArgumentException(err, e);
            }
        }

        // initial row/fields
        IRowFaker rowFaker = createRowFaker(ctx);

        setRowFaker(rowFaker);
    }

    /**
     * release resources.
     */
    public void release() {
        // write footer
        if (_sFooter != null && !"".equals(_sFooter)) {
            writeLine(_sFooter);
        }

        // close stream
        if (_fileOutputStream != null) {
            try {
                _fileOutputStream.close();
            } catch (IOException e) {
                _logger.error("catch IOException while close file output stream.", e);
            }
        }
    }

    /**
     * write one line to text file.
     * Note: the original line donot have '\n'
     *
     * @param line  one line string
     */
    protected void writeLine(String line) {
        if (getFakerContext().isDebug()) {
            return;
        }

        try {
            _fileOutputStream.write(line, 0, line.length());
            _fileOutputStream.newLine();
        } catch (IOException e) {
            _logger.error("catch IOException while write line to file.", e);
        }
    }

    /**
     * save one row to DB or write to file.
     *
     * @param row    the row instance or one row line(string)
     */
    protected void writeRow(final Object row) {
        writeLine(String.valueOf(row));
    }

    /**
     * create subclass IRowFaker.
     *
     * @param fakerContext faker context
     * @return IRowFaker
     */
    protected IRowFaker createRowFaker(final IFakerContext fakerContext) {
        return new TextPlainRowFaker(fakerContext, getRecordTypeCount());
    }
    ////// private methods ////////
} // END: TextPlainFileFaker
///:~
