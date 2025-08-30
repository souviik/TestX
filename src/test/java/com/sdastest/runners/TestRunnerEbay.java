package com.sdastest.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test
@CucumberOptions(
        features = "src/test/resources/features/EbayTest.feature",
        glue = {
                "com.sdastest.projects.website.ebay.stepdefinitions",
                "com.sdastest.hooks"
        },
        plugin = {
                "com.sdastest.hooks.CucumberListener",
                "pretty",
                "html:target/cucumber-reports/TestRunnerEbay.html",
                "json:target/cucumber-reports/TestRunnerEbay.json",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        },
        monochrome = true,
        tags = "@EbayTest"
)

public class TestRunnerEbay extends AbstractTestNGCucumberTests {
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}