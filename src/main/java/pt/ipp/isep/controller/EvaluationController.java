package pt.ipp.isep.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pt.ipp.isep.explanation.How;
import pt.ipp.isep.explanation.WhyNot;
import pt.ipp.isep.model.Evaluation;
import pt.ipp.isep.model.RealEstate;
import pt.ipp.isep.service.EvaluationService;
@AllArgsConstructor
@RestController
@RequestMapping("evaluation")
public class EvaluationController {

    private EvaluationService service;

    @PostMapping
    public Evaluation create(@RequestBody RealEstate realEstate) {
        return service.create(realEstate);
    }

    @GetMapping("/how/{id}")
    public String getHowById(@PathVariable String id){
        return new How().getExplanationById(id);
    }

    @GetMapping("/whynot/{id}/{quality}")
    public String geWhyNotByIdAndQuality(@PathVariable String id, @PathVariable String quality){
        return new WhyNot().getWhyNot(id,quality);
    }
}
