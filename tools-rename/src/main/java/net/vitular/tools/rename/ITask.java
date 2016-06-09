/*
 * -----------------------------------------------------------
 * file name  : ITask.java
 * creator    : camry(camry_camry@sina.com)
 * created    : Thu 09 Jun 2016 10:11:50 AM CST
 * copyright  : (c) 2016 Vitular Inc. All Rights Reserved.
 *
 * modifications:
 *
 * -----------------------------------------------------------
 */
package net.vitular.tools.rename;

/**
 * task 接口.
 *
 * @author $Author$
 * @version $Revision$ $Date$
 */
public interface ITask {

    /**
     * 根据参数执行命令.
     *
     * @param args 输入参数
     */
    public void execute(final String[] args);
} // END: ITask
///:~
