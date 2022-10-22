package pt.ipp.isep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.ipp.isep.model.PostalCode;
import pt.ipp.isep.model.Typology;
import pt.ipp.isep.sample.Property;

import java.util.List;
import java.util.Optional;

public interface PropertyRepository extends JpaRepository<Property, Long> {
    Optional<List<Property>> findAllbyTypologyAndPostalCodePrefixCode(Typology typology, String prefixCode);

    Optional<List<Property>> findAllbyTypologyAndPostalCode(Typology typology, PostalCode postalCode);
}
