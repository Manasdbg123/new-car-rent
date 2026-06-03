package com.carrental.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SpaController {
    // Forwards all frontend UI routes to index.html so JavaScript can handle them
    @RequestMapping(value = { "/", "/home", "/cars", "/bikes", "/dashboard" })
    public String forwardToFrontend() {
        return "forward:/index.html";
    }
}