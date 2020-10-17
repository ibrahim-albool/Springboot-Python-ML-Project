package com.teach.predict.service;

import com.teach.predict.service.dto.HistoryDTO;
import com.teach.predict.service.dto.MLModelDTO;
import com.teach.predict.service.dto.TeacherDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface MLPythonService {

//    List<TeacherDTO> predictTeachers (String fileName);
//
//    String predictTeachers1(String fileName);

    Optional<String> trainMLModel(String trainingFile, String labelsFile);

    Optional<MLModelDTO> getModelMetrics();

    Optional<HistoryDTO> getModelHistory();

    Optional<List<TeacherDTO>> predictXTT();

    Optional<String> predictXNew(String newDataFile);

    Optional<String> deleteModelCache();
}
