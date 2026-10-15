package com.tiket.web.dashboard.page.platform.sphinx.insurance.simulation;

import com.tiket.logging.ILogger;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;
import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.*;

import static org.hamcrest.CoreMatchers.*;

public abstract class CalculateSimulationPageWeb<T extends WebDriver> extends WebPage<T> {
    public CalculateSimulationPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> CalculateSimulationPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new CalculateSimulationPageDWeb<>(session);
            }
            case AMWEB, IMWEB -> {
                return new CalculateSimulationPageMWeb<>(session);
            }
            default -> throw new UnsupportedOperationException("Platform not supported");
        }
    }

    ILogger logger = BaseTest.mainLogger.get();

    private static final String INSURANCE_SIMULATION_PATH = "admin/insurance/simulation";
    private static final Locator LOADING = new Locator("LOADING", By.xpath("//div[@data-testid='simulation-loading']"));
    private static final Locator VERTICAL_INPUT = new Locator("VERTICAL_INPUT", By.xpath("//input[@id='vertical-name']"));
    private static final Locator VERTICAL_INPUT_SELECTIONS = new Locator("VERTICAL_INPUT_SELECTIONS", By.xpath("//div[@data-testid='input-select-item-simulation-vertical-search']"));
    private static final Locator TOTAL_AMOUNT_INPUT = new Locator("TOTAL_AMOUNT_INPUT", By.xpath("//input[@id='total-amount-order']"));
    private static final Locator SIMULATION_RESULT_TABLE = new Locator("SIMULATION_TABLE_RESULT", By.xpath("//table"));
    private static final Locator SIMULATION_EMPTY_RESULT = new Locator("SIMULATION_EMPTY_RESULT", By.xpath("//table/tbody/tr/td/span[text()='Simulation data is still empty, try to do a filter to show the data.']"));
    private static final Locator HEADER = new Locator("HEADER", By.xpath("//div[@class='Header_header__ox8es']"));
    private static final Locator PROFILE_SPAN = new Locator("PROFILE_SPAN", By.xpath("//span[@data-testid='user-info-name']"));
    private static final Locator LOGOUT_BUTTON = new Locator("LOGOUT_BUTTON", By.xpath("//div[@data-testid='user-info-detail']//a"));

    private static final Locator CALCULATE_BUTTON = new Locator("CALCULATE_BUTTON", By.xpath("//button[@data-testid='add-calculate-simulation-button'][2]"));

    public void goToSimulationDashboard() {
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        memberLoginPage.adminLoginViaCookie(sheetData.get("username"), sheetData.get("password"));
        memberLoginPage.goToPage(INSURANCE_SIMULATION_PATH);

    }

    public void clickCalculateSimulation() {
        driverTools.click(CALCULATE_BUTTON);
        driverTools.waitForElementToDisappear(LOADING, 5);
    }

    public void removeUnnecessaryVariable(String[] necessaryVariables) {
        logger.log("Necessary variables:");
        for (String v : necessaryVariables) {
            logger.log(v);
        }
        List<WebElement> variableNames = driverTools.getDriver().findElements(By.xpath("//input[contains(@data-testid, 'input-select-variable-select')]"));
        List<WebElement> variableDeleteButtons = driverTools.getDriver().findElements(By.xpath("//button[@data-testid='button-delete-variable']"));

        for (int i = 0; i < variableNames.size(); i++) {
            String value = driverTools.getValueAttribute(variableNames.get(i), "value");
            if (!Arrays.asList(necessaryVariables).contains(value)) {
                driverTools.scrollElementToCenter(variableDeleteButtons.get(i));
                driverTools.click(variableDeleteButtons.get(i));
            }
        }
    }

    public void inputVertical(String vertical) {
        driverTools.click(VERTICAL_INPUT);
        driverTools.dummyWait(2);
        List<WebElement> selections = driverTools.waitForElementsToAppear(VERTICAL_INPUT_SELECTIONS).orElse(null);

        if (selections == null || selections.isEmpty()) throw new NoSuchElementException("There are no selection for Vertical");

        for (WebElement selection : selections) {
            if (driverTools.getText(selection).equalsIgnoreCase(vertical)) {
                driverTools.clickElementByCoordinateDWeb(selection);
            }
        }
        driverTools.click(VERTICAL_INPUT);
    }

    public void inputTotalAmountOrder(String totalAmount) {
        driverTools.click(TOTAL_AMOUNT_INPUT);
        driverTools.clearInput(TOTAL_AMOUNT_INPUT);
        driverTools.sendKeys(TOTAL_AMOUNT_INPUT, totalAmount);
    }


    public void inputNecessaryVariables(String input) {
        Map<String, String> variablesInputs = parseStringToMap(input);
        List<WebElement> variableNames = driverTools.getDriver().findElements(By.xpath("//div[@data-testid='flex-container']/div[1]//input"));
        List<WebElement> variableInputs = driverTools.getDriver().findElements(By.xpath("//div[@data-testid='flex-container']/div[2]//div/input"));

        for (int i = 0; i < variableNames.size(); i++) {
            driverTools.scrollElementToCenter(variableInputs.get(i));
            String variable = driverTools.getValueAttribute(variableNames.get(i), "value");
            if (driverTools.getValueAttribute(variableInputs.get(i), "data-testid").contains("input-select-value-select-")) {
                inputForSelectInput(variableInputs.get(i), variablesInputs.get(variable));
            } else if (driverTools.getValueAttribute(variableInputs.get(i), "data-testid").contains("value-select-")) {
                inputForTextInput(variableInputs.get(i), variablesInputs.get(variable));
            }
        }
    }

    public void inputForTextInput(WebElement inputElement, String value) {
        driverTools.click(inputElement);
        driverTools.clearInput(inputElement);
        driverTools.sendKeys(inputElement, value);
    }

    public void inputForSelectInput(WebElement inputElement, String value) {
        logger.log("Inputting for: " + value);
        driverTools.click(inputElement);
        driverTools.dummyWait(1);
        List<WebElement> selections = driverTools.waitForElementsToAppear(new Locator("SELECTIONS", By.xpath("//div[contains(@data-testid, 'input-select-item-value-select')]"))).orElse(null);

        if (selections == null || selections.isEmpty()) throw new NoSuchElementException();

        for (WebElement selection : selections) {
            String text = driverTools.getText(selection);
            logger.log("Selection : " + text);
            if(text.equalsIgnoreCase(value)) {
                driverTools.click(selection);
            }
        }
        driverTools.click(HEADER);
    }

    public boolean verifySimulation() {
        driverTools.waitForElementToAppear(SIMULATION_RESULT_TABLE, 5);
        driverTools.dummyWait(5);
        WebElement emptyResult = driverTools.waitForElementToAppear(SIMULATION_EMPTY_RESULT, 1).orElse(null);

        return emptyResult == null;
    }

    public void logout() {
        driverTools.click(PROFILE_SPAN);
        driverTools.dummyWait(1);
        driverTools.click(LOGOUT_BUTTON);
    }

    private Map<String, String> parseStringToMap(String inputString) {
        Map<String, String> result = new HashMap<>();

        String[] pairs = inputString.split("_");

        for (String pair : pairs) {
            int delimiterIndex = pair.indexOf('=');
            if (delimiterIndex == -1) {
                delimiterIndex = pair.indexOf(':');
            }

            if (delimiterIndex != -1) {
                String key = pair.substring(0, delimiterIndex).trim();
                String value = pair.substring(delimiterIndex + 1).trim();
                result.put(key, value);
            }
        }
        return result;
    }

    @Getter
    @Setter
    Map<String, String> sheetData = new HashMap<>();
    @Getter
    Map<String, Object> successToastData = new HashMap<>();
    @Getter
    Map<String, String> actualCredData = new HashMap<>();
}