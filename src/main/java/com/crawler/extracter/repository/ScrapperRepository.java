package com.crawler.extracter.repository;

import com.crawler.extracter.model.ProductDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ScrapperRepository extends MongoRepository<ProductDetails,Integer> {

}
