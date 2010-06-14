package nl.javadude.timing.seam;

import org.jboss.seam.annotations.intercept.Interceptors;

import java.lang.annotation.*;

/**
 * Annotation that specifies whether Seam components are timed.
 *
 * @author Jeroen van Erp
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Interceptors(TimingInterceptor.class)
public @interface Monitored {

}
