package pt.ipp.isep.model;

public enum BussinessQuality {

    BAD(1, "Bad"),
    FAIR(2, "Fair"),
    GOOD(3, "Good");

    BussinessQuality(Integer id, String description) {
        this.id = id;
        this.description = description;
    }

    private Integer id;

    private String description;
}
