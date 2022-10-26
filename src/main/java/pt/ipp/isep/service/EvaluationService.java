package pt.ipp.isep.service;

import lombok.AllArgsConstructor;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pt.ipp.isep.model.ApiException;
import pt.ipp.isep.model.Evaluation;
import pt.ipp.isep.model.RealEstate;
import pt.ipp.isep.repository.PropertyRepository;
import pt.ipp.isep.sample.Property;

import java.util.List;

@AllArgsConstructor
@Service
public class EvaluationService {

    private PropertyRepository propertyRepository;

    public Evaluation create(RealEstate realEstate) {

        //First try to get properties by the typology and postal code
        //If not found, second time try to get by just prefix postal code
        List<Property> properties = propertyRepository.findAllByTypologyAndPostalCode(realEstate.getTypology(), realEstate.getPostalCode())
                .orElseGet(() -> propertyRepository.findAllByTypologyAndPostalCodePrefixCode(realEstate.getTypology(), realEstate.getPostalCode().getPrefixCode())
                        .orElseThrow(() -> new ApiException("Not found a base value for this typology and postal code", HttpStatus.NOT_FOUND)));

        //TODO Implement to call rules
        throw new NotYetImplementedException();
    }

}
