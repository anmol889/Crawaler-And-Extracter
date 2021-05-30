package com.crawler.extracter.controller;


import com.crawler.extracter.model.PriceTrend;
import com.crawler.extracter.model.ProductDetails;
import com.crawler.extracter.model.ProductDetailsRequest;
import com.crawler.extracter.service.ScrapperService;
import com.gargoylesoftware.htmlunit.html.HtmlAbbreviated;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.text.ParseException;
import java.util.List;


@RestController
@RequestMapping("/api")
public class ScrapperController {

    @Autowired
    ScrapperService scrapperService;

    @GetMapping("/getProductDetails/{productId}")
    public ProductDetails gettingProductDetails(@PathVariable final String productId) {
        return scrapperService.fetchingProductDetails(productId);
    }

    @GetMapping("/allCrawledProducts")
    public List<ProductDetails> allCrawledProducts() {
        return scrapperService.findAllCrawledProducts();
    }

    @GetMapping("/priceTrend/{productId}")
    public List<PriceTrend> priceTrend(@PathVariable final String productId) {
        return scrapperService.gettingPriceTrend(productId);
    }

    @PostMapping("/getProductDetailsHistory")
    public ResponseEntity<?> getProductDetailsHistory(@RequestBody ProductDetailsRequest productDetailsRequest) throws ParseException {
        return scrapperService.fetchingProductDetailsHistory(productDetailsRequest.getTimestamp(), productDetailsRequest.getProductId());
    }

}
