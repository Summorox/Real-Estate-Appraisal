package pt.ipp.isep.repository;

import org.springframework.data.repository.CrudRepository;
import pt.ipp.isep.model.Typology;
import pt.ipp.isep.sample.Property;

import java.util.List;

public interface PropertyRepository extends CrudRepository<Property, Long> {
    List<Property> findAllByTypologyAndPostalCodePrefixCode(Typology typology, String prefixCode);

    List<Property> findAllByTypologyAndPostalCodePrefixCodeAndPostalCodeSuffixCode(Typology typology,String prefixCode,String suffixCode);
}
