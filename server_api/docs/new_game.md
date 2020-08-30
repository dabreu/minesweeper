**Start a new game**
----
  REST endpoint to start a new game. The size of the board can be configured via data params as well as the number of mines

* **URL**

  `/minesweeper`

* **Method:**
  
  `POST`
  

* **Data Params**

  **Required:**
 
  Params are not mandatory, if data is not sent default values apply *(rows=10, columns=10, mines=10)* 

  **Optional:**
 
  `rows=[integer]`
 
  `columns=[integer]`
  
  `mines=[integer]`
  

* **Success Response:**
  
  Creates a new game and returns its information, including the id, status, duration (in secs) and board

  **Code:** 200 <br />
  **Content:**
  
  ```json 
  {
  "id": "1da908de-fcac-422d-aeb9-64980ea7d053",
  "status": "Started",
  "duration": 0,
  "board": {
    "cells": [
      ["C", "C", "C"],
      ["C", "C", "C"],
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
    "error": "invalid columns number: -10"
  }
  ```

* **Sample Call:**

  ```curl -X POST -d rows=8 -d columns=8 -d mines=10 http://localhost:8080/minesweeper```

