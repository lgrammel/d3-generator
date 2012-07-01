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
package ca.arini.d3_generator;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

public class BarChartGenerator {

    private RemoteWebDriver driver;

    // issue #65
    @Test
    public void generateWithSumAndSelectedMeasure() {
        driver.get("http://127.0.0.1:8080/");
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);

        select("measureOperation", "sum");
        select("inputMeasureColumn", "Area (1000 sq mi)");

        driver.findElement(By.id("buttonGenerateCode")).click();

        WebElement chartElement = driver.findElement(By.id("chart"));
        WebElement barLabelElement = chartElement
                .findElement(By.tagName("svg")).findElements(By.tagName("g"))
                .get(2).findElements(By.tagName("text")).get(0);

        assertEquals("Land area of Canada should be in first column",
                "3854.09", barLabelElement.getText());
    }

    private void select(String selectId, String optionValue) {
        WebElement select = driver.findElement(By.id(selectId));
        for (WebElement option : select.findElements(By.tagName("option"))) {
            if (optionValue.equals(option.getAttribute("value"))) {
                option.click();
                break;
            }
        }
    }

    @Before
    public void setUp() {
        driver = new FirefoxDriver();
    }

    @After
    public void tearDown() {
        driver.quit();
    }
}