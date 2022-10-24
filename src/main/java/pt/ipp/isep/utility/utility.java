package pt.ipp.isep.utility;

import org.drools.core.rule.consequence.KnowledgeHelper;

public class utility {
    public static void helper(final KnowledgeHelper drools, final String message){
        System.out.println(message);
        System.out.println("\nrule triggered: " + drools.getRule().getName());
    }
}
