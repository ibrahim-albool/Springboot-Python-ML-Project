package com.teach.predict.web.rest;

import com.teach.predict.TeachPredictorApp;
import com.teach.predict.domain.Course;
import com.teach.predict.domain.Teacher;
import com.teach.predict.repository.CourseRepository;
import com.teach.predict.service.CourseService;
import com.teach.predict.service.dto.CourseDTO;
import com.teach.predict.service.mapper.CourseMapper;
import com.teach.predict.service.dto.CourseCriteria;
import com.teach.predict.service.CourseQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.teach.predict.domain.enumeration.Specialization;
import com.teach.predict.domain.enumeration.Type;
/**
 * Integration tests for the {@link CourseResource} REST controller.
 */
@SpringBootTest(classes = TeachPredictorApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class CourseResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final Specialization DEFAULT_SPECIALIZATION = Specialization.Arabic;
    private static final Specialization UPDATED_SPECIALIZATION = Specialization.English;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Type DEFAULT_TYPE = Type.C;
    private static final Type UPDATED_TYPE = Type.P;

    private static final Integer DEFAULT_HOURS = 1;
    private static final Integer UPDATED_HOURS = 2;
    private static final Integer SMALLER_HOURS = 1 - 1;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseQueryService courseQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCourseMockMvc;

    private Course course;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Course createEntity(EntityManager em) {
        Course course = new Course()
            .code(DEFAULT_CODE)
            .specialization(DEFAULT_SPECIALIZATION)
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE)
            .hours(DEFAULT_HOURS);
        return course;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Course createUpdatedEntity(EntityManager em) {
        Course course = new Course()
            .code(UPDATED_CODE)
            .specialization(UPDATED_SPECIALIZATION)
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .hours(UPDATED_HOURS);
        return course;
    }

    @BeforeEach
    public void initTest() {
        course = createEntity(em);
    }

    @Test
    @Transactional
    public void createCourse() throws Exception {
        int databaseSizeBeforeCreate = courseRepository.findAll().size();
        // Create the Course
        CourseDTO courseDTO = courseMapper.toDto(course);
        restCourseMockMvc.perform(post("/api/courses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(courseDTO)))
            .andExpect(status().isCreated());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeCreate + 1);
        Course testCourse = courseList.get(courseList.size() - 1);
        assertThat(testCourse.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testCourse.getSpecialization()).isEqualTo(DEFAULT_SPECIALIZATION);
        assertThat(testCourse.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCourse.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testCourse.getHours()).isEqualTo(DEFAULT_HOURS);
    }

    @Test
    @Transactional
    public void createCourseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = courseRepository.findAll().size();

        // Create the Course with an existing ID
        course.setId(1L);
        CourseDTO courseDTO = courseMapper.toDto(course);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourseMockMvc.perform(post("/api/courses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(courseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCourses() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList
        restCourseMockMvc.perform(get("/api/courses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(course.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].specialization").value(hasItem(DEFAULT_SPECIALIZATION.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].hours").value(hasItem(DEFAULT_HOURS)));
    }

    @Test
    @Transactional
    public void getCourse() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get the course
        restCourseMockMvc.perform(get("/api/courses/{id}", course.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(course.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.specialization").value(DEFAULT_SPECIALIZATION.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.hours").value(DEFAULT_HOURS));
    }


    @Test
    @Transactional
    public void getCoursesByIdFiltering() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        Long id = course.getId();

        defaultCourseShouldBeFound("id.equals=" + id);
        defaultCourseShouldNotBeFound("id.notEquals=" + id);

        defaultCourseShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCourseShouldNotBeFound("id.greaterThan=" + id);

        defaultCourseShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCourseShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllCoursesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where code equals to DEFAULT_CODE
        defaultCourseShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the courseList where code equals to UPDATED_CODE
        defaultCourseShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllCoursesByCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where code not equals to DEFAULT_CODE
        defaultCourseShouldNotBeFound("code.notEquals=" + DEFAULT_CODE);

        // Get all the courseList where code not equals to UPDATED_CODE
        defaultCourseShouldBeFound("code.notEquals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllCoursesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where code in DEFAULT_CODE or UPDATED_CODE
        defaultCourseShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the courseList where code equals to UPDATED_CODE
        defaultCourseShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllCoursesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where code is not null
        defaultCourseShouldBeFound("code.specified=true");

        // Get all the courseList where code is null
        defaultCourseShouldNotBeFound("code.specified=false");
    }
                @Test
    @Transactional
    public void getAllCoursesByCodeContainsSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where code contains DEFAULT_CODE
        defaultCourseShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the courseList where code contains UPDATED_CODE
        defaultCourseShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllCoursesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where code does not contain DEFAULT_CODE
        defaultCourseShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the courseList where code does not contain UPDATED_CODE
        defaultCourseShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }


    @Test
    @Transactional
    public void getAllCoursesBySpecializationIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where specialization equals to DEFAULT_SPECIALIZATION
        defaultCourseShouldBeFound("specialization.equals=" + DEFAULT_SPECIALIZATION);

        // Get all the courseList where specialization equals to UPDATED_SPECIALIZATION
        defaultCourseShouldNotBeFound("specialization.equals=" + UPDATED_SPECIALIZATION);
    }

    @Test
    @Transactional
    public void getAllCoursesBySpecializationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where specialization not equals to DEFAULT_SPECIALIZATION
        defaultCourseShouldNotBeFound("specialization.notEquals=" + DEFAULT_SPECIALIZATION);

        // Get all the courseList where specialization not equals to UPDATED_SPECIALIZATION
        defaultCourseShouldBeFound("specialization.notEquals=" + UPDATED_SPECIALIZATION);
    }

    @Test
    @Transactional
    public void getAllCoursesBySpecializationIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where specialization in DEFAULT_SPECIALIZATION or UPDATED_SPECIALIZATION
        defaultCourseShouldBeFound("specialization.in=" + DEFAULT_SPECIALIZATION + "," + UPDATED_SPECIALIZATION);

        // Get all the courseList where specialization equals to UPDATED_SPECIALIZATION
        defaultCourseShouldNotBeFound("specialization.in=" + UPDATED_SPECIALIZATION);
    }

    @Test
    @Transactional
    public void getAllCoursesBySpecializationIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where specialization is not null
        defaultCourseShouldBeFound("specialization.specified=true");

        // Get all the courseList where specialization is null
        defaultCourseShouldNotBeFound("specialization.specified=false");
    }

    @Test
    @Transactional
    public void getAllCoursesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where name equals to DEFAULT_NAME
        defaultCourseShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the courseList where name equals to UPDATED_NAME
        defaultCourseShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCoursesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where name not equals to DEFAULT_NAME
        defaultCourseShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the courseList where name not equals to UPDATED_NAME
        defaultCourseShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCoursesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCourseShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the courseList where name equals to UPDATED_NAME
        defaultCourseShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCoursesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where name is not null
        defaultCourseShouldBeFound("name.specified=true");

        // Get all the courseList where name is null
        defaultCourseShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllCoursesByNameContainsSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where name contains DEFAULT_NAME
        defaultCourseShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the courseList where name contains UPDATED_NAME
        defaultCourseShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCoursesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where name does not contain DEFAULT_NAME
        defaultCourseShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the courseList where name does not contain UPDATED_NAME
        defaultCourseShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllCoursesByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where type equals to DEFAULT_TYPE
        defaultCourseShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the courseList where type equals to UPDATED_TYPE
        defaultCourseShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllCoursesByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where type not equals to DEFAULT_TYPE
        defaultCourseShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the courseList where type not equals to UPDATED_TYPE
        defaultCourseShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllCoursesByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultCourseShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the courseList where type equals to UPDATED_TYPE
        defaultCourseShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllCoursesByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where type is not null
        defaultCourseShouldBeFound("type.specified=true");

        // Get all the courseList where type is null
        defaultCourseShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllCoursesByHoursIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where hours equals to DEFAULT_HOURS
        defaultCourseShouldBeFound("hours.equals=" + DEFAULT_HOURS);

        // Get all the courseList where hours equals to UPDATED_HOURS
        defaultCourseShouldNotBeFound("hours.equals=" + UPDATED_HOURS);
    }

    @Test
    @Transactional
    public void getAllCoursesByHoursIsNotEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where hours not equals to DEFAULT_HOURS
        defaultCourseShouldNotBeFound("hours.notEquals=" + DEFAULT_HOURS);

        // Get all the courseList where hours not equals to UPDATED_HOURS
        defaultCourseShouldBeFound("hours.notEquals=" + UPDATED_HOURS);
    }

    @Test
    @Transactional
    public void getAllCoursesByHoursIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where hours in DEFAULT_HOURS or UPDATED_HOURS
        defaultCourseShouldBeFound("hours.in=" + DEFAULT_HOURS + "," + UPDATED_HOURS);

        // Get all the courseList where hours equals to UPDATED_HOURS
        defaultCourseShouldNotBeFound("hours.in=" + UPDATED_HOURS);
    }

    @Test
    @Transactional
    public void getAllCoursesByHoursIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where hours is not null
        defaultCourseShouldBeFound("hours.specified=true");

        // Get all the courseList where hours is null
        defaultCourseShouldNotBeFound("hours.specified=false");
    }

    @Test
    @Transactional
    public void getAllCoursesByHoursIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where hours is greater than or equal to DEFAULT_HOURS
        defaultCourseShouldBeFound("hours.greaterThanOrEqual=" + DEFAULT_HOURS);

        // Get all the courseList where hours is greater than or equal to UPDATED_HOURS
        defaultCourseShouldNotBeFound("hours.greaterThanOrEqual=" + UPDATED_HOURS);
    }

    @Test
    @Transactional
    public void getAllCoursesByHoursIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where hours is less than or equal to DEFAULT_HOURS
        defaultCourseShouldBeFound("hours.lessThanOrEqual=" + DEFAULT_HOURS);

        // Get all the courseList where hours is less than or equal to SMALLER_HOURS
        defaultCourseShouldNotBeFound("hours.lessThanOrEqual=" + SMALLER_HOURS);
    }

    @Test
    @Transactional
    public void getAllCoursesByHoursIsLessThanSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where hours is less than DEFAULT_HOURS
        defaultCourseShouldNotBeFound("hours.lessThan=" + DEFAULT_HOURS);

        // Get all the courseList where hours is less than UPDATED_HOURS
        defaultCourseShouldBeFound("hours.lessThan=" + UPDATED_HOURS);
    }

    @Test
    @Transactional
    public void getAllCoursesByHoursIsGreaterThanSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where hours is greater than DEFAULT_HOURS
        defaultCourseShouldNotBeFound("hours.greaterThan=" + DEFAULT_HOURS);

        // Get all the courseList where hours is greater than SMALLER_HOURS
        defaultCourseShouldBeFound("hours.greaterThan=" + SMALLER_HOURS);
    }


    @Test
    @Transactional
    public void getAllCoursesByTeachersIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);
        Teacher teachers = TeacherResourceIT.createEntity(em);
        em.persist(teachers);
        em.flush();
        course.addTeachers(teachers);
        courseRepository.saveAndFlush(course);
        Long teachersId = teachers.getNumber();

        // Get all the courseList where teachers equals to teachersId
        defaultCourseShouldBeFound("teachersId.equals=" + teachersId);

        // Get all the courseList where teachers equals to teachersId + 1
        defaultCourseShouldNotBeFound("teachersId.equals=" + (teachersId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCourseShouldBeFound(String filter) throws Exception {
        restCourseMockMvc.perform(get("/api/courses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(course.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].specialization").value(hasItem(DEFAULT_SPECIALIZATION.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].hours").value(hasItem(DEFAULT_HOURS)));

        // Check, that the count call also returns 1
        restCourseMockMvc.perform(get("/api/courses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCourseShouldNotBeFound(String filter) throws Exception {
        restCourseMockMvc.perform(get("/api/courses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCourseMockMvc.perform(get("/api/courses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingCourse() throws Exception {
        // Get the course
        restCourseMockMvc.perform(get("/api/courses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCourse() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        int databaseSizeBeforeUpdate = courseRepository.findAll().size();

        // Update the course
        Course updatedCourse = courseRepository.findById(course.getId()).get();
        // Disconnect from session so that the updates on updatedCourse are not directly saved in db
        em.detach(updatedCourse);
        updatedCourse
            .code(UPDATED_CODE)
            .specialization(UPDATED_SPECIALIZATION)
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .hours(UPDATED_HOURS);
        CourseDTO courseDTO = courseMapper.toDto(updatedCourse);

        restCourseMockMvc.perform(put("/api/courses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(courseDTO)))
            .andExpect(status().isOk());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
        Course testCourse = courseList.get(courseList.size() - 1);
        assertThat(testCourse.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCourse.getSpecialization()).isEqualTo(UPDATED_SPECIALIZATION);
        assertThat(testCourse.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCourse.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testCourse.getHours()).isEqualTo(UPDATED_HOURS);
    }

    @Test
    @Transactional
    public void updateNonExistingCourse() throws Exception {
        int databaseSizeBeforeUpdate = courseRepository.findAll().size();

        // Create the Course
        CourseDTO courseDTO = courseMapper.toDto(course);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseMockMvc.perform(put("/api/courses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(courseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCourse() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        int databaseSizeBeforeDelete = courseRepository.findAll().size();

        // Delete the course
        restCourseMockMvc.perform(delete("/api/courses/{id}", course.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
