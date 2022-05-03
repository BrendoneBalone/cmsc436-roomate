import requests
import json

URL = 'http://localhost:5000'

# Load prev data
f = open("data2.json")
jsonData = json.load(f)
f.close()

data = requests.post(URL+'/load', json=jsonData)

# Test generate roomcode
# r = requests.get(URL + '/new_room_code')
# roomcode = r.json()["roomcode"]


roomcode = "0PDZGV"

# # Test login
username = "Daniel"
r = requests.post(URL+"/login/roomcode/"+roomcode+"/username/"+username)

username = "Logan"
r = requests.post(URL+"/login/roomcode/"+roomcode+"/username/"+username)

username = "Brendan"
r = requests.post(URL+"/login/roomcode/"+roomcode+"/username/"+username)

# # Add grocery
groceryName = "eggs"
r = requests.post(URL+"/grocery/roomcode/"+roomcode+"/name/"+groceryName)

groceryName = "milk"
r = requests.post(URL+"/grocery/roomcode/"+roomcode+"/name/"+groceryName)

groceryName = "cereal"
r = requests.post(URL+"/grocery/roomcode/"+roomcode+"/name/"+groceryName)

# Remove grocery
groceryName = "cereal"
r = requests.delete(URL+"/grocery/roomcode/"+roomcode+"/name/"+groceryName)

# get grocery
groceryName = "eggs"
r = requests.get(URL+"/grocery/roomcode/"+roomcode+"/name/"+groceryName)
# print(r.json())

# Grocery
r = requests.get(URL+"/grocery/roomcode/"+ roomcode)
# print(r.json())

# Grocery complete
groceryName = "eggs"
r = requests.post(URL+"/grocery/roomcode/"+roomcode+"/name/"+groceryName+"/complete")

groceryName = "milk"
r = requests.post(URL+"/grocery/roomcode/"+roomcode+"/name/"+groceryName+"/complete")

# Grocery uncomplete
groceryName = "eggs"
r = requests.delete(URL+"/grocery/roomcode/"+roomcode+"/name/"+groceryName+"/complete")

# # roommates
r = requests.get(URL+"/roommates/roomcode/"+roomcode)
# print(r.json())

# chores
r = requests.get(URL+"/chores/roomcode/"+roomcode)
# print(r.json())

# add chores
choreName = "clean the dishes"
r = requests.post(URL+"/chores/roomcode/"+roomcode+"/name/"+choreName)

choreName = "vaccum"
r = requests.post(URL+"/chores/roomcode/"+roomcode+"/name/"+choreName)

choreName = "take out trash"
r = requests.post(URL+"/chores/roomcode/"+roomcode+"/name/"+choreName)

# remove chores
choreName = "take out trash"
r = requests.delete(URL+"/chores/roomcode/"+roomcode+"/name/"+choreName)

# complete chores
choreName = "clean the dishes"
r = requests.post(URL+"/chores/roomcode/"+roomcode+"/name/"+choreName+"/complete")

choreName = "vaccum"
r = requests.post(URL+"/chores/roomcode/"+roomcode+"/name/"+choreName+"/complete")

# uncomplete chores
choreName = "vaccum"
r = requests.delete(URL+"/chores/roomcode/"+roomcode+"/name/"+choreName+"/complete")

# add username chores
choreName = "vaccum"
username = "Daniel"
r = requests.post(URL+"/chores/roomcode/"+roomcode+"/name/"+choreName+"/username/"+username)

choreName = "clean the dishes"
username = "Logan"
r = requests.post(URL+"/chores/roomcode/"+roomcode+"/name/"+choreName+"/username/"+username)

# # remove username chores
choreName = "clean the dishes"
username = "Daniel"
r = requests.delete(URL+"/chores/roomcode/"+roomcode+"/name/"+choreName+"/username/"+username)

# # add weekday
choreName = "vaccum"
weekday = "sunday"
r = requests.post(URL+"/chores/roomcode/"+roomcode+"/name/"+choreName+"/weekday/"+weekday)

choreName = "clean the dishes"
weekday = "monday"
r = requests.post(URL+"/chores/roomcode/"+roomcode+"/name/"+choreName+"/weekday/"+weekday)

# # # remove weekday
choreName = "clean the dishes"
weekday = "monday"
r = requests.delete(URL+"/chores/roomcode/"+roomcode+"/name/"+choreName+"/weekday/"+weekday)


# Save Data

URL = 'http://localhost:5000'

data = requests.get(URL+'/save')

jsonData = json.dumps(data.json())

f = open("data.json", "w")
f.write(jsonData)
f.close()
