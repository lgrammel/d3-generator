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

import ca.arini.d3_generator.service.BarChartGeneratorService;
import ca.arini.d3_generator.servlet.IndexServlet;

import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public final class D3GeneratorConfiguration extends JerseyServletModule {

    private int port;

    private String mixpanelScript;

    public D3GeneratorConfiguration(int port, String mixpanelScript) {
        assert port >= 1;

        this.mixpanelScript = mixpanelScript;
        this.port = port;
    }

    private void configureRestServices() {
        bind(BarChartGeneratorService.class);
        bindConstant().annotatedWith(IndexServlet.MixpanelScript.class).to(
                mixpanelScript);

        serveRegex("^/generator/.*$").with(GuiceContainer.class);
        serve("/", "/index.*").with(IndexServlet.class);
    }

    @Override
    protected void configureServlets() {
        configureRestServices();
    }

    public int getPort() {
        return port;
    }

}