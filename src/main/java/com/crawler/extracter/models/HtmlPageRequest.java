package com.crawler.extracter.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "htmlPages")
public class HtmlPageRequest {
    private String productId;
    private String htmlPage;
}
