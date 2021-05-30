package com.crawler.extracter.mappers;

import com.crawler.extracter.model.ProductDetails;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class ResponseMapper {
    private ResponseMapper(){}
    public static ProductDetails responseMapper(String price, String description, String title, Map<String, String> ratingMap, final String productId){
        ProductDetails productDetails = new ProductDetails();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        productDetails.setPrice(price);
        productDetails.setDescription(description);
        productDetails.setTitle(title);
        productDetails.setRatings(ratingMap);
        productDetails.setProductId(productId);
        productDetails.setTimestamp(dtf.format(LocalDateTime.now()));
        return productDetails;
    }
}
