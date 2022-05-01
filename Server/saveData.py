import requests
import json

URL = 'http://localhost:5000'

data = requests.get(URL+'/save')

jsonData = json.dumps(data.json())

f = open("data.json", "w")
f.write(jsonData)
f.close()






