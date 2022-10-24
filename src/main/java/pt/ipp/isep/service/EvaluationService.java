package pt.ipp.isep.service;

import lombok.AllArgsConstructor;
import org.kie.api.KieServices;
import org.kie.api.runtime.ClassObjectFilter;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pt.ipp.isep.explanation.How;
import pt.ipp.isep.model.*;
import pt.ipp.isep.repository.ItemRepository;
import pt.ipp.isep.repository.PropertyRepository;
import pt.ipp.isep.sample.Property;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@Service
public class EvaluationService {

    private PropertyRepository propertyRepository;
    private ItemRepository itemRepository;
    private final KieContainer kieContainer = KieServices.Factory.get().getKieClasspathContainer();


    public Evaluation create(RealEstate realEstate) {

        KieSession kieSession = kieContainer.newKieSession("rulesSession");

        //First try to get properties by the typology and postal code
        //If not found, second time try to get by just prefix postal code
        List<Property> properties = propertyRepository.findAllbyTypologyAndPostalCode(realEstate.getTypology(), realEstate.getPostalCode())
                .orElseGet(() -> propertyRepository.findAllbyTypologyAndPostalCodePrefixCode(realEstate.getTypology(), realEstate.getPostalCode().getPrefixCode())
                        .orElseThrow(() -> new ApiException("Not found a base value for this typology and postal code", HttpStatus.NOT_FOUND)));

        //Calculate average price to use as base price for evaluation
        long evaluationBasePrice = Property.calculateAveragePrice(properties);

        //insert property to be evaluated
        kieSession.insert(realEstate);

        //insert evaluation
        kieSession.insert(Evaluation.builder().
                realEstate(realEstate).
                appraiseValue(evaluationBasePrice)
                .baseValue(evaluationBasePrice));

        kieSession.setGlobal("evaluationService", this);
        kieSession.fireAllRules();

        Collection<Evaluation> evaluationReturned = (Collection<Evaluation>) kieSession.getObjects(new ClassObjectFilter(Evaluation.class));

        // Dispose session
        kieSession.dispose();

        Evaluation finalEvaluation = evaluationReturned.iterator().next();
        finalEvaluation.setBussinessQuality(calculateBusinessQuality(finalEvaluation));
        How.addEvaluation(realEstate.getId(),finalEvaluation);
        System.out.println(How.getExplanationById(realEstate.getId()));

        return finalEvaluation;
    }

    public BussinessQuality calculateBusinessQuality(Evaluation evaluation) {
        long clientValue = evaluation.getRealEstate().getClientValue();
        float result = 0;
        result = ((clientValue - evaluation.getAppraiseValue()) * 100) / clientValue;
        if (result <= 5 && result >= -5) {
            return BussinessQuality.FAIR;
        }
        if(result < -5){
            return BussinessQuality.GOOD;
        }
        else{
            return BussinessQuality.BAD;
        }

    }

    public long appraiseConstructionYear(int constructionYear, long basePrice){
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int difference = currentYear-constructionYear;
        final double base= 1+(-0.005);
        double result = Math.pow(base, difference);
        long baseValueModifier=(Math.round(basePrice*result)-basePrice);
        return baseValueModifier;
    }

    public long appraiseParkingSlots(int parkingSlots, long basePrice){
        int exponent = parkingSlots;
        final double base= 1+(0.025);
        double result = Math.pow(base, exponent);
        long baseValueModifier=(Math.round(basePrice*result)-basePrice);
        return baseValueModifier;
    }

    public long appraiseBathrooms(int bathrooms, long basePrice){
        int exponent = bathrooms;
        final double base= 1+(0.025);
        double result = Math.pow(base, exponent);
        long baseValueModifier=(Math.round(basePrice*result)-basePrice);
        return baseValueModifier;
    }

    public long appraiseItem(RealEstateItem realEstateItem, long basePrice){
        List<Item> items = itemRepository.findAllByGroupId(realEstateItem.getItem().getGroupId());
        double calculatedValue = basePrice;
        if(items.size()==1){
            final double percentage = items.get(0).getAppreciationPercentage();
            final double multiplier= 1+(percentage);
            calculatedValue = calculatedValue*multiplier;
        }
        else if(items.size()>1){
            for(Item item: items){
                if(item.getId() == realEstateItem.getItem().getId()){
                    final double percentage = item.getAppreciationPercentage();
                    final double multiplier= 1+(percentage);
                    calculatedValue = calculatedValue*multiplier;
                }
            }
        }
        long baseValueModifier = Math.round(calculatedValue)-basePrice;
        return baseValueModifier;

    }

}
