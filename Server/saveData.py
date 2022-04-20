import requests
import json

URL = ''

data = requests.get(URL+'/save')

jsonData = json.dumps(data)

f = open("data.json", "w")
f.write(jsonData)
f.close()






