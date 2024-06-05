package com.acme;

import org.apache.camel.builder.RouteBuilder;

/**
 * A Camel Java DSL Router
 */
public class MyRouteBuilder extends RouteBuilder {

    private final MyProcessor myProcessor = new MyProcessor();

    /**
     * Let's configure the Camel routing rules using Java code...
     */
    public void configure() {

        from("file:src/data?noop=true").id("fromDir")
            .choice()
                .when(xpath("/person/city = 'London'"))
                    .log("${body}")
                    .unmarshal().jacksonXml(Person.class)
                    .process(this.myProcessor)
                    .log("UK message: ${body}")
                    .convertBodyTo(String.class)
                    // TODO: Send message to RabbitMQ.
                    .to("???").id("toUK")
                .otherwise()
                    .log("${body}")
                    .unmarshal().jacksonXml(Person.class)
                    .process(this.myProcessor)
                    .log("Other message: ${body}")
                    .convertBodyTo(String.class)
                    // TODO: Send message to RabbitMQ.
                    .to("???").id("toOthers")
            .end()
        ;

        // TODO: Consume from RabbitMQ
        from("???").id("fromUK")
            .log("<<< Received from UK: ${body}");

        from("???").id("fromOthers")
                .log("<<< Received from Others: ${body}");
    }

}
