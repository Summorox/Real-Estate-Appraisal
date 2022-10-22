package pt.ipp.isep.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Extra {

    private Integer id;

    private String description;

    private Double appreciationPercentage;

}
