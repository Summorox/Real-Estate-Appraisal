package pt.ipp.isep.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EvaluationController {

    @GetMapping
    public String get() {
        return "Running";
    }
}
