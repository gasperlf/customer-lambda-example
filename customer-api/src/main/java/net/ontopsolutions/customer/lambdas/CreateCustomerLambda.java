package net.ontopsolutions.customer.lambdas;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import net.ontopsolutions.customer.dto.Customer;
import net.ontopsolutions.customer.utils.UtilityJackson;

import java.util.UUID;

public class CreateCustomerLambda {

    private final DynamoDB dynamoDB = new DynamoDB(AmazonDynamoDBClientBuilder.defaultClient());

    public APIGatewayProxyResponseEvent createCustomer(APIGatewayProxyRequestEvent requestEvent) throws JsonProcessingException {

        Customer customer = UtilityJackson.parser(requestEvent.getBody(), Customer.class);
        customer.setId(UUID.randomUUID().toString());
        Table customerTable = dynamoDB.getTable(System.getenv("CUSTOMERS_TABLE"));
        Item item = new Item()
                .withPrimaryKey("id", customer.getId())
                .with("firstName", customer.getFirstName())
                .with("lastName", customer.getLastName())
                .with("rewardPoints", customer.getRewardPoints());

        customerTable.putItem(item);

        return new APIGatewayProxyResponseEvent().withStatusCode(200)
                .withBody(UtilityJackson.parserString(customer));
    }
}
