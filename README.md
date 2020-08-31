# Minesweeper

APIs to play the Minesweeper game

* The details of the REST API for the game can be found on the [Server API documentation](server_api/README.md)
* The details of a Python client API to play the game can be found on the [Client API documentation](client_api/README.md)

## Notes

### Server RESTful API

* The server RESTful API was developed in Java, using Spring Boot.
* The API supports creating a new game as well as getting an existing one to resume playing. 
* The game's logic supports uncovering cells, setting red flags and question marks (and removing them). 
* When a cell is uncovered, or a red flag/question mark is set (or removed), the changes are persisted and the modified game is returned.
* The game's data returned contains its id, status, duration (secs) and board's state. The status allows determining whether the game is over. The duration allows time tracking.
* The persistence was implemented using MongoDB. The Game information is persisted on a collection called *game*
* The API supports multiple users/accounts. To accomplish that the request must provide an *Authorization* header set with a valid session token
* A valid session token is generated when a registered user logs in. A REST endpoint is provided for that
* Also, an endpoint to register users is provided.
* The demo server is deployed on Heroku on http://minesweeper-md.herokuapp.com
* The database is currently deployed on MongoDB Atlas. This configuration can be changed on the  *application.properties* file

### Client API

* The client API was developed using Python.
* A couple of libraries were included to handle requests and better display the game board on the console 
* The instructions to install the client and run it can be found on [Client API documentation](client_api/README.md)


### TODO's

* Add endpoint to support user logout
* Support/handle token's expiration
* Add endpoint to return the list of games of a given user
* Enhance error handling
* Add more integration tests
