package pt.ipp.isep;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.definition.KiePackage;
import org.kie.api.definition.rule.Rule;
import org.kie.api.runtime.KieContainer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@Slf4j
public class DroolsTest {


    @Test
    public void droolsSetupTest() {
        KieServices kieServices = assertDoesNotThrow(() -> KieServices.Factory.get(), "Fail to get KieServices");
        KieContainer kieContainer = assertDoesNotThrow(() -> kieServices.getKieClasspathContainer() , "Fail to get KieContainer");
        KieBase kieBase = assertDoesNotThrow(() -> kieContainer.getKieBase(), "Fail to get KieBase");

        for ( KiePackage kp : assertDoesNotThrow(() -> kieBase.getKiePackages(),"Fail to get KiePackages") ) {
            Assertions.assertTrue(kp.getRules().size() > 0);
        }
    }

}
