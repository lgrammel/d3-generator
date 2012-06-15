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

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/generator/barchart")
@Singleton
public class BarChartGeneratorService {

    // JS Mime Type http://stackoverflow.com/questions/10047439/javascript-mime
    public static final String TEXT_JAVASCRIPT = "text/javascript";

    @GET
    @Produces(TEXT_JAVASCRIPT)
    public Response recommendVisualizations(
            @QueryParam("category-column") String categoryColumn,
            @QueryParam("measure-column") String measureColumn) {

        return Response.status(Status.ACCEPTED)
                .entity("TODO implement bar chart generator").build();
    }

}