/*
 * -----------------------------------------------------------
 * file name  : StatTask.java
 * creator    : wuhao(wuhao@fortinet.com)
 * created    : Wed 03 Jun 2015 10:38:30 AM CST
 *
 * modifications:
 *
 * -----------------------------------------------------------
 */
package net.vitular.tools.stat;

import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

import java.util.Date;
import java.util.TimerTask;

/**
 * Timer task for statistic.
 *
 * @author wuhao
 * @version $Revision$
 *          $Date$
 */
public abstract class StatTask extends TimerTask implements Releaseable {

    /**
     * output writer.
     */
    private BufferedWriter _outputWriter = null;

    /**
     * constructor.
     */
    public StatTask(final String output) {
        super();
        if (output != null && !"".equals(output)) {
            try {
                _outputWriter = new BufferedWriter(new FileWriter(output));
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    /**
     * print statistic data to output stream.
     *
     * @param stat  statistic data
     */
    private void print(final String stat) {
        String report = String.format("%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS, %2$s", new Date(), stat);

        if (_outputWriter == null) {
            System.out.println(report);
        } else {
            try {
                _outputWriter.write(report, 0, report.length());
                _outputWriter.newLine();
                _outputWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * print header.
     *
     * @param header
     */
    protected void printHeader(final String header) {
        print(header);
    }

    /**
     * run task and print statistic data.
     */
    public void run() {
        String stat = statistic();
        print(stat);
    }

    /**
     * close output file.
     */
    public void release() {
        if (_outputWriter != null) {
            try {
                _outputWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * statistic sth.
     *
     * implement by subclass.
     *
     * @return statistic data
     */
    protected abstract String statistic();
} // END: StatTask
///:~
