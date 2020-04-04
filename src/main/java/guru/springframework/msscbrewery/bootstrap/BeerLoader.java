package guru.springframework.msscbrewery.bootstrap;

import guru.springframework.msscbrewery.domain.Beer;
import guru.springframework.msscbrewery.repository.BeerRepository;
import guru.springframework.msscbrewery.web.model.BeerStyleEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Created by Julius Oduro on 16/03/2020.
 */
@Slf4j
@Component
public class BeerLoader implements CommandLineRunner {

    public static final String BEER_1_UPC = "0631234200036";
    public static final String BEER_2_UPC = "0631234300019";
    public static final String BEER_3_UPC = "0083783375213";

    private final BeerRepository beerRepository;

    public BeerLoader(BeerRepository beerRepository){
        this.beerRepository = beerRepository;
    }

    @Override
    public void run(String... args) {
        if (beerRepository.count() == 0) {
            loadBeerObjects();
        }
    }

    private void loadBeerObjects() {
        log.info("Bootstrapping Beer Objects into the repository ...");

        beerRepository.save(Beer.builder()
                .beerName("Club Beer")
                .beerStyle(BeerStyleEnum.ALE)
                .price(new BigDecimal("2.99"))
                .minOnHand(12)
                .quantityToBrew(200)
                .upc(BEER_1_UPC)
                .build());

        beerRepository.save(Beer.builder()
                .beerName("Star Beer")
                .beerStyle(BeerStyleEnum.LAGER)
                .price(new BigDecimal("2.49"))
                .minOnHand(10)
                .quantityToBrew(150)
                .upc(BEER_2_UPC)
                .build());

        beerRepository.save(Beer.builder()
                .beerName("Guinness")
                .beerStyle(BeerStyleEnum.STOUT)
                .price(new BigDecimal("2.79"))
                .minOnHand(20)
                .quantityToBrew(250)
                .upc(BEER_3_UPC)
                .build());

        log.info("Bootstrapping Beer Objects into the repository - DONE");
    }
}
