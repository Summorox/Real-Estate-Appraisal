package pt.ipp.isep.explanation;

import pt.ipp.isep.model.Evaluation;
import pt.ipp.isep.repository.EvaluationRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class How {

    private static HashMap<String, List<Justification>> justifications = new HashMap<>();

    private EvaluationRepository evaluationRepository = new EvaluationRepository();

    public static void addJustification(String realEstateId, Justification justification){

        List<Justification> justificationsForId = new ArrayList<>();

        if( justifications.containsKey(realEstateId)){
            justificationsForId.addAll(justifications.get(realEstateId));
        }

        justificationsForId.add(justification);
        justifications.put(realEstateId,justificationsForId);
    }



    public String getExplanationById(String realEstateId) {
        StringBuilder builder = new StringBuilder();
        List<Justification> factJustifications = justifications.get(realEstateId);
        Evaluation evaluation = evaluationRepository.getByRealEstateId(realEstateId);
        builder.append("The real estate number "+realEstateId+ " was estimated in " + evaluation.getAppraiseValue()
        + ",because based on the starting value " + evaluation.getBaseValue() + " we applied the following rules:" );

        if (factJustifications != null && !factJustifications.isEmpty() ) {
            for(Justification j : factJustifications) {
                builder.append("\n\n\n");
                builder.append("Because of the rule : "+ j.getRuleDesc());
                builder.append("\n");
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



    public static void resetJustifications() {
        justifications.clear();
    }



    public static HashMap<String, List<Justification>> getJustifications() {
        return justifications;
    }


}
