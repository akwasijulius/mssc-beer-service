package guru.springframework.msscbrewery.services.inventory;

import java.util.UUID;

/**
 * Created by Julius Oduro on 30/03/2020.
 */
public interface BeerInventoryService {

    Integer getOnhandInventory(UUID beerId);
}
