package id.co.bni.otf.bootcamp.service.mapper;

import id.co.bni.otf.bootcamp.entity.ClosingPrice;
import id.co.bni.otf.bootcamp.service.dto.ClosingPriceDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", uses = {StocksMapper.class})
public interface ClosingPriceMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "stocks", source = "stocksDTO")
    @Mapping(target = "date", source = "date")
    @Mapping(target = "price", source = "price")
    ClosingPrice dtoToEntity(ClosingPriceDTO closingPriceDTO);

    @InheritInverseConfiguration
    ClosingPriceDTO entityToDTO(ClosingPrice closingPrice);
}
