# -*- coding: utf-8 -*-
"""
Created on Fri Oct 16 08:27:23 2020

@author: ialbool
"""


from helper import Helper
import logging
import time
import flask
from flask import jsonify,abort,request

app = flask.Flask(__name__)
#app.config["DEBUG"] = False


helper = Helper()

logging.basicConfig(format=format, level=logging.INFO,datefmt="%H:%M:%S")


#On Train request API
@app.route('/trainMLModel', methods=['PUT'])
def trainTheModel():
    trainingFile  = request.args.get('trainingFile', None)
    labelsFile  = request.args.get('labelsFile', None)
    res = helper.modelTrain(trainingFile,labelsFile)
    return jsonify(res)

#On Get Metrics API
@app.route('/modelMetrics', methods=['GET'])
def getModelMetrics():
    res = helper.getModelMetrics()
    return jsonify(res)

#On Get History API
@app.route('/modelHistory', methods=['GET'])
def getModelHistory():
    res = helper.getModelHistory()
    return jsonify(res)

#On Get predict Xtraint xtest
@app.route('/predictXTT', methods=['GET'])
def predictXTT():
    res = helper.predictXTrainTestData()
    return jsonify(res)

#On Get predict new xtest
@app.route('/predictXNew', methods=['GET'])
def predictXNewData():
    newDataFile  = request.args.get('newDataFile', None)
    res = helper.predictNewData(newDataFile)
    return jsonify(res)

#On delete the model cache to start a new training
@app.route('/deleteModelCache', methods=['DELETE'])
def deleteModelCache():
    global helper
    helper.clearHistory()
    helper = Helper()
    return jsonify(1)

app.run()


