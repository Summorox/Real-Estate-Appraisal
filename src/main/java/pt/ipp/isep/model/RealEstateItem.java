package pt.ipp.isep.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RealEstateItem {

    private Integer id;

    private Item item;

}
