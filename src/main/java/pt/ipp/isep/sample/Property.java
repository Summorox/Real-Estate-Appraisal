package pt.ipp.isep.sample;


import lombok.Builder;
import lombok.Getter;
import pt.ipp.isep.model.PostalCode;
import pt.ipp.isep.model.Typology;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
@Builder
public class Property {

    @Id
    private Long id;

    private Double price;

    private Typology typology;

    @OneToOne
    private PostalCode postalCode;

    public static long calculateAveragePrice(List<Property> properties) {
        double totalValue=0;
        for(Property property: properties){
            totalValue = totalValue+property.getPrice();
        }
        long averagePrice= Math.round(totalValue/properties.size());
        return averagePrice;
    }


}
