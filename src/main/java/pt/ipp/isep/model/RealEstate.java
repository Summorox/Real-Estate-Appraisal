package pt.ipp.isep.model;


import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class RealEstate {

    private String id;

    private RealEstateType realStateType;

    private Double dependentGrossArea;

    private Typology typology;

    private Integer constructionYear;

    private Integer parkingSlot;

    private Integer numberBathrooms;

    private String address;

    private PostalCode postalCode;

    private String clientValue;

    private List<RealEstateItem> realEstateItemList;

}
