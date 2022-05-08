import requests
import json

URL = 'http://436env5.eba-vukmr2km.us-east-2.elasticbeanstalk.com'

data = requests.get(URL+'/save')

jsonData = json.dumps(data.json())

f = open("data2.json", "w")
f.write(jsonData)
f.close()






