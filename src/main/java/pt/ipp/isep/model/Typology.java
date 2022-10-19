package pt.ipp.isep.model;

public enum Typology {

    T0(0,"studio"),
    T1(1,"1 bedroom"),
    T2(2,"2 bedrooms"),
    T3(3,"3 bedrooms"),
    T4(4,"4 bedrooms"),
    T5(5,"5 bedrooms"),
    T6(6,"6 bedrooms");

    Typology(Integer id, String description) {
        this.id = id;
        this.description = description;
    }

    private Integer id;
    private String description;

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
