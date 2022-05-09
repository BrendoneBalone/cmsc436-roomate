import requests
import json

URL = 'http://436env6.eba-czfwhf2y.us-east-2.elasticbeanstalk.com'
# URL = 'http://127.0.0.1:5000'

data = requests.post(URL+'/clear_groceries')