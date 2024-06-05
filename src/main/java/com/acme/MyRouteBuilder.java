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
                .log("London message: ${body}")
                .convertBodyTo(String.class)
                .to("rabbitmq:london?connectionFactory=#connectionFactory").id("toUKQueue")
                .log(">>> Message sent to London Queue: ${body}")
            .otherwise()
                .log("${body}")
                .unmarshal().jacksonXml(Person.class)
                .process(this.myProcessor)
                .log("Other message: ${body}")
                .convertBodyTo(String.class)
                .to("rabbitmq:others?connectionFactory=#connectionFactory").id("toOthersQueue")
                .log(">>> Message sent to Others Queue: ${body}")
            .end();

    from("rabbitmq:london?connectionFactory=#connectionFactory").id("fromUK")
        .log("<<< Message received from London: ${body}");

    from("rabbitmq:others?connectionFactory=#connectionFactory").id("fromOthers")
        .log("<<< Message received from Others: ${body}");
    }

}
