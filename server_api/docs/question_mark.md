**Sets/removes a question mark on a cell of a game**
----
  REST endpoint to set or remove a question mark from a cell of a given game

* **URL**

  `/minesweeper/:gameId/question_mark`

* **Method:**
  
  `PUT` | `DELETE`
  
* **Header required:**
  
  `Autorization: <token>`
  
* **Data Params**

  **Required:**
 
  `row=[integer]. Values in [0, rows-1]` 
 
  `column=[integer]. Values in [0, columns-1]`
  

* **Success Response:**
  
  Put method sets the question mark on a cell. Delete method removes the question mark from the cell. Both methods return the game information
  
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
      ["?", "C", "C"]
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
  
  Or
  
  **Code:** 401 UNAUTHORIZED <br />
  **Content:**
  
  ```json
  {
    "timestamp": "10:00:16.295",
    "status": 401,
    "error": "Game does not belong to user"
  }
  ```

* **Sample Call:**

  Set question mark
  
  ```curl -X PUT -d row=1 -d column=2 -H "Authorization: YhfVcwnkBlurA_6hwZSKSyJDYaDXhjfv" http://localhost:8080/minesweeper/8439d287-2d61-4dd5-92c6-c487e9ceb4e7/question_mark```

  Remove question mark
  
  ```curl -X DELETE -d row=1 -d column=2 -H "Authorization: YhfVcwnkBlurA_6hwZSKSyJDYaDXhjfv" http://localhost:8080/minesweeper/8439d287-2d61-4dd5-92c6-c487e9ceb4e7/question_mark```

  