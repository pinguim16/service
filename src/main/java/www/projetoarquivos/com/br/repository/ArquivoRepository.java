package www.projetoarquivos.com.br.repository;

import www.projetoarquivos.com.br.domain.Arquivo;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Arquivo entity.
 */
@Repository
public interface ArquivoRepository extends JpaRepository<Arquivo,Long> {
	
	List<Arquivo> findAll();
}
