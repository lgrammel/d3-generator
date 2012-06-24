/* ***********************************************************************
 * 
 * ARINI CONFIDENTIAL
 * __________________
 * 
 *  Copyright Arini Software Inc. 
 *  All Rights Reserved.
 * 
 * NOTICE:  All information contained herein is, and remains
 * the property of Arini Software Inc. and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Arini Software Inc.
 * and its suppliers and may be covered by Canadian and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Arini Software Inc.
 *
 *************************************************************************/
package ca.arini.d3_generator.launch;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;

public class D3GeneratorLauncher {

    private static final String MODE = "MODE";

    private static final String WEB_APP_DIRECTORY = "src/main/webapp/";

    public static void main(String[] args) throws Exception {
        start(D3GeneratorConfiguration.create(System.getenv(MODE)));
    }

    public static void start(D3GeneratorConfiguration configuration)
            throws Exception {

        startServer(configuration.getPort(),
                Guice.createInjector(configuration));
    }

    private static void startServer(int port, Injector injector)
            throws Exception {

        ServletContextHandler contextHandler = new ServletContextHandler();
        contextHandler.setSessionHandler(new SessionHandler(
                new HashSessionManager()));
        contextHandler.setContextPath("/");
        contextHandler.setResourceBase(WEB_APP_DIRECTORY);

        // http://wiki.eclipse.org/Jetty/Howto/Deal_with_Locked_Windows_Files
        // http://stackoverflow.com/questions/184312/how-to-make-jetty-dynamically-load-static-pages
        ServletHolder holder = new ServletHolder(new DefaultServlet());
        holder.setInitParameter("useFileMappedBuffer", "false");
        holder.setInitParameter("dirAllowed", "false");
        holder.setInitOrder(0);
        contextHandler.getServletHandler().addServletWithMapping(holder, "/");

        // Guice - reroute all requests
        FilterHolder guiceFilter = new FilterHolder(
                injector.getInstance(GuiceFilter.class));
        contextHandler.addFilter(guiceFilter, "/*",
                EnumSet.allOf(DispatcherType.class));

        Server server = new Server(port);
        server.setHandler(contextHandler);
        server.start();
        server.join();
    }

}
