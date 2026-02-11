package com.ashot.config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources({"classpath:baseline.properties"})
public interface BaselineConfig extends Config {

    @Key("browser")
    @DefaultValue("chrome")
    String browser();

    @Key("prod.url")
    @DefaultValue("https://demoqa.com")
    String prodUrl();

    @Key("timeout")
    @DefaultValue("15")
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

    @Key("auto.create.baselines")
    @DefaultValue("true")
    boolean autoCreateBaselines();

    @Key("baseline.prefix")
    @DefaultValue("baseline_")
    String baselinePrefix();
}
