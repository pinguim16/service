package www.projetoarquivos.com.br.repository;

import www.projetoarquivos.com.br.domain.Mega;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Mega entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MegaRepository extends JpaRepository<Mega,Long> {
    
}
