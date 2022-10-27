package pt.ipp.isep.explanation;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class Fact {

    private String description;
    private double value;


}
