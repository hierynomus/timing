package nl.javadude.timing.seam;

import nl.javadude.timing.StatisticsFactory;
import nl.javadude.timing.Timer;
import org.jboss.seam.Component;
import org.jboss.seam.annotations.intercept.AroundInvoke;
import org.jboss.seam.annotations.intercept.Interceptor;
import org.jboss.seam.core.BijectionInterceptor;
import org.jboss.seam.intercept.AbstractInterceptor;
import org.jboss.seam.intercept.InvocationContext;

/**
 * @author Jeroen van Erp
 */
/**
 * Measuring tool for injection calls.
 */
@Interceptor(around = BijectionInterceptor.class, stateless = true)
public class TimingInterceptor extends AbstractInterceptor {
        private static final long serialVersionUID = 5274243063215400992L;

        private static final String CLASS_NAME = TimingInterceptor.class.getSimpleName();

        /**
         * Measure the timing of injection calls.
         * @param ic The Invocation Context.
         * @return The return value of the invocation.
         * @throws Exception Throws any exception which occurs in the invocationContext.
         */
        @AroundInvoke
        public Object aroundInvoke(InvocationContext ic) throws Exception {
            if (ic.getMethod().getAnnotation(Monitor.class) != null) {
                Component c = getComponent();
                String methodName = ic.getMethod().getName();
                Timer mon = getMonitor(CLASS_NAME + "-" + c.getName() + "." + methodName);
                Object o = null;
                try {
                        o = ic.proceed();
                } finally {
                        mon.stop();
                }
                return o;
            } else {
                return ic.proceed();
            }
        }

        /**
         * Extracted for testing purposes.
         * @param name The name of the monitor.
         * @return A newly created monitor.
         */
        protected Timer getMonitor(String name) {
                return StatisticsFactory.start(name);
        }

        /**
         * This interceptor is ALWAYS enabled :-).
         * @return true.
         */
        public boolean isInterceptorEnabled() {
                return true;
        }
}
