package com.crawler.extracter.service;

import com.crawler.extracter.constants.CssSelectors;
import com.crawler.extracter.constants.GatewayConstants;
import com.crawler.extracter.mappers.ResponseMapper;
import com.crawler.extracter.model.HtmlPageRequest;
import com.crawler.extracter.model.PriceTrend;
import com.crawler.extracter.model.ProductDetails;
import com.crawler.extracter.repository.HtmlPageRepository;
import com.crawler.extracter.repository.ProductDetailsRepository;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ScrapperService {

    @Autowired
    private ProductDetailsRepository productDetailsRepository;

    @Autowired
    private HtmlPageRepository htmlPageRepository;

    public ProductDetails fetchingProductDetails(final String productId) {
        WebDriver driver = gettingWebDriver(productId);
        String price = driver.findElement(By.cssSelector(CssSelectors.PRICE)).getText();
        String title = driver.findElement(By.cssSelector(CssSelectors.TITLE)).getText();
        String description = driver.findElement(By.cssSelector(CssSelectors.DESCRIPTION)).getText();
        String ratings = driver.findElement(By.cssSelector(CssSelectors.RATINGS)).getText();
        String totalCount = driver.findElement(By.cssSelector(CssSelectors.TOTAL_RATINGS_COUNT)).getText();
        Map<String, String> ratingMap = ratingStringToRatingMap(ratings, totalCount);
        ProductDetails productDetails = ResponseMapper.responseMapper(price, description, title, ratingMap, productId);
        productDetailsRepository.save(productDetails);
        return productDetails;
    }

    public Map<String, String> ratingStringToRatingMap(String ratingString, String totalCount) {
        Map<String, String> ratingMap = new LinkedHashMap<>();
        ratingMap.put("overallCount", totalCount.substring(0, 2));
        String[] ratingArray = ratingString.replace("\n", "").split("%");
        for (int i = 1; i <= ratingArray.length; i++) {
            ratingMap.put(ratingArray[i - 1].substring(0, 6), ratingArray[i - 1].substring(6) + "%");
        }
        ratingMap.put("overallCount", totalCount);
        return ratingMap;

    }

    public WebDriver gettingWebDriver(final String productId) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments(GatewayConstants.HEADLESS);
        WebDriver driver = new ChromeDriver(options);
        driver.get(GatewayConstants.URL.concat(productId));
        return driver;
    }

    public List<ProductDetails> findAllCrawledProducts() {
        return productDetailsRepository.findAll();
    }

    public List<PriceTrend> gettingPriceTrend(final String productId) {
        List<ProductDetails> productDetailsList = productDetailsRepository.findAllByProductIdOrderByTimestamp(productId);
        List<PriceTrend> updatedProductDetailsList = new ArrayList<>();
        for (ProductDetails productDetails : productDetailsList) {
            PriceTrend priceTrend = new PriceTrend();
            priceTrend.setTimestamp(productDetails.getTimestamp());
            priceTrend.setPrice(productDetails.getPrice());
            updatedProductDetailsList.add(priceTrend);
        }
        return updatedProductDetailsList;
    }

    public ResponseEntity<?> fetchingProductDetailsHistory(String dateTime, String productId) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = df.parse(dateTime);
        long epoch = date.getTime();
        List<ProductDetails> productDetailsList = productDetailsRepository.findAllByProductIdOrderByTimestampDesc(productId);
        for (ProductDetails productDetails : productDetailsList) {
            if (df.parse(productDetails.getTimestamp()).getTime() <= epoch) {
                return new ResponseEntity<>(productDetails, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("page is not crawled before this date, please enter valid date", HttpStatus.BAD_REQUEST);
    }

    public String scrappingHtmlPage(final String productId){
        WebDriver driver = gettingWebDriver(productId);
        HtmlPageRequest htmlPageRequest = new HtmlPageRequest();
        htmlPageRequest.setProductId(productId);
        htmlPageRequest.setHtmlPage(driver.getPageSource());
        htmlPageRepository.save(htmlPageRequest);
        return driver.getPageSource();
    }
}
