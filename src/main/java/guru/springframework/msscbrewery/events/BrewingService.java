package guru.springframework.msscbrewery.events;

import guru.sfg.brewery.model.events.NewInventoryEvent;
import guru.springframework.msscbrewery.config.JMSConfig;
import guru.springframework.msscbrewery.domain.Beer;
import guru.springframework.msscbrewery.repository.BeerRepository;
import guru.springframework.msscbrewery.services.inventory.BeerInventoryService;
import guru.springframework.msscbrewery.web.mappers.BeerMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by Julius Oduro on 07/04/2020.
 */
@Slf4j
@Service
public class BrewingService {
    private final BeerRepository beerRepository;
    private final BeerInventoryService inventoryService;
    private JmsTemplate jmsTemplate;
    private BeerMapper mapper;


    public BrewingService(BeerRepository beerRepository, BeerInventoryService inventoryService, JmsTemplate jmsTemplate, BeerMapper mapper) {
        this.beerRepository = beerRepository;
        this.inventoryService = inventoryService;
        this.jmsTemplate = jmsTemplate;
        this.mapper = mapper;
    }

    //@Scheduled(fixedRate = 30000) // every 5 seconds
    public void checkInventory(){
        Iterable<Beer> beers = beerRepository.findAll();
        beers.forEach(beer -> {
            Integer onhandInventory = inventoryService.getOnhandInventory(beer.getUpc());
            log.info("Min on hand for {} : {}", beer.getBeerName(), beer.getMinOnHand());
            log.info("inventory for {} : {}", beer.getBeerName(), onhandInventory);

            if(beer.getMinOnHand() >= onhandInventory){
                log.info("Sending message to {} to brew more beer for {} ...", JMSConfig.BREWING_REQUEST_QUEUE, beer.getBeerName());
                // BrewBeerEvent.builder()
                NewInventoryEvent inventoryEvent = NewInventoryEvent.builder()
                        .id(beer.getId())
                        .upc(beer.getUpc())
                        .quantityToBrew(beer.getQuantityToBrew())
                        .build();
                jmsTemplate.convertAndSend(JMSConfig.BREWING_REQUEST_QUEUE, inventoryEvent);
                log.info("Sent.");
            }

            //update minOhHand
        });
    }
}
