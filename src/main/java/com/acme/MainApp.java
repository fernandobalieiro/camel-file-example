package com.acme;

import com.rabbitmq.client.ConnectionFactory;
import org.apache.camel.main.Main;

/**
 * A Camel Application
 */
public class MainApp {

    /**
     * A main() so we can easily run these routing rules in our IDE
     */
    public static void main(String... args) throws Exception {
        final var main = new Main();
        main.bind("connectionFactory", connectionFactory());
        main.configure().addRoutesBuilder(new MyRouteBuilder());
        main.run(args);
    }

    private static ConnectionFactory connectionFactory() {
        final var connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        return connectionFactory;
    }

}

