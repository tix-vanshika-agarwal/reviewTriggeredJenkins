package com.tiket.web.test.prodmonitoring.sendgrafanasnap;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.testbase.BaseTest;
import com.tiket.web.WebTest;
import com.tiket.web.page.monitoring.GrafanaPage;
import org.testng.annotations.Test;

import javax.security.auth.login.FailedLoginException;
import java.time.ZoneId;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.tiket.testdata.TestData.getParam;

public class GrafanaSnapTest extends WebTest {
    @Author(names = "SDET_Team")
    @Module(name = "GrafanaMonitoring")
    @Tribe(name = "Accommodation")
    @Vertical(name = "GrafanaSnap")
    @Test
    public void sendAccommodationGrafanaReport() throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("sendAccommodationGrafanaReport");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        GrafanaPage<?> grafanaPage = new GrafanaPage<>(session);

        String verticalName = param.get("verticalName");
        String dashboardId = param.get("dashboardId");
        int orgId = Integer.parseInt(param.get("orgId"));
        Map<String, Map<String, Integer>> panelIds = CoreFunctions.buildGrafanaPanelIds(param);
        
        // Get configurable parameters from test data
        String timezone = param.get("timezone");
        int width = Integer.parseInt(param.get("width"));
        int height = Integer.parseInt(param.get("height"));
        // Theme + time-window + report period label are now sourced from properties via
        // GrafanaSnapConfig. Each resolver tries: <key>.<vertical> -> <key> -> hard default.
        String theme = GrafanaSnapConfig.resolveTheme(verticalName);
        String from = GrafanaSnapConfig.resolveFrom();
        String to = GrafanaSnapConfig.resolveTo();
        String reportPeriodLabel = from.split("T")[0];

        step("Execute: Capture Snapshots and Send Email Report for Grafana Monitoring");
        grafanaPage.captureAndSendGrafanaReport(dashboardId, verticalName, orgId, panelIds,
                timezone, width, height, theme, from, to, reportPeriodLabel);
    }

    @Author(names = "SDET_Team")
    @Module(name = "GrafanaMonitoring")
    @Tribe(name = "Flight")
    @Vertical(name = "GrafanaSnap")
    @Test
    public void sendFlightGrafanaReport() throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("sendFlightGrafanaReport");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        GrafanaPage<?> grafanaPage = new GrafanaPage<>(session);

        String verticalName = param.get("verticalName");
        String dashboardId = param.get("dashboardId");
        int orgId = Integer.parseInt(param.get("orgId"));
        Map<String, Map<String, Integer>> panelIds = CoreFunctions.buildGrafanaPanelIds(param);
        
        // Get configurable parameters from test data
        String timezone = param.get("timezone");
        int width = Integer.parseInt(param.get("width"));
        int height = Integer.parseInt(param.get("height"));
        // Theme + time-window + report period label are now sourced from properties via
        // GrafanaSnapConfig. Each resolver tries: <key>.<vertical> -> <key> -> hard default.
        String theme = GrafanaSnapConfig.resolveTheme(verticalName);
        String from = GrafanaSnapConfig.resolveFrom();
        String to = GrafanaSnapConfig.resolveTo();
        String reportPeriodLabel = from.split("T")[0];

        step("Execute: Capture Snapshots and Send Email Report for Grafana Monitoring");
        grafanaPage.captureAndSendGrafanaReport(dashboardId, verticalName, orgId, panelIds,
                timezone, width, height, theme, from, to, reportPeriodLabel);
    }

    @Author(names = "SDET_Team")
    @Module(name = "GrafanaMonitoring")
    @Tribe(name = "TTD")
    @Vertical(name = "GrafanaSnap")
    @Test
    public void sendTTDGrafanaReport() throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("sendTTDGrafanaReport");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        GrafanaPage<?> grafanaPage = new GrafanaPage<>(session);

        String verticalName = param.get("verticalName");
        String dashboardId = param.get("dashboardId");
        int orgId = Integer.parseInt(param.get("orgId"));
        Map<String, Map<String, Integer>> panelIds = CoreFunctions.buildGrafanaPanelIds(param);
        
        // Get configurable parameters from test data
        String timezone = param.get("timezone");
        int width = Integer.parseInt(param.get("width"));
        int height = Integer.parseInt(param.get("height"));
        // Theme + time-window + report period label are now sourced from properties via
        // GrafanaSnapConfig. Each resolver tries: <key>.<vertical> -> <key> -> hard default.
        String theme = GrafanaSnapConfig.resolveTheme(verticalName);
        String from = GrafanaSnapConfig.resolveFrom();
        String to = GrafanaSnapConfig.resolveTo();
        String reportPeriodLabel = from.split("T")[0];

        step("Execute: Capture Snapshots and Send Email Report for Grafana Monitoring");
        grafanaPage.captureAndSendGrafanaReport(dashboardId, verticalName, orgId, panelIds,
                timezone, width, height, theme, from, to, reportPeriodLabel);
    }
}
