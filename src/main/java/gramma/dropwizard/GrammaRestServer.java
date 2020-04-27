package gramma.dropwizard;


import gramma.impl.ActionDrivenMutantGraph;
import gramma.impl.Mutagens;
import gramma.impl.SimpleGrammar;
import gramma.app.Workspace;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class GrammaRestServer extends Application<GrammaServerConfiguration> {

    public GrammaRestServer() {

    }

    @Override
    public void run(GrammaServerConfiguration configuration, Environment environment) {

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