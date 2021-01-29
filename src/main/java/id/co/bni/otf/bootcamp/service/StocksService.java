package id.co.bni.otf.bootcamp.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import id.co.bni.otf.bootcamp.entity.QStocks;
import id.co.bni.otf.bootcamp.entity.Stocks;
import id.co.bni.otf.bootcamp.repository.StocksRepository;
import id.co.bni.otf.bootcamp.service.dto.StocksDTO;
import id.co.bni.otf.bootcamp.service.mapper.StocksMapper;
import id.co.bni.otf.bootcamp.validation.contract.OnCreate;
import id.co.bni.otf.bootcamp.validation.contract.OnUpdate;
import id.co.bni.otf.bootcamp.web.exception.DataNotFoundException;
import id.co.bni.otf.bootcamp.web.exception.DataNotUniqueException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QSort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * To handle stocks data
 *
 * @author efriandika
 */
@Service
@Validated
@Transactional
public class StocksService {

    private static final Logger LOG = LoggerFactory.getLogger(StocksService.class);

    private final StocksRepository stocksRepository;
    private final StocksMapper stocksMapper;

    public StocksService(StocksRepository stocksRepository, StocksMapper stocksMapper) {
        this.stocksRepository = stocksRepository;
        this.stocksMapper = stocksMapper;
    }

    /**
     * Creating a Stocks
     *
     * @param StocksDTO Stocks data
     * @return Stocks data
     */
    @Validated(OnCreate.class)
    public StocksDTO create(@Valid StocksDTO StocksDTO) {
        LOG.info("Creating a new Stocks with name = {}", StocksDTO.getName());

        if (stocksRepository.exists(QStocks.stocks.code.eq(StocksDTO.getCode()))) {
            throw new DataNotUniqueException("Stocks with ID=" + StocksDTO.getCode() + " is already in use");
        }

        Stocks Stocks = stocksRepository.save(stocksMapper.dtoToEntity(StocksDTO));
        return stocksMapper.entityToDTO(Stocks);
    }

    /**
     * Updating a stocks
     *
     * @param stocksDTO Stocks data
     * @return Stocks data
     */
    @Validated(OnUpdate.class)
    public StocksDTO update(@Valid StocksDTO stocksDTO) {
        LOG.info("Updating a stocks with id = {}", stocksDTO.getId());

        Optional<Stocks> stocks = stocksRepository.findById(stocksDTO.getId());

        if (stocks.isPresent()) {
            Stocks updatedStocks = stocks.get();

            updatedStocks.setCode(stocksDTO.getCode());
            updatedStocks.setName(stocksDTO.getName());
            updatedStocks.setActive(stocksDTO.getActive());

            return stocksMapper.entityToDTO(stocksRepository.save(updatedStocks));
        } else {
            LOG.error("Cannot find the stocks with id = {}", stocksDTO.getId());
            throw new DataNotFoundException("Stocks with ID=" + stocksDTO.getId() + " is not found");
        }
    }

    /**
     * Finding one stocks by its ID
     *
     * @param id Stocks ID
     * @return Stocks Data
     */
    @Transactional(readOnly = true)
    public StocksDTO findOne(Long id) {
        LOG.info("Finding a stocks with id = {}", id);

        Optional<Stocks> stocks = stocksRepository.findById(id);

        if (stocks.isPresent()) {
            return stocksMapper.entityToDTO(stocks.get());
        } else {
            LOG.error("Cannot find the stocks with id = {}", id);
            throw new DataNotFoundException("Stocks is not found");
        }
    }

    /**
     * Finding all stocks in pageable format
     *
     * @return Stocks Data
     */
    @Transactional(readOnly = true)
    public Page<StocksDTO> findAll(Pageable pageable, String code, String name, Boolean active) {
        LOG.debug("Finding all stocks in pageable format");

        BooleanBuilder predicate = commonFilter(code, name, active);

        return stocksRepository.findAll(predicate, pageable).map(stocksMapper::entityToDTO);
    }

    /**
     * Finding all stocks in pageable format
     *
     * @return Stocks Data
     */
    @Transactional(readOnly = true)
    public List<StocksDTO> findAll(String code, String name, Boolean active) {
        LOG.debug("Finding all stocks in list format");

        BooleanBuilder predicate = commonFilter(code, name, active);

        // Default Sorting
        QStocks qStocks = QStocks.stocks;
        OrderSpecifier<String> nameOrder = qStocks.name.asc();

        QSort sortingCriteria = new QSort(nameOrder);

        Iterable<Stocks> countries = stocksRepository.findAll(predicate, sortingCriteria);

        return StreamSupport.stream(countries.spliterator(), false)
                .map(stocksMapper::entityToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Deleting a Stocks data
     *
     * @param id Stocks ID that is going to be deleted
     */
    public void delete(Long id) {
        LOG.info("Deleting a stocks with id = {}", id);

        Optional<Stocks> stocks = stocksRepository.findById(id);

        if (stocks.isPresent()) {
            stocksRepository.deleteById(stocks.get().getId());
        } else {
            LOG.error("Deleting stocks with id = {} is failed", id);
            throw new DataNotFoundException("Stocks with ID=" + id + " is not found");
        }
    }

    /**
     * Building the query dsl filtering
     *
     * @param code   Stocks code, this will be filtered by exact match (eq)
     * @param name   Stocks name, this will be filtered by using "contains"
     * @param active Data state, active or not active
     * @return Query DSL Predicate
     */
    private BooleanBuilder commonFilter(String code, String name, Boolean active) {
        QStocks qStocks = QStocks.stocks;
        BooleanBuilder predicate = new BooleanBuilder();

        // Filtering by Query DSL
        if (code != null && !code.isEmpty()) {
            predicate.and(qStocks.code.eq(code));
        }

        if (name != null && !name.isEmpty()) {
            predicate.and(qStocks.name.contains(name));
        }

        if (active != null) {
            predicate.and(qStocks.active.eq(active));
        }

        return predicate;
    }
}
