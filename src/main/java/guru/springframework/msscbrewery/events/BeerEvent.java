package guru.springframework.msscbrewery.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by Julius Oduro on 07/04/2020.
 */
@Data
//@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BeerEvent implements Serializable {
    private static final long serialVersionUID = -4116448811542054146L;

    private UUID id;
    private String upc;
    private Integer quantityToBrew;

}
