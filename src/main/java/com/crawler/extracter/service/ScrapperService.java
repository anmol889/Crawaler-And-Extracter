package com.crawler.extracter.service;

import com.crawler.extracter.constants.CssSelectors;
import com.crawler.extracter.constants.GatewayConstants;
import com.crawler.extracter.constants.MathsConstants;
import com.crawler.extracter.mappers.ResponseMapper;
import com.crawler.extracter.model.ErrorResponse;
import com.crawler.extracter.model.HtmlPageRequest;
import com.crawler.extracter.model.PriceTrend;
import com.crawler.extracter.model.ProductDetails;
import com.crawler.extracter.repository.HtmlPageRepository;
import com.crawler.extracter.repository.ProductDetailsRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.xerces.xs.LSInputList;
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

@Slf4j
@Service
public class ScrapperService {

    @Autowired
    private ProductDetailsRepository productDetailsRepository;

    @Autowired
    private HtmlPageRepository htmlPageRepository;

    public ResponseEntity<?> fetchingProductDetails(final String productId) {
        try{
            long lastCrawled = crawlingEventCheck(productId);
            log.debug("[fetchingProductDetails] lastCrawled: {}", lastCrawled);
            if(lastCrawled > MathsConstants.TIME_LIMIT){
                WebDriver driver = gettingWebDriver(productId);
                String price = driver.findElement(By.cssSelector(CssSelectors.PRICE)).getText();
                String title = driver.findElement(By.cssSelector(CssSelectors.TITLE)).getText();
                String description = driver.findElement(By.cssSelector(CssSelectors.DESCRIPTION)).getText();
                String ratings = driver.findElement(By.cssSelector(CssSelectors.RATINGS)).getText();
                String totalCount = driver.findElement(By.cssSelector(CssSelectors.TOTAL_RATINGS_COUNT)).getText();
                Map<String, String> ratingMap = ratingStringToRatingMap(ratings, totalCount);
                ProductDetails productDetails = ResponseMapper.responseMapper(price, description, title, ratingMap, productId);
                productDetailsRepository.save(productDetails);
                return new ResponseEntity<>(productDetails, HttpStatus.OK);
            }
            else{
                ErrorResponse errorResponse = new ErrorResponse();
                errorResponse.setErrorCode(406);
                errorResponse.setMessage("requested page is just crawled, please try after "+(MathsConstants.TIME_LIMIT-lastCrawled)/MathsConstants.MIN_CONVERTER+" minutes");
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_ACCEPTABLE);
            }
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }

    public Map<String, String> ratingStringToRatingMap(String ratingString, String totalCount) {
        Map<String, String> ratingMap = new LinkedHashMap<>();
        ratingMap.put("overallCount", totalCount.substring(0, 2));
        String[] ratingArray = ratingString.replace("\n", "").split(MathsConstants.PERCENT);
        for (int i = 1; i <= ratingArray.length; i++) {
            ratingMap.put(ratingArray[i - 1].substring(0, 6), ratingArray[i - 1].substring(6) + MathsConstants.PERCENT);
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

    public ResponseEntity<?> findAllCrawledProducts() {
        try{
            List<ProductDetails> productDetailsList = productDetailsRepository.findAll();
            if(!productDetailsList.isEmpty()){
                return new ResponseEntity<>(productDetailsRepository.findAll(), HttpStatus.OK);
            }
            else{
                ErrorResponse errorResponse = new ErrorResponse();
                errorResponse.setErrorCode(500);
                errorResponse.setMessage("productDetails data is not present in DB, as this page is not crawled yet");
                return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> gettingPriceTrend(final String productId) {
        try{
            List<ProductDetails> productDetailsList = productDetailsRepository.findAllByProductIdOrderByTimestamp(productId);
            if(!productDetailsList.isEmpty()){
                List<PriceTrend> updatedProductDetailsList = new ArrayList<>();
                for (ProductDetails productDetails : productDetailsList) {
                    PriceTrend priceTrend = new PriceTrend();
                    priceTrend.setTimestamp(productDetails.getTimestamp());
                    priceTrend.setPrice(productDetails.getPrice());
                    updatedProductDetailsList.add(priceTrend);
                }
                return new ResponseEntity<>(updatedProductDetailsList, HttpStatus.OK);
            }
            else{
                ErrorResponse errorResponse = new ErrorResponse();
                errorResponse.setErrorCode(500);
                errorResponse.setMessage("productDetails data is not present in DB, as this page is not crawled yet");
                return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ResponseEntity<?> fetchingProductDetailsHistory(String dateTime, String productId)  {
        try{
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = df.parse(dateTime);
            long epoch = date.getTime();
            List<ProductDetails> productDetailsList = productDetailsRepository.findAllByProductIdOrderByTimestampDesc(productId);
            for (ProductDetails productDetails : productDetailsList) {
                if (df.parse(productDetails.getTimestamp()).getTime() <= epoch) {
                    return new ResponseEntity<>(productDetails, HttpStatus.OK);
                }
            }
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setErrorCode(400);
            errorResponse.setMessage("page is not crawled before given date, please enter valid date");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    public ResponseEntity<?> scrappingHtmlPage(final String productId) {
        try{
            long lastCrawled = crawlingEventCheck(productId);
            log.debug("[scrappingHtmlPage] lastCrawled: {}", lastCrawled);
            if(lastCrawled > MathsConstants.TIME_LIMIT) {
                WebDriver driver = gettingWebDriver(productId);
                HtmlPageRequest htmlPageRequest = new HtmlPageRequest();
                htmlPageRequest.setProductId(productId);
                htmlPageRequest.setHtmlPage(driver.getPageSource());
                htmlPageRepository.save(htmlPageRequest);
                return new ResponseEntity<>(driver.getPageSource(), HttpStatus.OK);
        }
            else{
                ErrorResponse errorResponse = new ErrorResponse();
                errorResponse.setErrorCode(406);
                errorResponse.setMessage("requested page is just crawled, please try after "+(MathsConstants.TIME_LIMIT-lastCrawled)/MathsConstants.MIN_CONVERTER+" minutes");
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_ACCEPTABLE);
            }
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }
    public long crawlingEventCheck(final String productId ) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<ProductDetails> productDetailsList = productDetailsRepository.findAllByProductIdOrderByTimestampDesc(productId);
        if(productDetailsList.isEmpty()){
            return Long.MAX_VALUE;
        }
        long lastCrawl = df.parse(productDetailsList.get(0).getTimestamp()).getTime();
        long currentCrawl = System.currentTimeMillis();
        return currentCrawl - lastCrawl ;
    }

}
