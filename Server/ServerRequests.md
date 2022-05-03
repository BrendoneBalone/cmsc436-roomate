# Server Request Syntax

## Running the server locally (just for testing)
I'm running python 3.8 with Flask 1.1.2. If you're running into issues try check your version of python and flask.

Steps to test using local server:
1. run application.py
2. run loadData.py (optional if you want to load some test data)
3. Set the 'link' in Kotlin to http://10.0.2.2:5000 (This is the default port) (Use localhost:5000 in your browser)
4. Use GET/POST requests at routes of the link (ex. 10.0.2.2:5000/login)
5. Save data by running saveData.py (again optional)

## Link
The link/url will be created later. All requests should be sent to URL+some route.
Ex. `someurl.com/room'

We should have a private const variable with the URL.

## Kotlin Syntax (Not yet tested!!)
Make sure this is in AndroidManifest.xml:
```
    <uses-permission android:name="android.permission.INTERNET" />
```

The input data should be sent as a JSON in a GET or POST request. The output of the request should also be a JSON.

## Routes
These are the routes implemented and their syntax. These brackets <> indicate a input in the route. For example, /login/roomcode/\<roomcode> indicates that the roomcode should be put into the route. /login/roomcode/H256IQ

If you send a request that is not supported, the error code will be 405.
All error codes are 400 and above.

### /login/roomcode/\<roomcode>/username/\<username>
- POST Request
    - Purpose: Verifying login information. Adding username to data if not present.
    - Input: roomcode and username
        - ex: /login/roomcode/H256IQ/username/Daniel
    - Output: {"roomcode": roomcode}, Response code 200
    - Error Output: Response code 400
        - Nonexistent roomcode

### /new_room_code
- GET Request
    - Purpose: Generates a new, valid roomcode.
    - Input: None
    - Output: {"roomcode": roomcode}, Response code 200
    - Error: Shouldn't happen

### /grocery/roomcode/\<roomcode>
- GET Request
    - Purpose: Returns a dict with all grocery information
    - Input: roomcode
        - ex: /grocery/roomcode/H256IQ
    - Output: {groceryName: {"completed": completedBool}}, Response code 200
        - ex: {"eggs": {"completed": "false"}, "milk": {"completed": "true"}}
        - I'm pretty sure bools are "true" and "false" in JSON
    - Error: Response code 400
        - Nonexistent roomcode

### /grocery/roomcode/\<roomcode>/name/\<groceryName>
- GET Request
    - Purpose: Returns a dict with data on the speficied grocery item
    - Input: roomcode and groceryName
        - ex: grocery/roomcode/H256IQ/name/eggs
    - Output: {groceryName: {"completed": completedBool}}, Response code 200
        - ex: {"eggs": {"completed": "false"}}
        - Only 1 item
    - Error: Response code 400
        - Nonexistent roomcode
- POST Request
    - Purpose: Adds a grocery item to data. Sets it's complete value to false.
    - Input: roomcode and groceryName
        - ex: grocery/roomcode/H256IQ/name/eggs
    - Output: {groceryName: {"completed": completedBool}}, Response code 200
        - ex: {"eggs": {"completed": "false"}, "milk": {"completed": "true"}}
        - Updated grocery list
        - All grocery items sent back
    - Error: Response code 400
        - Nonexistent roomcode
    - Edgecase?:
        - If an existing grocery item is sent, nothing happens and the response code will be 200
- DELETE Request
    - Purpose: Removes a grocery item from the data
    - Input: roomcode and groceryName
        - ex: grocery/roomcode/H256IQ/name/eggs
    - Output: {groceryName: {"completed": completedBool}}, Response code 200
        - ex: {"eggs": {"completed": "false"}, "milk": {"completed": "true"}}
        - Updated grocery list
    - Error: Response code 400
        - Nonexistent roomcode
        - Nonexistent grocery item

### /grocery/roomcode/\<roomcode>/name/\<groceryName>/complete
- POST Request
    - Purpose: Sets a grocery item's complete value to true.
    - Input: roomcode and groceryName
        - ex: grocery/roomcode/H256IQ/name/eggs
    - Output: {groceryName: {"completed": completedBool}}, Response code 200
        - ex: {"eggs": {"completed": "false"}, "milk": {"completed": "true"}}
        - Updated grocery list
    - Error: Response code 400
        - Nonexistent roomcode
        - Nonexistent grocery item
- DELETE Request
    - Purpose: Sets a grocery item's complete value to false.
    - Input: roomcode and groceryName
        - ex: grocery/roomcode/H256IQ/name/eggs
    - Output: {groceryName: {"completed": completedBool}}, Response code 200
        - ex: {"eggs": {"completed": "false"}, "milk": {"completed": "true"}}
        - Updated grocery list
    - Error: Response code 400
        - Nonexistent roomcode
        - Nonexistent grocery item

### /roommates/roomcode/\<roomcode>
- GET Request
    - Purpose: Returns a dict with all roommates
    - Input: roomcode
        - ex: /roommates/roomcode/H256IQ
    - Output: {"roommates": [roommateName]}, Response code 200
        - ex: {"roommates": ["Daniel", "Logan", "Brendan"]}
    - Error: Response code 400
        - Nonexistent roomcode
    
### /chores/roomcode/\<roomcode>
- GET Request
    - Purpose: Returns a dict with all chore information
    - Input: roomcode
        - ex: /chores/roomcode/H256IQ
    - Output: {choreName: {"completed": completedBool, "username": username, "weekday": weekday}}, Response code 200
        - ex: {"cleaning": {"completed": "false", "username": "Daniel", "weekday": "monday"}}
        - I'm pretty sure bools are "true" and "false" in JSON
    - Error: Response code 400
        - Nonexistent roomcode

### /chores/roomcode/\<roomcode>/name/\<choreName>
- GET Request
    - Purpose: Returns data on the specified chore
    - Input: roomcode and choreName
        - ex: chores/roomcode/H256IQ/name/cleaning
    - Output: {choreName: {"completed": completedBool, "username": username, "weekday": weekday}}, Response code 200
        - ex: {"cleaning": {"completed": "false", "username": "Daniel", "weekday": "monday"}}
        - Only 1 chore
    - Error: Response code 400
        - Nonexistent roomcode
- POST Request
    - Purpose: Adds a chore item to data. Sets it's complete value to false, username will be "", and weekday will be "".
    - Input: roomcode and choreName
        - ex: chores/roomcode/H256IQ/name/cleaning
    - Output: {choreName: {"completed": completedBool, "username": username, "weekday": weekday}}, Response code 200
        - ex: {"cleaning": {"completed": "false", "username": "Daniel", "weekday": "monday"}}
        - Updated chores list
    - Error: Response code 400
        - Nonexistent roomcode
    - Edgecase?:
        - If an existing chore item is sent, nothing happens and the response code will be 200
- DELETE Request
    - Purpose: Removes a chore item from the data
    - Input: roomcode and choreName
        - ex: chores/roomcode/H256IQ/name/cleaning
    - Output: {choreName: {"completed": completedBool, "username": username, "weekday": weekday}}, Response code 200
        - ex: {"cleaning": {"completed": "false", "username": "Daniel", "weekday": "monday"}}
        - Updated chores list
    - Error: Response code 400
        - Nonexistent roomcode
        - Nonexistent grocery item

### /chores/roomcode/\<roomcode>/name/\<choreName>/complete
- POST Request
    - Purpose: Sets a chore's complete value to true.
    - Input: roomcode and choreName
        - ex: chores/roomcode/H256IQ/name/cleaning/complete
    - Output: {choreName: {"completed": completedBool, "username": username, "weekday": weekday}}, Response code 200
        - ex: {"cleaning": {"completed": "false", "username": "Daniel", "weekday": "monday"}}
        - Updated chores list
    - Error: Response code 400
        - Nonexistent roomcode
        - Nonexistent grocery item
- DELETE Request
    - Purpose: Sets a chore's complete value to false.
    - Input: roomcode and choreName
        - ex: chores/roomcode/H256IQ/name/cleaning/complete
    - Output: {choreName: {"completed": completedBool, "username": username, "weekday": weekday}}, Response code 200
        - ex: {"cleaning": {"completed": "false", "username": "Daniel", "weekday": "monday"}}
        - Updated chores list
    - Error: Response code 400
        - Nonexistent roomcode
        - Nonexistent grocery item

### /chores/roomcode/\<roomcode>/name/\<choreName>/username/\<username>
- POST Request
    - Purpose: Assigns a username to a given chore
    - Input: roomcode, choreName, and username
        - ex: chores/roomcode/H256IQ/name/cleaning/username/Daniel
    - Output: {choreName: {"completed": completedBool, "username": username, "weekday": weekday}}, Response code 200
        - ex: {"cleaning": {"completed": "false", "username": "Daniel", "weekday": "monday"}}
        - Updated chores list
    - Error: Response code 400
        - Nonexistent roomcode
        - Nonexistent grocery item
        - Nonexistent username (in room's username array)
- DELETE Request
    - Purpose: Clears the username field for a given chore
    - Input: roomcode, choreName, and username
        - ex: chores/roomcode/H256IQ/name/cleaning/username/Daniel
    - Output: {choreName: {"completed": completedBool, "username": username, "weekday": weekday}}, Response code 200
        - ex: {"cleaning": {"completed": "false", "username": "Daniel", "weekday": "monday"}}
        - Updated chores list
    - Error: Response code 400
        - Nonexistent roomcode
        - Nonexistent grocery item

### /chores/roomcode/\<roomcode>/name/\<choreName>/weekday/\<weekday>
- POST Request
    - Purpose: Assigns a weekday to a given chore
    - Input: roomcode, choreName, and weekday
        - ex: chores/roomcode/H256IQ/name/cleaning/weekday/sunday
    - Output: {choreName: {"completed": completedBool, "username": username, "weekday": weekday}}, Response code 200
        - ex: {"cleaning": {"completed": "false", "username": "Daniel", "weekday": "monday"}}
        - Updated chores list
    - Error: Response code 400
        - Nonexistent roomcode
        - Nonexistent grocery item
        - Invalid weekday (should be all lowercase, correct spelling)
            - ex. "monday"
- DELETE Request
    - Purpose: Clears the weekday field for a given chore
    - Input: roomcode, choreName, and weekday
        - ex: chores/roomcode/H256IQ/name/cleaning/weekday/sunday
    - Output: {choreName: {"completed": completedBool, "username": username, "weekday": weekday}}, Response code 200
        - ex: {"cleaning": {"completed": "false", "username": "Daniel", "weekday": "monday"}}
        - Updated chores list
    - Error: Response code 400
        - Nonexistent roomcode
        - Nonexistent grocery item