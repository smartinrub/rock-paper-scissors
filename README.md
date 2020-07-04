# Rock Paper Scissors

## Requirements
- Java 14

## Getting Started

Run application:

```
./mvnw spring-boot:run
```

### Endpoints

Play Round:

```
curl -v -X POST localhost:8080/play
```

Get Rounds Summary:

```
curl -v -X GET localhost:8080/rounds-summary
```

Restart game:

```
curl -v -X DELETE localhost:8080/restart
```

Get All Games Summary:

```
curl -v -X GET localhost:8080/summary
```

## Running Tests

```
./mvnw clean test
```
