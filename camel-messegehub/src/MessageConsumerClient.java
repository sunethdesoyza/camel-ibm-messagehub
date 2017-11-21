import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MessageConsumerClient {

    private static final Logger LOG = LoggerFactory.getLogger(MessageConsumerClient.class);

    private MessageConsumerClient() {
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

                from("kafka:dev-default-topic?brokers=kafka01-prod02.messagehub.services.us-south.bluemix.net:9093,kafka02-prod02.messagehub.services.us-south.bluemix.net:9093,kafka03-prod02.messagehub.services.us-south.bluemix.net:9093,kafka04-prod02.messagehub.services.us-south.bluemix.net:9093,kafka05-prod02.messagehub.services.us-south.bluemix.net:9093"
                        + "&saslMechanism=PLAIN"
                        + "&securityProtocol=SASL_SSL"
                        + "&sslProtocol=TLSv1.2"
                        + "&sslEnabledProtocols=TLSv1.2"
                        + "&sslEndpointAlgorithm=HTTPS"
                        + "&saslJaasConfig=org.apache.kafka.common.security.plain.PlainLoginModule required username=\"gMv6eTagAteHkcof\" password=\"yLWapiHkJ7bxtAxHvMJILLHLSw8GOR4L\";"
                        + "&groupId=mygroup")//.to("stream:out")
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
