package me.brlw.bip.config.app;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * Created by vl on 17.09.16.
 */
public class BipAppInitializer implements WebApplicationInitializer
{
    @Override
    public void onStartup(ServletContext container) throws ServletException {
//        XmlWebApplicationContext rootContext = new XmlWebApplicationContext();
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
//        rootContext.setConfigLocation();
//        rootContext.setConfigLocation("/WEB-INF/classes/META-INF/spring/datasource-tx-jpa.xml");
        rootContext.scan("me.brlw.bip.account",
                "me.brlw.bip.help",
                "me.brlw.bip.redirection",
                "me.brlw.bip.statistics",
                "me.brlw.bip.config");

//        rootContext.register(WebConfig.class);
//        container.addListener(new ContextLoaderListener(rootContext));
//        rootContext.start();

        AnnotationConfigWebApplicationContext servletContext = new AnnotationConfigWebApplicationContext();
        servletContext.register(WebConfig.class);

        ServletRegistration.Dynamic dispatcher =
                container.addServlet("appServlet", new DispatcherServlet(servletContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        FilterRegistration.Dynamic filter =
                container.addFilter("Utf8EncodingFilter", newForceUtf8EncodingFilter());
//        filter.setInitParameter("encoding", "UTF-8");
//        filter.setInitParameter("forceEncoding", "true");
        filter.addMappingForUrlPatterns(null, true, "/*");
    }

    private CharacterEncodingFilter newForceUtf8EncodingFilter() {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        return filter;
    }

}
