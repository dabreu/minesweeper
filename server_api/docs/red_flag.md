**Sets/removes a red flag on a cell of a game**
----
  REST endpoint to set or remove a red flag from a cell of a given game

* **URL**

  `/minesweeper/:gameId/red_flag`

* **Method:**
  
  `PUT` | `DELETE`
  

* **Data Params**

  **Required:**
 
  `row=[integer]. Values in [0, rows-1]` 
 
  `column=[integer]. Values in [0, columns-1]`
  

* **Success Response:**
  
  Put method sets the red flag on a cell. Delete method removes the red flag from the cell. Both methods return the game information
  
  **Code:** 200 <br />
  **Content:**
  
  ```json 
  {
  "id": "8439d287-2d61-4dd5-92c6-c487e9ceb4e7",
  "status": "Started",
  "duration": 60,
  "board": {
    "cells": [
      ["C", "C", "C"],
      ["C", "2", "F"],
      ["C", "C", "C"]
      ]
    }
  }
  ```
 
* **Error Response:**

  **Code:** 400 BAD_REQUEST <br />
  **Content:**
  
  ```json
  {
    "timestamp": "16:32:28.09",
    "status": 400,
    "error": "action not allowed, the game is finished"
  }
  ```
  
  Or
  
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

  Set red flag
  
  ```curl -X PUT -d row=1 -d column=2 http://localhost:8080/minesweeper/8439d287-2d61-4dd5-92c6-c487e9ceb4e7/red_flag```

  Remove red flag
  
  ```curl -X DELETE -d row=1 -d column=2 http://localhost:8080/minesweeper/8439d287-2d61-4dd5-92c6-c487e9ceb4e7/red_flag```
  
* **Note**

  Cells with mine are not required to have a red flag to finish the game.
  