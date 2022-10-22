package pt.ipp.isep.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class PostalCode {

    private Integer id;

    private String prefixCode;

    private String suffixCode;

}
