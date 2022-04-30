from flask import Flask, request
import string
import random

application = Flask(__name__)

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
            assignee: String
            dates: String[] of Weekdays
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

    rooms[roomcode] = {"grocery": {}, "chores": {}}

    return roomcode

#################################
# "Private" Routes
#################################

@application.route('/save', methods=["GET"])
def save():
    return rooms

@application.route('/load', methods=["POST"])
def load():
    newData = request.json
    rooms = newData
    return "ok", 200


#################################
# Routes
#################################

@application.route('/login', methods=["POST"])
def room():
    data = request.json
    if "roomcode" in data:
        roomcode = data["roomcode"]
        if roomcode in rooms:
            if "username" in data:
                username = data["username"]
                
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
    if "roomcode" in data:
        roomcode = data["roomcode"]
        if roomcode in rooms:
            return rooms[roomcode]["grocery"], 200
    
    return "err", 400

@application.route('/grocery/add', methods=["POST"])
def add_grocery():
    data = request.json
    if "roomcode" in data:
        roomcode = data["roomcode"]
        if roomcode in rooms:
            if "groceryName" in data:
                groceryName = data["groceryName"]

                if groceryName not in rooms[roomcode]["grocery"]:
                    rooms[roomcode]["grocery"][groceryName]["completed"] = False
                
                return rooms[roomcode]["grocery"], 200

    return "err", 400

@application.route('/grocery/complete', methods=["POST"])
def add_grocery():
    data = request.json
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
def add_grocery():
    data = request.json
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
    if "roomcode" in data:
        roomcode = data["roomcode"]
        if roomcode in rooms:
            return {"roommates": rooms[roomcode]["roommates"]}, 200
    
    return "err", 400

@application.route('/chores', methods=["GET"])
def chores():
    data = request.json
    if "roomcode" in data:
        roomcode = data["roomcode"]
        if roomcode in rooms:
            return rooms[roomcode]["chores"], 200
    
    return "err", 400

@application.route('/chores/add', methods=["POST"])
def add_grocery():
    data = request.json
    if "roomcode" in data:
        roomcode = data["roomcode"]
        if roomcode in rooms:
            if "choreName" in data:
                choreName = data["choreName"]

                if choreName not in rooms[roomcode]["chores"]:
                    rooms[roomcode]["chores"][choreName]["completed"] = False
                    rooms[roomcode]["chores"][choreName]["assignee"] = None
                    rooms[roomcode]["chores"][choreName]["date"] = None
                
                return rooms[roomcode]["chores"], 200

    return "err", 400



if __name__ == "__main__":
    application.debug = True
    application.run()