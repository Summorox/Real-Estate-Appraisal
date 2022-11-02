package pt.ipp.isep.repository;

import lombok.Getter;
import pt.ipp.isep.model.Evaluation;

import java.util.HashMap;

public class EvaluationRepository {

    private static HashMap<String, Evaluation> evaluations = new HashMap<>();


    public Evaluation getByRealEstateId( String realEstateId) {
        return evaluations.get(realEstateId);
    }

    public static void addEvaluation(String realEstateId, Evaluation evaluation){

        if( evaluations.containsKey(realEstateId)){
            return;
        }
        evaluations.put(realEstateId,evaluation);
    }

    public static void resetEvaluations() {
        evaluations.clear();
    }

    public static HashMap<String, Evaluation> getEvaluations() {
        return evaluations;
    }
}
