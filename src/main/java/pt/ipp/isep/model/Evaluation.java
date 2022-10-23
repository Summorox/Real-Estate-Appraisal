package pt.ipp.isep.model;

import lombok.Builder;
import lombok.Getter;
import pt.ipp.isep.repository.ItemRepository;
import pt.ipp.isep.service.ItemService;

import javax.persistence.Id;
import java.util.Calendar;
import java.util.List;

@Getter
@Builder
public class Evaluation {

    private Integer id;

    private RealEstate realEstate;

    private long appraiseValue;

    private BussinessQuality bussinessQuality;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RealEstate getRealEstate() {
        return realEstate;
    }

    public void setRealEstate(RealEstate realEstate) {
        this.realEstate = realEstate;
    }

    public long getAppraiseValue() {
        return appraiseValue;
    }

    public void setAppraiseValue(long appraiseValue) {
        this.appraiseValue = appraiseValue;
    }

    public BussinessQuality getBussinessQuality() {
        return bussinessQuality;
    }

    public void setBussinessQuality(BussinessQuality bussinessQuality) {
        this.bussinessQuality = bussinessQuality;
    }
}
