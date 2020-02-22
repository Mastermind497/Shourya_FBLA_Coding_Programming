//package org.lpsstudents.shourya.ui;
//
//import com.vaadin.flow.server.startup.ServletContextListeners;
//import org.eclipse.jetty.annotations.AnnotationConfiguration;
//import org.eclipse.jetty.server.Server;
//import org.eclipse.jetty.util.resource.Resource;
//import org.eclipse.jetty.webapp.*;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
//
//import java.net.URI;
//import java.net.URL;
//
///**
// * The entry point of the Spring Boot application.
// */
//@SpringBootApplication
//public class Application extends SpringBootServletInitializer {
//
//    /**
//     * For Spring-Boot Running
//     *
//     * @param args standard main method argument
//     */
//    public static void main(String[] args) {
//        SpringApplication.run(Application.class, args);
//    }
//
//    /**
//     * For Jetty Running
//     */
//    public static void jettyMain(String[] args) throws Exception {
//        URL webRootLocation = Application.class.getResource("/webapp/");
//        URI webRootUri = webRootLocation.toURI();
//        WebAppContext context = new WebAppContext();
//        context.setBaseResource(Resource.newResource(webRootUri));
//        context.setContextPath("/");
//        context.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern", ".*");
//        context.setConfigurationDiscovered(true);
//        context.setConfigurations(new Configuration[]{
//                new AnnotationConfiguration(),
//                new WebInfConfiguration(),
//                new WebXmlConfiguration(),
//                new MetaInfConfiguration()
//        });
//        context.getServletContext().setExtendedListenerTypes(true);
//        context.addEventListener(new ServletContextListeners());
//
//        Server server = new Server(8080);
//        server.setHandler(context);
//        server.start();
//        server.join();
//    }
//}
