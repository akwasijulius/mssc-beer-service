package guru.springframework.msscbrewery.services;

import guru.springframework.msscbrewery.domain.Beer;
import guru.springframework.msscbrewery.repository.BeerRepository;
import guru.springframework.msscbrewery.web.mappers.BeerMapper;
import guru.springframework.msscbrewery.web.model.BeerDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by jt on 2019-04-20.
 */
@Slf4j
@Service
public class BeerServiceImpl implements BeerService {

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    BeerMapper mapper;

    @Override
    public BeerDto getBeerById(UUID beerId) {
        Optional<Beer> beer = beerRepository.findById(beerId);
        return beer.map(value -> mapper.beerToBeerDto(value)).orElseThrow(RuntimeException::new);
    }

    @Override
    public BeerDto saveBeer(BeerDto beerDto) {
        Beer saveBeer = beerRepository.save(mapper.beerDtoToBeer(beerDto));
        return mapper.beerToBeerDto(saveBeer);
    }

    @Override
    public void updateBeer(UUID beerId, BeerDto beerDto) {
        Optional<Beer> optionalBeer = beerRepository.findById(beerId);
        if (optionalBeer.isPresent()){
            Beer beer = optionalBeer.get();
            beerRepository.save(mapper.updateBeerFromDto(beerDto, beer));
        }else
            throw new RuntimeException("Beer with Id" + beerId + " not found");
    }

    @Override
    public void deleteBeer(UUID beerId) {
        beerRepository.deleteById(beerId);
    }
}
