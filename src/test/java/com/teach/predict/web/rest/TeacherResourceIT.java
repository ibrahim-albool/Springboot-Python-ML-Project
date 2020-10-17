package com.teach.predict.web.rest;

import com.teach.predict.TeachPredictorApp;
import com.teach.predict.domain.Teacher;
import com.teach.predict.domain.Course;
import com.teach.predict.repository.TeacherRepository;
import com.teach.predict.service.TeacherService;
import com.teach.predict.service.dto.TeacherDTO;
import com.teach.predict.service.mapper.TeacherMapper;
import com.teach.predict.service.dto.TeacherCriteria;
import com.teach.predict.service.TeacherQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.teach.predict.domain.enumeration.Specialization;
import com.teach.predict.domain.enumeration.Qualification;
import com.teach.predict.domain.enumeration.Stage;
/**
 * Integration tests for the {@link TeacherResource} REST controller.
 */
@SpringBootTest(classes = TeachPredictorApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class TeacherResourceIT {

    private static final Long DEFAULT_NUMBER = 1L;
    private static final Long UPDATED_NUMBER = 2L;
    private static final Long SMALLER_NUMBER = 1L - 1L;

    private static final Specialization DEFAULT_SPECIALIZATION = Specialization.Arabic;
    private static final Specialization UPDATED_SPECIALIZATION = Specialization.English;

    private static final Integer DEFAULT_EVALUATION = 1;
    private static final Integer UPDATED_EVALUATION = 2;
    private static final Integer SMALLER_EVALUATION = 1 - 1;

    private static final Qualification DEFAULT_QUALIFICATION = Qualification.Diploma;
    private static final Qualification UPDATED_QUALIFICATION = Qualification.Bachelor;

    private static final Stage DEFAULT_STAGE = Stage.Primary;
    private static final Stage UPDATED_STAGE = Stage.Secondary;

    private static final Integer DEFAULT_SUM_OF_HOURS = 1;
    private static final Integer UPDATED_SUM_OF_HOURS = 2;
    private static final Integer SMALLER_SUM_OF_HOURS = 1 - 1;

    private static final Boolean DEFAULT_IS_PREDICTED = false;
    private static final Boolean UPDATED_IS_PREDICTED = true;

    @Autowired
    private TeacherRepository teacherRepository;

    @Mock
    private TeacherRepository teacherRepositoryMock;

    @Autowired
    private TeacherMapper teacherMapper;

    @Mock
    private TeacherService teacherServiceMock;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private TeacherQueryService teacherQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTeacherMockMvc;

    private Teacher teacher;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Teacher createEntity(EntityManager em) {
        Teacher teacher = new Teacher()
            .number(DEFAULT_NUMBER)
            .specialization(DEFAULT_SPECIALIZATION)
            .evaluation(DEFAULT_EVALUATION)
            .qualification(DEFAULT_QUALIFICATION)
            .stage(DEFAULT_STAGE)
            .sumOfHours(DEFAULT_SUM_OF_HOURS)
            .isPredicted(DEFAULT_IS_PREDICTED);
        return teacher;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Teacher createUpdatedEntity(EntityManager em) {
        Teacher teacher = new Teacher()
            .number(UPDATED_NUMBER)
            .specialization(UPDATED_SPECIALIZATION)
            .evaluation(UPDATED_EVALUATION)
            .qualification(UPDATED_QUALIFICATION)
            .stage(UPDATED_STAGE)
            .sumOfHours(UPDATED_SUM_OF_HOURS)
            .isPredicted(UPDATED_IS_PREDICTED);
        return teacher;
    }

    @BeforeEach
    public void initTest() {
        teacher = createEntity(em);
    }

    @Test
    @Transactional
    public void createTeacher() throws Exception {
        int databaseSizeBeforeCreate = teacherRepository.findAll().size();
        // Create the Teacher
        TeacherDTO teacherDTO = teacherMapper.toDto(teacher);
        restTeacherMockMvc.perform(post("/api/teachers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(teacherDTO)))
            .andExpect(status().isCreated());

        // Validate the Teacher in the database
        List<Teacher> teacherList = teacherRepository.findAll();
        assertThat(teacherList).hasSize(databaseSizeBeforeCreate + 1);
        Teacher testTeacher = teacherList.get(teacherList.size() - 1);
        assertThat(testTeacher.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testTeacher.getSpecialization()).isEqualTo(DEFAULT_SPECIALIZATION);
        assertThat(testTeacher.getEvaluation()).isEqualTo(DEFAULT_EVALUATION);
        assertThat(testTeacher.getQualification()).isEqualTo(DEFAULT_QUALIFICATION);
        assertThat(testTeacher.getStage()).isEqualTo(DEFAULT_STAGE);
        assertThat(testTeacher.getSumOfHours()).isEqualTo(DEFAULT_SUM_OF_HOURS);
        assertThat(testTeacher.isIsPredicted()).isEqualTo(DEFAULT_IS_PREDICTED);
    }

    @Test
    @Transactional
    public void createTeacherWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = teacherRepository.findAll().size();

        // Create the Teacher with an existing ID
        teacher.setNumber(1L);
        TeacherDTO teacherDTO = teacherMapper.toDto(teacher);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTeacherMockMvc.perform(post("/api/teachers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(teacherDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Teacher in the database
        List<Teacher> teacherList = teacherRepository.findAll();
        assertThat(teacherList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllTeachers() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList
        restTeacherMockMvc.perform(get("/api/teachers?sort=number,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].specialization").value(hasItem(DEFAULT_SPECIALIZATION.toString())))
            .andExpect(jsonPath("$.[*].evaluation").value(hasItem(DEFAULT_EVALUATION)))
            .andExpect(jsonPath("$.[*].qualification").value(hasItem(DEFAULT_QUALIFICATION.toString())))
            .andExpect(jsonPath("$.[*].stage").value(hasItem(DEFAULT_STAGE.toString())))
            .andExpect(jsonPath("$.[*].sumOfHours").value(hasItem(DEFAULT_SUM_OF_HOURS)))
            .andExpect(jsonPath("$.[*].isPredicted").value(hasItem(DEFAULT_IS_PREDICTED.booleanValue())));
    }

    @SuppressWarnings({"unchecked"})
    public void getAllTeachersWithEagerRelationshipsIsEnabled() throws Exception {
        when(teacherServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTeacherMockMvc.perform(get("/api/teachers?eagerload=true"))
            .andExpect(status().isOk());

        verify(teacherServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllTeachersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(teacherServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTeacherMockMvc.perform(get("/api/teachers?eagerload=true"))
            .andExpect(status().isOk());

        verify(teacherServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getTeacher() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get the teacher
        restTeacherMockMvc.perform(get("/api/teachers/{id}", teacher.getNumber()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER.intValue()))
            .andExpect(jsonPath("$.specialization").value(DEFAULT_SPECIALIZATION.toString()))
            .andExpect(jsonPath("$.evaluation").value(DEFAULT_EVALUATION))
            .andExpect(jsonPath("$.qualification").value(DEFAULT_QUALIFICATION.toString()))
            .andExpect(jsonPath("$.stage").value(DEFAULT_STAGE.toString()))
            .andExpect(jsonPath("$.sumOfHours").value(DEFAULT_SUM_OF_HOURS))
            .andExpect(jsonPath("$.isPredicted").value(DEFAULT_IS_PREDICTED.booleanValue()));
    }


    @Test
    @Transactional
    public void getTeachersByNumberFiltering() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        Long number = teacher.getNumber();

        defaultTeacherShouldBeFound("number.equals=" + number);
        defaultTeacherShouldNotBeFound("number.notEquals=" + number);

        defaultTeacherShouldBeFound("number.greaterThanOrEqual=" + number);
        defaultTeacherShouldNotBeFound("number.greaterThan=" + number);

        defaultTeacherShouldBeFound("number.lessThanOrEqual=" + number);
        defaultTeacherShouldNotBeFound("number.lessThan=" + number);
    }


    @Test
    @Transactional
    public void getAllTeachersByNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where number equals to DEFAULT_NUMBER
        defaultTeacherShouldBeFound("number.equals=" + DEFAULT_NUMBER);

        // Get all the teacherList where number equals to UPDATED_NUMBER
        defaultTeacherShouldNotBeFound("number.equals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void getAllTeachersByNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where number not equals to DEFAULT_NUMBER
        defaultTeacherShouldNotBeFound("number.notEquals=" + DEFAULT_NUMBER);

        // Get all the teacherList where number not equals to UPDATED_NUMBER
        defaultTeacherShouldBeFound("number.notEquals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void getAllTeachersByNumberIsInShouldWork() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where number in DEFAULT_NUMBER or UPDATED_NUMBER
        defaultTeacherShouldBeFound("number.in=" + DEFAULT_NUMBER + "," + UPDATED_NUMBER);

        // Get all the teacherList where number equals to UPDATED_NUMBER
        defaultTeacherShouldNotBeFound("number.in=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void getAllTeachersByNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where number is not null
        defaultTeacherShouldBeFound("number.specified=true");

        // Get all the teacherList where number is null
        defaultTeacherShouldNotBeFound("number.specified=false");
    }

    @Test
    @Transactional
    public void getAllTeachersByNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where number is greater than or equal to DEFAULT_NUMBER
        defaultTeacherShouldBeFound("number.greaterThanOrEqual=" + DEFAULT_NUMBER);

        // Get all the teacherList where number is greater than or equal to UPDATED_NUMBER
        defaultTeacherShouldNotBeFound("number.greaterThanOrEqual=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void getAllTeachersByNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where number is less than or equal to DEFAULT_NUMBER
        defaultTeacherShouldBeFound("number.lessThanOrEqual=" + DEFAULT_NUMBER);

        // Get all the teacherList where number is less than or equal to SMALLER_NUMBER
        defaultTeacherShouldNotBeFound("number.lessThanOrEqual=" + SMALLER_NUMBER);
    }

    @Test
    @Transactional
    public void getAllTeachersByNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where number is less than DEFAULT_NUMBER
        defaultTeacherShouldNotBeFound("number.lessThan=" + DEFAULT_NUMBER);

        // Get all the teacherList where number is less than UPDATED_NUMBER
        defaultTeacherShouldBeFound("number.lessThan=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void getAllTeachersByNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where number is greater than DEFAULT_NUMBER
        defaultTeacherShouldNotBeFound("number.greaterThan=" + DEFAULT_NUMBER);

        // Get all the teacherList where number is greater than SMALLER_NUMBER
        defaultTeacherShouldBeFound("number.greaterThan=" + SMALLER_NUMBER);
    }


    @Test
    @Transactional
    public void getAllTeachersBySpecializationIsEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where specialization equals to DEFAULT_SPECIALIZATION
        defaultTeacherShouldBeFound("specialization.equals=" + DEFAULT_SPECIALIZATION);

        // Get all the teacherList where specialization equals to UPDATED_SPECIALIZATION
        defaultTeacherShouldNotBeFound("specialization.equals=" + UPDATED_SPECIALIZATION);
    }

    @Test
    @Transactional
    public void getAllTeachersBySpecializationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where specialization not equals to DEFAULT_SPECIALIZATION
        defaultTeacherShouldNotBeFound("specialization.notEquals=" + DEFAULT_SPECIALIZATION);

        // Get all the teacherList where specialization not equals to UPDATED_SPECIALIZATION
        defaultTeacherShouldBeFound("specialization.notEquals=" + UPDATED_SPECIALIZATION);
    }

    @Test
    @Transactional
    public void getAllTeachersBySpecializationIsInShouldWork() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where specialization in DEFAULT_SPECIALIZATION or UPDATED_SPECIALIZATION
        defaultTeacherShouldBeFound("specialization.in=" + DEFAULT_SPECIALIZATION + "," + UPDATED_SPECIALIZATION);

        // Get all the teacherList where specialization equals to UPDATED_SPECIALIZATION
        defaultTeacherShouldNotBeFound("specialization.in=" + UPDATED_SPECIALIZATION);
    }

    @Test
    @Transactional
    public void getAllTeachersBySpecializationIsNullOrNotNull() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where specialization is not null
        defaultTeacherShouldBeFound("specialization.specified=true");

        // Get all the teacherList where specialization is null
        defaultTeacherShouldNotBeFound("specialization.specified=false");
    }

    @Test
    @Transactional
    public void getAllTeachersByEvaluationIsEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where evaluation equals to DEFAULT_EVALUATION
        defaultTeacherShouldBeFound("evaluation.equals=" + DEFAULT_EVALUATION);

        // Get all the teacherList where evaluation equals to UPDATED_EVALUATION
        defaultTeacherShouldNotBeFound("evaluation.equals=" + UPDATED_EVALUATION);
    }

    @Test
    @Transactional
    public void getAllTeachersByEvaluationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where evaluation not equals to DEFAULT_EVALUATION
        defaultTeacherShouldNotBeFound("evaluation.notEquals=" + DEFAULT_EVALUATION);

        // Get all the teacherList where evaluation not equals to UPDATED_EVALUATION
        defaultTeacherShouldBeFound("evaluation.notEquals=" + UPDATED_EVALUATION);
    }

    @Test
    @Transactional
    public void getAllTeachersByEvaluationIsInShouldWork() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where evaluation in DEFAULT_EVALUATION or UPDATED_EVALUATION
        defaultTeacherShouldBeFound("evaluation.in=" + DEFAULT_EVALUATION + "," + UPDATED_EVALUATION);

        // Get all the teacherList where evaluation equals to UPDATED_EVALUATION
        defaultTeacherShouldNotBeFound("evaluation.in=" + UPDATED_EVALUATION);
    }

    @Test
    @Transactional
    public void getAllTeachersByEvaluationIsNullOrNotNull() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where evaluation is not null
        defaultTeacherShouldBeFound("evaluation.specified=true");

        // Get all the teacherList where evaluation is null
        defaultTeacherShouldNotBeFound("evaluation.specified=false");
    }

    @Test
    @Transactional
    public void getAllTeachersByEvaluationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where evaluation is greater than or equal to DEFAULT_EVALUATION
        defaultTeacherShouldBeFound("evaluation.greaterThanOrEqual=" + DEFAULT_EVALUATION);

        // Get all the teacherList where evaluation is greater than or equal to UPDATED_EVALUATION
        defaultTeacherShouldNotBeFound("evaluation.greaterThanOrEqual=" + UPDATED_EVALUATION);
    }

    @Test
    @Transactional
    public void getAllTeachersByEvaluationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where evaluation is less than or equal to DEFAULT_EVALUATION
        defaultTeacherShouldBeFound("evaluation.lessThanOrEqual=" + DEFAULT_EVALUATION);

        // Get all the teacherList where evaluation is less than or equal to SMALLER_EVALUATION
        defaultTeacherShouldNotBeFound("evaluation.lessThanOrEqual=" + SMALLER_EVALUATION);
    }

    @Test
    @Transactional
    public void getAllTeachersByEvaluationIsLessThanSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where evaluation is less than DEFAULT_EVALUATION
        defaultTeacherShouldNotBeFound("evaluation.lessThan=" + DEFAULT_EVALUATION);

        // Get all the teacherList where evaluation is less than UPDATED_EVALUATION
        defaultTeacherShouldBeFound("evaluation.lessThan=" + UPDATED_EVALUATION);
    }

    @Test
    @Transactional
    public void getAllTeachersByEvaluationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where evaluation is greater than DEFAULT_EVALUATION
        defaultTeacherShouldNotBeFound("evaluation.greaterThan=" + DEFAULT_EVALUATION);

        // Get all the teacherList where evaluation is greater than SMALLER_EVALUATION
        defaultTeacherShouldBeFound("evaluation.greaterThan=" + SMALLER_EVALUATION);
    }


    @Test
    @Transactional
    public void getAllTeachersByQualificationIsEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where qualification equals to DEFAULT_QUALIFICATION
        defaultTeacherShouldBeFound("qualification.equals=" + DEFAULT_QUALIFICATION);

        // Get all the teacherList where qualification equals to UPDATED_QUALIFICATION
        defaultTeacherShouldNotBeFound("qualification.equals=" + UPDATED_QUALIFICATION);
    }

    @Test
    @Transactional
    public void getAllTeachersByQualificationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where qualification not equals to DEFAULT_QUALIFICATION
        defaultTeacherShouldNotBeFound("qualification.notEquals=" + DEFAULT_QUALIFICATION);

        // Get all the teacherList where qualification not equals to UPDATED_QUALIFICATION
        defaultTeacherShouldBeFound("qualification.notEquals=" + UPDATED_QUALIFICATION);
    }

    @Test
    @Transactional
    public void getAllTeachersByQualificationIsInShouldWork() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where qualification in DEFAULT_QUALIFICATION or UPDATED_QUALIFICATION
        defaultTeacherShouldBeFound("qualification.in=" + DEFAULT_QUALIFICATION + "," + UPDATED_QUALIFICATION);

        // Get all the teacherList where qualification equals to UPDATED_QUALIFICATION
        defaultTeacherShouldNotBeFound("qualification.in=" + UPDATED_QUALIFICATION);
    }

    @Test
    @Transactional
    public void getAllTeachersByQualificationIsNullOrNotNull() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where qualification is not null
        defaultTeacherShouldBeFound("qualification.specified=true");

        // Get all the teacherList where qualification is null
        defaultTeacherShouldNotBeFound("qualification.specified=false");
    }

    @Test
    @Transactional
    public void getAllTeachersByStageIsEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where stage equals to DEFAULT_STAGE
        defaultTeacherShouldBeFound("stage.equals=" + DEFAULT_STAGE);

        // Get all the teacherList where stage equals to UPDATED_STAGE
        defaultTeacherShouldNotBeFound("stage.equals=" + UPDATED_STAGE);
    }

    @Test
    @Transactional
    public void getAllTeachersByStageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where stage not equals to DEFAULT_STAGE
        defaultTeacherShouldNotBeFound("stage.notEquals=" + DEFAULT_STAGE);

        // Get all the teacherList where stage not equals to UPDATED_STAGE
        defaultTeacherShouldBeFound("stage.notEquals=" + UPDATED_STAGE);
    }

    @Test
    @Transactional
    public void getAllTeachersByStageIsInShouldWork() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where stage in DEFAULT_STAGE or UPDATED_STAGE
        defaultTeacherShouldBeFound("stage.in=" + DEFAULT_STAGE + "," + UPDATED_STAGE);

        // Get all the teacherList where stage equals to UPDATED_STAGE
        defaultTeacherShouldNotBeFound("stage.in=" + UPDATED_STAGE);
    }

    @Test
    @Transactional
    public void getAllTeachersByStageIsNullOrNotNull() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where stage is not null
        defaultTeacherShouldBeFound("stage.specified=true");

        // Get all the teacherList where stage is null
        defaultTeacherShouldNotBeFound("stage.specified=false");
    }

    @Test
    @Transactional
    public void getAllTeachersBySumOfHoursIsEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where sumOfHours equals to DEFAULT_SUM_OF_HOURS
        defaultTeacherShouldBeFound("sumOfHours.equals=" + DEFAULT_SUM_OF_HOURS);

        // Get all the teacherList where sumOfHours equals to UPDATED_SUM_OF_HOURS
        defaultTeacherShouldNotBeFound("sumOfHours.equals=" + UPDATED_SUM_OF_HOURS);
    }

    @Test
    @Transactional
    public void getAllTeachersBySumOfHoursIsNotEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where sumOfHours not equals to DEFAULT_SUM_OF_HOURS
        defaultTeacherShouldNotBeFound("sumOfHours.notEquals=" + DEFAULT_SUM_OF_HOURS);

        // Get all the teacherList where sumOfHours not equals to UPDATED_SUM_OF_HOURS
        defaultTeacherShouldBeFound("sumOfHours.notEquals=" + UPDATED_SUM_OF_HOURS);
    }

    @Test
    @Transactional
    public void getAllTeachersBySumOfHoursIsInShouldWork() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where sumOfHours in DEFAULT_SUM_OF_HOURS or UPDATED_SUM_OF_HOURS
        defaultTeacherShouldBeFound("sumOfHours.in=" + DEFAULT_SUM_OF_HOURS + "," + UPDATED_SUM_OF_HOURS);

        // Get all the teacherList where sumOfHours equals to UPDATED_SUM_OF_HOURS
        defaultTeacherShouldNotBeFound("sumOfHours.in=" + UPDATED_SUM_OF_HOURS);
    }

    @Test
    @Transactional
    public void getAllTeachersBySumOfHoursIsNullOrNotNull() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where sumOfHours is not null
        defaultTeacherShouldBeFound("sumOfHours.specified=true");

        // Get all the teacherList where sumOfHours is null
        defaultTeacherShouldNotBeFound("sumOfHours.specified=false");
    }

    @Test
    @Transactional
    public void getAllTeachersBySumOfHoursIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where sumOfHours is greater than or equal to DEFAULT_SUM_OF_HOURS
        defaultTeacherShouldBeFound("sumOfHours.greaterThanOrEqual=" + DEFAULT_SUM_OF_HOURS);

        // Get all the teacherList where sumOfHours is greater than or equal to UPDATED_SUM_OF_HOURS
        defaultTeacherShouldNotBeFound("sumOfHours.greaterThanOrEqual=" + UPDATED_SUM_OF_HOURS);
    }

    @Test
    @Transactional
    public void getAllTeachersBySumOfHoursIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where sumOfHours is less than or equal to DEFAULT_SUM_OF_HOURS
        defaultTeacherShouldBeFound("sumOfHours.lessThanOrEqual=" + DEFAULT_SUM_OF_HOURS);

        // Get all the teacherList where sumOfHours is less than or equal to SMALLER_SUM_OF_HOURS
        defaultTeacherShouldNotBeFound("sumOfHours.lessThanOrEqual=" + SMALLER_SUM_OF_HOURS);
    }

    @Test
    @Transactional
    public void getAllTeachersBySumOfHoursIsLessThanSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where sumOfHours is less than DEFAULT_SUM_OF_HOURS
        defaultTeacherShouldNotBeFound("sumOfHours.lessThan=" + DEFAULT_SUM_OF_HOURS);

        // Get all the teacherList where sumOfHours is less than UPDATED_SUM_OF_HOURS
        defaultTeacherShouldBeFound("sumOfHours.lessThan=" + UPDATED_SUM_OF_HOURS);
    }

    @Test
    @Transactional
    public void getAllTeachersBySumOfHoursIsGreaterThanSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where sumOfHours is greater than DEFAULT_SUM_OF_HOURS
        defaultTeacherShouldNotBeFound("sumOfHours.greaterThan=" + DEFAULT_SUM_OF_HOURS);

        // Get all the teacherList where sumOfHours is greater than SMALLER_SUM_OF_HOURS
        defaultTeacherShouldBeFound("sumOfHours.greaterThan=" + SMALLER_SUM_OF_HOURS);
    }


    @Test
    @Transactional
    public void getAllTeachersByIsPredictedIsEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where isPredicted equals to DEFAULT_IS_PREDICTED
        defaultTeacherShouldBeFound("isPredicted.equals=" + DEFAULT_IS_PREDICTED);

        // Get all the teacherList where isPredicted equals to UPDATED_IS_PREDICTED
        defaultTeacherShouldNotBeFound("isPredicted.equals=" + UPDATED_IS_PREDICTED);
    }

    @Test
    @Transactional
    public void getAllTeachersByIsPredictedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where isPredicted not equals to DEFAULT_IS_PREDICTED
        defaultTeacherShouldNotBeFound("isPredicted.notEquals=" + DEFAULT_IS_PREDICTED);

        // Get all the teacherList where isPredicted not equals to UPDATED_IS_PREDICTED
        defaultTeacherShouldBeFound("isPredicted.notEquals=" + UPDATED_IS_PREDICTED);
    }

    @Test
    @Transactional
    public void getAllTeachersByIsPredictedIsInShouldWork() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where isPredicted in DEFAULT_IS_PREDICTED or UPDATED_IS_PREDICTED
        defaultTeacherShouldBeFound("isPredicted.in=" + DEFAULT_IS_PREDICTED + "," + UPDATED_IS_PREDICTED);

        // Get all the teacherList where isPredicted equals to UPDATED_IS_PREDICTED
        defaultTeacherShouldNotBeFound("isPredicted.in=" + UPDATED_IS_PREDICTED);
    }

    @Test
    @Transactional
    public void getAllTeachersByIsPredictedIsNullOrNotNull() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where isPredicted is not null
        defaultTeacherShouldBeFound("isPredicted.specified=true");

        // Get all the teacherList where isPredicted is null
        defaultTeacherShouldNotBeFound("isPredicted.specified=false");
    }

    @Test
    @Transactional
    public void getAllTeachersByCoursesIsEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);
        Course courses = CourseResourceIT.createEntity(em);
        em.persist(courses);
        em.flush();
        teacher.addCourses(courses);
        teacherRepository.saveAndFlush(teacher);
        Long coursesCode = courses.getCode();

        // Get all the teacherList where courses equals to coursesCode
        defaultTeacherShouldBeFound("coursesCode.equals=" + coursesCode);

        // Get all the teacherList where courses equals to coursesCode + 1
        defaultTeacherShouldNotBeFound("coursesCode.equals=" + (coursesCode + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTeacherShouldBeFound(String filter) throws Exception {
        restTeacherMockMvc.perform(get("/api/teachers?sort=number,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].specialization").value(hasItem(DEFAULT_SPECIALIZATION.toString())))
            .andExpect(jsonPath("$.[*].evaluation").value(hasItem(DEFAULT_EVALUATION)))
            .andExpect(jsonPath("$.[*].qualification").value(hasItem(DEFAULT_QUALIFICATION.toString())))
            .andExpect(jsonPath("$.[*].stage").value(hasItem(DEFAULT_STAGE.toString())))
            .andExpect(jsonPath("$.[*].sumOfHours").value(hasItem(DEFAULT_SUM_OF_HOURS)))
            .andExpect(jsonPath("$.[*].isPredicted").value(hasItem(DEFAULT_IS_PREDICTED.booleanValue())));

        // Check, that the count call also returns 1
        restTeacherMockMvc.perform(get("/api/teachers/count?sort=number,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTeacherShouldNotBeFound(String filter) throws Exception {
        restTeacherMockMvc.perform(get("/api/teachers?sort=number,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTeacherMockMvc.perform(get("/api/teachers/count?sort=number,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingTeacher() throws Exception {
        // Get the teacher
        restTeacherMockMvc.perform(get("/api/teachers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTeacher() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        int databaseSizeBeforeUpdate = teacherRepository.findAll().size();

        // Update the teacher
        Teacher updatedTeacher = teacherRepository.findById(teacher.getNumber()).get();
        // Disconnect from session so that the updates on updatedTeacher are not directly saved in db
        em.detach(updatedTeacher);
        updatedTeacher
            .number(UPDATED_NUMBER)
            .specialization(UPDATED_SPECIALIZATION)
            .evaluation(UPDATED_EVALUATION)
            .qualification(UPDATED_QUALIFICATION)
            .stage(UPDATED_STAGE)
            .sumOfHours(UPDATED_SUM_OF_HOURS)
            .isPredicted(UPDATED_IS_PREDICTED);
        TeacherDTO teacherDTO = teacherMapper.toDto(updatedTeacher);

        restTeacherMockMvc.perform(put("/api/teachers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(teacherDTO)))
            .andExpect(status().isOk());

        // Validate the Teacher in the database
        List<Teacher> teacherList = teacherRepository.findAll();
        assertThat(teacherList).hasSize(databaseSizeBeforeUpdate);
        Teacher testTeacher = teacherList.get(teacherList.size() - 1);
        assertThat(testTeacher.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testTeacher.getSpecialization()).isEqualTo(UPDATED_SPECIALIZATION);
        assertThat(testTeacher.getEvaluation()).isEqualTo(UPDATED_EVALUATION);
        assertThat(testTeacher.getQualification()).isEqualTo(UPDATED_QUALIFICATION);
        assertThat(testTeacher.getStage()).isEqualTo(UPDATED_STAGE);
        assertThat(testTeacher.getSumOfHours()).isEqualTo(UPDATED_SUM_OF_HOURS);
        assertThat(testTeacher.isIsPredicted()).isEqualTo(UPDATED_IS_PREDICTED);
    }

    @Test
    @Transactional
    public void updateNonExistingTeacher() throws Exception {
        int databaseSizeBeforeUpdate = teacherRepository.findAll().size();

        // Create the Teacher
        TeacherDTO teacherDTO = teacherMapper.toDto(teacher);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTeacherMockMvc.perform(put("/api/teachers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(teacherDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Teacher in the database
        List<Teacher> teacherList = teacherRepository.findAll();
        assertThat(teacherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTeacher() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        int databaseSizeBeforeDelete = teacherRepository.findAll().size();

        // Delete the teacher
        restTeacherMockMvc.perform(delete("/api/teachers/{id}", teacher.getNumber())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Teacher> teacherList = teacherRepository.findAll();
        assertThat(teacherList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
