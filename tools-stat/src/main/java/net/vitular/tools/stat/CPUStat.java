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

import java.io.IOException;

import java.util.List;
import java.util.Scanner;
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

    // proc file name
    public static final String CPU_INFO_FILE = "/proc/cpuinfo";
    public static final String CPU_STAT_FILE = "/proc/stat";
    public static final String PROCESS_STAT_FILE_PARTTERN = "/proc/%d/stat";
    public static final String THREAD_STAT_FILE_PARTTERN = "/proc/%d/task/%d/stat";

    // grep patterns
    public static final Pattern CPU_JIFFIES_PATTERN = Pattern.compile("^cpu\\s+(.*)");
    public static final Pattern CPU_NUM_PATTERN = Pattern.compile("^processor\\s+:\\s+(\\d+)");

    /**
     * default constructor.
     */
    public CPUStat() {
        super();
    }

    private static String getProcessStatFile(final long pid) {
        return String.format(PROCESS_STAT_FILE_PARTTERN, pid);
    }

    private static String getThreadStatFile(final long pid, final long tid) {
        return String.format(THREAD_STAT_FILE_PARTTERN, pid, tid);
    }

    /**
     * read /proc/cpuinfo to get cpu numbers.
     *
     * @return cpu number
     */
    public static int numCpus() {
        List<String> cpuline_list = null;

        try {
            cpuline_list = FileUtils.grepFile(CPU_NUM_PATTERN, CPU_INFO_FILE);
        } catch (IOException ioe) {
            System.err.println("cannot find stat file: " + CPU_STAT_FILE);
            System.exit(1);
            return 0;
        }

        if (cpuline_list != null) {
            return cpuline_list.size();
        }

        return 0;
    }

    /**
     * get current cpu usage data.
     *
     * @return CPUUsage
     */
    private CPUUsage currentCPUUsage() {
        List<String> cpuline_list = null;

        try {
            cpuline_list = FileUtils.grepFile(CPU_JIFFIES_PATTERN, CPU_STAT_FILE);
        } catch (IOException ioe) {
            System.err.println("cannot find stat file: " + CPU_STAT_FILE);
            System.exit(1);
            return null;
        }

        if (cpuline_list != null && !cpuline_list.isEmpty()) {
            return new CPUUsage(cpuline_list.get(0));
        }

        return null;
    }

    /**
     * get current process cpu usage data.
     *
     * @param pid process id
     * @return ProcessUsage
     */
    private ProcessUsage currentProcessUsage(final long pid) {
        String file = getProcessStatFile(pid);

        try {
            String cpuline = FileUtils.file2string(file);
            return new ProcessUsage(cpuline);
        } catch (IOException ioe) {
            System.err.println("cannot find stat of process: " + pid);
            System.exit(1);
            return null;
        }
    }

    /**
     * get current thread cpu usage data.
     *
     * @param pid process id
     * @param tid thread id
     * @return ThreadUsage
     */
    private ThreadUsage currentThreadUsage(final long pid, final long tid) {
        String file = getThreadStatFile(pid, tid);

        try {
            String cpuline = FileUtils.file2string(file);
            return new ThreadUsage(cpuline);
        } catch (IOException ioe) {
            System.err.println(String.format("cannot find stat of process: %d, thread: %d", pid, tid));
            System.exit(1);
            return null;
        }
    }

    /**
     * cpu statistic.
     *
     * @param args
     */
    public void statCPU(final String[] args) {
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

    /**
     * process cpu statistic.
     *
     * @param args
     */
    public void statProcess(final String[] args) {
        long pid = 1;
        long period = 2000;
        String outputFile = null;

        if (args.length > 0) {
            pid = Long.parseLong(args[0]);
        }
        if (args.length > 1) {
            period = Long.parseLong(args[1]);
        }
        if (args.length > 2) {
            outputFile = args[2];
        }

        ProcessStatTask task = new ProcessStatTask(pid, outputFile);

        Runtime.getRuntime().addShutdownHook(new ShutdownHook(task));

        Timer timer = new Timer();
        timer.schedule(task, 100, period);
    }

    /**
     * thread cpu statistic.
     *
     * @param args
     */
    public void statThread(final String[] args) {
        long pid = 1;
        long tid = 1;
        long period = 2000;
        String outputFile = null;

        if (args.length > 0) {
            pid = Long.parseLong(args[0]);
        }
        if (args.length > 1) {
            tid = Long.parseLong(args[1]);
        }
        if (args.length > 2) {
            period = Long.parseLong(args[2]);
        }
        if (args.length > 3) {
            outputFile = args[3];
        }

        ThreadStatTask task = new ThreadStatTask(pid, tid, outputFile);

        Runtime.getRuntime().addShutdownHook(new ShutdownHook(task));

        Timer timer = new Timer();
        timer.schedule(task, 100, period);
    }

    // --------------------------------------------
    // inner class
    // --------------------------------------------
    class CPUStatTask extends StatTask {
        private CPUUsage _last = null;

        public CPUStatTask(final String output) {
            super(output);
            _last = currentCPUUsage();
            printHeader("*CPU*, *IDLE*");
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

    class ProcessStatTask extends StatTask {
        private CPUUsage _lastCPU = null;
        private ProcessUsage _lastProcess = null;
        private long _lPid;
        private int _iCpuNum;

        /**
         * constructor.
         *
         * @param pid
         * @param output
         */
        public ProcessStatTask(final long pid, final String output) {
            super(output);
            _lastCPU = currentCPUUsage();
            _lastProcess = currentProcessUsage(pid);
            _lPid = pid;
            _iCpuNum = numCpus();

            printHeader("*PROCESS*, *CPU*");
        }

        /**
         * statistic sth.
         *
         * implement by subclass.
         *
         * @return statistic data
         */
        protected String statistic() {
            CPUUsage currentCPU = currentCPUUsage();
            ProcessUsage currentProcess = currentProcessUsage(_lPid);

            long totalCPU = currentCPU.total() - _lastCPU.total();

            double cpuPercent = currentCPU.usagePercent(_lastCPU);
            double processPercent = currentProcess.usagePercent(_lastProcess, totalCPU) * _iCpuNum;

            _lastCPU = currentCPU;
            _lastProcess = currentProcess;

            return String.format("%4.2f, %4.2f", processPercent, cpuPercent);
        }
    } // END: ProcessStatTask

    class ThreadStatTask extends StatTask {
        private CPUUsage _lastCPU = null;
        private ProcessUsage _lastProcess = null;
        private ThreadUsage _lastThread = null;
        private int _iCpuNum;

        private long _lPid;
        private long _lTid;

        /**
         * constructor.
         *
         * @param pid
         * @param tid
         * @param output
         */
        public ThreadStatTask(final long pid, final long tid, final String output) {
            super(output);
            _lastCPU = currentCPUUsage();
            _lastProcess = currentProcessUsage(pid);
            _lastThread = currentThreadUsage(pid, tid);
            _iCpuNum = numCpus();

            _lPid = pid;
            _lTid = tid;
            printHeader("*Thread*, *Process*, *CPU*");
        }

        /**
         * statistic sth.
         *
         * implement by subclass.
         *
         * @return statistic data
         */
        protected String statistic() {
            CPUUsage currentCPU = currentCPUUsage();
            ProcessUsage currentProcess = currentProcessUsage(_lPid);
            ThreadUsage currentThread = currentThreadUsage(_lPid, _lTid);

            long totalCPU = currentCPU.total() - _lastCPU.total();

            double cpuPercent = currentCPU.usagePercent(_lastCPU);
            double processPercent = currentProcess.usagePercent(_lastProcess, totalCPU) * _iCpuNum;
            double threadPercent = currentThread.usagePercent(_lastThread, totalCPU) * _iCpuNum;

            _lastCPU = currentCPU;
            _lastProcess = currentProcess;
            _lastThread = currentThread;

            return String.format("%4.2f, %4.2f, %4.2f", threadPercent, processPercent, cpuPercent);
        }
    } // END: ThreadStatTask
} // END: CPUStat

/**
 * cpu usage.
 *
 * @author camry(camry_camry@sina.com)
 * @version
 */
class CPUUsage {
    long _lUser;
    long _lNice;
    long _lSystem;
    long _lIdle;
    long _lIoWait;
    long _lIrq;
    long _lSoftIrq;
    long _lStealStolen;
    long _lGuest;
    long _lOther;

    public CPUUsage(final String cpuline) {
        assert(cpuline != null);

        Scanner scan = new Scanner(cpuline);
        //if (scan.hasNext()) scan.next();
        if (scan.hasNext()) _lUser = scan.nextLong();
        if (scan.hasNext()) _lNice = scan.nextLong();
        if (scan.hasNext()) _lSystem = scan.nextLong();
        if (scan.hasNext()) _lIdle = scan.nextLong();
        if (scan.hasNext()) _lIoWait = scan.nextLong();
        if (scan.hasNext()) _lIrq = scan.nextLong();
        if (scan.hasNext()) _lSoftIrq = scan.nextLong();
        if (scan.hasNext()) _lStealStolen = scan.nextLong();
        if (scan.hasNext()) _lGuest = scan.nextLong();
        if (scan.hasNext()) _lOther = scan.nextLong();
    }

    public long total() {
        return _lUser + _lNice + _lSystem + _lIdle + _lIoWait + _lIrq + _lSoftIrq + _lStealStolen + _lGuest + _lOther;
    }

    public double usagePercent(final CPUUsage last) {
        long lastTotal = last.total();
        long thisTotal = total();

        long lastIdle = last._lIdle;
        long thisIdle = this._lIdle;

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
        long lastTotal = last.total();
        long thisTotal = total();

        long lastIdle = last._lIdle;
        long thisIdle = this._lIdle;

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
            _lUser, _lNice, _lSystem, _lIdle, _lIoWait, _lIrq,
            _lSoftIrq, _lStealStolen, _lGuest, _lOther);
    }
} // END: CPUUsage

/**
 * process cpu usage.
 *
 * man 5 proc
 *
 * @author camry(camry_camry@sina.com)
 * @version
 */
class ProcessUsage {

    long        pid;                        /** The process id. **/
    String      comm;                       /** The filename of the executable **/
    String      state; /** 1 **/            /** R is running, S is sleeping,
                                                D is sleeping in an uninterruptible wait,
                                                Z is zombie, T is traced or stopped **/
    long        ppid;                       /** The pid of the parent. **/
    long        pgrp;                       /** The pgrp of the process. **/
    long        session;                    /** The session id of the process. **/
    long        tty;                        /** The tty the process uses **/
    long        tpgid;                      /** (too long) **/
    long        flags;                      /** The flags of the process. **/
    long        minflt;                     /** The number of minor faults **/
    long        cminflt;                    /** The number of minor faults with childs **/
    long        majflt;                     /** The number of major faults **/
    long        cmajflt;                    /** The number of major faults with childs **/
    long        utime;                      /** user mode jiffies **/
    long        stime;                      /** kernel mode jiffies **/
    long        cutime;                     /** user mode jiffies with childs **/
    long        cstime;                     /** kernel mode jiffies with childs **/
    long        priority;                   /** the standard nice value, plus fifteen **/
    long        nice;                       /** The nice value **/
    long        num_threads;                /** Number of threads in this process **/
    long        itrealvalue;                /** The time before the next SIGALRM is sent to the process **/
    long        starttime; /** 20 **/       /** Time the process started after system boot **/
    long        vsize;                      /** Virtual memory size **/
    long        rss;                        /** Resident Set Size **/
    long        rsslim;                     /** Current limit in bytes on the rss **/
    long        startcode;                  /** The address above which program text can run **/
    long        endcode;                    /** The address below which program text can run **/
    long        startstack;                 /** The address of the start of the stack **/
    long        kstkesp;                    /** The current value of ESP **/
    long        kstkeip;                    /** The current value of EIP **/
    long        signal;                     /** The bitmap of pending signals **/
    long        blocked; /** 30 **/         /** The bitmap of blocked signals **/
    long        sigignore;                  /** The bitmap of ignored signals **/
    long        sigcatch;                   /** The bitmap of catched signals **/
    long        wchan;  /** 33 **/          /** This is the "channel" in which the process is waiting. **/

    long        nswap;                      /** Number of pages swapped **/
    long        cnswap;                     /** Cumulative nswap for child processes **/
    long        exit_signal;                /** Signal to be sent to parent when we die. **/
    long        processor;                  /** CPU number last executed on. **/
    long        rt_priority;                /** Real-time scheduling priority **/
    long        policy;                     /** Scheduling policy **/
    long        delayacct_blkio_ticks;      /** Aggregated block I/O delays **/
    long        quest_time;                 /** Guest time of the process **/
    long        cquest_time;                /** Guest time of the process's children **/

    public ProcessUsage(final String cpuline) {
        assert(cpuline != null);

        Scanner scan = new Scanner(cpuline);
        if (scan.hasNext()) pid = scan.nextLong();
        if (scan.hasNext()) comm = scan.next();
        if (scan.hasNext()) state = scan.next();
        if (scan.hasNext()) ppid = scan.nextLong();
        if (scan.hasNext()) pgrp = scan.nextLong();
        if (scan.hasNext()) session = scan.nextLong();
        if (scan.hasNext()) tty = scan.nextLong();
        if (scan.hasNext()) tpgid = scan.nextLong();
        if (scan.hasNext()) flags = scan.nextLong();
        if (scan.hasNext()) minflt = scan.nextLong();
        if (scan.hasNext()) cminflt = scan.nextLong();
        if (scan.hasNext()) majflt = scan.nextLong();
        if (scan.hasNext()) cmajflt = scan.nextLong();
        if (scan.hasNext()) utime = scan.nextLong();
        if (scan.hasNext()) stime = scan.nextLong();
        if (scan.hasNext()) cutime = scan.nextLong();
        if (scan.hasNext()) cstime = scan.nextLong();
        if (scan.hasNext()) priority = scan.nextLong();
        if (scan.hasNext()) nice = scan.nextLong();
        if (scan.hasNext()) num_threads = scan.nextLong();
        if (scan.hasNext()) itrealvalue = scan.nextLong();
        if (scan.hasNext()) starttime = scan.nextLong();
        if (scan.hasNext()) vsize = scan.nextLong();
        if (scan.hasNext()) rss = scan.nextLong();
        if (scan.hasNext()) rsslim = scan.nextLong();
        if (scan.hasNext()) startcode = scan.nextLong();
        if (scan.hasNext()) endcode = scan.nextLong();
        if (scan.hasNext()) startstack = scan.nextLong();
        if (scan.hasNext()) kstkesp = scan.nextLong();
        if (scan.hasNext()) kstkeip = scan.nextLong();
        if (scan.hasNext()) signal = scan.nextLong();
        if (scan.hasNext()) blocked = scan.nextLong();
        if (scan.hasNext()) sigignore = scan.nextLong();
        if (scan.hasNext()) sigcatch = scan.nextLong();
        if (scan.hasNext()) wchan = scan.nextLong();
        if (scan.hasNext()) nswap = scan.nextLong();
        if (scan.hasNext()) cnswap = scan.nextLong();
        if (scan.hasNext()) exit_signal = scan.nextLong();
        if (scan.hasNext()) processor = scan.nextLong();
        if (scan.hasNext()) rt_priority = scan.nextLong();
        if (scan.hasNext()) policy = scan.nextLong();
        if (scan.hasNext()) delayacct_blkio_ticks = scan.nextLong();
        if (scan.hasNext()) quest_time = scan.nextLong();
        if (scan.hasNext()) cquest_time = scan.nextLong();
    }

    public String toString() {
        return String.format("utime=%d, stime=%d, cutime=%d, cstime=%d", utime, stime, cutime, cstime);
    }

    public long total() {
        return utime + stime + cutime + cstime;
    }

    public double usagePercent(final ProcessUsage last, final long totalCPUUsage) {
        long lastTotal = last.total();
        long thisTotal = total();

        double total = (double) (thisTotal - lastTotal);

        if (total < 0) {
            total = 0.0;
        }

        if (totalCPUUsage > 0) {
            return 100 * total / totalCPUUsage;
        } else {
            return 0.0;
        }
    }
} // END: ProcessUsage

/**
 * thread cpu usage.
 *
 * @author camry(camry_camry@sina.com)
 * @version
 */
class ThreadUsage extends ProcessUsage {

    public ThreadUsage(final String cpuline) {
        super(cpuline);
    }

    public long total() {
        return utime + stime;
    }
}
///:~
