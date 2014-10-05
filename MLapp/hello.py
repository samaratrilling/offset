from flask import Flask
app = Flask(__name__)

@app.route('/')
def hello_world():
    return 'Hello World!'

@app.route('/store', methods=['PUT'])
def store_results():
    return 'storing results'

@app.route('/nextmove', methods=['GET'])
def get_opponent_move():
    return 'getting opponent next move'


if __name__ == '__main__':
    app.debug = True
    app.run()
