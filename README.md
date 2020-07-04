# Rock Paper Scissors

## Requirements
- Java 14
- [npm](https://www.npmjs.com)

## Getting Started

Run backend application:

```
cd backend
./mvnw clean install
./mvnw spring-boot:run
```

Once it's built the service can be also ran like this:

```
java -jar target/backend-1.0.0.jar
```

Run frontend application:

```
cd frontend
npm install
npm start
```

The ReactJS app will be available at http://localhost:3000

## API Usage

### Play Round

```
curl -v -X POST localhost:8080/play
```

- Response example:

```
HTTP/1.1 201
Set-Cookie: JSESSIONID=D261498DD4F95C595FD2E5908BAA0C85; Path=/; HttpOnly
```

### Get Rounds Summary

```
curl -v -X GET localhost:8080/rounds-summary
```

- Response example:

```
[
   {
      "roundId":"5e9b467d-c84c-46fd-98c0-efa00e3dfee1",
      "playerOneChoice":"SCISSORS",
      "playerTwoChoice":"ROCK",
      "result":"PLAYER_2_WINS",
      "cleared":false
   },
   {
      "roundId":"d511a90b-9193-46a2-9c4b-37f6a05cbc86",
      "playerOneChoice":"PAPER",
      "playerTwoChoice":"ROCK",
      "result":"PLAYER_1_WINS",
      "cleared":false
   },
   {
      "roundId":"c2320972-2d7a-4835-a116-dae8aeb41fd7",
      "playerOneChoice":"ROCK",
      "playerTwoChoice":"ROCK",
      "result":"DRAW",
      "cleared":false
   }
]
```

```
HTTP/1.1 200
Content-Type: application/json
Set-Cookie: JSESSIONID=EE280D5754BBE866026F53A11AF7BD30; Path=/; HttpOnly
```

### Restart game

```
curl -v -X DELETE localhost:8080/restart
```

- Response example:

```
HTTP/1.1 204
Set-Cookie: JSESSIONID=1C036019BB1FF1BA7DCA3659521AA4E3; Path=/; HttpOnly
```

### Get All Games Summary

```
curl -v -X GET localhost:8080/summary
```

- Response example:

```
{
   "totalRoundsPlayed":15,
   "totalWinsFirstPlayers":2,
   "totalWinsSecondPlayers":9,
   "totalDraws":4
}
```

```
HTTP/1.1 200
Content-Type: application/json
```

## Running Tests

```
cd backend
./mvnw clean test
```
