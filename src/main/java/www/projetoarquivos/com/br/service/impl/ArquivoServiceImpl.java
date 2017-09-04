package www.projetoarquivos.com.br.service.impl;

import www.projetoarquivos.com.br.service.ArquivoService;
import www.projetoarquivos.com.br.domain.Arquivo;
import www.projetoarquivos.com.br.repository.ArquivoRepository;
import www.projetoarquivos.com.br.service.dto.ArquivoDTO;
import www.projetoarquivos.com.br.service.mapper.ArquivoMapper;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Arquivo.
 */
@Service
@Transactional
public class ArquivoServiceImpl implements ArquivoService{

    private final Logger log = LoggerFactory.getLogger(ArquivoServiceImpl.class);

    private final ArquivoRepository arquivoRepository;

    private final ArquivoMapper arquivoMapper;

    public ArquivoServiceImpl(ArquivoRepository arquivoRepository, ArquivoMapper arquivoMapper) {
        this.arquivoRepository = arquivoRepository;
        this.arquivoMapper = arquivoMapper;
    }

    /**
     * Save a arquivo.
     *
     * @param arquivoDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ArquivoDTO save(ArquivoDTO arquivoDTO) {
        log.debug("Request to save Arquivo : {}", arquivoDTO);
        Arquivo arquivo = arquivoMapper.toEntity(arquivoDTO);
        arquivo = arquivoRepository.save(arquivo);
        return arquivoMapper.toDto(arquivo);
    }

    /**
     *  Get all the arquivos.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ArquivoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Arquivos");
        return arquivoRepository.findAll(pageable)
            .map(arquivoMapper::toDto);
    }

    /**
     *  Get one arquivo by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ArquivoDTO findOne(Long id) {
        log.debug("Request to get Arquivo : {}", id);
        Arquivo arquivo = arquivoRepository.findOne(id);
        return arquivoMapper.toDto(arquivo);
    }

    /**
     *  Delete the  arquivo by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Arquivo : {}", id);
        arquivoRepository.delete(id);
    }

	@Override
	public List<ArquivoDTO> findAllArquivos() {
		List<Arquivo> listArquivos = arquivoRepository.findAll();
		return arquivoMapper.toDto(listArquivos);
	}
}
