from calendar import FRIDAY, SATURDAY, SUNDAY, THURSDAY, WEDNESDAY
from flask import Flask, request
import string
import random

from regex import R

application = Flask(__name__)

#################################
# Enums
#################################

class Weekdays:
    MONDAY = "monday"
    TUESDAY = "tuesday"
    WEDNESDAY = "wednesday"
    THURSDAY = "thursday"
    FRIDAY = "friday"
    SATURDAY = "saturday"
    SUNDAY = "sunday"

    @classmethod
    def is_weekday(self, weekday):
        x = [Weekdays.MONDAY, Weekdays.TUESDAY, Weekdays.WEDNESDAY, Weekdays.THURSDAY,
        Weekdays.FRIDAY, Weekdays.SATURDAY, Weekdays.SUNDAY]

        if weekday in x:
            return weekday

        return None

#################################
# Data
#################################
rooms = {} # Key: Roomcode, Value: All Data

'''
{
    grocery: {
        name: {
            completed: Boolean
        }
    },
    chores: {
        name: {
            completed: Boolean
            username: String
            weekday: String[] of Weekdays
        }
    },
    roommates: String[]
}
'''

#################################
# Private Functions
#################################
def generate_random_string():
    chars = string.ascii_uppercase + string.digits
    return "".join(random.choice(chars) for _ in range(6))

def generate_roomcode():
    roomcode = generate_random_string()

    while roomcode in rooms.keys():
        roomcode = generate_random_string()

    rooms[roomcode] = {"grocery": {}, "chores": {}, "roommates": []}

    return roomcode

#################################
# "Private" Routes
#################################
@application.route('/')
def main():
    return rooms

@application.route('/save', methods=["GET"])
def save():
    return rooms

@application.route('/load', methods=["POST"])
def load():
    newData = request.json
    for room in newData:
        rooms[room] = newData[room]
    return "ok", 200


#################################
# Routes
#################################

@application.route('/login', methods=["POST"])
def login():
    data = request.json
    if data is None:
        return "err", 400

    if "roomcode" in data:
        roomcode = data["roomcode"]
        if roomcode in rooms:
            if "username" in data:
                username = data["username"]

                if username == "":
                    return "err", 400
                
                if username not in rooms[roomcode]["roommates"]:
                    rooms[roomcode]["roommates"].append(username)

                return {"roomcode": roomcode}, 200

    return "err", 400

@application.route('/new_room_code', methods=["GET"])
def new_room_code():
    return {"roomcode": generate_roomcode()}, 200

@application.route('/grocery', methods=["GET"])
def grocery():
    data = request.json
    if data is None:
        return "err", 400

    if "roomcode" in data:
        roomcode = data["roomcode"]
        if roomcode in rooms:
            return rooms[roomcode]["grocery"], 200
    
    return "err", 400

@application.route('/grocery/add', methods=["POST"])
def grocery_add():
    data = request.json
    if data is None:
        return "err", 400

    if "roomcode" in data:
        roomcode = data["roomcode"]
        if roomcode in rooms:
            if "groceryName" in data:
                groceryName = data["groceryName"]

                if groceryName not in rooms[roomcode]["grocery"]:
                    rooms[roomcode]["grocery"][groceryName] = {}
                    rooms[roomcode]["grocery"][groceryName]["completed"] = False
                
                return rooms[roomcode]["grocery"], 200

    return "err", 400

@application.route('/grocery/remove', methods=["POST"])
def grocery_remove():
    data = request.json
    if data is None:
        return "err", 400

    if "roomcode" in data:
        roomcode = data["roomcode"]
        if roomcode in rooms:
            if "groceryName" in data:
                groceryName = data["groceryName"]

                if groceryName in rooms[roomcode]["grocery"]:
                    removed = rooms[roomcode]["grocery"].pop(groceryName)

                    if removed != None:
                        return rooms[roomcode]["grocery"], 200

    return "err", 400

@application.route('/grocery/complete', methods=["POST"])
def grocery_complete():
    data = request.json
    if data is None:
        return "err", 400
        
    if "roomcode" in data:
        roomcode = data["roomcode"]
        if roomcode in rooms:
            if "groceryName" in data:
                groceryName = data["groceryName"]
                if groceryName in rooms[roomcode]["grocery"]:
                    rooms[roomcode]["grocery"][groceryName]["completed"] = True
                    return rooms[roomcode]["grocery"], 200

    return "err", 400

@application.route('/grocery/uncomplete', methods=["POST"])
def grocery_uncomplete():
    data = request.json
    if data is None:
        return "err", 400
        
    if "roomcode" in data:
        roomcode = data["roomcode"]
        if roomcode in rooms:
            if "groceryName" in data:
                groceryName = data["groceryName"]

                if groceryName in rooms[roomcode]["grocery"]:
                    rooms[roomcode]["grocery"][groceryName]["completed"] = False
                    return rooms[roomcode]["grocery"], 200

    return "err", 400
    
