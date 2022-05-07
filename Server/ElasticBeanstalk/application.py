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

@application.route('/login/roomcode/<roomcode>/username/<username>', methods=["POST"])
def login(roomcode='', username=''):

    if roomcode in rooms:
            
        if username not in rooms[roomcode]["roommates"]:
            rooms[roomcode]["roommates"].append(username)

        return {"roomcode": roomcode}, 200

    return {"status": "fail"}, 400

@application.route('/new_room_code', methods=["GET"])
def new_room_code():
    return {"roomcode": generate_roomcode()}, 200

@application.route('/grocery/roomcode/<roomcode>', methods=["GET"])
def grocery(roomcode=''):

    if roomcode in rooms:
        return rooms[roomcode]["grocery"], 200
    
    return {"status": "fail"}, 400

@application.route('/grocery/roomcode/<roomcode>/name/<groceryName>', methods=["GET", "POST", "DELETE"])
def grocery_add(roomcode='', groceryName=''):
    if request.method == "GET":
        if roomcode in rooms:

            if groceryName in rooms[roomcode]["grocery"]:
                return {groceryName: rooms[roomcode]["grocery"][groceryName]}, 200

    elif request.method == "POST":
        if roomcode in rooms:

            if groceryName not in rooms[roomcode]["grocery"]:
                rooms[roomcode]["grocery"][groceryName] = {}
                rooms[roomcode]["grocery"][groceryName]["completed"] = False
            
            return rooms[roomcode]["grocery"], 200
    elif request.method == "DELETE":
        if roomcode in rooms:

            if groceryName in rooms[roomcode]["grocery"]:
                removed = rooms[roomcode]["grocery"].pop(groceryName)

                if removed != None:
                    return rooms[roomcode]["grocery"], 200

    return {"status": "fail"}, 400

@application.route('/grocery/roomcode/<roomcode>/name/<groceryName>/complete', methods=["POST", "DELETE"])
def grocery_complete(roomcode='', groceryName=''):

    if request.method == "POST":
        if roomcode in rooms:

            if groceryName in rooms[roomcode]["grocery"]:
                rooms[roomcode]["grocery"][groceryName]["completed"] = True
                return rooms[roomcode]["grocery"], 200

    elif request.method == "DELETE":
        if roomcode in rooms:

            if groceryName in rooms[roomcode]["grocery"]:
                rooms[roomcode]["grocery"][groceryName]["completed"] = False
                return rooms[roomcode]["grocery"], 200

    return {"status": "fail"}, 400
    
@application.route('/roommates/roomcode/<roomcode>', methods=["GET"])
def roommates(roomcode=''):
    if roomcode in rooms:
        return {"roommates": rooms[roomcode]["roommates"]}, 200
    
    return {"status": "fail"}, 400

@application.route('/chores/roomcode/<roomcode>', methods=["GET"])
def chores(roomcode=''):
    if roomcode in rooms:
        return rooms[roomcode]["chores"], 200
    
    return {"status": "fail"}, 400

@application.route('/chores/roomcode/<roomcode>/name/<choreName>', methods=["GET", "POST", "DELETE"])
def chores_add(roomcode='', choreName=''):
    if request.method == "GET":
        if roomcode in rooms:

            if choreName in rooms[roomcode]["chores"]:
                return rooms[roomcode]["chores"][choreName], 200
    elif request.method == "POST":
        if roomcode in rooms:

            if choreName not in rooms[roomcode]["chores"]:
                rooms[roomcode]["chores"][choreName] = {}
                rooms[roomcode]["chores"][choreName]["completed"] = False
                rooms[roomcode]["chores"][choreName]["username"] = ""
                rooms[roomcode]["chores"][choreName]["weekday"] = ""
                
                return rooms[roomcode]["chores"], 200
    elif request.method == "DELETE":
        if roomcode in rooms:

            if choreName in rooms[roomcode]["chores"]:
                removed = rooms[roomcode]["chores"].pop(choreName)

                if removed != None:
                    return rooms[roomcode]["chores"], 200

    return {"status": "fail"}, 400

@application.route('/chores/roomcode/<roomcode>/name/<choreName>/complete', methods=["POST", "DELETE"])
def chores_complete(roomcode='', choreName=''):
    if request.method == "POST":

        if roomcode in rooms:

            if choreName in rooms[roomcode]["chores"]:
                rooms[roomcode]["chores"][choreName]["completed"] = True
            
                return rooms[roomcode]["chores"], 200
    elif request.method == "DELETE":
        if roomcode in rooms:

            if choreName in rooms[roomcode]["chores"]:
                rooms[roomcode]["chores"][choreName]["completed"] = False
            
                return rooms[roomcode]["chores"], 200

    return {"status": "fail"}, 400

@application.route('/chores/roomcode/<roomcode>/name/<choreName>/username/<username>', methods=["POST", "DELETE"])
def chores_username_add(roomcode='', choreName='', username=''):

    if request.method == "POST":
        if roomcode in rooms:

            if choreName in rooms[roomcode]["chores"]:

                    if username in rooms[roomcode]["roommates"]:

                        rooms[roomcode]["chores"][choreName]["username"] = username
                    
                        return rooms[roomcode]["chores"], 200
    
    elif request.method == "DELETE":
        if roomcode in rooms:

            if choreName in rooms[roomcode]["chores"]:
                rooms[roomcode]["chores"][choreName]["username"] = ""
            
                return rooms[roomcode]["chores"], 200

    return {"status": "fail"}, 400

@application.route('/chores/roomcode/<roomcode>/name/<choreName>/weekday/<weekday>', methods=["POST", "DELETE"])
def chores_weekday_add(roomcode='', choreName='', weekday=''):
    if request.method == "POST":
        if roomcode in rooms:

            if choreName in rooms[roomcode]["chores"]:

                    if Weekdays.is_weekday(weekday) is not None:
                        rooms[roomcode]["chores"][choreName]["weekday"] = weekday
            
                        return rooms[roomcode]["chores"], 200
    elif request.method == "DELETE":
        if roomcode in rooms:

                if choreName in rooms[roomcode]["chores"]:
                    rooms[roomcode]["chores"][choreName]["weekday"] = ""
        
                    return rooms[roomcode]["chores"], 200

    return {"status": "fail"}, 400

if __name__ == "__main__":
    application.debug = True
    application.run()