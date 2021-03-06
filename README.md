# Kalah Game

This is a *Java RESTful Web Service*  that runs a game of 6-stone Kalah. The general rules
of the game are explained on Wikipedia: https://en.wikipedia.org/wiki/Kalah


#### Kalah Rules
Each of the two players has ​ **​ six pits​ ** ​ in front of him/her. To the right of the six pits, each player has a larger pit, his
Kalah or house.
At the start of the game, six stones are put in each pit.
The player who begins picks up all the stones in any of their own pits, and sows the stones on to the right, one in
each of the following pits, including his own Kalah. No stones are put in the opponent's' Kalah. If the players last
stone lands in his own Kalah, he gets another turn. This can be repeated any number of times before it's the other
player's turn.
When the last stone lands in an own empty pit, the player captures this stone and all stones in the opposite pit (the
other players' pit) and puts them in his own Kalah.
The game is over as soon as one of the sides run out of stones. The player who still has stones in his/her pits keeps
them and puts them in his/hers Kalah. The winner of the game is the player who has the most stones in his Kalah.

# Getting Started

### Prerequisites
* Java  - 1.8
* Docker - 20.10.2
* Docker-compose - 1.17.1
* Maven - 3.5.4

### How to run

Go to service mkdir
```
./mvnw clean install
```

Go to docker mkdir
```
docker-compose build
```
```
docker-compose up
```

# Swagger api

http://localhost:8080/swagger-ui.html#/
