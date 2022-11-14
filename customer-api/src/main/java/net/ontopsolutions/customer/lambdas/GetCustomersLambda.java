package net.ontopsolutions.customer.lambdas;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import net.ontopsolutions.customer.dto.Customer;
import net.ontopsolutions.customer.utils.UtilityJackson;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GetCustomersLambda {
    private final AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.defaultClient();

    public APIGatewayProxyResponseEvent getCustomers(APIGatewayProxyRequestEvent requestEvent) throws JsonProcessingException {

        String tableName = System.getenv("CUSTOMERS_TABLE");

        ScanResult ordersTable = dynamoDB.scan(new ScanRequest().withTableName(tableName));
        List<Customer> orderList = ordersTable.getItems()
                .stream()
                .map(this::mapperCustomer )
                .collect(Collectors.toList());

        String jsonString = UtilityJackson.parserString(orderList);

        return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(jsonString);
    }

    private Customer mapperCustomer(Map<String, AttributeValue> item) {
        return new Customer(item.get("id").getS(),
                item.get("firstName").getS(),
                item.get("lastName").getS(),
                Integer.parseInt(item.get("rewardPoints").getN()));


    }
}
