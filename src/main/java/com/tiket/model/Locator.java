package com.tiket.model;

import org.openqa.selenium.By;

import java.util.Objects;

public record Locator(String name, By by) {
    public Locator {
        Objects.requireNonNull(by);
        Objects.requireNonNull(name);
    }

    @Override
    public String toString() {
        return "[" + name + ", " + by + "]";
    }

}
