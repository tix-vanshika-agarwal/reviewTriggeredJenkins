package com.tiket.page.flight.pdp;

import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.either;
import static org.hamcrest.core.Is.is;

public abstract class FlightDescriptionPage <T extends WebDriver> extends BasePage<T> {
    public FlightDescriptionPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> FlightDescriptionPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new FlightDescriptionPageAndroid<>(session);}
            case IOS -> {return new FlightDescriptionPageIOS<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public abstract boolean verifyStopOver();
    public abstract boolean isAirlineLogoDisplayedPDP(); //Fixme Implementation pending iOS-> missing unique id
    public abstract boolean isAirlineRouteCorrectPDP(String route);
    public abstract String checkYellowBannerPDP();
    public abstract void clickYellowBannerPDP();
    public abstract boolean is100pRefundTagDisplayed();

    public void isContainsDestinationLocation(String route,String yellowBannerText){
        String[] routeArr = route.split(" ");
        String[] origin = routeArr[0].split("-");
        String[] destination = routeArr[2].split("-");
        String originPart1 = origin[0],originPart2 = origin[origin.length-1];
        String destinationPart1 = destination[0],destinationPart2 = destination[destination.length-1];

        Assertion.assertThat("is yellow banner contains destination location",yellowBannerText,is(either(containsString(originPart1)).or(containsString(originPart2)).or(containsString(destinationPart1)).or(containsString(destinationPart2))));
    }
}
