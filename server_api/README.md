# Minesweeper Game REST API

This document describes the available endpoints on the RESTful API for the Minesweeper game

## Endpoints


### Game related

Endpoints for playing the Minesweeper game

* [Start new game](docs/new_game.md) : `POST /minesweeper`
* [Get game](docs/get_game.md) : `GET /minesweeper/:gameId/`
* [Uncover cell](docs/uncover.md) : `PUT /minesweeper/:gameId/uncover`
* [Set red flag on cell](docs/red_flag.md) : `PUT /minesweeper/:gameId/red_flag`
* [Remove red flag from cell](docs/red_flag.md) : `DELETE /minesweeper/:gameId/red_flag`
* [Set question on cell](docs/question_mark.md) : `PUT /minesweeper/:gameId/question_mark`
* [Remove question from cell](docs/question_mark.md) : `DELETE /minesweeper/:gameId/question_mark`

