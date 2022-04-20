# Server Request Syntax

## Link
The link/url will be created later. All requests should be sent to URL+some route.
Ex. `someurl.com/room'

## Routes
These are the routes implemented and their syntax.

### /room
- GET Request
    - Input Data: {"roomcode": int} 
        - ex: {"roomcode": H256IQ}
        - If you want a new roomcode send {"roomcode": "new"}
    - Output: {roomcode}, Response code 200
        - On error, Response code 400