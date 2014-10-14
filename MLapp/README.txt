README FOR RUNNING FLASK PYTHON APP:
1. Follow directions here for installing Flask and venv.
Installation
-----------
Flask depends on two external libraries, Werkzeug and Jinja2. Werkzeug is a toolkit for WSGI, the standard Python interface between web applications and a variety of servers for both development and deployment. Jinja2 renders templates.

So how do you get all that on your computer quickly? There are many ways you could do that, but the most kick-ass method is virtualenv, so let’s have a look at that first.

You will need Python 2.6 or higher to get started, so be sure to have an up-to-date Python 2.x installation. For using Flask with Python 3 have a look at Python 3 Support.

------------
A.  virtualenv

If you are on Mac OS X or Linux, chances are that one of the following two commands will work for you:

$ sudo easy_install virtualenv
or even better:

$ sudo pip install virtualenv
If you use Ubuntu, try:

$ sudo apt-get install python-virtualenv
Make sure you have Python 2.6 or above.
http://flask.pocoo.org/docs/0.10/installation/#installation


---------------------------------------------------------------------------------
B.  Once you have virtualenv installed

1. Go to the MLapp/ directory within our code base. Run:
$ virtualenv venv
New python executable in venv/bin/python
Installing distribute............done.

2. Now, whenever you want to work on a project, you only have to activate the corresponding environment. On OS X and Linux, do the following:

$ . venv/bin/activate

3. Now you can just enter the following command to get Flask activated in your virtualenv:

$ pip install Flask
A few seconds later and you are good to go.

NOTE:
If you get an error like "error: could not create '/Library/Python/2.7/site-packages/flask': Permission denied"
try 
$ sudo pip install Flask

--------------------------------------------------------------------------------
 
2. Run the server:
python hello.py

3. Go to the IP address listed in your browser.
