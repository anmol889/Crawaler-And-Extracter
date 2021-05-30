package com.crawler.extracter.controller;


import com.crawler.extracter.model.ProductDetails;
import com.crawler.extracter.service.ScrapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class ScrapperController {

    @Autowired
    ScrapperService scrapperService;

    @GetMapping("/getProductDetails/{productId}")
    public ProductDetails gettingProductDetails(@PathVariable final String productId) {
        System.out.println(productId);
        return scrapperService.fetchingProductDetails(productId);
    }
}
