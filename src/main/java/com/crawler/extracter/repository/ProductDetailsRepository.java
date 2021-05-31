package com.crawler.extracter.repository;

import com.crawler.extracter.models.ProductDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductDetailsRepository extends MongoRepository<ProductDetails,String> {

    List<ProductDetails> findAllByProductIdOrderByTimestamp(String productId);
    List<ProductDetails> findAllByProductIdOrderByTimestampDesc(String productId);
}
