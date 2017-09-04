package www.projetoarquivos.com.br.web.rest;

import com.codahale.metrics.annotation.Timed;
import www.projetoarquivos.com.br.service.AutorService;
import www.projetoarquivos.com.br.web.rest.util.HeaderUtil;
import www.projetoarquivos.com.br.web.rest.util.PaginationUtil;
import www.projetoarquivos.com.br.service.dto.AutorDTO;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Autor.
 */
@RestController
@RequestMapping("/api")
public class AutorResource {

    private final Logger log = LoggerFactory.getLogger(AutorResource.class);

    private static final String ENTITY_NAME = "autor";

    private final AutorService autorService;

    public AutorResource(AutorService autorService) {
        this.autorService = autorService;
    }

    /**
     * POST  /autors : Create a new autor.
     *
     * @param autorDTO the autorDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new autorDTO, or with status 400 (Bad Request) if the autor has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/autors")
    @Timed
    public ResponseEntity<AutorDTO> createAutor(@RequestBody AutorDTO autorDTO) throws URISyntaxException {
        log.debug("REST request to save Autor : {}", autorDTO);
        if (autorDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new autor cannot already have an ID")).body(null);
        }
        AutorDTO result = autorService.save(autorDTO);
        return ResponseEntity.created(new URI("/api/autors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /autors : Updates an existing autor.
     *
     * @param autorDTO the autorDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated autorDTO,
     * or with status 400 (Bad Request) if the autorDTO is not valid,
     * or with status 500 (Internal Server Error) if the autorDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/autors")
    @Timed
    public ResponseEntity<AutorDTO> updateAutor(@RequestBody AutorDTO autorDTO) throws URISyntaxException {
        log.debug("REST request to update Autor : {}", autorDTO);
        if (autorDTO.getId() == null) {
            return createAutor(autorDTO);
        }
        AutorDTO result = autorService.save(autorDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, autorDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /autors : get all the autors.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of autors in body
     */
    @GetMapping("/autors")
    @Timed
    public ResponseEntity<List<AutorDTO>> getAllAutors(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Autors");
        Page<AutorDTO> page = autorService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/autors");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /autors/:id : get the "id" autor.
     *
     * @param id the id of the autorDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the autorDTO, or with status 404 (Not Found)
     */
    @GetMapping("/autors/{id}")
    @Timed
    public ResponseEntity<AutorDTO> getAutor(@PathVariable Long id) {
        log.debug("REST request to get Autor : {}", id);
        AutorDTO autorDTO = autorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(autorDTO));
    }

    /**
     * DELETE  /autors/:id : delete the "id" autor.
     *
     * @param id the id of the autorDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/autors/{id}")
    @Timed
    public ResponseEntity<Void> deleteAutor(@PathVariable Long id) {
        log.debug("REST request to delete Autor : {}", id);
        autorService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
