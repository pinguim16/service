package www.projetoarquivos.com.br.service.mapper;

import www.projetoarquivos.com.br.domain.*;
import www.projetoarquivos.com.br.service.dto.MegaDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Mega and its DTO MegaDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MegaMapper extends EntityMapper <MegaDTO, Mega> {
    
    
    default Mega fromId(Long id) {
        if (id == null) {
            return null;
        }
        Mega mega = new Mega();
        mega.setId(id);
        return mega;
    }
}
