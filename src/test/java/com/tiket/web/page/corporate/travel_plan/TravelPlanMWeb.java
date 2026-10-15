package com.tiket.web.page.corporate.travel_plan;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class TravelPlanMWeb<T extends WebDriver> extends TravelPlanWeb<T> {

    public TravelPlanMWeb(Session<T> session) {
        super(session);
    }
}
