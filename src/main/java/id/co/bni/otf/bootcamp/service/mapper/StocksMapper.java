package id.co.bni.otf.bootcamp.service.mapper;

import id.co.bni.otf.bootcamp.entity.Stocks;
import id.co.bni.otf.bootcamp.service.dto.StocksDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface StocksMapper {

    StocksDTO entityToDTO(Stocks stocks);

    @InheritInverseConfiguration
    Stocks dtoToEntity(StocksDTO stocksDTO);
}
