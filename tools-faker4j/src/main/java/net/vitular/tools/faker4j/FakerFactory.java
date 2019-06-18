/*
 * -----------------------------------------------------------
 * file name  : FakerFactory.java
 * creator    : camry(camry.wu@gmail.com)
 * created    : Mon 04 Feb 2013 01:08:43 PM CST
 * copyright  : (c) 2013 Vitular Inc. All Rights Reserved.
 *
 * modifications:
 *
 * -----------------------------------------------------------
 */
package net.vitular.tools.faker4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * to-do Factory for creating Faker Object.
 *
 * get config from properties file
 *
 * @author $Author$
 * @version $Revision$ $Date$
 */
public final class FakerFactory {

    /**
     * create IFileFaker base on faker context.
     *
     * @param rowSize       row size
     * @param fakerContext  faker context
     * @return IFileFaker
     */
    public static IFileFaker createFileFaker(final int rowSize, final IFakerContext fakerContext) {
        String sFakerClassName = fakerContext.getProperty(FakerConsts.FILE_FAKER);
        assert (sFakerClassName != null && !sFakerClassName.equals(""));

        Class<IFileFaker> fileFakerClass = null;
        try {
            fileFakerClass = (Class<IFileFaker>) Class.forName(sFakerClassName);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("unsupport this Faker: " + sFakerClassName, e);
        }

        Constructor<IFileFaker> c = null;
        Class[] paramClass = new Class[2];
        paramClass[0] = int.class;                  // row size
        paramClass[1] = IFakerContext.class;        // prop

        try {
            c = fileFakerClass.getConstructor(paramClass);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("cannot find constructor(int, IFakerContext): " + sFakerClassName, e);
        }

        IFileFaker fileFaker = null;
        try {
            fileFaker = c.newInstance(rowSize, fakerContext);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("cannot run constructor(int, IFakerContext): " + sFakerClassName, e);
        } catch (InstantiationException e) {
            throw new IllegalArgumentException("cannot run constructor(int, IFakerContext): " + sFakerClassName, e);
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException("cannot run constructor(int, IFakerContext): " + sFakerClassName, e);
        }

        fileFaker.initial();

        return fileFaker;
    } // END: createFileFaker

    /**
     * create FieldFaker base on name and context.
     *
     * @param name          field name
     * @param fakerContext  faker context
     * @return IFieldFaker
     */
    public static IFieldFaker createFieldFaker(final String name, final IFakerContext fakerContext) {

        String fakerExpression = fakerContext.getFakerFieldExpression(name);
        if (fakerExpression == null || "".equals(fakerExpression)) {
            throw new IllegalArgumentException("cannot find expression of the field: " + name);
        }

        FakerExpression expression = FakerExpression.getInstance(fakerExpression);

        String fieldFakerClassName = makeFieldFakerClassName(expression.getFieldType());

        Class<IFieldFaker> fieldFakerClass = null;
        try {
            fieldFakerClass = (Class<IFieldFaker>) Class.forName(fieldFakerClassName);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("cannot find class: " + fieldFakerClassName, e);
        }

        Constructor<IFieldFaker> c = null;
        Class[] paramClass = new Class[2];
        paramClass[0] = String.class;               // fieldName
        paramClass[1] = IFakerContext.class;        // faker context

        try {
            c = fieldFakerClass.getConstructor(paramClass);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("cannot find constructor(String, IFakerContext): " + fieldFakerClassName, e);
        }

        IFieldFaker fieldFaker = null;
        try {
            fieldFaker = c.newInstance(name, fakerContext);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("cannot run constructor(String, IFakerContext): " + fieldFakerClassName, e);
        } catch (InstantiationException e) {
            throw new IllegalArgumentException("cannot run constructor(String, IFakerContext): " + fieldFakerClassName, e);
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException("cannot run constructor(String, IFakerContext): " + fieldFakerClassName, e);
        }

        fieldFaker.initial(expression);

        return fieldFaker;
    }

    /**
     * make IFieldFaker sub class's classname.
     *
     * @param fieldType     Field type
     * @return
     */
    private static String makeFieldFakerClassName(final String fieldType) {
        StringBuffer fieldFakerClassName = new StringBuffer("net.vitular.tools.faker4j.");
        fieldFakerClassName.append(fieldType.toUpperCase().charAt(0));
        fieldFakerClassName.append(fieldType.substring(1));
        fieldFakerClassName.append("FieldFaker");

        return fieldFakerClassName.toString();
    }
} // END: FakerFactory
///:~
