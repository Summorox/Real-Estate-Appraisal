package pt.ipp.isep;

import lombok.extern.slf4j.Slf4j;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.runtime.ClassObjectFilter;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ipp.isep.model.*;
import pt.ipp.isep.service.EvaluationService;

import java.util.ArrayList;
import java.util.Collection;

import static org.mockito.Mockito.*;

@Slf4j
public class DroolsTest {

    @Test
    public void droolsSetupTest() {
        KieServices kieServices = assertDoesNotThrow(() -> KieServices.Factory.get(), "Fail to get KieServices");
        KieContainer kieContainer = assertDoesNotThrow(() -> kieServices.getKieClasspathContainer() , "Fail to get KieContainer");
        KieSession kieSession = assertDoesNotThrow(() -> kieContainer.newKieSession("rulesSession") , "Fail to get KieSession");

    }

    @Test
    public void testRulesFiring() {
        KieContainer kieContainer = KieServices.Factory.get().getKieClasspathContainer();

        Results verifyResults = kieContainer.verify();

        KieSession kieSession = kieContainer.newKieSession("rulesSession");


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


        RealEstate testEstate= RealEstate.
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

        Evaluation evaluation = Evaluation.builder()
                .realEstate(testEstate)
                .appraiseValue(15000)
                .baseValue(15000)
                .build();

        Evaluation expectedEvaluation = Evaluation.builder()
                .realEstate(testEstate)
                .appraiseValue(15441L)
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


        Collection<Evaluation> evaluationReturned = (Collection<Evaluation>) kieSession.getObjects(new ClassObjectFilter(Evaluation.class));
        Evaluation finalEvaluation = evaluationReturned.iterator().next();
        assertEquals( 1, evaluationReturned.size());
        Mockito.verify(service, times(1)).appraiseConstructionYear(2000,15000);
        Mockito.verify(service, times(1)).appraiseParkingSlots(2,15000L);
        Mockito.verify(service, times(1)).appraiseBathrooms(1,15000L);
        Mockito.verify(service, times(1)).appraiseItem(item1,15000L);
        Mockito.verify(service, times(1)).appraiseItem(item2,15000L);
        Mockito.verify(service, times(1)).appraiseItem(item3,15000L);
        assertEquals( 2, kieSession.getObjects().size());
        assertEquals(15577L,finalEvaluation.getAppraiseValue());

        kieSession.dispose();




    }

}
