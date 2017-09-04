package www.projetoarquivos.com.br.web.rest;

import www.projetoarquivos.com.br.ServiceApp;

import www.projetoarquivos.com.br.domain.Arquivo;
import www.projetoarquivos.com.br.repository.ArquivoRepository;
import www.projetoarquivos.com.br.service.ArquivoService;
import www.projetoarquivos.com.br.service.dto.ArquivoDTO;
import www.projetoarquivos.com.br.service.mapper.ArquivoMapper;
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
 * Test class for the ArquivoResource REST controller.
 *
 * @see ArquivoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceApp.class)
public class ArquivoResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_LINK_DOWNLOAD = "AAAAAAAAAA";
    private static final String UPDATED_LINK_DOWNLOAD = "BBBBBBBBBB";

    private static final Double DEFAULT_TAMANHO = 1D;
    private static final Double UPDATED_TAMANHO = 2D;

    private static final String DEFAULT_SENHA = "AAAAAAAAAA";
    private static final String UPDATED_SENHA = "BBBBBBBBBB";

    @Autowired
    private ArquivoRepository arquivoRepository;

    @Autowired
    private ArquivoMapper arquivoMapper;

    @Autowired
    private ArquivoService arquivoService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restArquivoMockMvc;

    private Arquivo arquivo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ArquivoResource arquivoResource = new ArquivoResource(arquivoService);
        this.restArquivoMockMvc = MockMvcBuilders.standaloneSetup(arquivoResource)
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
    public static Arquivo createEntity(EntityManager em) {
        Arquivo arquivo = new Arquivo()
            .nome(DEFAULT_NOME)
            .linkDownload(DEFAULT_LINK_DOWNLOAD)
            .tamanho(DEFAULT_TAMANHO)
            .senha(DEFAULT_SENHA);
        return arquivo;
    }

    @Before
    public void initTest() {
        arquivo = createEntity(em);
    }

    @Test
    @Transactional
    public void createArquivo() throws Exception {
        int databaseSizeBeforeCreate = arquivoRepository.findAll().size();

        // Create the Arquivo
        ArquivoDTO arquivoDTO = arquivoMapper.toDto(arquivo);
        restArquivoMockMvc.perform(post("/api/arquivos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(arquivoDTO)))
            .andExpect(status().isCreated());

        // Validate the Arquivo in the database
        List<Arquivo> arquivoList = arquivoRepository.findAll();
        assertThat(arquivoList).hasSize(databaseSizeBeforeCreate + 1);
        Arquivo testArquivo = arquivoList.get(arquivoList.size() - 1);
        assertThat(testArquivo.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testArquivo.getLinkDownload()).isEqualTo(DEFAULT_LINK_DOWNLOAD);
        assertThat(testArquivo.getTamanho()).isEqualTo(DEFAULT_TAMANHO);
        assertThat(testArquivo.getSenha()).isEqualTo(DEFAULT_SENHA);
    }

    @Test
    @Transactional
    public void createArquivoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = arquivoRepository.findAll().size();

        // Create the Arquivo with an existing ID
        arquivo.setId(1L);
        ArquivoDTO arquivoDTO = arquivoMapper.toDto(arquivo);

        // An entity with an existing ID cannot be created, so this API call must fail
        restArquivoMockMvc.perform(post("/api/arquivos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(arquivoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Arquivo> arquivoList = arquivoRepository.findAll();
        assertThat(arquivoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllArquivos() throws Exception {
        // Initialize the database
        arquivoRepository.saveAndFlush(arquivo);

        // Get all the arquivoList
        restArquivoMockMvc.perform(get("/api/arquivos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(arquivo.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].linkDownload").value(hasItem(DEFAULT_LINK_DOWNLOAD.toString())))
            .andExpect(jsonPath("$.[*].tamanho").value(hasItem(DEFAULT_TAMANHO.doubleValue())))
            .andExpect(jsonPath("$.[*].senha").value(hasItem(DEFAULT_SENHA.toString())));
    }

    @Test
    @Transactional
    public void getArquivo() throws Exception {
        // Initialize the database
        arquivoRepository.saveAndFlush(arquivo);

        // Get the arquivo
        restArquivoMockMvc.perform(get("/api/arquivos/{id}", arquivo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(arquivo.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.linkDownload").value(DEFAULT_LINK_DOWNLOAD.toString()))
            .andExpect(jsonPath("$.tamanho").value(DEFAULT_TAMANHO.doubleValue()))
            .andExpect(jsonPath("$.senha").value(DEFAULT_SENHA.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingArquivo() throws Exception {
        // Get the arquivo
        restArquivoMockMvc.perform(get("/api/arquivos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateArquivo() throws Exception {
        // Initialize the database
        arquivoRepository.saveAndFlush(arquivo);
        int databaseSizeBeforeUpdate = arquivoRepository.findAll().size();

        // Update the arquivo
        Arquivo updatedArquivo = arquivoRepository.findOne(arquivo.getId());
        updatedArquivo
            .nome(UPDATED_NOME)
            .linkDownload(UPDATED_LINK_DOWNLOAD)
            .tamanho(UPDATED_TAMANHO)
            .senha(UPDATED_SENHA);
        ArquivoDTO arquivoDTO = arquivoMapper.toDto(updatedArquivo);

        restArquivoMockMvc.perform(put("/api/arquivos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(arquivoDTO)))
            .andExpect(status().isOk());

        // Validate the Arquivo in the database
        List<Arquivo> arquivoList = arquivoRepository.findAll();
        assertThat(arquivoList).hasSize(databaseSizeBeforeUpdate);
        Arquivo testArquivo = arquivoList.get(arquivoList.size() - 1);
        assertThat(testArquivo.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testArquivo.getLinkDownload()).isEqualTo(UPDATED_LINK_DOWNLOAD);
        assertThat(testArquivo.getTamanho()).isEqualTo(UPDATED_TAMANHO);
        assertThat(testArquivo.getSenha()).isEqualTo(UPDATED_SENHA);
    }

    @Test
    @Transactional
    public void updateNonExistingArquivo() throws Exception {
        int databaseSizeBeforeUpdate = arquivoRepository.findAll().size();

        // Create the Arquivo
        ArquivoDTO arquivoDTO = arquivoMapper.toDto(arquivo);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restArquivoMockMvc.perform(put("/api/arquivos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(arquivoDTO)))
            .andExpect(status().isCreated());

        // Validate the Arquivo in the database
        List<Arquivo> arquivoList = arquivoRepository.findAll();
        assertThat(arquivoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteArquivo() throws Exception {
        // Initialize the database
        arquivoRepository.saveAndFlush(arquivo);
        int databaseSizeBeforeDelete = arquivoRepository.findAll().size();

        // Get the arquivo
        restArquivoMockMvc.perform(delete("/api/arquivos/{id}", arquivo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Arquivo> arquivoList = arquivoRepository.findAll();
        assertThat(arquivoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Arquivo.class);
        Arquivo arquivo1 = new Arquivo();
        arquivo1.setId(1L);
        Arquivo arquivo2 = new Arquivo();
        arquivo2.setId(arquivo1.getId());
        assertThat(arquivo1).isEqualTo(arquivo2);
        arquivo2.setId(2L);
        assertThat(arquivo1).isNotEqualTo(arquivo2);
        arquivo1.setId(null);
        assertThat(arquivo1).isNotEqualTo(arquivo2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArquivoDTO.class);
        ArquivoDTO arquivoDTO1 = new ArquivoDTO();
        arquivoDTO1.setId(1L);
        ArquivoDTO arquivoDTO2 = new ArquivoDTO();
        assertThat(arquivoDTO1).isNotEqualTo(arquivoDTO2);
        arquivoDTO2.setId(arquivoDTO1.getId());
        assertThat(arquivoDTO1).isEqualTo(arquivoDTO2);
        arquivoDTO2.setId(2L);
        assertThat(arquivoDTO1).isNotEqualTo(arquivoDTO2);
        arquivoDTO1.setId(null);
        assertThat(arquivoDTO1).isNotEqualTo(arquivoDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(arquivoMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(arquivoMapper.fromId(null)).isNull();
    }
}
