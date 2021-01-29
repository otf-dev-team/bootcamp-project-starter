package id.co.bni.otf.bootcamp.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "stocks")
public class Stocks implements Serializable {
    private Long id;
    private String code;
    private String name;
    private Boolean active;

    @Id
    @GeneratedValue(generator = "stocks_seq")
    @SequenceGenerator(name = "stocks_seq", sequenceName = "stocks_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @NotBlank
    @Size(max = 4)
    @Column(name = "code", nullable = false, unique = true, length = 4)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Basic
    @NotBlank
    @Size(max = 255)
    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @NotNull
    @Column(name = "active", nullable = false)
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Stocks)) return false;
        Stocks stocks = (Stocks) o;
        return Objects.equals(getId(), stocks.getId()) && Objects.equals(getCode(), stocks.getCode()) && Objects.equals(getName(), stocks.getName()) && Objects.equals(getActive(), stocks.getActive());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCode(), getName(), getActive());
    }
}
