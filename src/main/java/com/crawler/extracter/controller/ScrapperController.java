package com.crawler.extracter.controller;

import com.crawler.extracter.model.ProductDetailsRequest;
import com.crawler.extracter.service.ScrapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class ScrapperController {

    @Autowired
    ScrapperService scrapperService;

    @GetMapping("/getProductDetails/{productId}")
    public Map<String, Object> gettingProductDetails(@PathVariable final String productId) throws ParseException {
        Long from = System.currentTimeMillis();
        Map<String, Object> productDetailsMap = new HashMap<>();
        productDetailsMap.put("productDetails", scrapperService.fetchingProductDetails(productId).getBody());
        Long to = System.currentTimeMillis();
        productDetailsMap.put("timeTaken", (to - from)/1000.0+" seconds");
        return productDetailsMap;
    }

    @GetMapping("/allCrawledProducts")
    public Map<String, Object> allCrawledProducts() {
        Long from = System.currentTimeMillis();
        Map<String, Object> crawledDetails = new HashMap<>();
        crawledDetails.put("ProductDetails", scrapperService.findAllCrawledProducts());
        Long to = System.currentTimeMillis();
        crawledDetails.put("timeTaken",(to - from)/1000.0+" seconds");
        return crawledDetails;
    }

    @GetMapping("/priceTrend/{productId}")
    public Map<String, Object> priceTrend(@PathVariable final String productId) {
        Long from = System.currentTimeMillis();
        Map<String, Object> priceTrendsMap = new HashMap<>();
        priceTrendsMap.put("priceTrends",scrapperService.gettingPriceTrend(productId));
        Long to = System.currentTimeMillis();
        priceTrendsMap.put("timeTaken",(to - from)/1000.0+" seconds");
        return  priceTrendsMap;
    }

    @PostMapping("/getProductDetailsHistory")
    public Map<String, Object> getProductDetailsHistory(@RequestBody ProductDetailsRequest productDetailsRequest) throws ParseException {
        Long from = System.currentTimeMillis();
        Map<String, Object> productDetailsHistoryMap = new HashMap<>();
        productDetailsHistoryMap.put("productDetail",scrapperService.fetchingProductDetailsHistory(productDetailsRequest.getTimestamp(), productDetailsRequest.getProductId()).getBody());
        Long to = System.currentTimeMillis();
        productDetailsHistoryMap.put("timeTaken",(to - from)/1000.0+" seconds");
        return productDetailsHistoryMap;
    }

    @GetMapping("/getHtml/{productId}")
    public Map<String, Object> fetchingHtmlPage(@PathVariable final String productId)  {
        Long from = System.currentTimeMillis();
        Map<String, Object> htmlPageMap = new HashMap<>();
        htmlPageMap.put("pageSource",scrapperService.scrappingHtmlPage(productId).getBody());
        Long to = System.currentTimeMillis();
        htmlPageMap.put("timeTaken",(to - from)/1000.0+" seconds");
        return htmlPageMap;
    }
}
