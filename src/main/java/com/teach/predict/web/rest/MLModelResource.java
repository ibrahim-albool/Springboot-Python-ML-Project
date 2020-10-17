package com.teach.predict.web.rest;

import com.teach.predict.service.MLModelService;
import com.teach.predict.service.MLPythonService;
import com.teach.predict.service.TeacherService;
import com.teach.predict.service.dto.HistoryDTO;
import com.teach.predict.web.rest.errors.BadRequestAlertException;
import com.teach.predict.service.dto.MLModelDTO;
import com.teach.predict.service.dto.MLModelCriteria;
import com.teach.predict.service.MLModelQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.teach.predict.domain.MLModel}.
 */
@RestController
@RequestMapping("/api")
public class MLModelResource {

    private final Logger log = LoggerFactory.getLogger(MLModelResource.class);

    private static final String ENTITY_NAME = "mLModel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MLModelService mLModelService;

    private final MLModelQueryService mLModelQueryService;

    @Autowired
    private MLPythonService mlPythonService;

    @Autowired
    private TeacherService teacherService;

    public MLModelResource(MLModelService mLModelService, MLModelQueryService mLModelQueryService) {
        this.mLModelService = mLModelService;
        this.mLModelQueryService = mLModelQueryService;
    }

    /**
     * {@code POST  /ml-models} : Create a new mLModel.
     *
     * @param mLModelDTO the mLModelDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mLModelDTO, or with status {@code 400 (Bad Request)} if the mLModel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ml-models")
    public ResponseEntity<MLModelDTO> createMLModel(@RequestBody MLModelDTO mLModelDTO) throws URISyntaxException {
        log.debug("REST request to save MLModel : {}", mLModelDTO);
        if (mLModelDTO.getId() != null) {
            throw new BadRequestAlertException("A new mLModel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MLModelDTO result = mLModelService.save(mLModelDTO);
        return ResponseEntity.created(new URI("/api/ml-models/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ml-models} : Updates an existing mLModel.
     *
     * @param mLModelDTO the mLModelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mLModelDTO,
     * or with status {@code 400 (Bad Request)} if the mLModelDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mLModelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ml-models")
    public ResponseEntity<MLModelDTO> updateMLModel(@RequestBody MLModelDTO mLModelDTO) throws URISyntaxException {
        log.debug("REST request to update MLModel : {}", mLModelDTO);
        if (mLModelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MLModelDTO result = mLModelService.save(mLModelDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, mLModelDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /ml-models} : get all the mLModels.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mLModels in body.
     */
    @GetMapping("/ml-models")
    public ResponseEntity<List<MLModelDTO>> getAllMLModels(MLModelCriteria criteria) {
        log.debug("REST request to get MLModels by criteria: {}", criteria);
        List<MLModelDTO> entityList = mLModelQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /ml-models/count} : count all the mLModels.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/ml-models/count")
    public ResponseEntity<Long> countMLModels(MLModelCriteria criteria) {
        log.debug("REST request to count MLModels by criteria: {}", criteria);
        return ResponseEntity.ok().body(mLModelQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ml-models/:id} : get the "id" mLModel.
     *
     * @param id the id of the mLModelDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mLModelDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ml-models/{id}")
    public ResponseEntity<MLModelDTO> getMLModel(@PathVariable Long id) {
        log.debug("REST request to get MLModel : {}", id);
        Optional<MLModelDTO> mLModelDTO = mLModelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mLModelDTO);
    }

    /**
     * {@code DELETE  /ml-models/:id} : delete the "id" mLModel.
     *
     * @param id the id of the mLModelDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ml-models/{id}")
    public ResponseEntity<Void> deleteMLModel(@PathVariable Long id) {
        log.debug("REST request to delete MLModel : {}", id);
        mLModelService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }


    /**
     *
      * @param trainingFile
     * @param labelsFile
     * @return
     * @throws URISyntaxException
     */
    @PostMapping("/trainMLModel")
    public ResponseEntity trainMLModel(@RequestParam String trainingFile, @RequestParam String labelsFile) throws URISyntaxException {
        log.debug("REST request to train the MLModel.");
        Optional<String> res = mlPythonService.trainMLModel(trainingFile, labelsFile);
        if(res.isEmpty()){
            return ResponseEntity.status(404).body("Model is already trained");
        }
        return ResponseEntity.ok(res.get());
    }

    @PostMapping("/predictData")
    public ResponseEntity<String> predictXNew(@RequestParam String newDataFile) {
        log.debug("REST request the model teachers");
        Optional<String> res = mlPythonService.predictXNew(newDataFile);
        if (!res.isPresent()) {
            ResponseEntity.status(404).body("");
        }
        return ResponseEntity.ok(res.get());
    }

    @GetMapping("/modelMetrics")
    public ResponseEntity<MLModelDTO> getModelMetrics() {
        log.debug("REST request the model metrics");
        return ResponseUtil.wrapOrNotFound(mLModelService.findAll().stream().findFirst());
    }

    @GetMapping("/modelHistory")
    public ResponseEntity<HistoryDTO> getModelHistory() {
        log.debug("REST request the model history");
        Optional<HistoryDTO> metrics = mlPythonService.getModelHistory();
        return ResponseUtil.wrapOrNotFound(metrics);
    }

    @DeleteMapping("/deleteModelAndTeachers")
    public ResponseEntity<String> deleteModelAndTeachers() {
        log.debug("REST request the model metrics");
        mLModelService.deleteAll();
        teacherService.deleteAll();
        Optional<String> result = mlPythonService.deleteModelCache();
        return ResponseUtil.wrapOrNotFound(result);
    }

}
