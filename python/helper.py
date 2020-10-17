# -*- coding: utf-8 -*-
"""
Created on Fri Oct 16 05:31:01 2020

@author: ialbool
"""

from preproc import DataPreProcess
from model import MLModel
import logging
import threading


class Helper:
    def __init__(self):
        self.data = DataPreProcess()
        self.model = MLModel()
        format = "%(asctime)s: %(message)s"
        logging.basicConfig(format=format, level=logging.INFO,datefmt="%H:%M:%S")
        
    def modelTrain(self,dataXFile,dataYFile):
        try:
            if self.model.metrics is not None: # The variable
                return None
        except AttributeError:
            threading.Thread(target=self.trainThread, args=(dataXFile,dataYFile)).start()
            return "Model is training"
        
    def trainThread(self,dataXFile,dataYFile):
        logging.info("Training Started!")
        self.data.procX(dataXFile)
        self.data.procY(dataYFile)
        X_train,Y_train,X_test,Y_test = self.data.splitTrainTestData()
        self.model.train(X_train, Y_train)
        self.model.evaluateTestData(X_test,Y_test)
        self.model.saveModel()
        self.data.saveData()
        logging.info("Training is done!")
        
    def getModelMetrics(self):
        try:
            if self.model.metrics is not None: # The variable
                return self.model.metrics
        except AttributeError:
            return None
        
    def getModelHistory(self):
        try:
            if self.model.metrics is not None: # The variable
                return self.model.history
        except AttributeError:
            return None
        
    def predictXTrainTestData(self):
        return self.data.teachersToJson(self.model.predict(self.data.X_train),False,True) + self.data.teachersToJson(self.model.predict(self.data.X_test),True,True)
    
    def predictNewData(self,filePath):
        try:
            if self.model.metrics is not None: 
                self.data.procNewX(filePath)
                return self.data.teachersToJson(self.model.predict(self.data.X),True,False)
        except AttributeError: # Model is not trained yet
            return None
        
    def clearHistory(self):
        self.model.clearModel()
        self.data.clearData()

    
        
#class ModelLoader:
    