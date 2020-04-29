package gramma.dropwizard;

import gramma.impl.ActionDrivenMutantGraph;
import gramma.impl.Mutagens;
import gramma.impl.SimpleGrammar;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;

import org.eclipse.jetty.servlets.CrossOriginFilter;

import gramma.app.Workspace;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class GrammaRestServer extends Application<GrammaServerConfiguration> {

    public GrammaRestServer() {

    }

    @Override
    public void run(GrammaServerConfiguration configuration, Environment environment) {

        // Enable CORS headers
        final FilterRegistration.Dynamic cors =
        environment.servlets().addFilter("CORS", CrossOriginFilter.class);

        // Configure CORS parameters
        cors.setInitParameter("allowedOrigins", "*");
        cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
        cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");

        // Add URL mapping
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");


        final Workspace workspace = new Workspace(
            new ActionDrivenMutantGraph(),
            SimpleGrammar.of(
                Mutagens.ifEmptyNewNode("Message"),
                Mutagens.ifNodeWithValueSpawnChild("Message", "Reaction"),
                Mutagens.ifNodeWithValueSpawnParent("Message", "Message")
            )
        );

        final GrammaServerHealthCheck healthCheck = new GrammaServerHealthCheck();
        environment.healthChecks().register("test", healthCheck);

        final GrammaServerResource resource = new GrammaServerResource(workspace);
        environment.jersey().register(resource);
    }
}