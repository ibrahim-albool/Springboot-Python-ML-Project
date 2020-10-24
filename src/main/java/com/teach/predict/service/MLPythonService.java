package com.teach.predict.service;

import com.teach.predict.service.dto.HistoryDTO;
import com.teach.predict.service.dto.MLModelDTO;
import com.teach.predict.service.dto.TeacherDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;
import java.util.Optional;

@Service
public interface MLPythonService {

    Optional<String> trainMLModel(String trainingFile, String labelsFile);

    Optional<MLModelDTO> getModelMetrics();

    Optional<HistoryDTO> getModelHistory();

    Optional<List<TeacherDTO>> predictXTT();

    Optional<String> predictXNew(String newDataFile) throws ResourceAccessException, HttpClientErrorException;

    Optional<String> deleteModelCache() throws ResourceAccessException;
}
