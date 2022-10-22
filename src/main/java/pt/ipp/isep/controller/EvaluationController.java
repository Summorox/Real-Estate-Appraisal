package pt.ipp.isep.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.ipp.isep.model.Evaluation;
import pt.ipp.isep.model.RealEstate;
import pt.ipp.isep.service.EvaluationService;

@RestController
@RequestMapping("evaluation")
public class EvaluationController {

    private EvaluationService service;

    @PostMapping
    public Evaluation create(@RequestBody RealEstate realEstate) {
        return service.create(realEstate);
    }
}
