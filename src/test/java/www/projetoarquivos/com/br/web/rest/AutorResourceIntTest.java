package www.projetoarquivos.com.br.web.rest;

import www.projetoarquivos.com.br.ServiceApp;

import www.projetoarquivos.com.br.domain.Autor;
import www.projetoarquivos.com.br.repository.AutorRepository;
import www.projetoarquivos.com.br.service.AutorService;
import www.projetoarquivos.com.br.service.dto.AutorDTO;
import www.projetoarquivos.com.br.service.mapper.AutorMapper;
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
 * Test class for the AutorResource REST controller.
 *
 * @see AutorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceApp.class)
public class AutorResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private AutorMapper autorMapper;

    @Autowired
    private AutorService autorService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAutorMockMvc;

    private Autor autor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AutorResource autorResource = new AutorResource(autorService);
        this.restAutorMockMvc = MockMvcBuilders.standaloneSetup(autorResource)
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
    public static Autor createEntity(EntityManager em) {
        Autor autor = new Autor()
            .nome(DEFAULT_NOME);
        return autor;
    }

    @Before
    public void initTest() {
        autor = createEntity(em);
    }

    @Test
    @Transactional
    public void createAutor() throws Exception {
        int databaseSizeBeforeCreate = autorRepository.findAll().size();

        // Create the Autor
        AutorDTO autorDTO = autorMapper.toDto(autor);
        restAutorMockMvc.perform(post("/api/autors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(autorDTO)))
            .andExpect(status().isCreated());

        // Validate the Autor in the database
        List<Autor> autorList = autorRepository.findAll();
        assertThat(autorList).hasSize(databaseSizeBeforeCreate + 1);
        Autor testAutor = autorList.get(autorList.size() - 1);
        assertThat(testAutor.getNome()).isEqualTo(DEFAULT_NOME);
    }

    @Test
    @Transactional
    public void createAutorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = autorRepository.findAll().size();

        // Create the Autor with an existing ID
        autor.setId(1L);
        AutorDTO autorDTO = autorMapper.toDto(autor);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAutorMockMvc.perform(post("/api/autors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(autorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Autor> autorList = autorRepository.findAll();
        assertThat(autorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAutors() throws Exception {
        // Initialize the database
        autorRepository.saveAndFlush(autor);

        // Get all the autorList
        restAutorMockMvc.perform(get("/api/autors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(autor.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())));
    }

    @Test
    @Transactional
    public void getAutor() throws Exception {
        // Initialize the database
        autorRepository.saveAndFlush(autor);

        // Get the autor
        restAutorMockMvc.perform(get("/api/autors/{id}", autor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(autor.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAutor() throws Exception {
        // Get the autor
        restAutorMockMvc.perform(get("/api/autors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAutor() throws Exception {
        // Initialize the database
        autorRepository.saveAndFlush(autor);
        int databaseSizeBeforeUpdate = autorRepository.findAll().size();

        // Update the autor
        Autor updatedAutor = autorRepository.findOne(autor.getId());
        updatedAutor
            .nome(UPDATED_NOME);
        AutorDTO autorDTO = autorMapper.toDto(updatedAutor);

        restAutorMockMvc.perform(put("/api/autors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(autorDTO)))
            .andExpect(status().isOk());

        // Validate the Autor in the database
        List<Autor> autorList = autorRepository.findAll();
        assertThat(autorList).hasSize(databaseSizeBeforeUpdate);
        Autor testAutor = autorList.get(autorList.size() - 1);
        assertThat(testAutor.getNome()).isEqualTo(UPDATED_NOME);
    }

    @Test
    @Transactional
    public void updateNonExistingAutor() throws Exception {
        int databaseSizeBeforeUpdate = autorRepository.findAll().size();

        // Create the Autor
        AutorDTO autorDTO = autorMapper.toDto(autor);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAutorMockMvc.perform(put("/api/autors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(autorDTO)))
            .andExpect(status().isCreated());

        // Validate the Autor in the database
        List<Autor> autorList = autorRepository.findAll();
        assertThat(autorList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAutor() throws Exception {
        // Initialize the database
        autorRepository.saveAndFlush(autor);
        int databaseSizeBeforeDelete = autorRepository.findAll().size();

        // Get the autor
        restAutorMockMvc.perform(delete("/api/autors/{id}", autor.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Autor> autorList = autorRepository.findAll();
        assertThat(autorList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Autor.class);
        Autor autor1 = new Autor();
        autor1.setId(1L);
        Autor autor2 = new Autor();
        autor2.setId(autor1.getId());
        assertThat(autor1).isEqualTo(autor2);
        autor2.setId(2L);
        assertThat(autor1).isNotEqualTo(autor2);
        autor1.setId(null);
        assertThat(autor1).isNotEqualTo(autor2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AutorDTO.class);
        AutorDTO autorDTO1 = new AutorDTO();
        autorDTO1.setId(1L);
        AutorDTO autorDTO2 = new AutorDTO();
        assertThat(autorDTO1).isNotEqualTo(autorDTO2);
        autorDTO2.setId(autorDTO1.getId());
        assertThat(autorDTO1).isEqualTo(autorDTO2);
        autorDTO2.setId(2L);
        assertThat(autorDTO1).isNotEqualTo(autorDTO2);
        autorDTO1.setId(null);
        assertThat(autorDTO1).isNotEqualTo(autorDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(autorMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(autorMapper.fromId(null)).isNull();
    }
}
