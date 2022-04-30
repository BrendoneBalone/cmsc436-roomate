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
        - username is empty ""

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

### /grocery/remove
- POST Request
    - Purpose: Removes a grocery item from the data
    - Input JSON: {"roomcode": roomcode, "groceryName", groceryName}
        - ex: {"roomcode": H256IQ, "groceryName": "eggs"}
    - Output: {groceryName: {"completed": completedBool}}, Response code 200
        - ex: {"eggs": {"completed": "false"}, "milk": {"completed": "true"}}
        - Updated grocery list
    - Error: Response code 400
        - Nonexistent roomcode
        - Nonexistent grocery item

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
    
### /chores
- GET Request
    - Purpose: Returns a dict with all chore information
    - Input JSON: {"roomcode": roomcode}
        - ex: {"roomcode": H256IQ}
    - Output: {choreName: {"completed": completedBool, "username": username, "weekday": weekday}}, Response code 200
        - ex: {"cleaning": {"completed": "false", "username": "Daniel", "weekday": "monday"}}
        - I'm pretty sure bools are "true" and "false" in JSON
    - Error: Response code 400
        - Nonexistent roomcode

### /chores/add
- POST Request
    - Purpose: Adds a chore item to data. Sets it's complete value to false, username will be "", and weekday will be "".
    - Input JSON: {"roomcode": roomcode, "choreName", choreName}
        - ex: {"roomcode": H256IQ, "choreName": "cleaning"}
    - Output: {choreName: {"completed": completedBool, "username": username, "weekday": weekday}}, Response code 200
        - ex: {"cleaning": {"completed": "false", "username": "Daniel", "weekday": "monday"}}
        - Updated chores list
    - Error: Response code 400
        - Nonexistent roomcode
    - Edgecase?:
        - If an existing grocery item is sent, nothing happens and the response code will be 200

### /chores/remove
- POST Request
    - Purpose: Removes a chore item from the data
    - Input JSON: {"roomcode": roomcode, "choreName", choreName}
        - ex: {"roomcode": H256IQ , "choreName": "cleaning"}
    - Output: {choreName: {"completed": completedBool, "username": username, "weekday": weekday}}, Response code 200
        - ex: {"cleaning": {"completed": "false", "username": "Daniel", "weekday": "monday"}}
        - Updated chores list
    - Error: Response code 400
        - Nonexistent roomcode
        - Nonexistent grocery item

### /chores/complete
- POST Request
    - Purpose: Sets a chore's complete value to true.
    - Input JSON: {"roomcode": roomcode, "choreName", choreName}
        - ex: {"roomcode": H256IQ , "choreName": "cleaning"}
    - Output: {choreName: {"completed": completedBool, "username": username, "weekday": weekday}}, Response code 200
        - ex: {"cleaning": {"completed": "false", "username": "Daniel", "weekday": "monday"}}
        - Updated chores list
    - Error: Response code 400
        - Nonexistent roomcode
        - Nonexistent grocery item

### /chores/uncomplete
- POST Request
    - Purpose: Sets a chore's complete value to false.
    - Input JSON: {"roomcode": roomcode, "choreName", choreName}
        - ex: {"roomcode": H256IQ , "choreName": "cleaning"}
    - Output: {choreName: {"completed": completedBool, "username": username, "weekday": weekday}}, Response code 200
        - ex: {"cleaning": {"completed": "false", "username": "Daniel", "weekday": "monday"}}
        - Updated chores list
    - Error: Response code 400
        - Nonexistent roomcode
        - Nonexistent grocery item

### /chores/username/add
- POST Request
    - Purpose: Assigns a username to a given chore
    - Input JSON: {"roomcode": roomcode, "choreName", choreName, "username": username}
        - ex: {"roomcode": H256IQ , "choreName": "cleaning", "username": "Daniel"}
    - Output: {choreName: {"completed": completedBool, "username": username, "weekday": weekday}}, Response code 200
        - ex: {"cleaning": {"completed": "false", "username": "Daniel", "weekday": "monday"}}
        - Updated chores list
    - Error: Response code 400
        - Nonexistent roomcode
        - Nonexistent grocery item
        - Nonexistent username (in room's username array)

### /chores/username/remove
- POST Request
    - Purpose: Clears the username field for a given chore
    - Input JSON: {"roomcode": roomcode, "choreName", choreName}
        - ex: {"roomcode": H256IQ , "choreName": "cleaning"}
    - Output: {choreName: {"completed": completedBool, "username": username, "weekday": weekday}}, Response code 200
        - ex: {"cleaning": {"completed": "false", "username": "Daniel", "weekday": "monday"}}
        - Updated chores list
    - Error: Response code 400
        - Nonexistent roomcode
        - Nonexistent grocery item

### /chores/weekday/add
- POST Request
    - Purpose: Assigns a weekday to a given chore
    - Input JSON: {"roomcode": roomcode, "choreName", choreName, "weekday": weekday}
        - ex: {"roomcode": H256IQ , "choreName": "cleaning", "weekday": "monday"}
    - Output: {choreName: {"completed": completedBool, "username": username, "weekday": weekday}}, Response code 200
        - ex: {"cleaning": {"completed": "false", "username": "Daniel", "weekday": "monday"}}
        - Updated chores list
    - Error: Response code 400
        - Nonexistent roomcode
        - Nonexistent grocery item
        - Invalid weekday (should be all lowercase, correct spelling)
            - ex. "monday"

### /chores/weekday/remove
- POST Request
    - Purpose: Clears the weekday field for a given chore
    - Input JSON: {"roomcode": roomcode, "choreName", choreName}
        - ex: {"roomcode": H256IQ , "choreName": "cleaning"}
    - Output: {choreName: {"completed": completedBool, "username": username, "weekday": weekday}}, Response code 200
        - ex: {"cleaning": {"completed": "false", "username": "Daniel", "weekday": "monday"}}
        - Updated chores list
    - Error: Response code 400
        - Nonexistent roomcode
        - Nonexistent grocery item