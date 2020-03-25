package guru.springframework.msscbrewery.web.mappers;

import guru.springframework.msscbrewery.domain.Beer;
import guru.springframework.msscbrewery.web.model.BeerDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

/**
 * Created by Julius Oduro on 24/03/2020.
 */
@Mapper(uses = DateMapper.class)
public interface BeerMapper {

    //@Mapping(target = "lastUpdatedDate", source = "")
    BeerDto beerToBeerDto(Beer beer);

    Beer beerDtoToBeer(BeerDto beerDto);

    Beer updateBeerFromDto(BeerDto beerDto, @MappingTarget Beer beer);
}
