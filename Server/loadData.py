import requests
import json

URL = 'http://localhost:5000'

f = open("data.json")
jsonData = json.load(f)
f.close()

data = requests.post(URL+'/load', json=jsonData)




