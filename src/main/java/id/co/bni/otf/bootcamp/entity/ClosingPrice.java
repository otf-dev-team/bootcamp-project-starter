package id.co.bni.otf.bootcamp.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "closing_price", schema = "public")
public class ClosingPrice implements Serializable {
    private Long id;
    private Stocks stocks;
    private LocalDate date;
    private BigDecimal price;

    @Id
    @GeneratedValue(generator = "closing_price_seq")
    @SequenceGenerator(name = "closing_price_seq", sequenceName = "closing_price_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    @ManyToOne
    @JoinColumn(name = "stocks_id", referencedColumnName = "id")
    public Stocks getStocks() {
        return stocks;
    }

    public void setStocks(Stocks stocks) {
        this.stocks = stocks;
    }

    @Basic
    @NotNull
    @Column(name = "date")
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Basic
    @NotNull
    @Column(name = "price")
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
