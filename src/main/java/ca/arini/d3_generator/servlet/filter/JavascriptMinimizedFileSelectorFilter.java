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

import java.io.File;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.google.inject.Singleton;

@Singleton
public class JavascriptMinimizedFileSelectorFilter implements Filter {

    private FilterConfig filterConfig;

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            String lowerCaseRequestUri = httpRequest.getRequestURI()
                    .toLowerCase();

            if (!lowerCaseRequestUri.endsWith(".min.js")
                    && lowerCaseRequestUri.endsWith(".js")) {

                String minifiedURI = lowerCaseRequestUri.subSequence(0,
                        lowerCaseRequestUri.length() - 3) + ".min.js";
                File minifiedFile = new File(filterConfig.getServletContext()
                        .getRealPath(minifiedURI));

                if (minifiedFile.exists()) {
                    request.getRequestDispatcher(minifiedURI).forward(request,
                            response);
                    return;
                }
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }
}