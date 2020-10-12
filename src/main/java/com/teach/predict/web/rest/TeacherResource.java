package com.teach.predict.web.rest;

import com.teach.predict.service.TeacherService;
import com.teach.predict.web.rest.errors.BadRequestAlertException;
import com.teach.predict.service.dto.TeacherDTO;
import com.teach.predict.service.dto.TeacherCriteria;
import com.teach.predict.service.TeacherQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.teach.predict.domain.Teacher}.
 */
@RestController
@RequestMapping("/api")
public class TeacherResource {

    private final Logger log = LoggerFactory.getLogger(TeacherResource.class);

    private static final String ENTITY_NAME = "teacher";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TeacherService teacherService;

    private final TeacherQueryService teacherQueryService;

    public TeacherResource(TeacherService teacherService, TeacherQueryService teacherQueryService) {
        this.teacherService = teacherService;
        this.teacherQueryService = teacherQueryService;
    }

    /**
     * {@code POST  /teachers} : Create a new teacher.
     *
     * @param teacherDTO the teacherDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new teacherDTO, or with status {@code 400 (Bad Request)} if the teacher has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/teachers")
    public ResponseEntity<TeacherDTO> createTeacher(@RequestBody TeacherDTO teacherDTO) throws URISyntaxException {
        log.debug("REST request to save Teacher : {}", teacherDTO);
        if (teacherDTO.getNumber() != null) {
            throw new BadRequestAlertException("A new teacher cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TeacherDTO result = teacherService.save(teacherDTO);
        return ResponseEntity.created(new URI("/api/teachers/" + result.getNumber()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getNumber().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /teachers} : Updates an existing teacher.
     *
     * @param teacherDTO the teacherDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated teacherDTO,
     * or with status {@code 400 (Bad Request)} if the teacherDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the teacherDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/teachers")
    public ResponseEntity<TeacherDTO> updateTeacher(@RequestBody TeacherDTO teacherDTO) throws URISyntaxException {
        log.debug("REST request to update Teacher : {}", teacherDTO);
        if (teacherDTO.getNumber() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TeacherDTO result = teacherService.save(teacherDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, teacherDTO.getNumber().toString()))
            .body(result);
    }

    /**
     * {@code GET  /teachers} : get all the teachers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of teachers in body.
     */
    @GetMapping("/teachers")
    public ResponseEntity<List<TeacherDTO>> getAllTeachers(TeacherCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Teachers by criteria: {}", criteria);
        Page<TeacherDTO> page = teacherQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /teachers/count} : count all the teachers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/teachers/count")
    public ResponseEntity<Long> countTeachers(TeacherCriteria criteria) {
        log.debug("REST request to count Teachers by criteria: {}", criteria);
        return ResponseEntity.ok().body(teacherQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /teachers/:id} : get the "id" teacher.
     *
     * @param id the id of the teacherDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the teacherDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/teachers/{id}")
    public ResponseEntity<TeacherDTO> getTeacher(@PathVariable Long id) {
        log.debug("REST request to get Teacher : {}", id);
        Optional<TeacherDTO> teacherDTO = teacherService.findOne(id);
        return ResponseUtil.wrapOrNotFound(teacherDTO);
    }

    /**
     * {@code DELETE  /teachers/:id} : delete the "id" teacher.
     *
     * @param id the id of the teacherDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/teachers/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {
        log.debug("REST request to delete Teacher : {}", id);
        teacherService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
