package com.wgmanager.app;

public class Server {
    private String name;
    private String location;
    private String countryCode;
    private boolean connected;

    public Server(String name, String location, String countryCode) {
        this.name = name;
        this.location = location;
        this.countryCode = countryCode;
        this.connected = false;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getCountryCode() { return countryCode; }
    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }
    public boolean isConnected() { return connected; }
    public void setConnected(boolean connected) { this.connected = connected; }
}
