package www.projetoarquivos.com.br.service.mapper;

import www.projetoarquivos.com.br.domain.*;
import www.projetoarquivos.com.br.service.dto.ArquivoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Arquivo and its DTO ArquivoDTO.
 */
@Mapper(componentModel = "spring", uses = {MegaMapper.class, AutorMapper.class })
public interface ArquivoMapper extends EntityMapper <ArquivoDTO, Arquivo> {

    ArquivoDTO toDto(Arquivo arquivo); 

    Arquivo toEntity(ArquivoDTO arquivoDTO); 
    
    default Arquivo fromId(Long id) {
        if (id == null) {
            return null;
        }
        Arquivo arquivo = new Arquivo();
        arquivo.setId(id);
        return arquivo;
    }
}
