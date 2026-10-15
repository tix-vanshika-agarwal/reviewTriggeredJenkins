package com.tiket.web.page.common.mission;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

/**
 * Mobile Web (MWeb) implementation of the Mission list page.
 */
public class MissionPageMWeb<T extends WebDriver> extends MissionPageWeb<T> {

    public MissionPageMWeb(Session<T> session) {
        super(session);
    }

}