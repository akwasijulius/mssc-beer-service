package guru.springframework.msscbrewery.services;

import guru.springframework.msscbrewery.domain.Beer;
import guru.springframework.msscbrewery.repository.BeerRepository;
import guru.springframework.msscbrewery.web.mappers.BeerMapper;
import guru.springframework.msscbrewery.web.model.BeerDto;
import guru.springframework.msscbrewery.web.model.BeerPagedList;
import guru.springframework.msscbrewery.web.model.BeerStyleEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @Cacheable(cacheNames = "beerCache", key = "#beerId", condition = "#showInventoryOnHand == false")
    @Override
    public BeerDto getBeerById(UUID beerId, boolean showInventoryOnHand) {
        log.info("getBeerById() called with beerId {} and showInventoryOnHand {}", beerId, showInventoryOnHand);
        Optional<Beer> beer = beerRepository.findById(beerId);
        return beer.map(value -> {
            if (showInventoryOnHand) {
                return mapper.beerToBeerDtoWithInventory(value);
            }else {
                log.info("mapper.beerToBeerDto(value) called");
                return mapper.beerToBeerDto(value);
            }}).orElseThrow(RuntimeException::new);
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

    @Cacheable(cacheNames = "beerListCache", condition = "#showInventoryOnHand == false")
    @Override
    public BeerPagedList getBeers(String beerName, BeerStyleEnum beerStyleEnum, Integer pageNumber, Integer pageSize, boolean showInventoryOnHand) {
        Page<Beer> allBeers;

        if (beerStyleEnum == null && beerName == null) {
            allBeers = beerRepository.findAll(PageRequest.of(pageNumber, pageSize));
        }
        else if (beerStyleEnum != null && Strings.isNotBlank(beerName ) ) {
            allBeers = beerRepository.findAllByBeerNameAndBeerStyle(beerName, beerStyleEnum, PageRequest.of(pageNumber, pageSize));
        }
        else if (Strings.isNotBlank(beerName )){
            allBeers = beerRepository.findAllByBeerName(beerName, PageRequest.of(pageNumber, pageSize));
        }
        else {
            allBeers = beerRepository.findAllByBeerStyle(beerStyleEnum, PageRequest.of(pageNumber, pageSize));
        }

        List<BeerDto> beerDtos;
        if (showInventoryOnHand){
            beerDtos = allBeers.getContent().stream().map(beer -> mapper.beerToBeerDtoWithInventory(beer)).collect(Collectors.toList());
        }else {
            beerDtos = allBeers.getContent().stream().map(beer -> mapper.beerToBeerDto(beer)).collect(Collectors.toList());
        }
        return new BeerPagedList(beerDtos, allBeers.getPageable(), allBeers.getTotalElements());
    }
}
