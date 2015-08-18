/*
 * -----------------------------------------------------------
 * file name  : Cacheable.java
 * creator    : camry(camry_camry@sina.com)
 * created    : Sat 15 Aug 2015 16:00:39 PM CST
 *
 * modifications:
 *
 * -----------------------------------------------------------
 */
package net.vitular.tools.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * annotation for Cacheable method.
 *
 * @author camry
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Cacheable {
    String cacheName();
}
