
# Minesweeper Game Client API

Client API built in Python to play the Minesweeper game

## Installation
* Clone the repository
* Go to client_api folder
* Create virtualenv and install dependencies:
```
$ virtualenv -p python3 venv
$ . venv/bin/activate
$ pip install -r requirements.txt
```
## Description

The client API provides the following methods to play a Minesweeper game

```
# Instantiates the client to connect to the Game server (serverl_url)
MinesweeperClient(server_url)

# Registers a new user
register(username, password)

# Registered user login, which creates a new session. This is a required step before start playing.
login(username, password) 

# Start a new game
new_game(rows=10, columns=10, mines=10)-> game:

# Get a game given its id
get_game(self, id) -> game:

# Uncover a cell
uncover(game, row, column) -> game:

# Set red flag on a cell
set_red_flag(game, row, column) -> game:

# Remove red flag from a cell
remove_red_flag(game, row, column) -> game:

# Set question mark on a cell
set_question_mark(game, row, column) -> game:

# Remove question mark from a cell
remove_question_mark(game, row, column) -> game:

```

## Sample usage of the API

The demo server is deployed on Heroku on http://minesweeper-md.herokuapp.com, then this is the url that must be used when creating a client instance.

```
$python3
```
```
>>> from minesweeper_client import MinesweeperClient
>>> client = MinesweeperClient('http://minesweeper-md.herokuapp.com')
>>> client.register("user", "password")  # user registration needs to be done only once
>>> client.login("user", "password")
>>> game = client.new_game()
>>> game.print()
```
````
Game: Started
Time: 0 secs
╭───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───╮
│ - │ 0 │ 1 │ 2 │ 3 │ 4 │ 5 │ 6 │ 7 │ 8 │ 9 │
├───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┤
│ 0 │ - │ - │ - │ - │ - │ - │ - │ - │ - │ - │
├───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┤
│ 1 │ - │ - │ - │ - │ - │ - │ - │ - │ - │ - │
├───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┤
│ 2 │ - │ - │ - │ - │ - │ - │ - │ - │ - │ - │
├───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┤
│ 3 │ - │ - │ - │ - │ - │ - │ - │ - │ - │ - │
├───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┤
│ 4 │ - │ - │ - │ - │ - │ - │ - │ - │ - │ - │
├───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┤
│ 5 │ - │ - │ - │ - │ - │ - │ - │ - │ - │ - │
├───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┤
│ 6 │ - │ - │ - │ - │ - │ - │ - │ - │ - │ - │
├───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┤
│ 7 │ - │ - │ - │ - │ - │ - │ - │ - │ - │ - │
├───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┤
│ 8 │ - │ - │ - │ - │ - │ - │ - │ - │ - │ - │
├───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┤
│ 9 │ - │ - │ - │ - │ - │ - │ - │ - │ - │ - │
╰───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───╯
````
````
>>> game = client.uncover(game, 2, 3)
>>> game = client.uncover(game, 4, 5)
````
````
Game: Started
Time: 26 secs
╭───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───╮
│ - │ 0 │ 1 │ 2 │ 3 │ 4 │ 5 │ 6 │ 7 │ 8 │ 9 │
├───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┤
│ 0 │ - │ - │ - │ - │ 1 │ 0 │ 0 │ 1 │ - │ - │
├───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┤
│ 1 │ - │ - │ - │ - │ 1 │ 0 │ 0 │ 1 │ - │ - │
├───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┤
│ 2 │ - │ - │ 2 │ 1 │ 1 │ 0 │ 0 │ 1 │ - │ - │
├───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┤
│ 3 │ - │ - │ 2 │ 0 │ 0 │ 0 │ 1 │ 1 │ - │ - │
├───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┤
│ 4 │ - │ - │ 1 │ 0 │ 0 │ 0 │ 1 │ - │ - │ - │
├───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┤
│ 5 │ - │ 3 │ 1 │ 0 │ 0 │ 0 │ 1 │ 1 │ 2 │ - │
├───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┤
│ 6 │ 1 │ 1 │ 0 │ 0 │ 0 │ 0 │ 0 │ 0 │ 1 │ - │
├───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┤
│ 7 │ 0 │ 0 │ 0 │ 0 │ 0 │ 0 │ 0 │ 0 │ 1 │ - │
├───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┤
│ 8 │ 0 │ 1 │ 1 │ 1 │ 0 │ 0 │ 0 │ 0 │ 1 │ - │
├───┼───┼───┼───┼───┼───┼───┼───┼───┼───┼───┤
│ 9 │ 0 │ 1 │ - │ 1 │ 0 │ 0 │ 0 │ 0 │ 1 │ - │
╰───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───╯
````
````
>>> game = client.set_question_mark(game, 9, 9)
>>> game = client.set_question_mark(game, 8, 9)
>>> game = client.set_red_flag(game, 1, 3)
````
````
Game: Started
Time: 56 secs
╭───┬───┬───┬────┬────┬───┬───┬───┬───┬───┬────╮
│ - │ 0 │ 1 │ 2  │ 3  │ 4 │ 5 │ 6 │ 7 │ 8 │ 9  │
├───┼───┼───┼────┼────┼───┼───┼───┼───┼───┼────┤
│ 0 │ - │ - │ -  │ -  │ 1 │ 0 │ 0 │ 1 │ - │ -  │
├───┼───┼───┼────┼────┼───┼───┼───┼───┼───┼────┤
│ 1 │ - │ - │ -  │ 🚩 │ 1 │ 0 │ 0 │ 1 │ - │ -  │
├───┼───┼───┼────┼────┼───┼───┼───┼───┼───┼────┤
│ 2 │ - │ - │ 2  │ 1  │ 1 │ 0 │ 0 │ 1 │ - │ -  │
├───┼───┼───┼────┼────┼───┼───┼───┼───┼───┼────┤
│ 3 │ - │ - │ 2  │ 0  │ 0 │ 0 │ 1 │ 1 │ - │ -  │
├───┼───┼───┼────┼────┼───┼───┼───┼───┼───┼────┤
│ 4 │ - │ - │ 1  │ 0  │ 0 │ 0 │ 1 │ - │ - │ -  │
├───┼───┼───┼────┼────┼───┼───┼───┼───┼───┼────┤
│ 5 │ - │ 3 │ 1  │ 0  │ 0 │ 0 │ 1 │ 1 │ 2 │ -  │
├───┼───┼───┼────┼────┼───┼───┼───┼───┼───┼────┤
│ 6 │ 1 │ 1 │ 0  │ 0  │ 0 │ 0 │ 0 │ 0 │ 1 │ -  │
├───┼───┼───┼────┼────┼───┼───┼───┼───┼───┼────┤
│ 7 │ 0 │ 0 │ 0  │ 0  │ 0 │ 0 │ 0 │ 0 │ 1 │ -  │
├───┼───┼───┼────┼────┼───┼───┼───┼───┼───┼────┤
│ 8 │ 0 │ 1 │ 1  │ 1  │ 0 │ 0 │ 0 │ 0 │ 1 │ ❓ │
├───┼───┼───┼────┼────┼───┼───┼───┼───┼───┼────┤
│ 9 │ 0 │ 1 │ 🚩 │ 1  │ 0 │ 0 │ 0 │ 0 │ 1 │ ❓ │
╰───┴───┴───┴────┴────┴───┴───┴───┴───┴───┴────╯
````
````
>>> game = client.uncover(game, 2, 1)
````
````
Game: Lost
Time: 65 secs
╭───┬────┬────┬────┬────┬───┬───┬───┬────┬────┬────╮
│ - │ 0  │ 1  │ 2  │ 3  │ 4 │ 5 │ 6 │ 7  │ 8  │ 9  │
├───┼────┼────┼────┼────┼───┼───┼───┼────┼────┼────┤
│ 0 │ 0  │ 0  │ 1  │ 1  │ 1 │ 0 │ 0 │ 1  │ 1  │ 1  │
├───┼────┼────┼────┼────┼───┼───┼───┼────┼────┼────┤
│ 1 │ 1  │ 1  │ 2  │ 💣 │ 1 │ 0 │ 0 │ 1  │ 💣 │ 1  │
├───┼────┼────┼────┼────┼───┼───┼───┼────┼────┼────┤
│ 2 │ 1  │ 💣 │ 2  │ 1  │ 1 │ 0 │ 0 │ 1  │ 1  │ 1  │
├───┼────┼────┼────┼────┼───┼───┼───┼────┼────┼────┤
│ 3 │ 3  │ 3  │ 2  │ 0  │ 0 │ 0 │ 1 │ 1  │ 1  │ 0  │
├───┼────┼────┼────┼────┼───┼───┼───┼────┼────┼────┤
│ 4 │ 💣 │ 💣 │ 1  │ 0  │ 0 │ 0 │ 1 │ 💣 │ 2  │ 1  │
├───┼────┼────┼────┼────┼───┼───┼───┼────┼────┼────┤
│ 5 │ 💣 │ 3  │ 1  │ 0  │ 0 │ 0 │ 1 │ 1  │ 2  │ 💣 │
├───┼────┼────┼────┼────┼───┼───┼───┼────┼────┼────┤
│ 6 │ 1  │ 1  │ 0  │ 0  │ 0 │ 0 │ 0 │ 0  │ 1  │ 1  │
├───┼────┼────┼────┼────┼───┼───┼───┼────┼────┼────┤
│ 7 │ 0  │ 0  │ 0  │ 0  │ 0 │ 0 │ 0 │ 0  │ 1  │ 1  │
├───┼────┼────┼────┼────┼───┼───┼───┼────┼────┼────┤
│ 8 │ 0  │ 1  │ 1  │ 1  │ 0 │ 0 │ 0 │ 0  │ 1  │ 💣 │
├───┼────┼────┼────┼────┼───┼───┼───┼────┼────┼────┤
│ 9 │ 0  │ 1  │ 💣 │ 1  │ 0 │ 0 │ 0 │ 0  │ 1  │ 1  │
╰───┴────┴────┴────┴────┴───┴───┴───┴────┴────┴────╯
````



