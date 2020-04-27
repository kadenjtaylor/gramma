package gramma.app;

import gramma.dropwizard.GrammaRestServer;

public class App {

    public static void main(String[] args) throws Exception {
        basicRun();
    }

    private static void runWithArgs(String[] args) throws Exception {
        new GrammaRestServer().run(args);
    }

    private static void runWithConfig(String configFilePath) throws Exception {
        String [] args = new String[] {"server", configFilePath};
        new GrammaRestServer().run(args);
    }

    private static void basicRun() throws Exception {
        runWithConfig("src/main/resources/dropwizard-config.json");
    }
}