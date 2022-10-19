package pt.ipp.isep.model;

public enum RealEstateType {

    APARTMENT(1, "Apartment"),
    HOUSE(2, "house");

    RealEstateType(Integer id, String description) {
        this.id = id;
        this.description = description;
    }

    private Integer id;

    private String description;
}
