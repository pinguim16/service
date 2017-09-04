package www.projetoarquivos.com.br.repository;

import www.projetoarquivos.com.br.domain.Autor;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Autor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AutorRepository extends JpaRepository<Autor,Long> {
    
}
