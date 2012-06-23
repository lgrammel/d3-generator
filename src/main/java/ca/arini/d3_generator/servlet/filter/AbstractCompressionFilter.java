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
package ca.arini.d3_generator.servlet.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractCompressionFilter extends AbstractFilter {

    protected abstract String compress(String originalOutput,
            HttpServletRequest request);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        if (response instanceof HttpServletResponse) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            HttpServletRequest httpRequest = (HttpServletRequest) request;

            String output = compress(
                    getOutputFromChain(request, response, chain), httpRequest);

            httpResponse.setContentLength(output.length());
            httpResponse.getWriter().write(output);
        } else {
            chain.doFilter(request, response);
        }
    }

    private String getOutputFromChain(ServletRequest request,
            ServletResponse response, FilterChain chain) throws IOException,
            ServletException {

        StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);
        chain.doFilter(request, new DelegatingHttpServletResponse(
                (HttpServletResponse) response) {

            @Override
            public ServletOutputStream getOutputStream() throws IOException {
                return new ServletOutputStream() {
                    @Override
                    public void write(int b) throws IOException {
                        printWriter.write(b);
                    }
                };
            }

            @Override
            public PrintWriter getWriter() throws IOException {
                return printWriter;
            }

            @Override
            public void setContentLength(int len) {
            }
        });
        return stringWriter.toString();
    }
}
