package guru.sfg.brewery.model.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by Julius Oduro on 07/04/2020.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewInventoryEvent implements Serializable {

    private static final long serialVersionUID = -7077891922881552007L;

    private UUID id;
    private String upc;
    private Integer quantityToBrew;

}
