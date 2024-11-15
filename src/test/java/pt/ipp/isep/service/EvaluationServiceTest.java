package pt.ipp.isep.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.model.*;
import pt.ipp.isep.repository.EvaluationRepository;
import pt.ipp.isep.repository.ItemRepository;
import pt.ipp.isep.repository.PropertyRepository;
import pt.ipp.isep.sample.Property;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
public class EvaluationServiceTest {

    private static final Item ITEM_1 = new Item(1, "Pool", 0.07, 1, "Pool");
    public static final Item ITEM_2 = new Item(2, "Garden", 0.05, 2, "Garden");
    public static final Item ITEM_3 = new Item(3, "Bad", -0.05, 3, "Condition");
    PropertyRepository propertyRepository;
    ItemRepository itemRepository;
    EvaluationService service;

    Evaluation evaluation;

    @BeforeEach
    public void init() {
        propertyRepository = mock(PropertyRepository.class);
        itemRepository = mock(ItemRepository.class);

        when(itemRepository.findById(1)).thenReturn(Optional.of(ITEM_1));
        when(itemRepository.findById(2)).thenReturn(Optional.of(ITEM_2));
        when(itemRepository.findById(3)).thenReturn(Optional.of(ITEM_3));
        service = new EvaluationService(propertyRepository,itemRepository);

        RealEstateItem item1 = RealEstateItem.builder()
                .id(1)
                .item(ITEM_1)
                .build();

        RealEstateItem item2 = RealEstateItem.builder()
                .id(2)
                .item(ITEM_2)
                .build();

        RealEstateItem item3 = RealEstateItem.builder()
                .id(2)
                .item(ITEM_3)
                .build();

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

        evaluation = Evaluation.builder()
                .realEstate(testEstate)
                .appraiseValue(15000)
                .baseValue(14000)
                .build();
    }

    @Test
    public void testAveragePriceCalculation() {

        ArrayList<Property> properties = new ArrayList<Property>();
        properties.add(Property.builder()
                        .id(1L)
                        .price(13000.00)
                        .typology(Typology.T1)
                        .postalCode(PostalCode.builder().prefixCode("1450").suffixCode("479").build())
                .build());

        properties.add(Property.builder()
                .id(2L)
                .price(17000.00)
                .typology(Typology.T1)
                .postalCode(PostalCode.builder().prefixCode("1450").suffixCode("479").build())
                .build());

        properties.add(Property.builder()
                .id(3L)
                .price(15500.00)
                .typology(Typology.T1)
                .postalCode(PostalCode.builder().prefixCode("1450").suffixCode("479").build())
                .build());

        properties.add(Property.builder()
                .id(4L)
                .price(16000.00)
                .typology(Typology.T1)
                .postalCode(PostalCode.builder().prefixCode("1450").suffixCode("479").build())
                .build());

        properties.add(Property.builder()
                .id(5L)
                .price(14000.00)
                .typology(Typology.T1)
                .postalCode(PostalCode.builder().prefixCode("1450").suffixCode("479").build())
                .build());


        long result = Property.calculateAveragePrice(properties);
        long expected = 15100L;
        assertEquals(expected,result);
    }

    @Test
    public void testCalculateBusinessQuality() {

        long perc = service.calculateQualityPercentage(this.evaluation);
        evaluation.setPercQuality(perc);
        BussinessQuality result = service.calculateBusinessQuality(this.evaluation);
        evaluation.setPercQuality(perc);
        BussinessQuality expected = BussinessQuality.FAIR;
        assertEquals(expected,result);


        evaluation.setAppraiseValue(10000);
        perc = service.calculateQualityPercentage(this.evaluation);
        evaluation.setPercQuality(perc);
        result = service.calculateBusinessQuality(evaluation);
        expected = BussinessQuality.BAD;
        assertEquals(expected,result);

        evaluation.setAppraiseValue(18000);
        perc = service.calculateQualityPercentage(this.evaluation);
        evaluation.setPercQuality(perc);
        result = service.calculateBusinessQuality(evaluation);
        expected = BussinessQuality.GOOD;
        assertEquals(expected,result);
    }

    @Test
    public void testAppraiseConstructionYear() {


        long result = service.appraiseConstructionYear(2000,14000);
        long expected = -1462L;
        assertEquals(expected,result);

    }

    @Test
    public void testAppraiseParkingSlots() {


        long result = service.appraiseParkingSlots(2,14000);
        long expected = 709L;
        assertEquals(expected,result);

    }

    @Test
    public void testAppraiseBathrooms() {


        long result = service.appraiseBathrooms(1,14000);
        long expected = 350L;
        assertEquals(expected,result);

    }

    @Test
    public void testAppraiseItem() {


        long result = service.appraiseItem(RealEstateItem.builder()
                .id(1)
                .item(ITEM_1)
                .build(),14000);
        long expected = 980L;
        assertEquals(expected,result);

        result = service.appraiseItem(RealEstateItem.builder()
                .id(2)
                .item(ITEM_2)
                .build(),14000);
        expected = 700L;
        assertEquals(expected,result);

        result = service.appraiseItem(RealEstateItem.builder()
                .id(3)
                .item(ITEM_3)
                .build(),14000);
        expected = -700L;
        assertEquals(expected,result);

    }
}
