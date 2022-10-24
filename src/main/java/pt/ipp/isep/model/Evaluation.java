package pt.ipp.isep.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pt.ipp.isep.repository.ItemRepository;
import pt.ipp.isep.service.ItemService;

import javax.persistence.Id;
import java.util.Calendar;
import java.util.List;

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
