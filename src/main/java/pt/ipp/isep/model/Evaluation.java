package pt.ipp.isep.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Evaluation {

    private Integer id;

    private RealEstate realEstate;

    private Double appraiseValue;

    private BussinessQuality bussinessQuality;

}
