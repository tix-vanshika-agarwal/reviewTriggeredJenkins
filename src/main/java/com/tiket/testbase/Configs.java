package com.tiket.testbase;

import com.tiket.excel.dbproperties.DBPropertiesParserFactory;
import com.tiket.excel.properties.PropertiesParserFactory;
import com.tiket.excel.testdata.TestDataParserFactory;
import com.tiket.io.local.PropertiesReader;
import com.tiket.mitmproxy.MitmProxy;
import com.tiket.model.*;
import io.appium.mitmproxy.InterceptedMessage;
import io.appium.mitmproxy.MitmproxyJava;

import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Configs {

    public static final String LAMBDA_TEST_DEBUG = "lt:options";
    public static final String BROWSERSTACK_DEBUG = "bstack:options";
    public static final int THREADS = Integer.parseInt(System.getProperty("threads"));
    public static final int SESSIONS = Integer.parseInt(System.getProperty("sessions"));
    public static final Platform PLATFORM = Platform.parse(System.getProperty("platform"));
    public static final Location LOCATION = Location.parse(System.getProperty("testdata"));
    public static final TestType TEST_TYPE = TestType.parse(System.getProperty("testtype"));
    public static final Environment ENVIRONMENT = Environment.parse(System.getProperty("env"));
    public static final String TRIBE = System.getProperty("tribe");
    public static final String MODULE = System.getProperty("module");
    public static final String TEST_NAME = System.getProperty("testname");
    public static final boolean CODE_COVERAGE = Boolean.parseBoolean(System.getProperty("coverage"));
    public static final boolean DB_USE = Boolean.parseBoolean(System.getProperty("dbUse"));
    public static final boolean GRAFANA_USE = Boolean.parseBoolean(System.getProperty("grafanaUse"));
    public static final boolean PROXY_USE = Boolean.parseBoolean(System.getProperty("proxyUse"));
    public static final boolean SEND_EMAIL = Boolean.parseBoolean(System.getProperty("sendEmail"));
    public static final boolean DEBUG_MODE = Boolean.parseBoolean(System.getProperty("debugMode"));

    public static final boolean FORCE_DEVICE_FARM_BINARY_SHUTDOWN = Boolean
            .parseBoolean(System.getProperty("forceDeviceFarmBinaryShutdown"));

    // Proxy configs must be setup after reading system properties and before any
    // subsequent configs through api calls.
    public static final List<InterceptedMessage> MESSAGES = MitmProxy.MESSAGES;
    public static final MitmproxyJava PROXY = MitmProxy.PROXY;

    public static final Properties CREDENTIALS = PropertiesReader.read("src/main/resources/credential.properties");
    public static final Properties CONFIG = PropertiesReader.read("src/main/resources/config.properties");

    public static final List<TestDataElement> TEST_DATA_ELEMENTS = TestDataParserFactory.get().getData();
    public static final Map<String, String> PROPERTIES_DATA = PropertiesParserFactory.get().getData();
    public static final List<DBProperties> DB_PROPERTIES = DBPropertiesParserFactory.get().getData();

    // Should be initiated after reading properties data
    public static final DeviceFarm DEVICE_FARM = DeviceFarm.parse(System.getProperty("deviceFarm"));
}
