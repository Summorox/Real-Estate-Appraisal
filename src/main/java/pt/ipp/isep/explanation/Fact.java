package pt.ipp.isep.explanation;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Fact {

    private String description;
    private double value;
}
