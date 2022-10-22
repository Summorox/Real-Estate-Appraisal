package pt.ipp.isep.model;

import lombok.Getter;

@Getter
public enum Condition {

    BAD(1, "Bad"),
    FAIR(2, "Fair"),
    GOOD(3, "Good"),
    LUXURIOUS(4, "Luxurious");

    Condition(Integer id, String description) {
        this.id = id;
        this.description = description;
    }

    private Integer id;

    private String description;

}
