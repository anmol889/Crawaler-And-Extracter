package com.crawler.extracter.controller;

import com.crawler.extracter.models.ProductDetailsRequest;
import com.crawler.extracter.service.ScrapperService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
@Api(value = "Crawler", description = "REST API for Crawler and Extractor", tags = { "Crawler" })
public class ScrapperController {

    @Autowired
    ScrapperService scrapperService;

    @GetMapping("/getProductDetails/{productId}")
    public Map<String, Object> gettingProductDetails(@PathVariable final String productId)  {
        log.info("[gettingProductDetails] productId: {}", productId);
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
        crawledDetails.put("ProductDetails", scrapperService.findAllCrawledProducts().getBody());
        Long to = System.currentTimeMillis();
        crawledDetails.put("timeTaken",(to - from)/1000.0+" seconds");
        return crawledDetails;
    }

    @GetMapping("/priceTrend/{productId}")
    public Map<String, Object> priceTrend(@PathVariable final String productId) {
        log.info("[priceTrend] productId: {}", productId);
        Long from = System.currentTimeMillis();
        Map<String, Object> priceTrendsMap = new HashMap<>();
        priceTrendsMap.put("priceTrends",scrapperService.gettingPriceTrend(productId).getBody());
        Long to = System.currentTimeMillis();
        priceTrendsMap.put("timeTaken",(to - from)/1000.0+" seconds");
        return  priceTrendsMap;
    }

    @PostMapping("/getProductDetailsHistory")
    public Map<String, Object> getProductDetailsHistory(@RequestBody ProductDetailsRequest productDetailsRequest) throws ParseException {
        log.info("[getProductDetailsHistory] productDetailsRequest: {}", productDetailsRequest);
        Long from = System.currentTimeMillis();
        Map<String, Object> productDetailsHistoryMap = new HashMap<>();
        productDetailsHistoryMap.put("productDetail",scrapperService.fetchingProductDetailsHistory(productDetailsRequest.getTimestamp(), productDetailsRequest.getProductId()).getBody());
        Long to = System.currentTimeMillis();
        productDetailsHistoryMap.put("timeTaken",(to - from)/1000.0+" seconds");
        return productDetailsHistoryMap;
    }

    @GetMapping("/getHtml/{productId}")
    public Map<String, Object> fetchingHtmlPage(@PathVariable final String productId)  {
        log.info("[gettingProductDetails] productId: {}", productId);
        Long from = System.currentTimeMillis();
        Map<String, Object> htmlPageMap = new HashMap<>();
        htmlPageMap.put("pageSource",scrapperService.scrappingHtmlPage(productId));
        Long to = System.currentTimeMillis();
        htmlPageMap.put("timeTaken",(to - from)/1000.0+" seconds");
        return htmlPageMap;
    }
}
