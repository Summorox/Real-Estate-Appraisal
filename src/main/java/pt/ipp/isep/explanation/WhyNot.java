package pt.ipp.isep.explanation;

import pt.ipp.isep.model.BussinessQuality;
import pt.ipp.isep.model.Evaluation;
import pt.ipp.isep.repository.EvaluationRepository;

public class WhyNot {

    private EvaluationRepository evaluationRepository = new EvaluationRepository();
    public String getWhyNot(final String id, final String quality) {
        StringBuilder builder = new StringBuilder();
        Evaluation evaluation = evaluationRepository.getByRealEstateId(id);
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

}
