package www.projetoarquivos.com.br.web.rest;

import com.codahale.metrics.annotation.Timed;
import www.projetoarquivos.com.br.service.MegaService;
import www.projetoarquivos.com.br.web.rest.util.HeaderUtil;
import www.projetoarquivos.com.br.web.rest.util.PaginationUtil;
import www.projetoarquivos.com.br.service.dto.MegaDTO;
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
 * REST controller for managing Mega.
 */
@RestController
@RequestMapping("/api")
public class MegaResource {

    private final Logger log = LoggerFactory.getLogger(MegaResource.class);

    private static final String ENTITY_NAME = "mega";

    private final MegaService megaService;

    public MegaResource(MegaService megaService) {
        this.megaService = megaService;
    }

    /**
     * POST  /megas : Create a new mega.
     *
     * @param megaDTO the megaDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new megaDTO, or with status 400 (Bad Request) if the mega has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/megas")
    @Timed
    public ResponseEntity<MegaDTO> createMega(@RequestBody MegaDTO megaDTO) throws URISyntaxException {
        log.debug("REST request to save Mega : {}", megaDTO);
        if (megaDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new mega cannot already have an ID")).body(null);
        }
        MegaDTO result = megaService.save(megaDTO);
        return ResponseEntity.created(new URI("/api/megas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /megas : Updates an existing mega.
     *
     * @param megaDTO the megaDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated megaDTO,
     * or with status 400 (Bad Request) if the megaDTO is not valid,
     * or with status 500 (Internal Server Error) if the megaDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/megas")
    @Timed
    public ResponseEntity<MegaDTO> updateMega(@RequestBody MegaDTO megaDTO) throws URISyntaxException {
        log.debug("REST request to update Mega : {}", megaDTO);
        if (megaDTO.getId() == null) {
            return createMega(megaDTO);
        }
        MegaDTO result = megaService.save(megaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, megaDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /megas : get all the megas.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of megas in body
     */
    @GetMapping("/megas")
    @Timed
    public ResponseEntity<List<MegaDTO>> getAllMegas(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Megas");
        Page<MegaDTO> page = megaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/megas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /megas/:id : get the "id" mega.
     *
     * @param id the id of the megaDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the megaDTO, or with status 404 (Not Found)
     */
    @GetMapping("/megas/{id}")
    @Timed
    public ResponseEntity<MegaDTO> getMega(@PathVariable Long id) {
        log.debug("REST request to get Mega : {}", id);
        MegaDTO megaDTO = megaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(megaDTO));
    }

    /**
     * DELETE  /megas/:id : delete the "id" mega.
     *
     * @param id the id of the megaDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/megas/{id}")
    @Timed
    public ResponseEntity<Void> deleteMega(@PathVariable Long id) {
        log.debug("REST request to delete Mega : {}", id);
        megaService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
