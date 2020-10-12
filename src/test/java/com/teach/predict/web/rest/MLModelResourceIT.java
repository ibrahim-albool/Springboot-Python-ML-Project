package com.teach.predict.web.rest;

import com.teach.predict.TeachPredictorApp;
import com.teach.predict.domain.MLModel;
import com.teach.predict.repository.MLModelRepository;
import com.teach.predict.service.MLModelService;
import com.teach.predict.service.dto.MLModelDTO;
import com.teach.predict.service.mapper.MLModelMapper;
import com.teach.predict.service.dto.MLModelCriteria;
import com.teach.predict.service.MLModelQueryService;

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

/**
 * Integration tests for the {@link MLModelResource} REST controller.
 */
@SpringBootTest(classes = TeachPredictorApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class MLModelResourceIT {

    private static final Float DEFAULT_TP = 1F;
    private static final Float UPDATED_TP = 2F;
    private static final Float SMALLER_TP = 1F - 1F;

    private static final Float DEFAULT_TN = 1F;
    private static final Float UPDATED_TN = 2F;
    private static final Float SMALLER_TN = 1F - 1F;

    private static final Float DEFAULT_FP = 1F;
    private static final Float UPDATED_FP = 2F;
    private static final Float SMALLER_FP = 1F - 1F;

    private static final Float DEFAULT_FN = 1F;
    private static final Float UPDATED_FN = 2F;
    private static final Float SMALLER_FN = 1F - 1F;

    private static final Float DEFAULT_ACCURACY = 1F;
    private static final Float UPDATED_ACCURACY = 2F;
    private static final Float SMALLER_ACCURACY = 1F - 1F;

    private static final Float DEFAULT_PRECISION = 1F;
    private static final Float UPDATED_PRECISION = 2F;
    private static final Float SMALLER_PRECISION = 1F - 1F;

    private static final Float DEFAULT_RECALL = 1F;
    private static final Float UPDATED_RECALL = 2F;
    private static final Float SMALLER_RECALL = 1F - 1F;

    @Autowired
    private MLModelRepository mLModelRepository;

    @Autowired
    private MLModelMapper mLModelMapper;

    @Autowired
    private MLModelService mLModelService;

    @Autowired
    private MLModelQueryService mLModelQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMLModelMockMvc;

    private MLModel mLModel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MLModel createEntity(EntityManager em) {
        MLModel mLModel = new MLModel()
            .tp(DEFAULT_TP)
            .tn(DEFAULT_TN)
            .fp(DEFAULT_FP)
            .fn(DEFAULT_FN)
            .accuracy(DEFAULT_ACCURACY)
            .precision(DEFAULT_PRECISION)
            .recall(DEFAULT_RECALL);
        return mLModel;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MLModel createUpdatedEntity(EntityManager em) {
        MLModel mLModel = new MLModel()
            .tp(UPDATED_TP)
            .tn(UPDATED_TN)
            .fp(UPDATED_FP)
            .fn(UPDATED_FN)
            .accuracy(UPDATED_ACCURACY)
            .precision(UPDATED_PRECISION)
            .recall(UPDATED_RECALL);
        return mLModel;
    }

    @BeforeEach
    public void initTest() {
        mLModel = createEntity(em);
    }

    @Test
    @Transactional
    public void createMLModel() throws Exception {
        int databaseSizeBeforeCreate = mLModelRepository.findAll().size();
        // Create the MLModel
        MLModelDTO mLModelDTO = mLModelMapper.toDto(mLModel);
        restMLModelMockMvc.perform(post("/api/ml-models")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(mLModelDTO)))
            .andExpect(status().isCreated());

        // Validate the MLModel in the database
        List<MLModel> mLModelList = mLModelRepository.findAll();
        assertThat(mLModelList).hasSize(databaseSizeBeforeCreate + 1);
        MLModel testMLModel = mLModelList.get(mLModelList.size() - 1);
        assertThat(testMLModel.getTp()).isEqualTo(DEFAULT_TP);
        assertThat(testMLModel.getTn()).isEqualTo(DEFAULT_TN);
        assertThat(testMLModel.getFp()).isEqualTo(DEFAULT_FP);
        assertThat(testMLModel.getFn()).isEqualTo(DEFAULT_FN);
        assertThat(testMLModel.getAccuracy()).isEqualTo(DEFAULT_ACCURACY);
        assertThat(testMLModel.getPrecision()).isEqualTo(DEFAULT_PRECISION);
        assertThat(testMLModel.getRecall()).isEqualTo(DEFAULT_RECALL);
    }

    @Test
    @Transactional
    public void createMLModelWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = mLModelRepository.findAll().size();

        // Create the MLModel with an existing ID
        mLModel.setId(1L);
        MLModelDTO mLModelDTO = mLModelMapper.toDto(mLModel);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMLModelMockMvc.perform(post("/api/ml-models")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(mLModelDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MLModel in the database
        List<MLModel> mLModelList = mLModelRepository.findAll();
        assertThat(mLModelList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllMLModels() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList
        restMLModelMockMvc.perform(get("/api/ml-models?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mLModel.getId().intValue())))
            .andExpect(jsonPath("$.[*].tp").value(hasItem(DEFAULT_TP.doubleValue())))
            .andExpect(jsonPath("$.[*].tn").value(hasItem(DEFAULT_TN.doubleValue())))
            .andExpect(jsonPath("$.[*].fp").value(hasItem(DEFAULT_FP.doubleValue())))
            .andExpect(jsonPath("$.[*].fn").value(hasItem(DEFAULT_FN.doubleValue())))
            .andExpect(jsonPath("$.[*].accuracy").value(hasItem(DEFAULT_ACCURACY.doubleValue())))
            .andExpect(jsonPath("$.[*].precision").value(hasItem(DEFAULT_PRECISION.doubleValue())))
            .andExpect(jsonPath("$.[*].recall").value(hasItem(DEFAULT_RECALL.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getMLModel() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get the mLModel
        restMLModelMockMvc.perform(get("/api/ml-models/{id}", mLModel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mLModel.getId().intValue()))
            .andExpect(jsonPath("$.tp").value(DEFAULT_TP.doubleValue()))
            .andExpect(jsonPath("$.tn").value(DEFAULT_TN.doubleValue()))
            .andExpect(jsonPath("$.fp").value(DEFAULT_FP.doubleValue()))
            .andExpect(jsonPath("$.fn").value(DEFAULT_FN.doubleValue()))
            .andExpect(jsonPath("$.accuracy").value(DEFAULT_ACCURACY.doubleValue()))
            .andExpect(jsonPath("$.precision").value(DEFAULT_PRECISION.doubleValue()))
            .andExpect(jsonPath("$.recall").value(DEFAULT_RECALL.doubleValue()));
    }


    @Test
    @Transactional
    public void getMLModelsByIdFiltering() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        Long id = mLModel.getId();

        defaultMLModelShouldBeFound("id.equals=" + id);
        defaultMLModelShouldNotBeFound("id.notEquals=" + id);

        defaultMLModelShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMLModelShouldNotBeFound("id.greaterThan=" + id);

        defaultMLModelShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMLModelShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllMLModelsByTpIsEqualToSomething() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where tp equals to DEFAULT_TP
        defaultMLModelShouldBeFound("tp.equals=" + DEFAULT_TP);

        // Get all the mLModelList where tp equals to UPDATED_TP
        defaultMLModelShouldNotBeFound("tp.equals=" + UPDATED_TP);
    }

    @Test
    @Transactional
    public void getAllMLModelsByTpIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where tp not equals to DEFAULT_TP
        defaultMLModelShouldNotBeFound("tp.notEquals=" + DEFAULT_TP);

        // Get all the mLModelList where tp not equals to UPDATED_TP
        defaultMLModelShouldBeFound("tp.notEquals=" + UPDATED_TP);
    }

    @Test
    @Transactional
    public void getAllMLModelsByTpIsInShouldWork() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where tp in DEFAULT_TP or UPDATED_TP
        defaultMLModelShouldBeFound("tp.in=" + DEFAULT_TP + "," + UPDATED_TP);

        // Get all the mLModelList where tp equals to UPDATED_TP
        defaultMLModelShouldNotBeFound("tp.in=" + UPDATED_TP);
    }

    @Test
    @Transactional
    public void getAllMLModelsByTpIsNullOrNotNull() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where tp is not null
        defaultMLModelShouldBeFound("tp.specified=true");

        // Get all the mLModelList where tp is null
        defaultMLModelShouldNotBeFound("tp.specified=false");
    }

    @Test
    @Transactional
    public void getAllMLModelsByTpIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where tp is greater than or equal to DEFAULT_TP
        defaultMLModelShouldBeFound("tp.greaterThanOrEqual=" + DEFAULT_TP);

        // Get all the mLModelList where tp is greater than or equal to UPDATED_TP
        defaultMLModelShouldNotBeFound("tp.greaterThanOrEqual=" + UPDATED_TP);
    }

    @Test
    @Transactional
    public void getAllMLModelsByTpIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where tp is less than or equal to DEFAULT_TP
        defaultMLModelShouldBeFound("tp.lessThanOrEqual=" + DEFAULT_TP);

        // Get all the mLModelList where tp is less than or equal to SMALLER_TP
        defaultMLModelShouldNotBeFound("tp.lessThanOrEqual=" + SMALLER_TP);
    }

    @Test
    @Transactional
    public void getAllMLModelsByTpIsLessThanSomething() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where tp is less than DEFAULT_TP
        defaultMLModelShouldNotBeFound("tp.lessThan=" + DEFAULT_TP);

        // Get all the mLModelList where tp is less than UPDATED_TP
        defaultMLModelShouldBeFound("tp.lessThan=" + UPDATED_TP);
    }

    @Test
    @Transactional
    public void getAllMLModelsByTpIsGreaterThanSomething() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where tp is greater than DEFAULT_TP
        defaultMLModelShouldNotBeFound("tp.greaterThan=" + DEFAULT_TP);

        // Get all the mLModelList where tp is greater than SMALLER_TP
        defaultMLModelShouldBeFound("tp.greaterThan=" + SMALLER_TP);
    }


    @Test
    @Transactional
    public void getAllMLModelsByTnIsEqualToSomething() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where tn equals to DEFAULT_TN
        defaultMLModelShouldBeFound("tn.equals=" + DEFAULT_TN);

        // Get all the mLModelList where tn equals to UPDATED_TN
        defaultMLModelShouldNotBeFound("tn.equals=" + UPDATED_TN);
    }

    @Test
    @Transactional
    public void getAllMLModelsByTnIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where tn not equals to DEFAULT_TN
        defaultMLModelShouldNotBeFound("tn.notEquals=" + DEFAULT_TN);

        // Get all the mLModelList where tn not equals to UPDATED_TN
        defaultMLModelShouldBeFound("tn.notEquals=" + UPDATED_TN);
    }

    @Test
    @Transactional
    public void getAllMLModelsByTnIsInShouldWork() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where tn in DEFAULT_TN or UPDATED_TN
        defaultMLModelShouldBeFound("tn.in=" + DEFAULT_TN + "," + UPDATED_TN);

        // Get all the mLModelList where tn equals to UPDATED_TN
        defaultMLModelShouldNotBeFound("tn.in=" + UPDATED_TN);
    }

    @Test
    @Transactional
    public void getAllMLModelsByTnIsNullOrNotNull() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where tn is not null
        defaultMLModelShouldBeFound("tn.specified=true");

        // Get all the mLModelList where tn is null
        defaultMLModelShouldNotBeFound("tn.specified=false");
    }

    @Test
    @Transactional
    public void getAllMLModelsByTnIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where tn is greater than or equal to DEFAULT_TN
        defaultMLModelShouldBeFound("tn.greaterThanOrEqual=" + DEFAULT_TN);

        // Get all the mLModelList where tn is greater than or equal to UPDATED_TN
        defaultMLModelShouldNotBeFound("tn.greaterThanOrEqual=" + UPDATED_TN);
    }

    @Test
    @Transactional
    public void getAllMLModelsByTnIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where tn is less than or equal to DEFAULT_TN
        defaultMLModelShouldBeFound("tn.lessThanOrEqual=" + DEFAULT_TN);

        // Get all the mLModelList where tn is less than or equal to SMALLER_TN
        defaultMLModelShouldNotBeFound("tn.lessThanOrEqual=" + SMALLER_TN);
    }

    @Test
    @Transactional
    public void getAllMLModelsByTnIsLessThanSomething() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where tn is less than DEFAULT_TN
        defaultMLModelShouldNotBeFound("tn.lessThan=" + DEFAULT_TN);

        // Get all the mLModelList where tn is less than UPDATED_TN
        defaultMLModelShouldBeFound("tn.lessThan=" + UPDATED_TN);
    }

    @Test
    @Transactional
    public void getAllMLModelsByTnIsGreaterThanSomething() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where tn is greater than DEFAULT_TN
        defaultMLModelShouldNotBeFound("tn.greaterThan=" + DEFAULT_TN);

        // Get all the mLModelList where tn is greater than SMALLER_TN
        defaultMLModelShouldBeFound("tn.greaterThan=" + SMALLER_TN);
    }


    @Test
    @Transactional
    public void getAllMLModelsByFpIsEqualToSomething() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where fp equals to DEFAULT_FP
        defaultMLModelShouldBeFound("fp.equals=" + DEFAULT_FP);

        // Get all the mLModelList where fp equals to UPDATED_FP
        defaultMLModelShouldNotBeFound("fp.equals=" + UPDATED_FP);
    }

    @Test
    @Transactional
    public void getAllMLModelsByFpIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where fp not equals to DEFAULT_FP
        defaultMLModelShouldNotBeFound("fp.notEquals=" + DEFAULT_FP);

        // Get all the mLModelList where fp not equals to UPDATED_FP
        defaultMLModelShouldBeFound("fp.notEquals=" + UPDATED_FP);
    }

    @Test
    @Transactional
    public void getAllMLModelsByFpIsInShouldWork() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where fp in DEFAULT_FP or UPDATED_FP
        defaultMLModelShouldBeFound("fp.in=" + DEFAULT_FP + "," + UPDATED_FP);

        // Get all the mLModelList where fp equals to UPDATED_FP
        defaultMLModelShouldNotBeFound("fp.in=" + UPDATED_FP);
    }

    @Test
    @Transactional
    public void getAllMLModelsByFpIsNullOrNotNull() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where fp is not null
        defaultMLModelShouldBeFound("fp.specified=true");

        // Get all the mLModelList where fp is null
        defaultMLModelShouldNotBeFound("fp.specified=false");
    }

    @Test
    @Transactional
    public void getAllMLModelsByFpIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where fp is greater than or equal to DEFAULT_FP
        defaultMLModelShouldBeFound("fp.greaterThanOrEqual=" + DEFAULT_FP);

        // Get all the mLModelList where fp is greater than or equal to UPDATED_FP
        defaultMLModelShouldNotBeFound("fp.greaterThanOrEqual=" + UPDATED_FP);
    }

    @Test
    @Transactional
    public void getAllMLModelsByFpIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where fp is less than or equal to DEFAULT_FP
        defaultMLModelShouldBeFound("fp.lessThanOrEqual=" + DEFAULT_FP);

        // Get all the mLModelList where fp is less than or equal to SMALLER_FP
        defaultMLModelShouldNotBeFound("fp.lessThanOrEqual=" + SMALLER_FP);
    }

    @Test
    @Transactional
    public void getAllMLModelsByFpIsLessThanSomething() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where fp is less than DEFAULT_FP
        defaultMLModelShouldNotBeFound("fp.lessThan=" + DEFAULT_FP);

        // Get all the mLModelList where fp is less than UPDATED_FP
        defaultMLModelShouldBeFound("fp.lessThan=" + UPDATED_FP);
    }

    @Test
    @Transactional
    public void getAllMLModelsByFpIsGreaterThanSomething() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where fp is greater than DEFAULT_FP
        defaultMLModelShouldNotBeFound("fp.greaterThan=" + DEFAULT_FP);

        // Get all the mLModelList where fp is greater than SMALLER_FP
        defaultMLModelShouldBeFound("fp.greaterThan=" + SMALLER_FP);
    }


    @Test
    @Transactional
    public void getAllMLModelsByFnIsEqualToSomething() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where fn equals to DEFAULT_FN
        defaultMLModelShouldBeFound("fn.equals=" + DEFAULT_FN);

        // Get all the mLModelList where fn equals to UPDATED_FN
        defaultMLModelShouldNotBeFound("fn.equals=" + UPDATED_FN);
    }

    @Test
    @Transactional
    public void getAllMLModelsByFnIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where fn not equals to DEFAULT_FN
        defaultMLModelShouldNotBeFound("fn.notEquals=" + DEFAULT_FN);

        // Get all the mLModelList where fn not equals to UPDATED_FN
        defaultMLModelShouldBeFound("fn.notEquals=" + UPDATED_FN);
    }

    @Test
    @Transactional
    public void getAllMLModelsByFnIsInShouldWork() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where fn in DEFAULT_FN or UPDATED_FN
        defaultMLModelShouldBeFound("fn.in=" + DEFAULT_FN + "," + UPDATED_FN);

        // Get all the mLModelList where fn equals to UPDATED_FN
        defaultMLModelShouldNotBeFound("fn.in=" + UPDATED_FN);
    }

    @Test
    @Transactional
    public void getAllMLModelsByFnIsNullOrNotNull() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where fn is not null
        defaultMLModelShouldBeFound("fn.specified=true");

        // Get all the mLModelList where fn is null
        defaultMLModelShouldNotBeFound("fn.specified=false");
    }

    @Test
    @Transactional
    public void getAllMLModelsByFnIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where fn is greater than or equal to DEFAULT_FN
        defaultMLModelShouldBeFound("fn.greaterThanOrEqual=" + DEFAULT_FN);

        // Get all the mLModelList where fn is greater than or equal to UPDATED_FN
        defaultMLModelShouldNotBeFound("fn.greaterThanOrEqual=" + UPDATED_FN);
    }

    @Test
    @Transactional
    public void getAllMLModelsByFnIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where fn is less than or equal to DEFAULT_FN
        defaultMLModelShouldBeFound("fn.lessThanOrEqual=" + DEFAULT_FN);

        // Get all the mLModelList where fn is less than or equal to SMALLER_FN
        defaultMLModelShouldNotBeFound("fn.lessThanOrEqual=" + SMALLER_FN);
    }

    @Test
    @Transactional
    public void getAllMLModelsByFnIsLessThanSomething() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where fn is less than DEFAULT_FN
        defaultMLModelShouldNotBeFound("fn.lessThan=" + DEFAULT_FN);

        // Get all the mLModelList where fn is less than UPDATED_FN
        defaultMLModelShouldBeFound("fn.lessThan=" + UPDATED_FN);
    }

    @Test
    @Transactional
    public void getAllMLModelsByFnIsGreaterThanSomething() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where fn is greater than DEFAULT_FN
        defaultMLModelShouldNotBeFound("fn.greaterThan=" + DEFAULT_FN);

        // Get all the mLModelList where fn is greater than SMALLER_FN
        defaultMLModelShouldBeFound("fn.greaterThan=" + SMALLER_FN);
    }


    @Test
    @Transactional
    public void getAllMLModelsByAccuracyIsEqualToSomething() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where accuracy equals to DEFAULT_ACCURACY
        defaultMLModelShouldBeFound("accuracy.equals=" + DEFAULT_ACCURACY);

        // Get all the mLModelList where accuracy equals to UPDATED_ACCURACY
        defaultMLModelShouldNotBeFound("accuracy.equals=" + UPDATED_ACCURACY);
    }

    @Test
    @Transactional
    public void getAllMLModelsByAccuracyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where accuracy not equals to DEFAULT_ACCURACY
        defaultMLModelShouldNotBeFound("accuracy.notEquals=" + DEFAULT_ACCURACY);

        // Get all the mLModelList where accuracy not equals to UPDATED_ACCURACY
        defaultMLModelShouldBeFound("accuracy.notEquals=" + UPDATED_ACCURACY);
    }

    @Test
    @Transactional
    public void getAllMLModelsByAccuracyIsInShouldWork() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where accuracy in DEFAULT_ACCURACY or UPDATED_ACCURACY
        defaultMLModelShouldBeFound("accuracy.in=" + DEFAULT_ACCURACY + "," + UPDATED_ACCURACY);

        // Get all the mLModelList where accuracy equals to UPDATED_ACCURACY
        defaultMLModelShouldNotBeFound("accuracy.in=" + UPDATED_ACCURACY);
    }

    @Test
    @Transactional
    public void getAllMLModelsByAccuracyIsNullOrNotNull() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where accuracy is not null
        defaultMLModelShouldBeFound("accuracy.specified=true");

        // Get all the mLModelList where accuracy is null
        defaultMLModelShouldNotBeFound("accuracy.specified=false");
    }

    @Test
    @Transactional
    public void getAllMLModelsByAccuracyIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where accuracy is greater than or equal to DEFAULT_ACCURACY
        defaultMLModelShouldBeFound("accuracy.greaterThanOrEqual=" + DEFAULT_ACCURACY);

        // Get all the mLModelList where accuracy is greater than or equal to UPDATED_ACCURACY
        defaultMLModelShouldNotBeFound("accuracy.greaterThanOrEqual=" + UPDATED_ACCURACY);
    }

    @Test
    @Transactional
    public void getAllMLModelsByAccuracyIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where accuracy is less than or equal to DEFAULT_ACCURACY
        defaultMLModelShouldBeFound("accuracy.lessThanOrEqual=" + DEFAULT_ACCURACY);

        // Get all the mLModelList where accuracy is less than or equal to SMALLER_ACCURACY
        defaultMLModelShouldNotBeFound("accuracy.lessThanOrEqual=" + SMALLER_ACCURACY);
    }

    @Test
    @Transactional
    public void getAllMLModelsByAccuracyIsLessThanSomething() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where accuracy is less than DEFAULT_ACCURACY
        defaultMLModelShouldNotBeFound("accuracy.lessThan=" + DEFAULT_ACCURACY);

        // Get all the mLModelList where accuracy is less than UPDATED_ACCURACY
        defaultMLModelShouldBeFound("accuracy.lessThan=" + UPDATED_ACCURACY);
    }

    @Test
    @Transactional
    public void getAllMLModelsByAccuracyIsGreaterThanSomething() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where accuracy is greater than DEFAULT_ACCURACY
        defaultMLModelShouldNotBeFound("accuracy.greaterThan=" + DEFAULT_ACCURACY);

        // Get all the mLModelList where accuracy is greater than SMALLER_ACCURACY
        defaultMLModelShouldBeFound("accuracy.greaterThan=" + SMALLER_ACCURACY);
    }


    @Test
    @Transactional
    public void getAllMLModelsByPrecisionIsEqualToSomething() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where precision equals to DEFAULT_PRECISION
        defaultMLModelShouldBeFound("precision.equals=" + DEFAULT_PRECISION);

        // Get all the mLModelList where precision equals to UPDATED_PRECISION
        defaultMLModelShouldNotBeFound("precision.equals=" + UPDATED_PRECISION);
    }

    @Test
    @Transactional
    public void getAllMLModelsByPrecisionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where precision not equals to DEFAULT_PRECISION
        defaultMLModelShouldNotBeFound("precision.notEquals=" + DEFAULT_PRECISION);

        // Get all the mLModelList where precision not equals to UPDATED_PRECISION
        defaultMLModelShouldBeFound("precision.notEquals=" + UPDATED_PRECISION);
    }

    @Test
    @Transactional
    public void getAllMLModelsByPrecisionIsInShouldWork() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where precision in DEFAULT_PRECISION or UPDATED_PRECISION
        defaultMLModelShouldBeFound("precision.in=" + DEFAULT_PRECISION + "," + UPDATED_PRECISION);

        // Get all the mLModelList where precision equals to UPDATED_PRECISION
        defaultMLModelShouldNotBeFound("precision.in=" + UPDATED_PRECISION);
    }

    @Test
    @Transactional
    public void getAllMLModelsByPrecisionIsNullOrNotNull() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where precision is not null
        defaultMLModelShouldBeFound("precision.specified=true");

        // Get all the mLModelList where precision is null
        defaultMLModelShouldNotBeFound("precision.specified=false");
    }

    @Test
    @Transactional
    public void getAllMLModelsByPrecisionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where precision is greater than or equal to DEFAULT_PRECISION
        defaultMLModelShouldBeFound("precision.greaterThanOrEqual=" + DEFAULT_PRECISION);

        // Get all the mLModelList where precision is greater than or equal to UPDATED_PRECISION
        defaultMLModelShouldNotBeFound("precision.greaterThanOrEqual=" + UPDATED_PRECISION);
    }

    @Test
    @Transactional
    public void getAllMLModelsByPrecisionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where precision is less than or equal to DEFAULT_PRECISION
        defaultMLModelShouldBeFound("precision.lessThanOrEqual=" + DEFAULT_PRECISION);

        // Get all the mLModelList where precision is less than or equal to SMALLER_PRECISION
        defaultMLModelShouldNotBeFound("precision.lessThanOrEqual=" + SMALLER_PRECISION);
    }

    @Test
    @Transactional
    public void getAllMLModelsByPrecisionIsLessThanSomething() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where precision is less than DEFAULT_PRECISION
        defaultMLModelShouldNotBeFound("precision.lessThan=" + DEFAULT_PRECISION);

        // Get all the mLModelList where precision is less than UPDATED_PRECISION
        defaultMLModelShouldBeFound("precision.lessThan=" + UPDATED_PRECISION);
    }

    @Test
    @Transactional
    public void getAllMLModelsByPrecisionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where precision is greater than DEFAULT_PRECISION
        defaultMLModelShouldNotBeFound("precision.greaterThan=" + DEFAULT_PRECISION);

        // Get all the mLModelList where precision is greater than SMALLER_PRECISION
        defaultMLModelShouldBeFound("precision.greaterThan=" + SMALLER_PRECISION);
    }


    @Test
    @Transactional
    public void getAllMLModelsByRecallIsEqualToSomething() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where recall equals to DEFAULT_RECALL
        defaultMLModelShouldBeFound("recall.equals=" + DEFAULT_RECALL);

        // Get all the mLModelList where recall equals to UPDATED_RECALL
        defaultMLModelShouldNotBeFound("recall.equals=" + UPDATED_RECALL);
    }

    @Test
    @Transactional
    public void getAllMLModelsByRecallIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where recall not equals to DEFAULT_RECALL
        defaultMLModelShouldNotBeFound("recall.notEquals=" + DEFAULT_RECALL);

        // Get all the mLModelList where recall not equals to UPDATED_RECALL
        defaultMLModelShouldBeFound("recall.notEquals=" + UPDATED_RECALL);
    }

    @Test
    @Transactional
    public void getAllMLModelsByRecallIsInShouldWork() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where recall in DEFAULT_RECALL or UPDATED_RECALL
        defaultMLModelShouldBeFound("recall.in=" + DEFAULT_RECALL + "," + UPDATED_RECALL);

        // Get all the mLModelList where recall equals to UPDATED_RECALL
        defaultMLModelShouldNotBeFound("recall.in=" + UPDATED_RECALL);
    }

    @Test
    @Transactional
    public void getAllMLModelsByRecallIsNullOrNotNull() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where recall is not null
        defaultMLModelShouldBeFound("recall.specified=true");

        // Get all the mLModelList where recall is null
        defaultMLModelShouldNotBeFound("recall.specified=false");
    }

    @Test
    @Transactional
    public void getAllMLModelsByRecallIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where recall is greater than or equal to DEFAULT_RECALL
        defaultMLModelShouldBeFound("recall.greaterThanOrEqual=" + DEFAULT_RECALL);

        // Get all the mLModelList where recall is greater than or equal to UPDATED_RECALL
        defaultMLModelShouldNotBeFound("recall.greaterThanOrEqual=" + UPDATED_RECALL);
    }

    @Test
    @Transactional
    public void getAllMLModelsByRecallIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where recall is less than or equal to DEFAULT_RECALL
        defaultMLModelShouldBeFound("recall.lessThanOrEqual=" + DEFAULT_RECALL);

        // Get all the mLModelList where recall is less than or equal to SMALLER_RECALL
        defaultMLModelShouldNotBeFound("recall.lessThanOrEqual=" + SMALLER_RECALL);
    }

    @Test
    @Transactional
    public void getAllMLModelsByRecallIsLessThanSomething() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where recall is less than DEFAULT_RECALL
        defaultMLModelShouldNotBeFound("recall.lessThan=" + DEFAULT_RECALL);

        // Get all the mLModelList where recall is less than UPDATED_RECALL
        defaultMLModelShouldBeFound("recall.lessThan=" + UPDATED_RECALL);
    }

    @Test
    @Transactional
    public void getAllMLModelsByRecallIsGreaterThanSomething() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        // Get all the mLModelList where recall is greater than DEFAULT_RECALL
        defaultMLModelShouldNotBeFound("recall.greaterThan=" + DEFAULT_RECALL);

        // Get all the mLModelList where recall is greater than SMALLER_RECALL
        defaultMLModelShouldBeFound("recall.greaterThan=" + SMALLER_RECALL);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMLModelShouldBeFound(String filter) throws Exception {
        restMLModelMockMvc.perform(get("/api/ml-models?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mLModel.getId().intValue())))
            .andExpect(jsonPath("$.[*].tp").value(hasItem(DEFAULT_TP.doubleValue())))
            .andExpect(jsonPath("$.[*].tn").value(hasItem(DEFAULT_TN.doubleValue())))
            .andExpect(jsonPath("$.[*].fp").value(hasItem(DEFAULT_FP.doubleValue())))
            .andExpect(jsonPath("$.[*].fn").value(hasItem(DEFAULT_FN.doubleValue())))
            .andExpect(jsonPath("$.[*].accuracy").value(hasItem(DEFAULT_ACCURACY.doubleValue())))
            .andExpect(jsonPath("$.[*].precision").value(hasItem(DEFAULT_PRECISION.doubleValue())))
            .andExpect(jsonPath("$.[*].recall").value(hasItem(DEFAULT_RECALL.doubleValue())));

        // Check, that the count call also returns 1
        restMLModelMockMvc.perform(get("/api/ml-models/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMLModelShouldNotBeFound(String filter) throws Exception {
        restMLModelMockMvc.perform(get("/api/ml-models?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMLModelMockMvc.perform(get("/api/ml-models/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingMLModel() throws Exception {
        // Get the mLModel
        restMLModelMockMvc.perform(get("/api/ml-models/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMLModel() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        int databaseSizeBeforeUpdate = mLModelRepository.findAll().size();

        // Update the mLModel
        MLModel updatedMLModel = mLModelRepository.findById(mLModel.getId()).get();
        // Disconnect from session so that the updates on updatedMLModel are not directly saved in db
        em.detach(updatedMLModel);
        updatedMLModel
            .tp(UPDATED_TP)
            .tn(UPDATED_TN)
            .fp(UPDATED_FP)
            .fn(UPDATED_FN)
            .accuracy(UPDATED_ACCURACY)
            .precision(UPDATED_PRECISION)
            .recall(UPDATED_RECALL);
        MLModelDTO mLModelDTO = mLModelMapper.toDto(updatedMLModel);

        restMLModelMockMvc.perform(put("/api/ml-models")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(mLModelDTO)))
            .andExpect(status().isOk());

        // Validate the MLModel in the database
        List<MLModel> mLModelList = mLModelRepository.findAll();
        assertThat(mLModelList).hasSize(databaseSizeBeforeUpdate);
        MLModel testMLModel = mLModelList.get(mLModelList.size() - 1);
        assertThat(testMLModel.getTp()).isEqualTo(UPDATED_TP);
        assertThat(testMLModel.getTn()).isEqualTo(UPDATED_TN);
        assertThat(testMLModel.getFp()).isEqualTo(UPDATED_FP);
        assertThat(testMLModel.getFn()).isEqualTo(UPDATED_FN);
        assertThat(testMLModel.getAccuracy()).isEqualTo(UPDATED_ACCURACY);
        assertThat(testMLModel.getPrecision()).isEqualTo(UPDATED_PRECISION);
        assertThat(testMLModel.getRecall()).isEqualTo(UPDATED_RECALL);
    }

    @Test
    @Transactional
    public void updateNonExistingMLModel() throws Exception {
        int databaseSizeBeforeUpdate = mLModelRepository.findAll().size();

        // Create the MLModel
        MLModelDTO mLModelDTO = mLModelMapper.toDto(mLModel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMLModelMockMvc.perform(put("/api/ml-models")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(mLModelDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MLModel in the database
        List<MLModel> mLModelList = mLModelRepository.findAll();
        assertThat(mLModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMLModel() throws Exception {
        // Initialize the database
        mLModelRepository.saveAndFlush(mLModel);

        int databaseSizeBeforeDelete = mLModelRepository.findAll().size();

        // Delete the mLModel
        restMLModelMockMvc.perform(delete("/api/ml-models/{id}", mLModel.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MLModel> mLModelList = mLModelRepository.findAll();
        assertThat(mLModelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
