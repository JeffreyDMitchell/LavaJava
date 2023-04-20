
from hashlib import md5
from bottle import post, run, request, response, get
from tinydb import TinyDB, Query

secret = "supersecret"
auth_users = {}

@post("/createAccount/")
def createAccount():
    global db, secret

    username = request.json.get('username')
    password = request.json.get('password')

    if not (username and password):
        # failure case, insufficient data provided in request
        response.status = 400
        return
    
    q = Query()
    if db.search(q.username == username):
        #falure case, username already exists
        response.status = 406
        return

    db.insert({'username': username, 'password_md5':md5(password.encode()).hexdigest(), 'hiscore':0})

    # success case
    response.status = 200
    token = authenticate(username)
    return {"token": token}

@post("/login/")
def login():
    global db, secret

    username = request.json.get('username')
    password = request.json.get('password')

    if not (username and password):
        # failure case, insufficient data provided in request
        response.status = 400
        return
    
    q = Query()
    result = db.search(q.username == username)

    if not result:
        # user does not exist
        response.status = 404
        return
    
    if not md5(password.encode()).hexdigest() == result[0]['password_md5']:
        # bad password
        response.status = 403
        return
    
    #  successful login
    response.status = 200
    token = authenticate(username)
    return {"token": token}

@post("/addScore/")
def addScore():
    global auth_users, db

    token = request.json.get('token')
    score = request.json.get('score')

    if not (token and score):
        # required params not found
        response.status = 400
        return
    
    if not score.isnumeric():
        # bad score provided
        response.status = 400
        return

    auth, username = checkAuthent(token)

    if not auth:
        # invalid token provided
        response.status = 403
        return
    
    # at this point, account is guaranteed to be valid
    # update score if it is indeed a new hiscore
    q = Query()
    result = db.search(q.username == username)
    if int(result[0]['hiscore']) < int(score):
        db.update({'hiscore': int(score)}, q.username == username)

    response.status = 200
    return

# currently no need for authentication to get scores. 
# we could always change that
# TODO maybe have seperate endpoints for individual and all score gets
@get("/getHiscore/")
def getScore():
    global db

    username = request.json.get('username')
    
    q = Query()
    if username:
        result = db.search(q.username == username)
        if not result:
            response.status = 404
            return
        
        response.status = 200

        return {username: result[0]['hiscore']}
    else:
        result = db.all()

        scores = {}
        for entry in result:
            scores[entry['username']] = entry['hiscore']

        return scores

def authenticate(username):
    global secret, auth_users

    token = md5((username + secret).encode()).hexdigest()
    auth_users[token] = username

    return token

def checkAuthent(token):
    global auth_users
    
    hit = token in auth_users

    return hit, auth_users[token] if hit else ""

if __name__ == '__main__':
    global db

    # create / load database
    db = TinyDB("data.json")

    # run bottle server
    run(host='0.0.0.0', port=3000)