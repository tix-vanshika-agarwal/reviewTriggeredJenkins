package com.tiket.pojo.b2b.corporate;

import java.util.List;

public class PostConfigTravelPolicyBody {
    public List<Config> config;

    public static class Config {
        public String name;
        public boolean value;
    }
}
