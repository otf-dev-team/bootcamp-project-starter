package id.co.bni.otf.bootcamp.web.controller.stocks;

import id.co.bni.otf.bootcamp.service.StocksService;
import id.co.bni.otf.bootcamp.service.dto.StocksDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Validated
@RequestMapping(value = "/stocks", produces = MediaType.APPLICATION_JSON_VALUE)
public class StocksController {

    private final StocksService stocksService;

    public StocksController(StocksService stocksService) {
        this.stocksService = stocksService;
    }

    @GetMapping("/")
    public ResponseEntity<Object> findAllPageable(
            Pageable pageable,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean active
    ) {
        return ResponseEntity.ok().body(stocksService.findAll(pageable, code, name, active));
    }

    @GetMapping("/lists")
    public ResponseEntity<Object> findAllList(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean active
    ) {
        return ResponseEntity.ok().body(stocksService.findAll(code, name, active));
    }

    @GetMapping("/{id}")
    public ResponseEntity <Object> findOne(@PathVariable Long id) {
        return ResponseEntity.ok().body(stocksService.findOne(id));
    }

    @PostMapping("/")
    public ResponseEntity <Object> create(@Valid @RequestBody StocksDTO stocksDTO) {
        return ResponseEntity.ok().body(stocksService.create(stocksDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity <Object> update(@PathVariable Long id, @Valid @RequestBody StocksDTO stocksDTO) {
        stocksDTO.setId(id);
        return ResponseEntity.ok().body(stocksService.update(stocksDTO));
    }

    @DeleteMapping("/{id}")
    public HttpStatus delete(@PathVariable Long id) {
        stocksService.delete(id);
        return HttpStatus.NO_CONTENT;
    }
}
