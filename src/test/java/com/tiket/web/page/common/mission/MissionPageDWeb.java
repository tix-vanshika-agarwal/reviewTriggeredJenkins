package com.tiket.web.page.common.mission;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

/**
 * Desktop Web (DWeb) implementation of the Mission list page.
 */
public class MissionPageDWeb<T extends WebDriver> extends MissionPageWeb<T> {

    public MissionPageDWeb(Session<T> session) {
        super(session);
    }

}