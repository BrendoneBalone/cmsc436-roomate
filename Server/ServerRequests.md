# Server Request Syntax

## Link
The link/url will be created later. All requests should be sent to URL+some route.
Ex. `someurl.com/room'

We should have a private const variable with the URL.

## Kotlin Syntax (Not yet tested!!)
To send a post req:
```
try {

     val httpclient:HttpClient=DefaultHttpClient()
     val httppost: HttpPost =HttpPost(URL)

     // nameValuePairs can be an ArrayList<>
     // This is how you send data to the server
     httppost.entity = UrlEncodedFormEntity(nameValuePairs)

     val httpresponse:HttpResponse=httpclient.execute(httppost)
     val httpentity: HttpEntity =httpresponse.entity

     // To check response code
     httpentity.code

 }
```

The input data should be sent as a JSON in a GET or POST request. The output of the request should also be a JSON.

## Routes
These are the routes implemented and their syntax.

### /login
- POST Request
    - Purpose: Verifying login information. Adding username to data if not present.
    - Input JSON: {"roomcode": roomcode, "username": username} 
        - ex: {"roomcode": H256IQ, "username": "Daniel"}
    - Output: {"roomcode": roomcode}, Response code 200
    - Error Output: Response code 400
        - Nonexistent roomcode

### /new_room_code
- GET Request
    - Purpose: Generates a new, valid roomcode.
    - Input: None
    - Output: {"roomcode": roomcode}, Response code 200
    - Error: Shouldn't happen

### /grocery
- GET Request
    - Purpose: Returns a dict with all grocery information
    - Input JSON: {"roomcode": roomcode}
        - ex: {"roomcode": H256IQ}
    - Output: {groceryName: {"completed": completedBool}}, Response code 200
        - ex: {"eggs": {"completed": "false"}, "milk": {"completed": "true"}}
        - I'm pretty sure bools are "true" and "false" in JSON
    - Error: Response code 400
        - Nonexistent roomcode

### /grocery/add
- POST Request
    - Purpose: Adds a grocery item to data. Sets it's complete value to false.
    - Input JSON: {"roomcode": roomcode, "groceryName", groceryName}
        - ex: {"roomcode": H256IQ, "groceryName": "eggs"}
    - Output: {groceryName: {"completed": completedBool}}, Response code 200
        - ex: {"eggs": {"completed": "false"}, "milk": {"completed": "true"}}
        - Updated grocery list
    - Error: Response code 400
        - Nonexistent roomcode
    - Edgecase?:
        - If an existing grocery item is sent, nothing happens and the response code will be 200

### /grocery/complete
- POST Request
    - Purpose: Sets a grocery item's complete value to true.
    - Input JSON: {"roomcode": roomcode, "groceryName", groceryName}
        - ex: {"roomcode": H256IQ, "groceryName": "eggs"}
    - Output: {groceryName: {"completed": completedBool}}, Response code 200
        - ex: {"eggs": {"completed": "false"}, "milk": {"completed": "true"}}
        - Updated grocery list
    - Error: Response code 400
        - Nonexistent roomcode
        - Nonexistent grocery item

### /grocery/uncomplete
- POST Request
    - Purpose: Sets a grocery item's complete value to false.
    - Input JSON: {"roomcode": roomcode, "groceryName", groceryName}
        - ex: {"roomcode": H256IQ, "groceryName": "eggs"}
    - Output: {groceryName: {"completed": completedBool}}, Response code 200
        - ex: {"eggs": {"completed": "false"}, "milk": {"completed": "true"}}
        - Updated grocery list
    - Error: Response code 400
        - Nonexistent roomcode
        - Nonexistent grocery item

### /roommates
- GET Request
    - Input JSON: {"roomcode": roomcode}
        - ex: {"roomcode": H256IQ}
    - Output: {"roommates": [roommateName]}, Response code 200
        - ex: {"roommates": ["Daniel", "Logan", "Brendan"]}
    - Error: Response code 400
        - Nonexistent roomcode