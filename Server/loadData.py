import requests
import json

URL = 'http://436env7.eba-ayuzpbp2.us-east-2.elasticbeanstalk.com'
# URL = 'http://127.0.0.1:5000'

f = open("data.json")
jsonData = json.load(f)
f.close()

data = requests.post(URL+'/load', json=jsonData)




