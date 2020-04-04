package guru.springframework.msscbrewery.web.controller;

import guru.springframework.msscbrewery.services.BeerService;
import guru.springframework.msscbrewery.web.model.BeerDto;
import guru.springframework.msscbrewery.web.model.BeerPagedList;
import guru.springframework.msscbrewery.web.model.BeerStyleEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

/**
 * Created by jt on 2019-04-20.
 */
@Slf4j
@RequestMapping("/api/v1/beer")
@RestController
public class BeerController {

    private static final Integer DEFAULT_PAGE_NUMBER = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 25;
    private static final String host = "localhost";

    private final BeerService beerService;

    public BeerController(BeerService beerService) {
        this.beerService = beerService;
    }


    @GetMapping({"/"})
    public ResponseEntity<BeerPagedList> listBeers(@RequestParam(value = "beerName", required = false) String beerName,
                                                   @RequestParam(value = "beerStyleEnum", required = false) BeerStyleEnum beerStyleEnum,
                                                   @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                                   @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                   @RequestParam(value = "showInventoryOnHand", required = false) Boolean showInventoryOnHand){

        if(pageNumber == null || pageNumber < 0){
            pageNumber = DEFAULT_PAGE_NUMBER;
        }
        if (pageSize == null || pageSize < 1){
            pageSize = DEFAULT_PAGE_SIZE;
        }

        if (showInventoryOnHand == null){
            showInventoryOnHand = Boolean.FALSE;
        }

        BeerPagedList pagedList = beerService.getBeers(beerName, beerStyleEnum, pageNumber, pageSize, showInventoryOnHand);
        return new ResponseEntity<>(pagedList, HttpStatus.OK);

    }

    @GetMapping({"/{beerId}"})
    public ResponseEntity<BeerDto> getBeer(@PathVariable("beerId") UUID beerId,
                                           @RequestParam(value = "showInventoryOnHand", required = false) Boolean showInventoryOnHand){
        log.info("Calling GET Request method with {}", beerId);
        if (showInventoryOnHand == null){
            showInventoryOnHand = Boolean.FALSE;
        }
        return new ResponseEntity<>(beerService.getBeerById(beerId, showInventoryOnHand), HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<?> createBeer(@Valid @RequestBody BeerDto  beerDto){
        BeerDto saveBeerDto = beerService.saveBeer(beerDto);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", host + "/api/v1/beer/" + saveBeerDto.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }


    @PutMapping({"/{beerId}"})
    public ResponseEntity<?> updateBeer(@PathVariable("beerId") UUID beerId, @RequestBody BeerDto beerDto){
        beerService.updateBeer(beerId, beerDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping({"/{beerId}"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity deleteBeer(@PathVariable("beerId") UUID beerId){
        beerService.deleteBeer(beerId);
        return new ResponseEntity(HttpStatus.OK);
    }


}
