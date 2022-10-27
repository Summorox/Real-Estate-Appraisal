package pt.ipp.isep.explanation;

import pt.ipp.isep.model.BussinessQuality;
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

    public static String getWhyNot(final String id, final String quality) {
        StringBuilder builder = new StringBuilder();
        Evaluation evaluation = evaluations.get(id);
        long clientValue = Math.round(evaluation.getAppraiseValue()*(1-evaluation.getPercQuality()));
        if(quality.equals(evaluation.getBussinessQuality().getDescription())){
            builder.append("This real estate deal is already rated as " + quality);
            return builder.toString();
        }
        builder.append("The real estate deal was rated as "+evaluation.getBussinessQuality().getDescription());
        builder.append("\n\n");

        builder.append("Because the real estate estimated value is " + evaluation.getPercQuality());
        if(evaluation.getPercQuality() >=0){
            builder.append(" above the client requested value "+clientValue);
        }
        else{
            builder.append(" below the client requested value "+clientValue);
        }

        builder.append("\n\n");

        builder.append("For it to be rated as  " + quality + " deal:");
        if(quality.equals(BussinessQuality.AWFUL.toString().toLowerCase())){
            builder.append("\nThe estimated value should be below ");
            builder.append(Math.round(evaluation.getAppraiseValue()*(1-0.5)));
        }
        if(quality.equals(BussinessQuality.BAD.toString().toLowerCase())){
            builder.append("\nThe estimated value should be between ");
            builder.append(Math.round(evaluation.getAppraiseValue()*(1-0.49)));
            builder.append(" and ");
            builder.append(Math.round(evaluation.getAppraiseValue()*(1-0.20)));
        }
        if(quality.equals(BussinessQuality.FAIR.toString().toLowerCase())){
            builder.append("\nThe estimated value should be between ");
            builder.append(Math.round(evaluation.getAppraiseValue()*(1-0.19)));
            builder.append(" and ");
            builder.append(Math.round(evaluation.getAppraiseValue()*(1+0.19)));
        }
        if(quality.equals(BussinessQuality.GOOD.toString().toLowerCase())){
            builder.append("\nThe estimated value should be between ");
            builder.append(Math.round(evaluation.getAppraiseValue()*(1+0.20)));
            builder.append(" and ");
            builder.append(Math.round(evaluation.getAppraiseValue()*(1+0.49)));
        }
        if(quality.equals(BussinessQuality.EXCELLENT.toString().toLowerCase())){
            builder.append("\nThe estimated value should be above ");
            builder.append(Math.round(evaluation.getAppraiseValue()*(1+0.50)));
        }
        return builder.toString();
    }

    public static void resetJustifications() {
        justifications.clear();
    }

    public static void resetEvaluations() {
        evaluations.clear();
    }

    public static HashMap<String, List<Justification>> getJustifications() {
        return justifications;
    }

    public static HashMap<String, Evaluation> getEvaluations() {
        return evaluations;
    }
}
