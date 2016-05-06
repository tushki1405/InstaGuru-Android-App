
# A very simple Flask Hello World app for you to get started with...

from __future__ import print_function
from flask import Flask, request
from datetime import datetime
import mysql.connector
import json
from werkzeug import secure_filename,SharedDataMiddleware
import os

app = Flask(__name__)

config = {
  'user': 'tushki1405',
  'password': 'tushargupta',
  'host': 'tushki1405.mysql.pythonanywhere-services.com',
  'database': 'tushki1405$itp341'
}

#setting upload path
APP_ROOT = os.path.dirname(os.path.abspath(__file__))
UPLOAD_FOLDER = os.path.join(APP_ROOT, 'pics')
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER

#set config to download files
app.add_url_rule('/pics/<filename>', 'uploaded_file',
                 build_only=True)
app.wsgi_app = SharedDataMiddleware(app.wsgi_app, {'/pics':  app.config['UPLOAD_FOLDER']})


#api end points

@app.route('/')
def hello_world():
    return '<html><head></head><body><h1>ITP341: Project API</h1><br><h3>Tushar Gupta</h3></body></html>'


@app.route('/adduser', methods=['GET','POST'])
def add_user():
    """
    Add or update user information
    """
    if request.method == 'POST':
        print('In Adduser post method')
        data = request.form
        pics=[]
        try:
            #save in database here, use userid when actually saving
            for name in request.files:
                    f = request.files[name]
                    filename = secure_filename(f.filename)
                    pics.append(filename)

            userid=data.get('userid',None)
            resp = AddUpdateUser(data['name'], data['dob'], data['email'], data['mentorfor'], \
            data['learn'], data['about'], str(pics), userid)
            #error, return message
            if resp == -1:
                print('In Adduser post method: returning -1')
                return json.dumps({'msg':'error', 'code':-1})
            #else return userid
            else:
                #del pics[:]
                print('In Adduser post method: returning 200')
                for name in request.files:
                    f = request.files[name]
                    filename = str(resp) + '_' + secure_filename(f.filename)
                    #pics.append("http://tushki1405.pythonanywhere.com/pics/" + filename)
                    f.save(os.path.join(app.config['UPLOAD_FOLDER'], filename))
                return json.dumps({'msg':'success', 'code':200, 'userid':resp, 'name':data['name']})
        except Exception as e:
            return json.dumps({'msg':str(e), 'code':-1})
    return 'add user'

@app.route('/getmatches/<userid>', methods=['GET','POST'])
def get_matches(userid):
    """
    Get matches for the home screen list
    """
    if request.method == 'POST':
        try:
            userid = int(userid)
            data = GetMatches(userid)
            return json.dumps({'code':200, 'result':data})
        except Exception as e:
            return json.dumps({'msg':str(e), 'code':-1})
    return 'get matches for user - ' + userid


@app.route('/getsearch/<searchterm>', methods=['GET','POST'])
def get_search(searchterm):
    """
    Get matches for the home screen list
    """
    if request.method == 'POST':
        try:
            data = GetSearch(searchterm)
            return json.dumps({'code':200, 'result':data})
        except Exception as e:
            return json.dumps({'msg':str(e), 'code':-1})
    return 'get search for term - ' + searchterm


@app.route('/addcontact', methods=['GET','POST'])
def add_contact():
    """
    Add new contact requests
    """
    return 'add contact'

@app.route('/getcontacts/<userid>', methods=['GET'])
def get_contacts(userid):
    """
    get new contact requests for the user and mark them as received in the database
    """
    return 'get contacts - ' + userid




#private methods for database interaction

#method to add or update user info
def AddUpdateUser(name, dob, email, mentorfor, learn, about, pictures, id=None):
    print('In AddUpdateUser method')
    cnx = mysql.connector.connect(**config)
    cursor = cnx.cursor()

    #insert if id is none
    if id is None:
        print('In AddUpdateUser method: inserting data')
        query = ("insert into userinfo (name, dob, email, mentorfor, learn, about, pictures) "
        "values (%s,%s,%s,%s,%s,%s,%s);")
        data_query = (name, datetime.strptime(dob, '%m/%d/%Y').date(), email, mentorfor, learn, about, pictures)
        cursor.execute(query, data_query)
        user_no = cursor.lastrowid
        cnx.commit()
        cursor.close()
        cnx.close()
        print('In AddUpdateUser method: insert done')
        return  user_no
    else:
        print('Implement update functionality...')
    print('In AddUpdateUser method: returning -1')
    return -1


#method to find matches for the given userid
def GetMatches(userid):
    print('In GetMatches method')
    cnx = mysql.connector.connect(**config)

    #get mentorfor and learn values
    cursor = cnx.cursor()
    query = ("select mentorfor, learn from userinfo where id=%s;")
    data_query = (int(userid),)
    cursor.execute(query, data_query)

    mentorlist = []
    learnlist = []
    for (mentorfor, learn) in cursor:
        mentorlist = mentorfor.split(',')
        learnlist = learn.split(',')
    cursor.close()
    print (mentorlist, learnlist)

    #get good matches where both mentor and learn match
    cursor = cnx.cursor()
    query = ("select id, name, dob, email, mentorfor, learn, about, pictures from userinfo where ")

    #for matches, mentor should match users learn
    notFirst = False
    for s in mentorlist:
        if notFirst:
            query += (" or ")
        else:
            notFirst = True
            query += (" (")
        query += ("learn like '%"+ s.strip() +"%' ")

    if notFirst:
        query += (") ")

    if len(learnlist) > 0:
        query += (" and ")
        notFirst = False

    # and learn should match mentor
    for s in learnlist:
        if notFirst:
            query += (" or ")
        else:
            notFirst = True
            query += (" (")
        query += ("mentorfor like '%"+ s.strip() +"%' ")

    if notFirst:
        query += (");")

    print (query)

    cursor.execute(query)
    result = []
    for (id, name, dob, email, mentorfor, learn, about, pictures) in cursor:
        cur = {"id":id, "name":name, "dob":str(dob), "email":email, "mentorfor":mentorfor, "learn":learn, "about":about, "pictures":pictures}
        result.append(cur)
    print (result)

    cursor.close()
    cnx.close()
    return result


#method to find matches for the given userid
def GetSearch(searchterm):
    print('In GetSearch method')
    cnx = mysql.connector.connect(**config)

    #get mentorfor and learn values
    cursor = cnx.cursor()
    query = ("select id, name, dob, email, mentorfor, learn, about, pictures from userinfo where mentorfor like '%"+ searchterm +"%' or learn like '%"+ searchterm +"%';")
    print (query)
    cursor.execute(query)
    result = []
    for (id, name, dob, email, mentorfor, learn, about, pictures) in cursor:
        cur = {"id":id, "name":name, "dob":str(dob), "email":email, "mentorfor":mentorfor, "learn":learn, "about":about, "pictures":pictures}
        result.append(cur)
    print (result)

    cursor.close()
    cnx.close()
    return result

def AddContact():
    pass

def MarkReceived():
    pass



