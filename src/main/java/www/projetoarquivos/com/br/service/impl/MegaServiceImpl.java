package www.projetoarquivos.com.br.service.impl;

import www.projetoarquivos.com.br.service.MegaService;
import www.projetoarquivos.com.br.domain.Mega;
import www.projetoarquivos.com.br.repository.MegaRepository;
import www.projetoarquivos.com.br.service.dto.MegaDTO;
import www.projetoarquivos.com.br.service.mapper.MegaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Mega.
 */
@Service
@Transactional
public class MegaServiceImpl implements MegaService{

    private final Logger log = LoggerFactory.getLogger(MegaServiceImpl.class);

    private final MegaRepository megaRepository;

    private final MegaMapper megaMapper;

    public MegaServiceImpl(MegaRepository megaRepository, MegaMapper megaMapper) {
        this.megaRepository = megaRepository;
        this.megaMapper = megaMapper;
    }

    /**
     * Save a mega.
     *
     * @param megaDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public MegaDTO save(MegaDTO megaDTO) {
        log.debug("Request to save Mega : {}", megaDTO);
        Mega mega = megaMapper.toEntity(megaDTO);
        mega = megaRepository.save(mega);
        return megaMapper.toDto(mega);
    }

    /**
     *  Get all the megas.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MegaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Megas");
        return megaRepository.findAll(pageable)
            .map(megaMapper::toDto);
    }

    /**
     *  Get one mega by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public MegaDTO findOne(Long id) {
        log.debug("Request to get Mega : {}", id);
        Mega mega = megaRepository.findOne(id);
        return megaMapper.toDto(mega);
    }

    /**
     *  Delete the  mega by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Mega : {}", id);
        megaRepository.delete(id);
    }
}
