package de.dyncloud.dyncloud.cloudwrapper;

/**
 * Created by Alexander on 14.04.2017.
 */
public class CloudWrapper {

    public static CloudWrapper instance;
    public String cloudPath = "/home/CloudSystem/TestBungee/plugins/DynCloud/";
    public static void main(String[] args) {
        instance = new CloudWrapper();
    }

    public static CloudWrapper getWrapper() {
        return instance;
    }
}
