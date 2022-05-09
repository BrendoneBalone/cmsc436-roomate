import requests
import json

URL = 'http://436env6.eba-czfwhf2y.us-east-2.elasticbeanstalk.com'

data = requests.get(URL+'/save')

jsonData = json.dumps(data.json())

f = open("data2.json", "w")
f.write(jsonData)
f.close()






