package pt.ipp.isep.model;

import lombok.Getter;

@Getter
public enum BussinessQuality {

    AWFUL(1, "Awful"),
    BAD(2, "Bad"),
    FAIR(3, "Fair"),
    GOOD(4, "Good"),

    EXCELLENT(5, "Excellent");


    BussinessQuality(Integer id, String description) {
        this.id = id;
        this.description = description;
    }

    private Integer id;

    private String description;
}
