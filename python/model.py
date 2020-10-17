# -*- coding: utf-8 -*-
"""
Created on Thu Oct 15 02:39:55 2020

@author: ialbool
"""

import tensorflow as tf
import numpy as np
from tensorflow.keras.layers import Input, Dense,Dropout
from tensorflow.keras.models import Model
import tensorflow.keras.backend as K
import os.path
import os
import json
import errno


class MLModel:
    def __init__(self,x_shape=340,y_shape=93):
        print("constructor")
        input_shape = (x_shape, )
        inputs = Input(shape=input_shape, name='input')
        x = Dense(200, activation='relu')(inputs)
        x = Dropout(0.4)(x)
        x = Dense(200, activation='relu')(x)
        x = Dropout(0.4)(x)
        x = Dense(200, activation='relu')(x)
        x = Dropout(0.4)(x)
        output = Dense(y_shape, activation='sigmoid')(x)
        self.model = Model(inputs, output, name='teacher-model')
        self.model.compile(loss='binary_crossentropy', optimizer='adam', metrics=['accuracy'])
        self.loadModel()

    def train(self,X_train,Y_train):
        history = self.model.fit(X_train, Y_train, epochs=600, batch_size=100)
        self.history = history.history
        #self.history['accuracy'] = np.array(self.history['accuracy']).astype(np.float64).tolist()
        self.history['accuracy'] = np.array(self.history['accuracy']).astype(np.float64).tolist()
        _, self.accuracy = self.model.evaluate(X_train, Y_train)
    
    def predict(self,X):
        predictions = self.model.predict(X)
        return np.round(predictions)
        
    def evaluateTestData(self,X_test,Y_test):
        predictions = self.predict(X_test)
        
        data_size = Y_test.shape[0]*Y_test.shape[1]
        accuracy = (data_size-np.square(predictions-Y_test).sum())/data_size
        tp = np.sum(predictions*Y_test)
        fp = np.sum(predictions*(1-Y_test))
        tn = np.sum((1-predictions)*(1-Y_test))
        fn = np.sum((1-predictions)*Y_test)
        Prec = tp/(tp+fp)
        Rec = tp/(tp+fn)
        F1score = Prec*Rec/(Prec+Rec)
        
        metrics = {"data_size":data_size,
                   "accuracy":accuracy,
                   "tp":tp,
                   "fp":fp,
                   "tn":tn,
                   "fn":fn,
                   "precision":Prec,
                   "recall":Rec,
                   "F1score":F1score}
        self.metrics = metrics
        
    def clearModel(self):
        K.clear_session()
        if os.path.isfile('Model/weights.h5') == True and os.path.isfile('Model/metrics.json') and os.path.isfile('Model/history.json'):
            os.remove("Model/weights.h5")
            os.remove("Model/metrics.json")
            os.remove("Model/history.json")
        
    def saveModel(self):
        self.model.save_weights('Model/weights.h5')
        with open('Model/metrics.json', 'w') as outfile:
            json.dump(self.metrics, outfile)
        with open('Model/history.json', 'w') as outfile:
            json.dump(self.history, outfile)
        
    def loadModel(self):
        try:
            os.makedirs('Model')
        except OSError as e:
            if e.errno != errno.EEXIST:
                raise
        if os.path.isfile('Model/weights.h5') == True and os.path.isfile('Model/metrics.json') and os.path.isfile('Model/history.json'):
            self.model.load_weights('Model/weights.h5')
            with open('Model/metrics.json') as json_file:
                self.metrics = json.load(json_file)
            with open('Model/history.json') as json_file:
                self.history = json.load(json_file)


        
        
        
        
        
        

