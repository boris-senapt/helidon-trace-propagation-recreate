package uk.co.borismorris;

import io.helidon.common.context.Contexts;
import io.helidon.webserver.http.HttpRules;
import io.helidon.webserver.http.HttpService;
import io.helidon.webserver.http.ServerRequest;
import io.helidon.webserver.http.ServerResponse;
import io.opentelemetry.context.Context;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PingServiceOtel implements HttpService {
    private static final Logger LOGGER = LogManager.getLogger(PingService.class);

    private final ExecutorService executor = Contexts.wrap(Context.taskWrapping(Executors.newVirtualThreadPerTaskExecutor()));

    @Override
    public void routing(final HttpRules httpRules) {
        httpRules.get("/ping-async", this::pingAsync);
    }

    private void pingAsync(final ServerRequest serverRequest, final ServerResponse serverResponse) {
        LOGGER.info("Async ping request received");
        executor.submit(() -> {
            LOGGER.info("Running async ping request");
        });
        serverResponse.send("pong");
    }

    @Override
    public void afterStop() {
        executor.shutdownNow();
    }
}
