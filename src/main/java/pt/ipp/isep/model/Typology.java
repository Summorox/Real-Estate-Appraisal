package pt.ipp.isep.model;

public enum Typology {

    T0("studio"),
    T1("1 bedroom"),
    T2("2 bedrooms"),
    T3("3 bedrooms"),
    T4("4 bedrooms"),
    T5("5 bedrooms"),
    T6("6 bedrooms");

    Typology(String description) {
        this.description = description;
    }

    private String description;
}
