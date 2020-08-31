**User login **
----
  REST endpoint to login a user so it can start playing

* **URL**

  `/minesweeper/login`

* **Method:**
  
  `POST`
  
* **Header required:**
  
  `No`
  
* **Data Params**

  **Required:**
 
  `username=[string]`
 
  `password=[string]`
  

* **Success Response:**
  
  Login the user and returns the token which must be set on the `Authorization` header on the playing endpoints

  **Code:** 200 <br />

  The token to be used on the `Authorization` header
  
  Example
  
  ```json
  {
    "token": "H7CmoymwqR20XfTAyGLBAO9XAwS1VDxN"
  }
  ```
  
* **Error Response:**

  **Code:** 401 UNAUTHORIZED <br />
  **Content:**
  
  ```json
  {
    "timestamp": "10:00:57.178",
    "status": 401,
    "error": null
  }
  ```

* **Sample Call:**

  ```curl -X POST -d username=testuser -d password=testpassword http://localhost:8080/minesweeper/login```

