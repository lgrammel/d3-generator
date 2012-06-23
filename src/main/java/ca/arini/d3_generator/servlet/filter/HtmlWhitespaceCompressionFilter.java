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
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Singleton;

@Singleton
public class HtmlWhitespaceCompressionFilter implements Filter {

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        if (response instanceof HttpServletResponse) {
            String htmlOutput = getHtmlOutputFromChain(request, response, chain);

            // remove leading whitespace
            htmlOutput = Pattern.compile("^\\s+", Pattern.MULTILINE)
                    .matcher(htmlOutput).replaceAll("");
            // remove trailing whitespace
            htmlOutput = Pattern.compile("\\s+$", Pattern.MULTILINE)
                    .matcher(htmlOutput).replaceAll("");
            // remove line end
            htmlOutput = htmlOutput.replaceAll("\\n", "");
            htmlOutput = htmlOutput.replaceAll("\\r", "");
            // collapse spaces
            htmlOutput = htmlOutput.replaceAll("\\s+", " ");

            ((HttpServletResponse) response).setContentLength(htmlOutput
                    .length());
            ((HttpServletResponse) response).getWriter().write(htmlOutput);
        } else {
            chain.doFilter(request, response);
        }
    }

    private String getHtmlOutputFromChain(ServletRequest request,
            ServletResponse response, FilterChain chain) throws IOException,
            ServletException {

        StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);
        chain.doFilter(request, new HttpServletResponseDelegate(
                (HttpServletResponse) response) {
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

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
}