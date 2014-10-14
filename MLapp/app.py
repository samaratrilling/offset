from flask import Flask, jsonify
from flask import make_response

app = Flask(__name__)

@app.route('/')
def hello_world():
    return 'Hello World!'

#Note: all return values should be in JSON format.
@app.route('/store', methods=['PUT'])
def store_results():
    return 'storing results'

@app.route('/nextmove', methods=['GET'])
def get_opponent_move():
    return 'getting opponent next move'

@app.errorhandler(404)
def not_found(error):
    return make_response(jsonify({'error': 'Not found'}), 404)

if __name__ == '__main__':
    app.debug = True
    app.run()
