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
package ca.arini.d3_generator.servlet;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.BindingAnnotation;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class IndexServlet extends HttpServlet {

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.FIELD, ElementType.PARAMETER })
    @BindingAnnotation
    public @interface MixpanelScript {
    }

    private static final long serialVersionUID = 1L;

    @Inject
    @MixpanelScript
    private String mixpanelScript;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setAttribute("mixpanelScript", mixpanelScript);
        req.getRequestDispatcher("/WEB-INF/jsp/index.min.jsp").forward(req,
                resp);
    }

}