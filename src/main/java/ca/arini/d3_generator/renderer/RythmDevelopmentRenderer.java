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
package ca.arini.d3_generator.renderer;

import java.util.Map;
import java.util.Properties;

import com.greenlaw110.rythm.Rythm;

public class RythmDevelopmentRenderer implements Renderer {

    @Override
    public String render(String template, Map<String, Object> arguments) {
        Properties properties = new Properties();
        properties.put("rythm.mode", "dev");
        properties.put("rythm.compactOutput", false);
        Rythm.init(properties);

        return Rythm.render(template, arguments);
    }

}
