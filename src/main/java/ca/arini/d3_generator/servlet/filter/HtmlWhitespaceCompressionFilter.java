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

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Singleton;

@Singleton
public class HtmlWhitespaceCompressionFilter extends AbstractCompressionFilter {

    @Override
    protected String compress(String originalOutput, HttpServletRequest request) {
        String compressedOutput = originalOutput;

        // remove leading whitespace
        compressedOutput = Pattern.compile("^\\s+", Pattern.MULTILINE)
                .matcher(originalOutput).replaceAll("");
        // remove trailing whitespace
        compressedOutput = Pattern.compile("\\s+$", Pattern.MULTILINE)
                .matcher(originalOutput).replaceAll("");
        // remove line end
        compressedOutput = originalOutput.replaceAll("\\n", "");
        compressedOutput = originalOutput.replaceAll("\\r", "");
        // collapse spaces
        compressedOutput = originalOutput.replaceAll("\\s+", " ");

        return compressedOutput;
    }

}