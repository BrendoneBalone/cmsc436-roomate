import requests
import json

URL = 'http://localhost:5000'

# Load prev data
f = open("data.json")
jsonData = json.load(f)
f.close()

data = requests.post(URL+'/load', json=jsonData)

# Test generate roomcode
# r = requests.get(URL + '/new_room_code')
# roomcode = r.json()["roomcode"]


roomcode = "U7HDV4"

# # Test login
inputData = {"roomcode": roomcode, "username": "Daniel"}
r = requests.post(URL+"/login", json=inputData)

inputData = {"roomcode": roomcode, "username": "Logan"}
r = requests.post(URL+"/login", json=inputData)

inputData = {"roomcode": roomcode, "username": "Brendan"}
r = requests.post(URL+"/login", json=inputData)

# Add grocery
inputData = {"roomcode": roomcode, "groceryName": "eggs"}
r = requests.post(URL+"/grocery/add", json=inputData)

inputData = {"roomcode": roomcode, "groceryName": "milk"}
r = requests.post(URL+"/grocery/add", json=inputData)

inputData = {"roomcode": roomcode, "groceryName": "cereal"}
r = requests.post(URL+"/grocery/add", json=inputData)

# Remove grocery
inputData = {"roomcode": roomcode, "groceryName": "cereal"}
r = requests.post(URL+"/grocery/remove", json=inputData)

# Grocery
inputData = {"roomcode": roomcode}
r = requests.get(URL+"/grocery", json=inputData)

# Grocery complete
inputData = {"roomcode": roomcode, "groceryName": "eggs"}
r = requests.post(URL+"/grocery/complete", json=inputData)

inputData = {"roomcode": roomcode, "groceryName": "milk"}
r = requests.post(URL+"/grocery/complete", json=inputData)

# Grocery uncomplete
inputData = {"roomcode": roomcode, "groceryName": "eggs"}
r = requests.post(URL+"/grocery/uncomplete", json=inputData)

# roommates
Data = {"roomcode": roomcode}
r = requests.get(URL+"/roommates", json=inputData)
# print(r.json())

# chores
Data = {"roomcode": roomcode}
r = requests.get(URL+"/chores", json=inputData)
# print(r.json())

# add chores
inputData = {"roomcode": roomcode, "choreName": "Wash dishes"}
r = requests.post(URL+"/chores/add", json=inputData)

inputData = {"roomcode": roomcode, "choreName": "Vaccum"}
r = requests.post(URL+"/chores/add", json=inputData)

inputData = {"roomcode": roomcode, "choreName": "Take out trash"}
r = requests.post(URL+"/chores/add", json=inputData)

# remove chores
inputData = {"roomcode": roomcode, "choreName": "Take out trash"}
r = requests.post(URL+"/chores/remove", json=inputData)

# complete chores
inputData = {"roomcode": roomcode, "choreName": "Wash dishes"}
r = requests.post(URL+"/chores/complete", json=inputData)

inputData = {"roomcode": roomcode, "choreName": "Vaccum"}
r = requests.post(URL+"/chores/complete", json=inputData)

# uncomplete chores
inputData = {"roomcode": roomcode, "choreName": "Wash dishes"}
r = requests.post(URL+"/chores/uncomplete", json=inputData)

# add username chores
inputData = {"roomcode": roomcode, "choreName": "Wash dishes", "username": "Logan"}
r = requests.post(URL+"/chores/username/add", json=inputData)

inputData = {"roomcode": roomcode, "choreName": "Vaccum", "username": "Daniel"}
r = requests.post(URL+"/chores/username/add", json=inputData)

# # remove username chores
# inputData = {"roomcode": roomcode, "choreName": "Vaccum", "username": "Logan"}
# r = requests.post(URL+"/chores/username/remove", json=inputData)

# add weekday
inputData = {"roomcode": roomcode, "choreName": "Wash dishes", "weekday": "monday"}
r = requests.post(URL+"/chores/weekday/add", json=inputData)

inputData = {"roomcode": roomcode, "choreName": "Vaccum", "weekday": "sunday"}
r = requests.post(URL+"/chores/weekday/add", json=inputData)

# # remove weekday
# inputData = {"roomcode": roomcode, "choreName": "Vaccum"}
# r = requests.post(URL+"/chores/weekday/remove", json=inputData)






# Save Data

URL = 'http://localhost:5000'

data = requests.get(URL+'/save')

jsonData = json.dumps(data.json())

f = open("data.json", "w")
f.write(jsonData)
f.close()
