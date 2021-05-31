package com.crawler.extracter.repository;

import com.crawler.extracter.models.HtmlPageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HtmlPageRepository extends MongoRepository<HtmlPageRequest,String> {
}
