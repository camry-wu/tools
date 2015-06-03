/*
 * -----------------------------------------------------------
 * file name  : CPUStat.java
 * creator    : camry(camry_camry@sina.com)
 * created    : Tue 02 Jun 2015 10:48:50 AM CST
 *
 * modifications:
 *
 * -----------------------------------------------------------
 */
package net.vitular.tools.stat;

import java.util.List;
import java.util.Timer;
import java.util.regex.Pattern;

/**
 * statistic cpu info.
 *
 * @author wuhao
 * @version $Revision$
 *          $Date$
 */
public class CPUStat {

    public static final String CPU_STAT_FILE = "/proc/stat";
    public static final String PROCESS_STAT_FILE_PARTTERN = "/proc/%d/stat";
    public static final String THREAD_STAT_FILE_PARTTERN = "/proc/%d/task/%d/stat";

    public static final Pattern CPU_JIFFIES_PATTERN = Pattern.compile("^cpu\\s.*");

    /**
     * default constructor.
     */
    public CPUStat() {
        super();
    }

    private static String getProcessStatFile(final int pid) {
        return String.format(PROCESS_STAT_FILE_PARTTERN, pid);
    }

    private static String getThreadStatFile(final int pid, final int tid) {
        return String.format(THREAD_STAT_FILE_PARTTERN, pid, tid);
    }

    /**
     * get current cpu usage data.
     *
     * @return CPUUsage
     */
    private CPUUsage currentCPUUsage() {
        List<String> cpuline_list = FileUtils.grepFile(CPU_JIFFIES_PATTERN, CPU_STAT_FILE);
        if (cpuline_list != null && !cpuline_list.isEmpty()) {
            return new CPUUsage(cpuline_list.get(0));
        }

        return null;
    }

    /**
     * execute cpu stat.
     *
     * @param args
     */
    public void execute(final String[] args) {
        long period = 2000;
        String outputFile = null;

        if (args.length > 0) {
            period = Long.parseLong(args[0]);
        }
        if (args.length > 1) {
            outputFile = args[1];
        }

        CPUStatTask task = new CPUStatTask(outputFile);

        Runtime.getRuntime().addShutdownHook(new ShutdownHook(task));

        Timer timer = new Timer();
        timer.schedule(task, 100, period);
    }

    class CPUStatTask extends StatTask {
        private CPUUsage _last = null;

        public CPUStatTask(final String output) {
            super(output);
            _last = currentCPUUsage();
        }

        /**
         * statistic sth.
         *
         * implement by subclass.
         *
         * @return statistic data
         */
        protected String statistic() {
            CPUUsage current = currentCPUUsage();
            double usagePercent = current.usagePercent(_last);
            double idlePercent = current.idlePercent(_last);
            _last = current;

            return String.format("%4.2f, %4.2f", usagePercent, idlePercent);
        }
    } // END: CPUStatTask
} // END: CPUStat

/**
 * cpu usage.
 *
 * @author camry(camry_camry@sina.com)
 * @version
 */
class CPUUsage {
    int _iUser;
    int _iNice;
    int _iSystem;
    int _iIdle;
    int _iIoWait;
    int _iIrq;
    int _iSoftIrq;
    int _iStealStolen;
    int _iGuest;
    int _iOther;

    public CPUUsage(final String cpuline) {
        assert(cpuline != null);
        String[] line = cpuline.split("[\\s]+");

        if (line.length > 1) {
            _iUser = Integer.parseInt(line[1]);
        }
        if (line.length > 2) {
            _iNice = Integer.parseInt(line[2]);
        }
        if (line.length > 3) {
            _iSystem = Integer.parseInt(line[3]);
        }
        if (line.length > 4) {
            _iIdle = Integer.parseInt(line[4]);
        }
        if (line.length > 5) {
            _iIoWait = Integer.parseInt(line[5]);
        }
        if (line.length > 6) {
            _iIrq = Integer.parseInt(line[6]);
        }
        if (line.length > 7) {
            _iSoftIrq = Integer.parseInt(line[7]);
        }
        if (line.length > 8) {
            _iStealStolen = Integer.parseInt(line[8]);
        }
        if (line.length > 9) {
            _iGuest = Integer.parseInt(line[9]);
        }
        if (line.length > 10) {
            _iOther = Integer.parseInt(line[10]);
        }
    }

    public int total() {
        return _iUser + _iNice + _iSystem + _iIdle + _iIoWait + _iIrq + _iSoftIrq + _iStealStolen + _iGuest + _iOther;
    }

    public double usagePercent(final CPUUsage last) {
        int lastTotal = last.total();
        int thisTotal = total();

        int lastIdle = last._iIdle;
        int thisIdle = this._iIdle;

        double total = (double) (thisTotal - lastTotal);
        double idle = (double) (thisIdle - lastIdle);

        if (total < 0) {
            total = 0.0;
        }
        if (idle < 0) {
            idle = 0.0;
        }

        if (total > 0) {
            return 100 * (total - idle) / total;
        } else {
            return 0.0;
        }
    }

    public double idlePercent(final CPUUsage last) {
        int lastTotal = last.total();
        int thisTotal = total();

        int lastIdle = last._iIdle;
        int thisIdle = this._iIdle;

        double total = (double) (thisTotal - lastTotal);
        double idle = (double) (thisIdle - lastIdle);

        if (total < 0) {
            total = 0.0;
        }
        if (idle < 0) {
            idle = 0.0;
        }

        if (total > 0) {
            return 100 * idle / total;
        } else {
            return 0.0;
        }
    }

    public String toString() {
        return String.format("user=%d, nice=%d, system=%d, idle=%d, iowait=%d, irq=%d, softIrq=%d, stealStolen=%d, guest=%d, other=%d",
            _iUser, _iNice, _iSystem, _iIdle, _iIoWait, _iIrq,
            _iSoftIrq, _iStealStolen, _iGuest, _iOther);
    }
} // END: CPUUsage

/**
 * process cpu usage.
 *
 * @author camry(camry_camry@sina.com)
 * @version
 */
class ProcessUsage {
    int _iUTime;
    int _iSTime;
    int _iCUTime;
    int _iCSTime;
    int _iOther;

    public ProcessUsage(final String cpuline) {
    }

    public String toString() {
        return String.format("utime=%d, stime=%d, cutime=%d, cstime=%d, other=%d",
            _iUTime, _iSTime, _iCUTime, _iCSTime, _iOther);
    }
}

/**
 * thread cpu usage.
 *
 * @author camry(camry_camry@sina.com)
 * @version
 */
class ThreadUsage {
    int _iUTime;
    int _iSTime;

    public ThreadUsage(final String cpuline) {
    }

    public String toString() {
        return String.format("utime=%d, stime=%d", _iUTime, _iSTime);
    }
}
///:~
