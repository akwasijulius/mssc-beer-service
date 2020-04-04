package guru.springframework.msscbrewery.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.msscbrewery.bootstrap.BeerLoader;
import guru.springframework.msscbrewery.services.BeerService;
import guru.springframework.msscbrewery.web.model.BeerDto;
import guru.springframework.msscbrewery.web.model.BeerStyleEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;




//@ExtendWith(SpringExtension.class) - no longer needed because its part of the @WebMvcTest
@WebMvcTest(BeerController.class)
public class BeerControllerTest {

    public static final String API_V1_BEER = "/api/v1/beer/";

    @MockBean
    BeerService beerService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    BeerDto validBeerDto;

    @BeforeEach
    public void setUp(){
        System.out.println("Setting up data ...");
        validBeerDto = BeerDto.builder().id(UUID.randomUUID())
                .beerName("Test Beer")
                .beerStyle(BeerStyleEnum.LAGER.name())
                .upc(BeerLoader.BEER_1_UPC)
                .build();
    }

    //@Disabled
    @Test
    public void getBeer() throws Exception {
        given(beerService.getBeerById(any(UUID.class), anyBoolean())).willReturn(validBeerDto);

        mockMvc.perform(get(API_V1_BEER + UUID.randomUUID().toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(validBeerDto.getId().toString())))
                .andExpect(jsonPath("$.beerName", is("Test Beer")));
    }

    @Test
    public void createBeer() throws Exception {
        BeerDto beerDto = validBeerDto;
        beerDto.setId(null);
        BeerDto savedDto = BeerDto.builder().id(UUID.randomUUID()).beerName("Test Beer 2").build();
        String beerDtoJson = objectMapper.writeValueAsString(beerDto);

        given(beerService.saveBeer(any())).willReturn(savedDto);

        mockMvc.perform(post(API_V1_BEER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(beerDtoJson))
                .andExpect(status().isCreated());
    }


    @Test
    public void updateBeer() throws Exception {
        BeerDto beerDto = validBeerDto;
        beerDto.setBeerName("Updated Beer Name");
        String beerDtoJson = objectMapper.writeValueAsString(beerDto);

        mockMvc.perform(put(API_V1_BEER + beerDto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(beerDtoJson))
                .andExpect(status().isOk());

        //then(beerService).should().updateBeer(any(), any());
    }

    @Test
    public void deleteBeer() {
    }
}