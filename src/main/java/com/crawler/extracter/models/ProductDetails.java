package com.crawler.extracter.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@Document(collection = "productDetails")
public class ProductDetails {
    private String productId;
    private String title;
    private String price;
    private String description;
    private Map<String, String> ratings;
    private String timestamp;
}
