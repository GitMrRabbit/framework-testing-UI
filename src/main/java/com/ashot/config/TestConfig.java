package com.ashot.config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources({"classpath:test.properties"})
public interface TestConfig extends Config {

    @Key("browser")
    @DefaultValue("chrome")
    String browser();

    @Key("base.url")
    @DefaultValue("https://demoqa.com/")
    String baseUrl();

    @Key("timeout")
    @DefaultValue("10")
    int timeout();

    @Key("screenshot.dir")
    @DefaultValue("screenshots")
    String screenshotDir();

    @Key("baseline.dir")
    @DefaultValue("baselines")
    String baselineDir();

    @Key("diff.dir")
    @DefaultValue("diff")
    String diffDir();

    @Key("headless")
    @DefaultValue("false")
    boolean headless();

    @Key("webview")
    @DefaultValue("false")
    boolean webview();

    @Key("retry.attempts")
    @DefaultValue("3")
    int retryAttempts();

    @Key("retry.delay")
    @DefaultValue("2000")
    int retryDelay();

    @Key("stabilization.timeout")
    @DefaultValue("5000")
    int stabilizationTimeout();

    @Key("page.load.timeout")
    @DefaultValue("120")
    int pageLoadTimeout();

    @Key("script.timeout")
    @DefaultValue("60")
    int scriptTimeout();

    @Key("implicit.wait")
    @DefaultValue("15")
    int implicitWait();
}
