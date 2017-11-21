import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CamelKafkaMessageConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(CamelKafkaMessageConsumer.class);

    private CamelKafkaMessageConsumer() {
    }

    public static void main(String[] args) throws Exception {

        LOG.info("About to run Kafka-camel integration...");

        CamelContext camelContext = new DefaultCamelContext();

        // Add route to send messages to Kafka

        camelContext.addRoutes(new RouteBuilder() {
            public void configure() {
                PropertiesComponent pc = getContext().getComponent("properties", PropertiesComponent.class);
                pc.setLocation("classpath:application.properties");

                log.info("About to start route: Kafka Server -> Log ");

                from("kafka:cptb?brokers=kafka01-prod02.messagehub.services.eu-gb.bluemix.net:9093,kafka02-prod02.messagehub.services.eu-gb.bluemix.net:9093,kafka03-prod02.messagehub.services.eu-gb.bluemix.net:9093,kafka04-prod02.messagehub.services.eu-gb.bluemix.net:9093,kafka05-prod02.messagehub.services.eu-gb.bluemix.net:9093"
                        + "&saslMechanism=PLAIN"
                        + "&securityProtocol=SASL_SSL"
                        + "&sslProtocol=TLSv1.2"
                        + "&sslEnabledProtocols=TLSv1.2"
                        + "&sslEndpointAlgorithm=HTTPS"
                        + "&saslJaasConfig=org.apache.kafka.common.security.plain.PlainLoginModule required username=\"lzMdTcMvMrJzdfnL\" password=\"xH4eWDI051gmIs0lLn3O34QRlP1uMj31\";"
                    // + "&saslJaasConfig=org.apache.kafka.common.security.plain.PlainLoginModule required username=\"ltscrdtthWw284Aj\" password=\"dIgGq0mQSAb4rn8AoaPuk8WtjnfNChWR\";"
                     + "&groupId=mygroup").to("stream:out")
                        .routeId("FromKafka")
                    .log("${body}");
            }
        });
        camelContext.start();
        // let it run for 5 minutes before shutting down
        LOG.info("Started");
        Thread.sleep(5 * 60 * 1000);

        camelContext.stop();
        LOG.info("Stoped");
    }

}
