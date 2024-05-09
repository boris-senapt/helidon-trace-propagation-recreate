package uk.co.borismorris;

import io.helidon.common.context.Contexts;
import io.helidon.config.Config;
import io.helidon.logging.common.LogConfig;
import io.helidon.webserver.WebServer;
import io.opentelemetry.sdk.autoconfigure.AutoConfiguredOpenTelemetrySdk;

import java.util.Collections;

public class Main {
    static {
        setupLogging();
    }

    public static void main(String[] args) throws Throwable {
        LogConfig.configureRuntime();

        final var config = Config.create();
        Config.global(config);
        setupTracing(config);

        WebServer.builder()
                .routing(routing -> routing
                        .register("/", new PingService())
                        .register("/otel", new PingServiceOtel()))
                .config(config.get("server"))
                .build()
                .start();
    }

    private static void setupTracing(final Config config) {
        final var otel = AutoConfiguredOpenTelemetrySdk.builder()
                .addPropertiesSupplier(() -> config.get("otel").asMap().orElseGet(Collections::emptyMap))
                .setResultAsGlobal()
                .build();
        Contexts.globalContext().register(otel);
    }

    /**
     * Configure JUL to Log4j2.
     */
    private static void setupLogging() {
        System.setProperty("java.util.logging.manager", "org.apache.logging.log4j.jul.LogManager");
    }
}
