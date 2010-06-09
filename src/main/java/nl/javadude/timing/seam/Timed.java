package nl.javadude.timing.seam;

import org.jboss.seam.annotations.intercept.Interceptors;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that specifies whether Seam components are timed.
 *
 * @author Jeroen van Erp
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Interceptors(TimingInterceptor.class)
public @interface Timed {

}
