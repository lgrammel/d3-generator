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

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.jasper.servlet.JspServlet;
import org.eclipse.jetty.servlets.GzipFilter;

import ca.arini.d3_generator.renderer.Renderer;
import ca.arini.d3_generator.renderer.RythmDevelopmentRenderer;
import ca.arini.d3_generator.renderer.RythmProductionRenderer;
import ca.arini.d3_generator.service.BarChartGeneratorService;
import ca.arini.d3_generator.servlet.IndexServlet;
import ca.arini.d3_generator.servlet.filter.HtmlWhitespaceCompressionFilter;
import ca.arini.d3_generator.servlet.filter.JavascriptCompressionFilter;
import ca.arini.d3_generator.servlet.filter.JavascriptMinimizedFileSelectorFilter;

import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public final class D3GeneratorConfiguration extends JerseyServletModule {

    private static final String ERRORHANDLER_LOGGING = "errorhandler-logging.js";

    private static final String ERRORHANDLER_NULL = "errorhandler-null.js";

    private static final String MIXPANEL_LOGGING = "mixpanel-logging-stub.js";

    private static final String MIXPANEL_ERROR = "mixpanel-error-stub.js";

    private static final String MIXPANEL_NULL = "mixpanel-null-stub.js";

    private static final String MIXPANEL_TEST = "mixpanel-test.js";

    private static final String MIXPANEL_PRODUCTION = "mixpanel-production.js";

    private static final String DEVELOPMENT_MODE = "development";

    private static final String STAGING_MODE = "staging";

    private static final String PRODUCTION_MODE = "production";

    private static final String PORT = "PORT";

    private static final int DEVELOPMENT_PORT = 8080;

    public static D3GeneratorConfiguration create(String mode) throws Exception {
        if (PRODUCTION_MODE.equals(mode)) {
            return createProductionConfiguration();
        } else if (STAGING_MODE.equals(mode)) {
            return createStagingConfiguration();
        } else if (DEVELOPMENT_MODE.equals(mode)) {
            return createDevelopmentConfiguration();
        } else {
            throw new Exception("No configuration for mode `" + mode + "`");
        }
    }

    private static D3GeneratorConfiguration createDevelopmentConfiguration()
            throws Exception {

        D3GeneratorConfiguration configuration = new D3GeneratorConfiguration();

        configuration.port = DEVELOPMENT_PORT;
        configuration.mixpanelScript = loadScript(MIXPANEL_LOGGING);
        configuration.errorHandlerScript = loadScript(ERRORHANDLER_LOGGING);
        configuration.renderer = new RythmDevelopmentRenderer();

        return configuration;
    }

    private static D3GeneratorConfiguration createProductionConfiguration()
            throws Exception {

        D3GeneratorConfiguration configuration = new D3GeneratorConfiguration();

        configuration.port = Integer.parseInt(System.getenv(PORT));
        configuration.mixpanelScript = loadScript(MIXPANEL_PRODUCTION);
        configuration.errorHandlerScript = loadScript(ERRORHANDLER_NULL);
        configuration.renderer = new RythmProductionRenderer();

        return configuration;
    }

    private static D3GeneratorConfiguration createStagingConfiguration()
            throws Exception {

        D3GeneratorConfiguration configuration = new D3GeneratorConfiguration();

        configuration.port = Integer.parseInt(System.getenv(PORT));
        configuration.mixpanelScript = loadScript(MIXPANEL_TEST);
        configuration.errorHandlerScript = loadScript(ERRORHANDLER_LOGGING);
        configuration.renderer = new RythmProductionRenderer();

        return configuration;
    }

    private static String loadScript(String scriptFilename) throws IOException {
        FileReader reader = new FileReader("config/" + scriptFilename);
        try {
            return IOUtils.toString(reader);
        } finally {
            reader.close();
        }
    }

    private int port;

    private String mixpanelScript;

    private String errorHandlerScript;

    private Renderer renderer;

    private void configureRestServices() {
        bind(BarChartGeneratorService.class);
        bind(Renderer.class).toInstance(renderer);
        bindConstant().annotatedWith(IndexServlet.MixpanelScript.class).to(
                mixpanelScript);
        bindConstant().annotatedWith(IndexServlet.ErrorHandlerScript.class).to(
                errorHandlerScript);

        filter("*").through(new GzipFilter());
        filter("/").through(HtmlWhitespaceCompressionFilter.class);
        filter("*.js").through(JavascriptMinimizedFileSelectorFilter.class);
        filter("/js/d3-generator.js")
                .through(JavascriptCompressionFilter.class);

        serveRegex("^/generator/.*$").with(GuiceContainer.class);
        serve("/", "/index.*").with(IndexServlet.class);

        Map<String, String> jspParams = new HashMap<String, String>();
        jspParams.put("fork", "false");
        serve("/WEB-INF/jsp/*").with(new JspServlet(), jspParams);
    }

    @Override
    protected void configureServlets() {
        configureRestServices();
    }

    public int getPort() {
        return port;
    }

}