package com.sdastest.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test
@CucumberOptions(
        features = "src/test/resources/features/CoinGeckoTest.feature",
        glue = {
                "com.sdastest.projects.api.coingecko.stepdefinitions",
                "com.sdastest.hooks"
        },
        plugin = {
                "com.sdastest.hooks.CucumberListener",
                "pretty",
                "html:target/cucumber-reports/TestRunnerCoinGecko.html",
                "json:target/cucumber-reports/TestRunnerCoinGecko.json",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        },
        monochrome = true,
        tags = "@CoinGecko"
)

public class TestRunnerCoinGecko extends AbstractTestNGCucumberTests {
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}