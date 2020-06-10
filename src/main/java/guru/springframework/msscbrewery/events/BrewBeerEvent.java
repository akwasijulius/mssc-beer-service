package guru.springframework.msscbrewery.events;

import java.util.UUID;

/**
 * Created by Julius Oduro on 07/04/2020.
 */
//@Data
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
public class BrewBeerEvent extends BeerEvent {
    public BrewBeerEvent() {
    }

    public BrewBeerEvent(UUID id, String upc, Integer quantityToBrew) {
        super(id, upc, quantityToBrew);
    }
}
