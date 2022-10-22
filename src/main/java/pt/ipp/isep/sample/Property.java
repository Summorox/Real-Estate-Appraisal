package pt.ipp.isep.sample;


import lombok.Getter;
import pt.ipp.isep.model.PostalCode;
import pt.ipp.isep.model.Typology;

import javax.persistence.*;

@Getter
@Entity
public class Property {

    @Id
    private Long id;

    private Double price;

    private Typology typology;

    @OneToOne
    private PostalCode postalCode;

}
