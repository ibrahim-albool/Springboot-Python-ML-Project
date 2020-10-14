package com.teach.predict.service;

import com.teach.predict.service.dto.TeacherDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MLService {

    List<TeacherDTO> predictTeachers (String fileName);

}