@application.route('/roommates', methods=["GET"])
def roommates():
    data = request.json
    if data is None:
        return "err", 400
        
    if "roomcode" in data:
        roomcode = data["roomcode"]
        if roomcode in rooms:
            return {"roommates": rooms[roomcode]["roommates"]}, 200
    
    return "err", 400

@application.route('/chores', methods=["GET"])
def chores():
    data = request.json
    if data is None:
        return "err", 400
        
    if "roomcode" in data:
        roomcode = data["roomcode"]
        if roomcode in rooms:
            return rooms[roomcode]["chores"], 200
    
    return "err", 400

@application.route('/chores/add', methods=["POST"])
def chores_add():
    data = request.json
    if data is None:
        return "err", 400
        
    if "roomcode" in data:
        roomcode = data["roomcode"]
        if roomcode in rooms:
            if "choreName" in data:
                choreName = data["choreName"]

                if choreName not in rooms[roomcode]["chores"]:
                    rooms[roomcode]["chores"][choreName] = {}
                    rooms[roomcode]["chores"][choreName]["completed"] = False
                    rooms[roomcode]["chores"][choreName]["username"] = ""
                    rooms[roomcode]["chores"][choreName]["weekday"] = ""
                
                return rooms[roomcode]["chores"], 200

    return "err", 400

@application.route('/chores/remove', methods=["POST"])
def chores_remove():
    data = request.json
    if data is None:
        return "err", 400
        
    if "roomcode" in data:
        roomcode = data["roomcode"]
        if roomcode in rooms:
            if "choreName" in data:
                choreName = data["choreName"]

                if choreName in rooms[roomcode]["chores"]:
                    removed = rooms[roomcode]["chores"].pop(choreName)

                    if removed != None:
                        return rooms[roomcode]["chores"], 200

    return "err", 400

@application.route('/chores/complete', methods=["POST"])
def chores_complete():
    data = request.json
    if data is None:
        return "err", 400
        
    if "roomcode" in data:
        roomcode = data["roomcode"]
        if roomcode in rooms:
            if "choreName" in data:
                choreName = data["choreName"]

                if choreName in rooms[roomcode]["chores"]:
                    rooms[roomcode]["chores"][choreName]["completed"] = True
                
                    return rooms[roomcode]["chores"], 200

    return "err", 400

@application.route('/chores/uncomplete', methods=["POST"])
def chores_uncomplete():
    data = request.json
    if data is None:
        return "err", 400
        
    if "roomcode" in data:
        roomcode = data["roomcode"]
        if roomcode in rooms:
            if "choreName" in data:
                choreName = data["choreName"]

                if choreName in rooms[roomcode]["chores"]:
                    rooms[roomcode]["chores"][choreName]["completed"] = False
                
                    return rooms[roomcode]["chores"], 200

    return "err", 400

@application.route('/chores/username/add', methods=["POST"])
def chores_username_add():
    data = request.json
    if data is None:
        return "err", 400
        
    if "roomcode" in data:
        roomcode = data["roomcode"]
        if roomcode in rooms:
            if "choreName" in data:
                choreName = data["choreName"]

                if choreName in rooms[roomcode]["chores"]:
                    if "username" in data:
                        username = data["username"]

                        if username in rooms[roomcode]["roommates"]:

                            rooms[roomcode]["chores"][choreName]["username"] = username
                        
                            return rooms[roomcode]["chores"], 200

    return "err", 400

@application.route('/chores/username/remove', methods=["POST"])
def chores_username_remove():
    data = request.json
    if data is None:
        return "err", 400
        
    if "roomcode" in data:
        roomcode = data["roomcode"]
        if roomcode in rooms:
            if "choreName" in data:
                choreName = data["choreName"]

                if choreName in rooms[roomcode]["chores"]:
                    rooms[roomcode]["chores"][choreName]["username"] = ""
                
                    return rooms[roomcode]["chores"], 200

    return "err", 400

@application.route('/chores/weekday/add', methods=["POST"])
def chores_weekday_add():
    data = request.json
    if data is None:
        return "err", 400
        
    if "roomcode" in data:
        roomcode = data["roomcode"]
        if roomcode in rooms:
            if "choreName" in data:
                choreName = data["choreName"]

                if choreName in rooms[roomcode]["chores"]:
                    if "weekday" in data:
                        weekday = data["weekday"]

                        if Weekdays.is_weekday(weekday) is not None:
                            rooms[roomcode]["chores"][choreName]["weekday"] = weekday
                
                            return rooms[roomcode]["chores"], 200

    return "err", 400

@application.route('/chores/weekday/remove', methods=["POST"])
def chores_weekday_remove():
    data = request.json
    if data is None:
        return "err", 400
        
    if "roomcode" in data:
        roomcode = data["roomcode"]
        if roomcode in rooms:
            if "choreName" in data:
                choreName = data["choreName"]

                if choreName in rooms[roomcode]["chores"]:
                    rooms[roomcode]["chores"][choreName]["weekday"] = ""
        
                    return rooms[roomcode]["chores"], 200

    return "err", 400

if __name__ == "__main__":
    application.debug = True
    application.run()