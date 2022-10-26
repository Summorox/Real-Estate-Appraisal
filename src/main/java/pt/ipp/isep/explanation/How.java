package pt.ipp.isep.explanation;

import org.drools.core.util.Entry;
import pt.ipp.isep.model.Evaluation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class How {

    private static HashMap<String, List<Justification>> justifications = new HashMap<>();
    private static HashMap<String, Evaluation> evaluations = new HashMap<>();;


    public static void addJustification(String realEstateId, Justification justification){

        List<Justification> justificationsForId = new ArrayList<>();

        if( justifications.containsKey(realEstateId)){
            justificationsForId.addAll(justifications.get(realEstateId));
        }

        justificationsForId.add(justification);
        justifications.put(realEstateId,justificationsForId);
    }

    public static void addEvaluation(String realEstateId, Evaluation evaluation){

        if( evaluations.containsKey(realEstateId)){
            return;
        }
        evaluations.put(realEstateId,evaluation);
    }

    public static String getExplanationById(String realEstateId) {
        StringBuilder builder = new StringBuilder();
        List<Justification> factJustifications = justifications.get(realEstateId);
        Evaluation evaluation = evaluations.get(realEstateId);
        builder.append("The real estate number "+realEstateId+ "was estimated in " + evaluation.getAppraiseValue()
        + ",because based on the starting value " + evaluation.getBaseValue() + " we applied the following rules:" );

        if (factJustifications != null && !factJustifications.isEmpty() ) {
            for(Justification j : factJustifications) {
                builder.append("\n\n\n");
                builder.append("Because of the rule : "+ j.getRuleDesc());
                builder.append("\n\n");
                if(j.getFact().getValue()<0){
                    builder.append("The value decreased by :");
                }else{
                    builder.append("The value increased by :");
                }
                builder.append(" "+j.getFact().getValue());
                builder.append("\n");
                builder.append(j.getFact().getDescription());
            }
        }
        builder.append("\n\n");
        builder.append("Making the calculations, we estimated "+evaluation.getAppraiseValue());
        builder.append("\n");
        builder.append("According to the price determined by the client, we determine this deal as "
                +evaluation.getBussinessQuality().getDescription());

        return builder.toString();
    }

    public static String getWhyNot(String id, String quality) {
        return "";
    }
}
