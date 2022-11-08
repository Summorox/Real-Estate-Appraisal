package pt.ipp.isep.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Evaluation {

    private Integer id;

    private RealEstate realEstate;

    private long appraiseValue;

    private long baseValue;

    private long percQuality;

    private BussinessQuality bussinessQuality;

}
