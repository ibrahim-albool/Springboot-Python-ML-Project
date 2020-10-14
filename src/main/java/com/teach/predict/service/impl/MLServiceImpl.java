package com.teach.predict.service.impl;

import com.teach.predict.MLAPIs.MLClientAPIs;
import com.teach.predict.service.MLService;
import com.teach.predict.service.dto.TeacherDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MLServiceImpl implements MLService {

    @Autowired
    private MLClientAPIs mlClientAPIs;

    @Override
    public List<TeacherDTO> predictTeachers(String fileName) {
        String path = "";
        ResponseEntity<List<TeacherDTO>> result = mlClientAPIs.performMLApiRequest(
            mlClientAPIs.getMLFullUrl(path),
            null,
            HttpMethod.GET,
            List.class);
        return result.getBody();
    }

}
