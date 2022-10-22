package pt.ipp.isep.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PostalCode {

    @Id
    private Integer id;

    private String prefixCode;

    private String suffixCode;

}
