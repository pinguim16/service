package www.projetoarquivos.com.br.service;

import www.projetoarquivos.com.br.service.dto.ArquivoDTO;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Arquivo.
 */
public interface ArquivoService {

    /**
     * Save a arquivo.
     *
     * @param arquivoDTO the entity to save
     * @return the persisted entity
     */
    ArquivoDTO save(ArquivoDTO arquivoDTO);

    /**
     *  Get all the arquivos.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ArquivoDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" arquivo.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ArquivoDTO findOne(Long id);

    /**
     *  Delete the "id" arquivo.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
    
    /**
     *  Get all the arquivos.
     *
     *  @return the list of entities
     */
    List<ArquivoDTO> findAllArquivos();
}
