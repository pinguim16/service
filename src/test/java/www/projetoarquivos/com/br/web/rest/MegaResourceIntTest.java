package www.projetoarquivos.com.br.web.rest;

import www.projetoarquivos.com.br.ServiceApp;

import www.projetoarquivos.com.br.domain.Mega;
import www.projetoarquivos.com.br.repository.MegaRepository;
import www.projetoarquivos.com.br.service.MegaService;
import www.projetoarquivos.com.br.service.dto.MegaDTO;
import www.projetoarquivos.com.br.service.mapper.MegaMapper;
import www.projetoarquivos.com.br.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MegaResource REST controller.
 *
 * @see MegaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceApp.class)
public class MegaResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_SENHA = "AAAAAAAAAA";
    private static final String UPDATED_SENHA = "BBBBBBBBBB";

    @Autowired
    private MegaRepository megaRepository;

    @Autowired
    private MegaMapper megaMapper;

    @Autowired
    private MegaService megaService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMegaMockMvc;

    private Mega mega;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MegaResource megaResource = new MegaResource(megaService);
        this.restMegaMockMvc = MockMvcBuilders.standaloneSetup(megaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mega createEntity(EntityManager em) {
        Mega mega = new Mega()
            .nome(DEFAULT_NOME)
            .senha(DEFAULT_SENHA);
        return mega;
    }

    @Before
    public void initTest() {
        mega = createEntity(em);
    }

    @Test
    @Transactional
    public void createMega() throws Exception {
        int databaseSizeBeforeCreate = megaRepository.findAll().size();

        // Create the Mega
        MegaDTO megaDTO = megaMapper.toDto(mega);
        restMegaMockMvc.perform(post("/api/megas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(megaDTO)))
            .andExpect(status().isCreated());

        // Validate the Mega in the database
        List<Mega> megaList = megaRepository.findAll();
        assertThat(megaList).hasSize(databaseSizeBeforeCreate + 1);
        Mega testMega = megaList.get(megaList.size() - 1);
        assertThat(testMega.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testMega.getSenha()).isEqualTo(DEFAULT_SENHA);
    }

    @Test
    @Transactional
    public void createMegaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = megaRepository.findAll().size();

        // Create the Mega with an existing ID
        mega.setId(1L);
        MegaDTO megaDTO = megaMapper.toDto(mega);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMegaMockMvc.perform(post("/api/megas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(megaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Mega> megaList = megaRepository.findAll();
        assertThat(megaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllMegas() throws Exception {
        // Initialize the database
        megaRepository.saveAndFlush(mega);

        // Get all the megaList
        restMegaMockMvc.perform(get("/api/megas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mega.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].senha").value(hasItem(DEFAULT_SENHA.toString())));
    }

    @Test
    @Transactional
    public void getMega() throws Exception {
        // Initialize the database
        megaRepository.saveAndFlush(mega);

        // Get the mega
        restMegaMockMvc.perform(get("/api/megas/{id}", mega.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(mega.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.senha").value(DEFAULT_SENHA.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMega() throws Exception {
        // Get the mega
        restMegaMockMvc.perform(get("/api/megas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMega() throws Exception {
        // Initialize the database
        megaRepository.saveAndFlush(mega);
        int databaseSizeBeforeUpdate = megaRepository.findAll().size();

        // Update the mega
        Mega updatedMega = megaRepository.findOne(mega.getId());
        updatedMega
            .nome(UPDATED_NOME)
            .senha(UPDATED_SENHA);
        MegaDTO megaDTO = megaMapper.toDto(updatedMega);

        restMegaMockMvc.perform(put("/api/megas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(megaDTO)))
            .andExpect(status().isOk());

        // Validate the Mega in the database
        List<Mega> megaList = megaRepository.findAll();
        assertThat(megaList).hasSize(databaseSizeBeforeUpdate);
        Mega testMega = megaList.get(megaList.size() - 1);
        assertThat(testMega.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testMega.getSenha()).isEqualTo(UPDATED_SENHA);
    }

    @Test
    @Transactional
    public void updateNonExistingMega() throws Exception {
        int databaseSizeBeforeUpdate = megaRepository.findAll().size();

        // Create the Mega
        MegaDTO megaDTO = megaMapper.toDto(mega);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMegaMockMvc.perform(put("/api/megas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(megaDTO)))
            .andExpect(status().isCreated());

        // Validate the Mega in the database
        List<Mega> megaList = megaRepository.findAll();
        assertThat(megaList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMega() throws Exception {
        // Initialize the database
        megaRepository.saveAndFlush(mega);
        int databaseSizeBeforeDelete = megaRepository.findAll().size();

        // Get the mega
        restMegaMockMvc.perform(delete("/api/megas/{id}", mega.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Mega> megaList = megaRepository.findAll();
        assertThat(megaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Mega.class);
        Mega mega1 = new Mega();
        mega1.setId(1L);
        Mega mega2 = new Mega();
        mega2.setId(mega1.getId());
        assertThat(mega1).isEqualTo(mega2);
        mega2.setId(2L);
        assertThat(mega1).isNotEqualTo(mega2);
        mega1.setId(null);
        assertThat(mega1).isNotEqualTo(mega2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MegaDTO.class);
        MegaDTO megaDTO1 = new MegaDTO();
        megaDTO1.setId(1L);
        MegaDTO megaDTO2 = new MegaDTO();
        assertThat(megaDTO1).isNotEqualTo(megaDTO2);
        megaDTO2.setId(megaDTO1.getId());
        assertThat(megaDTO1).isEqualTo(megaDTO2);
        megaDTO2.setId(2L);
        assertThat(megaDTO1).isNotEqualTo(megaDTO2);
        megaDTO1.setId(null);
        assertThat(megaDTO1).isNotEqualTo(megaDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(megaMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(megaMapper.fromId(null)).isNull();
    }
}
