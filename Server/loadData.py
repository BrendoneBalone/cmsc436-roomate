import requests
import json

URL = 'http://436env5.eba-vukmr2km.us-east-2.elasticbeanstalk.com'

f = open("data.json")
jsonData = json.load(f)
f.close()

data = requests.post(URL+'/load', json=jsonData)




