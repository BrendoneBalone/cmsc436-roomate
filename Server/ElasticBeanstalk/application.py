from tokenize import String
from xmlrpc.client import Boolean
from flask import Flask
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
        1: {
            name: String
            completed: Boolean
        }
    },

    chores: {
        1: {
            name: String
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

    rooms["roomcode"] = {"grocery": {}, "chores": {}}

    return roomcode

#################################
# Routes
#################################

@application.route('/save', methods=["GET"])
def save():
    return rooms

@application.route('/room', methods=["GET"])
def room():
    data = Flask.request.data
    if "roomcode" in data:
        if data["roomcode"] == "new":
            return generate_roomcode()
        elif len(data["roomcode"]) == 6:
            return data["roomcode"]
    
    return "err", 400

@application.route('/grocery', methods=["GET", "POST"])
def grocery():
    pass


if __name__ == "__main__":
    application.debug = True
    application.run()