**Get a game**
----
  REST endpoint to get an existing game given its id
  
* **URL**

  `/minesweeper/:gameId`

* **Method:**
  
  `GET`
  

* **Success Response:**
  
  The game information will be returned, including its id, status, duration (in secs) and board

  **Code:** 200 <br />
  **Content:**
  
  The game information consisting on:
  
  * *id*: unique identifier of the game (UUID)
  * *status*: status of the game. Values = Started|Won|Lost
  * *duration*: time since the game started in seconds
  * *board*: matrix representing the board where each element of the cells array corresponds to a row.
    * "C" (Covered) | "F" (Red flag) | "Q" (Question Mark) | "M" (Mine uncovered) | [0-8] (Cell uncovered without mine)
  
  Example
  
  ```json 
  {
  "id": "8439d287-2d61-4dd5-92c6-c487e9ceb4e7",
  "status": "Started",
  "duration": 120,
  "board": {
    "cells": [
      ["C", "C", "0"],
      ["C", "F", "C"],
      ["R", "C", "C"]
      ]
    }
  }
  ```
 
* **Error Response:**

  **Code:** 404 NOT_FOUND <br />
  **Content:**
  
  ```json
  {
    "timestamp": "16:32:28.09",
    "status": 404,
    "error": "Game not found"
  }
  ```

* **Sample Call:**

  ```curl -X GET http://localhost:8080/minesweeper/8439d287-2d61-4dd5-92c6-c487e9ceb4e7```

