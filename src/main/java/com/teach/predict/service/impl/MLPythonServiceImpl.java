package com.teach.predict.service.impl;

import com.teach.predict.MLAPIs.MLClientAPIs;
import com.teach.predict.service.MLModelService;
import com.teach.predict.service.MLPythonService;
import com.teach.predict.service.TeacherService;
import com.teach.predict.service.dto.HistoryDTO;
import com.teach.predict.service.dto.MLModelDTO;
import com.teach.predict.service.dto.TeacherDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MLPythonServiceImpl implements MLPythonService {

    private static final Logger log = LoggerFactory.getLogger(MLPythonServiceImpl.class);

    @Autowired
    private MLClientAPIs mlClientAPIs;

    @Autowired
    private MLModelService mlModelService;

    @Autowired
    private TeacherService teacherService;

    @Override
    public Optional<String> trainMLModel(String trainingFile, String labelsFile) {
        String path = "trainMLModel?trainingFile=" + trainingFile + "&labelsFile=" + labelsFile;
        ResponseEntity<String> result =  mlClientAPIs.performMLApiRequest(
            mlClientAPIs.getMLFullUrl(path),
            null,
            HttpMethod.PUT,
            String.class);
        if(result.getStatusCode() == HttpStatus.OK && result.getBody()!=null && !result.getBody().contains("null")){
            Thread thread = new Thread(() -> syncModelTrainingData());
            thread.start();
            return Optional.of(result.getBody());
        }
        return Optional.empty();
    }

    private void syncModelTrainingData(){
        boolean successfullyGotMetrics=false;
        boolean successfullyGotData=false;
        //5 min. waiting.
        for (int i = 0; i < 60; i++) {
            Optional<MLModelDTO> metrics = getModelMetrics();
            if (metrics.isPresent()){
                if(mlModelService.count()>0){
                    mlModelService.deleteAll();
                }
                mlModelService.save(metrics.get());
                successfullyGotMetrics=true;
                break;
            }
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(!successfullyGotMetrics){
            log.error("Metrics retrieval is taking too much time.");
            Thread.currentThread().interrupt();
        }
        //5 min. waiting
        for (int i = 0; i < 60; i++) {
            Optional<List<TeacherDTO>> teachers = predictXTT();
            if (teachers.isPresent()){
                if(teacherService.count()>0){
                    teacherService.deleteAll();
                }
                log.info("Storing training teachers");
                teacherService.saveAll(teachers.get());
                successfullyGotData=true;
                break;
            }
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(!successfullyGotData){
            log.error("Data retrieval is taking too much time.");
        }

    }

    @Override
    public Optional<MLModelDTO> getModelMetrics() {
        String path = "modelMetrics";
        ResponseEntity<MLModelDTO> result = mlClientAPIs.performMLApiRequest(
            mlClientAPIs.getMLFullUrl(path),
            null,
            HttpMethod.GET,
            MLModelDTO.class);
        if(result.getStatusCode() == HttpStatus.OK && result.getBody()!=null){
            //save the metrics;
            return Optional.of(result.getBody());
        }
        return Optional.empty();
    }

    @Override
    public Optional<HistoryDTO> getModelHistory() {
        String path = "modelHistory";
        ResponseEntity<HistoryDTO> result = mlClientAPIs.performMLApiRequest(
            mlClientAPIs.getMLFullUrl(path),
            null,
            HttpMethod.GET,
            HistoryDTO.class);
        if(result.getStatusCode() == HttpStatus.OK && result.getBody()!=null){
            //save the metrics;
            return Optional.of(result.getBody());
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<TeacherDTO>> predictXTT() {
        String path = "predictXTT";
        ResponseEntity<List<TeacherDTO>> result = mlClientAPIs.performMLApiRequest(
            mlClientAPIs.getMLFullUrl(path),
            null,
            HttpMethod.GET,
            List.class);
        if(result.getStatusCode() == HttpStatus.OK && result.getBody()!=null){
            //return the training data;
            return Optional.of(result.getBody());
        }
        return Optional.empty();
    }

    @Override
    public Optional<String> predictXNew(String newDataFile) {
        String path = "predictXTT?newDataFile=" + newDataFile;
        ResponseEntity<List<TeacherDTO>> result = mlClientAPIs.performMLApiRequest(
            mlClientAPIs.getMLFullUrl(path),
            null,
            HttpMethod.GET,
            List.class);
        if (result.getStatusCode() == HttpStatus.OK && result.getBody() != null && result.getBody().size() > 0) {
            Thread thread = new Thread(() -> {
                log.info("Storing predicted teachers");
                List<TeacherDTO> teachers = result.getBody();
                teacherService.saveAll(teachers);
            });
            thread.start();
            return Optional.of("Prediction succeeded.");
        }
        return Optional.empty();
    }

    @Override
    public Optional<String> deleteModelCache() {
        String path = "deleteModelCache";
        ResponseEntity<String> result =  mlClientAPIs.performMLApiRequest(
            mlClientAPIs.getMLFullUrl(path),
            null,
            HttpMethod.DELETE,
            String.class);
        if(result.getStatusCode() == HttpStatus.OK && result.getBody()!=null){
            return Optional.of(result.getBody());
        }
        return Optional.empty();
    }

}
