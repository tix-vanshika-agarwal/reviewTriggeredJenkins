package com.tiket.pojo.lambdatest;

import java.util.List;

public class LambdaTestTunnelsResponse {
    public List<TunnelData> data;
    public String message;
    public String status;

    public static class TunnelData {
        public String dns;
        public String email;
        public String name;
        public String local_domains;
        public long org_id;
        public long user_id;
        public String start_timestamp;
        public String status_ind;
        public long tunnel_id;
        public String tunnel_name;
        public boolean shared_tunnel;
        public FolderPath folder_path;
    }

    public static class FolderPath {
        public String String;
        public boolean Valid;
    }
}

