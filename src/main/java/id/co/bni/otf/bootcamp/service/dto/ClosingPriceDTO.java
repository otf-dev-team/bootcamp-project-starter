package id.co.bni.otf.bootcamp.service.dto;

import id.co.bni.otf.bootcamp.validation.contract.OnCreate;
import id.co.bni.otf.bootcamp.validation.contract.OnUpdate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ClosingPriceDTO implements Serializable {
    private Long id;
    private StocksDTO stocksDTO;
    private LocalDate date;
    private BigDecimal price;

    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    public StocksDTO getStocksDTO() {
        return stocksDTO;
    }

    public void setStocksDTO(StocksDTO stocksDTO) {
        this.stocksDTO = stocksDTO;
    }

    @NotNull
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @NotNull
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
