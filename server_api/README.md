# Minesweeper Game REST API

This document describes the available endpoints on the RESTful API for the Minesweeper game

## Endpoints

Endpoints for user registration and login to support multiple users/accounts

* [Register new user](docs/register.md) : `POST /minesweeper/register`
* [Login](docs/login.md) : `POST /minesweeper/login`
* Logout: *Not yet implemented*

Endpoints for playing the Minesweeper game

* [Start new game](docs/new_game.md) : `POST /minesweeper`
* [Get game](docs/get_game.md) : `GET /minesweeper/:gameId/`
* [Uncover cell](docs/uncover.md) : `PUT /minesweeper/:gameId/uncover`
* [Set red flag on cell](docs/red_flag.md) : `PUT /minesweeper/:gameId/red_flag`
* [Remove red flag from cell](docs/red_flag.md) : `DELETE /minesweeper/:gameId/red_flag`
* [Set question on cell](docs/question_mark.md) : `PUT /minesweeper/:gameId/question_mark`
* [Remove question from cell](docs/question_mark.md) : `DELETE /minesweeper/:gameId/question_mark`

## Demo Server

The demo server is deployed and running on Heroku on *http://minesweeper-md.herokuapp.com*. 

The steps to deploy it are:

1. Create a Heroku account
2. Install the Heroku CLI
3. Clone the repository
4. `heroku login` to login to Heroku
5. `heroku create <app_name>` provide the app name, i.e. minesweeper
6. `heroku buildpacks:clear` if necessary
7. `heroku buildpacks:set https://github.com/timanovsky/subdir-heroku-buildpack`
8. `heroku buildpacks:add heroku/java`
9. `heroku config:set PROJECT_PATH=server_api` steps 7-9 are required given that the code is present on a subfolder
10. `git push heroku master` to deploy the code


## Local Server

The steps to build and run the server locally are:

1. Clone the repository
2. Cd to directory *server_api*
3. `mvn clean install`
4. `mvn spring-boot:run` to start the server with Maven or `java -jar target/minesweeper-0.0.1-SNAPSHOT.jar` using java cmd
5. The server connects by default to MongoDB Atlas. This can be changed on the *application.properties* file to use a local Mongo database
