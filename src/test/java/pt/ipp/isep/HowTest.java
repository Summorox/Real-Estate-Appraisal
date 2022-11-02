package pt.ipp.isep;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.api.KieServices;
import static org.junit.jupiter.api.Assertions.*;
import org.kie.api.runtime.ClassObjectFilter;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import pt.ipp.isep.explanation.Fact;
import pt.ipp.isep.explanation.How;
import pt.ipp.isep.explanation.Justification;
import pt.ipp.isep.explanation.WhyNot;
import pt.ipp.isep.model.*;
import pt.ipp.isep.repository.EvaluationRepository;
import pt.ipp.isep.service.EvaluationService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class HowTest {

    private KieSession kieSession;
    private RealEstate testEstate;
    private Evaluation evaluation;

    private EvaluationRepository evaluationRepository = new EvaluationRepository();

    @BeforeEach
    public void init() {
        KieContainer kieContainer = KieServices.Factory.get().getKieClasspathContainer();

        kieSession = kieContainer.newKieSession("rulesSession");

        Item conditionItemBad = new Item(3,"Bad",-0.05,3,"Condition");
        Item conditionItemFair = new Item(4,"Fair",0.00,3,"Condition");
        Item conditionItemGood = new Item(5,"Good",0.05,3,"Condition");

        RealEstateItem item1 = RealEstateItem.builder()
                .id(1)
                .item(new Item(1,"Pool",0.07,1,"Pool"))
                .build();

        RealEstateItem item2 = RealEstateItem.builder()
                .id(2)
                .item(new Item(2,"Garden",0.05,2,"Garden"))
                .build();

        RealEstateItem item3 = RealEstateItem.builder()
                .id(2)
                .item(conditionItemBad)
                .build();

        ArrayList<Item> conditionItemList = new ArrayList<>();
        conditionItemList.add(conditionItemBad);
        conditionItemList.add(conditionItemFair);
        conditionItemList.add(conditionItemGood);

        ArrayList<RealEstateItem> list1 = new ArrayList<>();
        list1.add(item1);
        list1.add(item2);
        list1.add(item3);


        testEstate= RealEstate.
                builder().
                id("123").
                realEstateType(RealEstateType.APARTMENT)
                .dependentGrossArea(123.00)
                .typology(Typology.T1)
                .constructionYear(2000)
                .parkingSlot(2)
                .numberBathrooms(1)
                .address("Rua xx, Lordelo de Ouro")
                .postalCode(PostalCode.builder().
                        prefixCode("1450")
                        .suffixCode("479")
                        .build())
                .clientValue(15000)
                .realEstateItemList(list1)
                .build();

        evaluation = Evaluation.builder()
                .realEstate(testEstate)
                .appraiseValue(15000)
                .baseValue(15000)
                .build();

        EvaluationService service = mock(EvaluationService.class);
        when(service.appraiseConstructionYear(2000,15000)).thenReturn(-1462L);
        when(service.appraiseParkingSlots(2,15000)).thenReturn(709L);
        when(service.appraiseBathrooms(1,15000)).thenReturn(350L);
        when(service.appraiseItem(item1,15000L)).thenReturn(980L);
        when(service.appraiseItem(item2,15000L)).thenReturn(700L);
        when(service.appraiseItem(item3,15000L)).thenReturn(-700L);

        kieSession.setGlobal("evaluationService", service);
        kieSession.insert(testEstate);
        kieSession.insert(evaluation);
        kieSession.fireAllRules();

    }

    @Test
    public void testHow() {

        Justification expectedJustification1 = Justification.builder().
                fact(Fact.builder().
                        description("Because the property is 22 years old").
                        value(-1462.0)
                        .build())
                .ruleDesc("r1: Value of the property should depreciate per year")
                .build();

        Justification expectedJustification2 = Justification.builder().
                fact(Fact.builder().
                        description("Because the property has 2 parking slots").
                        value(709)
                        .build())
                .ruleDesc("r2: Value of the property should appreciate per parking spot")
                .build();

        Justification expectedJustification3 = Justification.builder().
                fact(Fact.builder().
                        description("Because the property has 1 bathrooms").
                        value(350)
                        .build())
                .ruleDesc("r3: Value of the property should appreciate per bathroom")
                .build();

        Justification expectedJustification4 = Justification.builder().
                fact(Fact.builder().
                        description("Because the property has the item: Pool").
                        value(980)
                        .build())
                .ruleDesc("r4: Value of the property should appreciate or depreciate accordingly per item that it has")
                .build();

        Justification expectedJustification5 = Justification.builder().
                fact(Fact.builder().
                        description("Because the property has the item: Garden").
                        value(700)
                        .build())
                .ruleDesc("r4: Value of the property should appreciate or depreciate accordingly per item that it has")
                .build();

        Justification expectedJustification6 = Justification.builder().
                fact(Fact.builder().
                        description("Because the property has the item: Bad").
                        value(-700)
                        .build())
                .ruleDesc("r4: Value of the property should appreciate or depreciate accordingly per item that it has")
                .build();

        List<Justification> expectedJustifications = new ArrayList<>();
        expectedJustifications.add(expectedJustification1);
        expectedJustifications.add(expectedJustification2);
        expectedJustifications.add(expectedJustification3);
        expectedJustifications.add(expectedJustification4);
        expectedJustifications.add(expectedJustification5);
        expectedJustifications.add(expectedJustification6);

        List<Justification> actualJustifications = How.getJustifications().get(evaluation.getRealEstate().getId());
        assertEquals(expectedJustifications.size(),actualJustifications.size());
        for(int i = 0;i<actualJustifications.size();i++){
            assertEquals(expectedJustifications.get(i).getRuleDesc(),actualJustifications.get(i).getRuleDesc());
            assertEquals(expectedJustifications.get(i).getFact().getDescription(),actualJustifications.get(i).getFact().getDescription());
            assertEquals(expectedJustifications.get(i).getFact().getValue(),actualJustifications.get(i).getFact().getValue());
        }

        Collection<Evaluation> evaluationReturned = (Collection<Evaluation>) kieSession.getObjects(new ClassObjectFilter(Evaluation.class));
        Evaluation finalEvaluation = evaluationReturned.iterator().next();
        finalEvaluation.setPercQuality(0);
        finalEvaluation.setBussinessQuality(BussinessQuality.FAIR);
        evaluationRepository.addEvaluation(testEstate.getId(),finalEvaluation);

        assertEquals(finalEvaluation, evaluationRepository.getByRealEstateId(testEstate.getId()));
        String expectedHow = "The real estate number 123 was estimated in 15577,because based on the starting value 15000 we applied the following rules:\n" +
                "\n" +
                "\n" +
                "Because of the rule : r1: Value of the property should depreciate per year\n" +
                "The value decreased by : -1462.0\n" +
                "Because the property is 22 years old\n" +
                "\n" +
                "\n" +
                "Because of the rule : r2: Value of the property should appreciate per parking spot\n" +
                "The value increased by : 709.0\n" +
                "Because the property has 2 parking slots\n" +
                "\n" +
                "\n" +
                "Because of the rule : r3: Value of the property should appreciate per bathroom\n" +
                "The value increased by : 350.0\n" +
                "Because the property has 1 bathrooms\n" +
                "\n" +
                "\n" +
                "Because of the rule : r4: Value of the property should appreciate or depreciate accordingly per item that it has\n" +
                "The value increased by : 980.0\n" +
                "Because the property has the item: Pool\n" +
                "\n" +
                "\n" +
                "Because of the rule : r4: Value of the property should appreciate or depreciate accordingly per item that it has\n" +
                "The value increased by : 700.0\n" +
                "Because the property has the item: Garden\n" +
                "\n" +
                "\n" +
                "Because of the rule : r4: Value of the property should appreciate or depreciate accordingly per item that it has\n" +
                "The value decreased by : -700.0\n" +
                "Because the property has the item: Bad\n" +
                "\n" +
                "Making the calculations, we estimated 15577\n" +
                "According to the price determined by the client, we determine this deal as Fair";
        String actualHow = new How().getExplanationById(testEstate.getId());
        assertEquals(expectedHow,actualHow);
        kieSession.dispose();

    }

    @Test
    public void testWhyNot() {
        Collection<Evaluation> evaluationReturned = (Collection<Evaluation>) kieSession.getObjects(new ClassObjectFilter(Evaluation.class));
        Evaluation finalEvaluation = evaluationReturned.iterator().next();
        finalEvaluation.setPercQuality(0);
        finalEvaluation.setBussinessQuality(BussinessQuality.FAIR);
        evaluationRepository.addEvaluation(testEstate.getId(),finalEvaluation);
        String expectedWhyNot = "The real estate deal was rated as Fair\n" +
                "\n" +
                "Because the real estate estimated value is 0 above the client requested value 15577\n" +
                "\n" +
                "For it to be rated as  bad deal:\n" +
                "The estimated value should be between 7944 and 12462";
        String actualWhyNot = new WhyNot().getWhyNot(testEstate.getId(),"bad");
        assertEquals(expectedWhyNot,actualWhyNot);
        kieSession.dispose();

    }
}
