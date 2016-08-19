# TransactionService

A simple RESTFull service for storing and reading transactions. A simple example is:

    PUT /transactionservice/transaction/10 { "amount": 5000, "type": "cars" }
    => { "status": "ok" }
    PUT /transactionservice/transaction/11 { "amount": 10000, "type": "shopping", "parent_id": 10 }
    => { "status": "ok" }
    GET /transactionservice/types/cars 
    => [10]
    GET /transactionservice/sum/10
    => {"sum":15000}
    GET /transactionservice/sum/11
    => {"sum":10000} 

## Requirements

* Java 8

## Build and Run

From the TransactionService directory run the gradel build script:

* gradlew build

You can then run TransactionService:

* java -jar build/libs/gs-rest-service-0.1.0.jar

This will start TransactionService on http://localhost:8080/transactionservice

## Tests

There are some JUnit unit tests in the 'src\test' directory.

No integration tests yet.

## Usage

To interact with the serice use a REST client such as Postman (https://www.getpostman.com/). You can then use PUT and GET to retrieve transactions. 

### PUT Transaction

    PUT /transactionservice/transaction/$transaction_id
    Body:
    { "amount":double,"type":string,"parent_id":long }
    where:
    transaction_id is a long specifying a new transaction
    amount is a double specifying the amount
    type is a string specifying a type of the transaction.
    parent_id is an optional long that may specify the parent transaction of this transaction. 
    Returns:
    { "status": "ok" }
    
### GET Transaction

    GET /transactionservice/transaction/$transaction_id
    Returns:
    { "amount":double,"type":string,"parent_id":long } 

### GET A json list of all transaction ids that share the same type $type

    GET /transactionservice/types/$type
    Returns:
    [ long, long, .... ] 

### GET A sum of all transactions that are transitively linked by their parent_id to $transaction_id

    GET /transactionservice/sum/$transaction_id
    Returns
    { "sum", double }
