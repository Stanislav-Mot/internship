package com.internship.internship.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class RedirectController {

    @GetMapping("/")
    public RedirectView redirect(){
        return new RedirectView("swagger-ui/index.html?configUrl=/v3/api-docs");
    }
}
