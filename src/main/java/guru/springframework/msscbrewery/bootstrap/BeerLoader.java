package guru.springframework.msscbrewery.bootstrap;

import guru.springframework.msscbrewery.domain.Beer;
import guru.springframework.msscbrewery.repository.BeerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Created by Julius Oduro on 16/03/2020.
 */
@Component
public class BeerLoader implements CommandLineRunner {
    private final BeerRepository beerRepository;

    public BeerLoader(BeerRepository beerRepository){
        this.beerRepository = beerRepository;
    }

    @Override
    public void run(String... args) {
        loadBeerObjects();
    }

    private void loadBeerObjects() {
        if (beerRepository.count() == 0){
           beerRepository.save(Beer.builder()
                    .beerName("Club Beer")
                    .beerStyle("ALE")
                    .price(new BigDecimal("2.99"))
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .upc(31200001L)
                    .build());

           beerRepository.save(Beer.builder()
                    .beerName("Star Beer")
                    .beerStyle("PALE_ALE")
                    .price(new BigDecimal("2.49"))
                    .minOnHand(10)
                    .quantityToBrew(150)
                    .upc(31200002L)
                    .build());

           //System.out.println("beer count = " + beerRepository.count());
        }
    }
}
