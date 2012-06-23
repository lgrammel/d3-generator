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

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Singleton;
import com.google.javascript.jscomp.CompilationLevel;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSSourceFile;

@Singleton
public class JavascriptCompressionFilter extends AbstractCompressionFilter {

    @Override
    protected String compress(String originalOutput, HttpServletRequest request) {
        // separate & only for certain files
        // compress on the fly with the closure compiler
        Compiler compiler = new Compiler();

        // only simple optimizations to not break dependencies
        CompilerOptions options = new CompilerOptions();
        CompilationLevel.SIMPLE_OPTIMIZATIONS
                .setOptionsForCompilationLevel(options);

        JSSourceFile input = JSSourceFile.fromCode(request.getRequestURI(),
                originalOutput);

        compiler.compile(JSSourceFile.fromCode("extern", ""), input, options);

        return compiler.toSource();
    }

}