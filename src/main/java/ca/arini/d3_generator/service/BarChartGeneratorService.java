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
package ca.arini.d3_generator.service;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import ca.arini.d3_generator.renderer.Renderer;

import com.google.inject.Inject;

@Path("/generator/barchart")
@Singleton
public class BarChartGeneratorService {

    // JS Mime Type http://stackoverflow.com/questions/10047439/javascript-mime
    public static final String TEXT_JAVASCRIPT = "text/javascript";

    @Inject
    private Renderer renderer;

    @GET
    @Produces(TEXT_JAVASCRIPT)
    public Response recommendVisualizations(
            @QueryParam("categoryColumn") String categoryColumn,
            @QueryParam("measureOperation") String measureOperation,
            @QueryParam("measureColumn") String measureColumn,
            @QueryParam("orderColumn") String orderColumn) {

        // TODO Enums & value checking
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("categoryColumn", categoryColumn);
        args.put("measureOperation", measureOperation);
        args.put("measureColumn", measureColumn);
        args.put("order", BarChartOrder.valueOf(orderColumn));

        String generatedCode = renderer.render("templates/barchart.js", args);

        return Response.status(Status.ACCEPTED).entity(generatedCode).build();
    }
}