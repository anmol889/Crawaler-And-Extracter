package com.crawler.extracter.service;

import com.crawler.extracter.constants.CssSelectors;
import com.crawler.extracter.constants.GatewayConstants;
import com.crawler.extracter.mappers.ResponseMapper;
import com.crawler.extracter.model.ProductDetails;
import com.crawler.extracter.repository.ScrapperRepository;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class ScrapperService {

    @Autowired
    private ScrapperRepository scrapperRepository;

    public ProductDetails fetchingProductDetails(final String productId) {
        WebDriver driver = gettingDriver(productId);
        String price = driver.findElement(By.cssSelector(CssSelectors.PRICE)).getText();
        String title = driver.findElement(By.cssSelector(CssSelectors.TITLE)).getText();
        String description = driver.findElement(By.cssSelector(CssSelectors.DESCRIPTION)).getText();
        String ratings = driver.findElement(By.cssSelector(CssSelectors.RATINGS)).getText();
        String totalCount = driver.findElement(By.cssSelector(CssSelectors.TOTAL_RATINGS_COUNT)).getText();
        Map<String, String> ratingMap = ratingStringToRatingMap(ratings,totalCount);
        ProductDetails productDetails = ResponseMapper.responseMapper(price,description,title,ratingMap);
        scrapperRepository.save(productDetails);
        return productDetails;
    }

    public Map<String, String> ratingStringToRatingMap(String ratingString, String totalCount){
        Map<String, String> ratingMap = new LinkedHashMap<>();
        ratingMap.put("overallCount",totalCount.substring(0,2));
        String[] ratingArray = ratingString.replace("\n", "").split("%");
        for(int i = 1;i<=ratingArray.length;i++){
            ratingMap.put(ratingArray[i-1].substring(0,6),ratingArray[i-1].substring(6)+"%");
        }
        ratingMap.put("overallCount",totalCount);
        return  ratingMap;

    }

    public WebDriver gettingDriver(final String productId){
        ChromeOptions options = new ChromeOptions();
        options.addArguments(GatewayConstants.HEADLESS);
        WebDriver driver = new ChromeDriver(options);
        driver.get(GatewayConstants.URL.concat(productId));
        return driver;
    }
}
