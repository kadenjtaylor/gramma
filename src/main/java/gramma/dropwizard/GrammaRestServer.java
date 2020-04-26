package gramma.dropwizard;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class GrammaRestServer extends Application<GrammaServerConfiguration> {

    @Override
    public void run(GrammaServerConfiguration configuration, Environment environment){

        final GrammaServerHealthCheck healthCheck = new GrammaServerHealthCheck();
        environment.healthChecks().register("test", healthCheck);

        
        final GrammaServerResource resource = new GrammaServerResource();
        environment.jersey().register(resource);
    }

}