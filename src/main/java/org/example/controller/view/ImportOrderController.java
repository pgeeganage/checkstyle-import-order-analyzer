package org.example.controller.view;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class ImportOrderController {

    @GetMapping
    public String viewImportOrder() {
        return "index";
    }
}