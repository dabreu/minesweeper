**Registers a new user**
----
  REST endpoint to register a new user

* **URL**

  `/minesweeper/register`

* **Method:**
  
  `POST`
  
* **Header required:**
  
  `No`
  
* **Data Params**

  **Required:**
 
  `username=[string]`
 
  `password=[string]`
  

* **Success Response:**
  
  Creates a new user

  **Code:** 200 <br />

 
* **Error Response:**

  **Code:** 400 BAD_REQUEST <br />
  **Content:**
  
  ```json
  {
    "timestamp": "16:32:28.09",
    "status": 400,
    "error": "invalid username"
  }
  ```
  
  Or
  
  **Code:** 409 CONFLICT <br />
  **Content:**
  
  ```json
  {
    "timestamp": "10:00:16.295",
    "status": 409,
    "error": "User already exists"
  }
  ```

* **Sample Call:**

  ```curl -X POST -d username=testuser -d password=testpassword http://localhost:8080/minesweeper/register```

