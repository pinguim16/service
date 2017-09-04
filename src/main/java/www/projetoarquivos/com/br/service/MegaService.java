package www.projetoarquivos.com.br.service;

import www.projetoarquivos.com.br.service.dto.MegaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Mega.
 */
public interface MegaService {

    /**
     * Save a mega.
     *
     * @param megaDTO the entity to save
     * @return the persisted entity
     */
    MegaDTO save(MegaDTO megaDTO);

    /**
     *  Get all the megas.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<MegaDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" mega.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    MegaDTO findOne(Long id);

    /**
     *  Delete the "id" mega.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
