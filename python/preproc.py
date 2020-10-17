# -*- coding: utf-8 -*-
"""
Created on Thu Oct 15 02:39:31 2020

@author: ialbool
"""

import pandas as pd
import numpy as np
from sklearn.preprocessing import MinMaxScaler
from sklearn.preprocessing import StandardScaler
from sklearn.preprocessing import RobustScaler
from datetime import datetime
import os.path
import os
import json
import errno

class DataNormalize:
    def __init__(self, X, method='Standard'):
        X = X.reshape(-1,1)
        if method=='Standard':
            self.transformer = StandardScaler().fit(X)
        elif method=='MinMax':
            self.transformer = MinMaxScaler().fit(X)
        elif method=='Robust':
            self.transformer = RobustScaler().fit(X)
    def normalize(self, X):
        return self.transformer.transform(X.reshape(-1,1))
    
class DataPreProcess:
    def __init__(self):
        self.specNumToName={
            0:'English',
            1:'Science',
            2:'Arabic',
            3:'Math'
            }
        self.qualNumToName={
            0:'Diploma',
            1:'Bachelor',
            2:'Master',
            3:'PhD'
            }
        self.stageNumToName={
            0:'Primary',
            1:'Secondary'
            }
        self.loadData()
    
    def procX(self,dataXFile):
        teachers_courses = pd.read_excel(dataXFile)
        
        self.orig_teachers_courses = teachers_courses.copy()
        self.orig_teachers_courses = self.orig_teachers_courses.groupby('number').max()[['specialization','evaluation','qualification','stage','sumOfHours']].astype('int')
        
        
        teachers_courses['number']=teachers_courses['number']-1
        teachers = teachers_courses.groupby('number').max()[['specialization','evaluation','qualification','stage','sumOfHours']]
        
        
        #Normalizaion
        normalizationMethod = 'Standard'
        #normalizationMethod = 'MinMax'
        #normalizationMethod = 'Robust'
        
        evaluationNormalizer = DataNormalize(teachers['evaluation'].to_numpy(),normalizationMethod)
        evaluation = evaluationNormalizer.normalize(teachers['evaluation'].to_numpy())
        teachers['evaluation']=evaluation.reshape(-1,)
        
        sumNormalizer = DataNormalize(teachers['sumOfHours'].to_numpy(),normalizationMethod)
        sumT = sumNormalizer.normalize(teachers['sumOfHours'].to_numpy())
        teachers['sumOfHours']=sumT.reshape(-1,)
        self.teachers = teachers
        #teachers.to_excel('normalized_teacher.xlsx')
        
        unique_courses = teachers_courses.groupby(['specialization'])['courses'].unique()
        courses_inverse = np.zeros((unique_courses.shape[0],unique_courses[0].max()+1))
        for i in range(unique_courses.shape[0]):
            for u in range(unique_courses[i].shape[0]):
                courses_inverse[i][unique_courses[i][u]]=u
        self.courses_inverse=courses_inverse
        courses_abolute_address = np.zeros((teachers_courses.shape[0],))
        for i in range(courses_abolute_address.shape[0]):
            courses_abolute_address[i] = teachers_courses['specialization'][i]*86 + courses_inverse[teachers_courses['specialization'][i]][teachers_courses['courses'][i]]
        courses_abolute_address = courses_abolute_address.astype(np.int64)
        self.course_absolute_address_max = courses_abolute_address.max()+1
        
        teachers_indexes = teachers_courses.groupby('number').max().index
        teachers_encoded_coursers = np.zeros((teachers_indexes.shape[0],courses_abolute_address.max()+1))
        for teacher_no in teachers_indexes:
            teachers_encoded_coursers[teacher_no][courses_abolute_address[teachers_courses['number'].loc[teachers_courses['number'] == teacher_no].index]] = 1
            
        
        #generete X
        X_original = np.zeros((teachers_encoded_coursers.shape[0],11+329))
        X_original[:,11:] = teachers_encoded_coursers
        X_original[:,[4,9,10]] = teachers.to_numpy()[:,[1,3,4]]
        for i in range(X_original.shape[0]):
            X_original[i,[int(teachers.to_numpy()[i,0]),int(teachers.to_numpy()[i,2]+5)]]=1
            
        
        #shuffle teachers
        teachers_indexes = np.arange(X_original.shape[0])
        np.random.shuffle(teachers_indexes)
        X = X_original[teachers_indexes,:]
        self.X = X
        self.teachers_indexes = teachers_indexes
        
        
        
    def procNewX(self,filePath):
        teachers_courses = pd.read_excel(filePath)
        
        self.orig_teachers_courses = teachers_courses.copy()
        self.orig_teachers_courses = self.orig_teachers_courses.groupby('number').agg({'specialization':"max",
                                                       'hours':"sum",
                                                       'evaluation':"max",
                                                       'qualification':"max",
                                                       'stage':"max",
                                                       'sumOfHours':"max"}).astype('int')
        self.orig_teachers_courses['sumOfHours']=self.orig_teachers_courses['hours']
        self.orig_teachers_courses = self.orig_teachers_courses.drop(columns=['hours'])
        
        
        self.teachers_offset = teachers_courses['number'].min()
        teachers_courses['number']=teachers_courses['number']-self.teachers_offset
        #teachers = teachers_courses.groupby('number').max()[['specialization','evaluation','qualification','stage','sumOfHours']]
        teachers = teachers_courses.groupby('number').agg({'specialization':"max",
                                                       'hours':"sum",
                                                       'evaluation':"max",
                                                       'qualification':"max",
                                                       'stage':"max",
                                                       'sumOfHours':"max"})
        teachers['sumOfHours']=teachers['hours']
        teachers = teachers.drop(columns=['hours'])
        
        
        #Normalizaion
        normalizationMethod = 'Standard'
        #normalizationMethod = 'MinMax'
        #normalizationMethod = 'Robust'
        
        evaluationNormalizer = DataNormalize(teachers['evaluation'].to_numpy(),normalizationMethod)
        evaluation = evaluationNormalizer.normalize(teachers['evaluation'].to_numpy())
        teachers['evaluation']=evaluation.reshape(-1,)
        
        sumNormalizer = DataNormalize(teachers['sumOfHours'].to_numpy(),normalizationMethod)
        sumT = sumNormalizer.normalize(teachers['sumOfHours'].to_numpy())
        teachers['sumOfHours']=sumT.reshape(-1,)
        self.teachers = teachers
        #teachers.to_excel('normalized_teacher.xlsx')
        
        #unique_courses = teachers_courses.groupby(['specialization'])['courses'].unique()
        #courses_inverse = np.zeros((unique_courses.shape[0],unique_courses[0].max()+1))
        #for i in range(unique_courses.shape[0]):
        #    for u in range(unique_courses[i].shape[0]):
        #        courses_inverse[i][unique_courses[i][u]]=u
        courses_abolute_address = np.zeros((teachers_courses.shape[0],))
        for i in range(courses_abolute_address.shape[0]):
            courses_abolute_address[i] = teachers_courses['specialization'][i]*86 + self.courses_inverse[teachers_courses['specialization'][i]][teachers_courses['courses'][i]]
        courses_abolute_address = courses_abolute_address.astype(np.int64)
        
        
        teachers_indexes = teachers_courses.groupby('number').max().index
        teachers_encoded_coursers = np.zeros((teachers_indexes.shape[0],self.course_absolute_address_max))
        for teacher_no in teachers_indexes:
            teachers_encoded_coursers[teacher_no][courses_abolute_address[teachers_courses['number'].loc[teachers_courses['number'] == teacher_no].index]] = 1
            
        
        #generete X
        X_original = np.zeros((teachers_encoded_coursers.shape[0],11+329))
        X_original[:,11:] = teachers_encoded_coursers
        X_original[:,[4,9,10]] = teachers.to_numpy()[:,[1,3,4]]
        for i in range(X_original.shape[0]):
            X_original[i,[int(teachers.to_numpy()[i,0]),int(teachers.to_numpy()[i,2]+5)]]=1

        self.X = X_original

        
        
    def procY(self,dataYFile):
        sgstd_courses = pd.read_excel(dataYFile) 
        sgstd_courses['number']-=1
        
        teachers_courses_suggested = pd.merge(sgstd_courses, self.teachers['specialization'], left_on='number', right_on='number')
        
        unique_courses_y = teachers_courses_suggested.groupby(['specialization'])['suggestion'].unique()
        self.unique_courses_y = unique_courses_y
        courses_inverse_y = np.zeros((unique_courses_y.shape[0],unique_courses_y[2].max()+1))
        for i in range(unique_courses_y.shape[0]):
            for u in range(unique_courses_y[i].shape[0]):
                courses_inverse_y[i][unique_courses_y[i][u]]=u
        courses_abolute_address_y = np.zeros((teachers_courses_suggested.shape[0],))
        for i in range(courses_abolute_address_y.shape[0]):
            courses_abolute_address_y[i] = teachers_courses_suggested['specialization'][i]*25 + courses_inverse_y[teachers_courses_suggested['specialization'][i]][teachers_courses_suggested['suggestion'][i]]
        courses_abolute_address_y = courses_abolute_address_y.astype(np.int64)
        
        
        teachers_indexes_y = teachers_courses_suggested.groupby('number').max().index
        teachers_encoded_coursers_y = np.zeros((teachers_indexes_y.shape[0],courses_abolute_address_y.max()+1))
        for teacher_no in teachers_indexes_y:
            teachers_encoded_coursers_y[teacher_no][courses_abolute_address_y[teachers_courses_suggested['number'].loc[teachers_courses_suggested['number'] == teacher_no].index]] = 1
        teachers_encoded_coursers_y[[286 , 904],:]=0
        Y_orginal = teachers_encoded_coursers_y
        self.Y_orginal = Y_orginal
        Y = Y_orginal[self.teachers_indexes,:]
        self.Y = Y
        return Y

    def splitTrainTestData(self):
        X = self.X
        Y = self.Y
        self.X_train = X[:850,:]
        self.X_test = X[850:,:]
        self.Y_train = Y[:850,:]
        self.Y_test = Y[850:,:]
        return self.X_train,self.Y_train,self.X_test,self.Y_test
    
    def decodeTeacher(self,index,isTestSet=True):
        teachers_indexes = self.teachers_indexes
        if isTestSet==True:
            the_teacher=teachers_indexes[index+850]
        else:
            the_teacher=teachers_indexes[index]
        return the_teacher
    
    def decodeTeacherCourses(self,index,predictions):
        val = predictions[index]
        spec_res = np.where(val==1)[0]/25
        spec_res = spec_res.astype(np.int64)
        course_res = np.where(val==1)[0]%25
        course_res = course_res.astype(np.int64)
        list_of_teacher_courses = []
        for i in range(spec_res.shape[0]):
            list_of_teacher_courses.append({"specialization":self.specNumToName[spec_res[i]],
                                            "code":int(self.unique_courses_y[spec_res[i]][course_res[i]])})
        return list_of_teacher_courses
    
    def teacherToJson(self,index,predictions,isTestSet=True,isOrginalSet=True):
        if isOrginalSet==True:
            teacherNo = self.decodeTeacher(index,isTestSet)
        else:
            teacherNo = index
        courses = self.decodeTeacherCourses(index,predictions)
        teachers = self.orig_teachers_courses
        teacherDict = teachers.iloc[teacherNo].to_dict()
        if isOrginalSet==True:
            teacherDict['number']=int(teacherNo+1)
        else:
            teacherDict['number']=int(teacherNo+self.teachers_offset)
        if teacherDict['evaluation']>=90:
            teacherDict['courses']=None
        else:
            teacherDict['courses']=courses
        teacherDict['specialization']=self.specNumToName[teacherDict['specialization']]
        teacherDict['qualification']=self.qualNumToName[teacherDict['qualification']]
        teacherDict['stage']=self.stageNumToName[teacherDict['stage']]
        return teacherDict
    
    def teachersToJson(self,predictions,isTestSet=True,isOrginalSet=True):
        teachers=[]
        now = datetime.now()
        d = now.strftime("%Y-%m-%dT%H:%M:%SZ")
        for i in range(predictions.shape[0]):
            teacher = self.teacherToJson(i,predictions,isTestSet,isOrginalSet)
            teacher['isPredicted'] = 'true' if isTestSet == True else 'false'
            teacher['creationDate'] = d
            teachers.append(teacher)
        return teachers
    def loadData(self):
        try:
            os.makedirs('Data')
        except OSError as e:
            if e.errno != errno.EEXIST:
                raise
        if os.path.isfile('Data/X') == True:
            self.orig_teachers_courses = pd.read_pickle('Data/orig_teachers_courses')
            self.teachers = pd.read_pickle('Data/teachers')
            self.unique_courses_y = pd.read_pickle('Data/unique_courses_y')
            
            with open('Data/courses_inverse', 'rb') as json_file:
                self.courses_inverse = np.load(json_file)
            with open('Data/course_absolute_address_max', 'rb') as json_file:
                self.course_absolute_address_max = np.load(json_file)
            with open('Data/X', 'rb') as json_file:
                self.X = np.load(json_file)
            with open('Data/teachers_indexes', 'rb') as json_file:
                self.teachers_indexes = np.load(json_file)
            with open('Data/Y_orginal', 'rb') as json_file:
                self.Y_orginal = np.load(json_file)
            with open('Data/Y', 'rb') as json_file:
                self.Y = np.load(json_file)
            with open('Data/X_train', 'rb') as json_file:
                self.X_train = np.load(json_file)
            with open('Data/Y_train', 'rb') as json_file:
                self.Y_train = np.load(json_file)
            with open('Data/X_test', 'rb') as json_file:
                self.X_test = np.load(json_file)
            with open('Data/Y_test', 'rb') as json_file:
                self.Y_test = np.load(json_file)

    def saveData(self):
        self.orig_teachers_courses.to_pickle('Data/orig_teachers_courses')
        self.teachers.to_pickle('Data/teachers')
        self.unique_courses_y.to_pickle('Data/unique_courses_y')
            
        with open('Data/courses_inverse', 'wb') as outfile:
            np.save(outfile, self.courses_inverse)
        with open('Data/course_absolute_address_max', 'wb') as outfile:
            np.save(outfile, self.course_absolute_address_max)
        with open('Data/X', 'wb') as outfile:
            np.save(outfile, self.X)
        with open('Data/teachers_indexes', 'wb') as outfile:
            np.save(outfile, self.teachers_indexes)
        with open('Data/Y_orginal', 'wb') as outfile:
            np.save(outfile, self.Y_orginal)
        with open('Data/Y', 'wb') as outfile:
            np.save(outfile, self.Y)
        with open('Data/X_train', 'wb') as outfile:
            np.save(outfile, self.X_train)
        with open('Data/Y_train', 'wb') as outfile:
            np.save(outfile, self.Y_train)
        with open('Data/X_test', 'wb') as outfile:
            np.save(outfile, self.X_test)
        with open('Data/Y_test', 'wb') as outfile:
            np.save(outfile, self.Y_test)
            
    def clearData(self):
        if os.path.isfile('Data/X') == True:
            os.remove("Data/orig_teachers_courses")
            os.remove("Data/teachers")
            os.remove("Data/courses_inverse")
            os.remove("Data/course_absolute_address_max")
            os.remove("Data/X")
            os.remove("Data/teachers_indexes")
            os.remove("Data/unique_courses_y")
            os.remove("Data/Y_orginal")
            os.remove("Data/Y")
            os.remove("Data/X_train")
            os.remove("Data/Y_train")
            os.remove("Data/X_test")
            os.remove("Data/Y_test")
            

