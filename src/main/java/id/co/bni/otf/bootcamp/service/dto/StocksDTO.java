package id.co.bni.otf.bootcamp.service.dto;

import id.co.bni.otf.bootcamp.validation.contract.OnCreate;
import id.co.bni.otf.bootcamp.validation.contract.OnUpdate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class StocksDTO implements Serializable {
    private Long id;
    private String code;
    private String name;
    private Boolean active;

    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotBlank
    @Size(max = 4)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @NotBlank
    @Size(max = 255)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
