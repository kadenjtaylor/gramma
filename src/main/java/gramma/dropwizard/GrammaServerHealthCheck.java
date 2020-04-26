package gramma.dropwizard;

import com.codahale.metrics.health.HealthCheck;

public class GrammaServerHealthCheck extends HealthCheck {

    @Override
    protected Result check() throws Exception {
        return Result.healthy();
    }

}