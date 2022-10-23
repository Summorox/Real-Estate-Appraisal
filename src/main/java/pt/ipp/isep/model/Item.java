package pt.ipp.isep.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String description;

    private Double appreciationPercentage;

    private Integer groupId;

    private String groupName;

    public Item(Integer id, String description, Double appreciationPercentage, Integer groupId, String groupName) {
        this.id = id;
        this.description = description;
        this.appreciationPercentage = appreciationPercentage;
        this.groupId = groupId;
        this.groupName = groupName;
    }
}
