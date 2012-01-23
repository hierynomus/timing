package nl.javadude.timing.seam;

import nl.javadude.timing.StatisticsFactory;
import nl.javadude.timing.Timer;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.annotations.web.Filter;
import org.jboss.seam.web.AbstractFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Jeroen van Erp
 */
@Scope(ScopeType.APPLICATION)
@Name("timingFilter")
@BypassInterceptors
@Install(value = false, precedence = Install.APPLICATION)
@Filter
public class TimingFilter extends AbstractFilter {


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            final String s = "TimingFilter-" + ((HttpServletRequest) request).getServletPath();
            final Timer timer = StatisticsFactory.start(s);
            try {
                chain.doFilter(request, response);
            } finally {
                timer.stop();
            }
        } else {
            chain.doFilter(request, response);
        }
    }
}
