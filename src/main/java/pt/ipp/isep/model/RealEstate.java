package pt.ipp.isep.model;


import java.util.List;

public class RealEstate {

    private String id;

    private RealEstateType realStateType;

    private Condition condition;

    private Double dependentGrossArea;

    private Typology typology;

    private Integer constructionYear;

    private Integer parkingSlot;

    private Integer numberBathrooms;

    private String address;

    private PostalCode postalCode;

    private String clientValue;

    private List<Extra> extraList;

}
