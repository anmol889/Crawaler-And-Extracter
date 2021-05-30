# Crawaler-And-Extracter

In this problem, I am building a service which takes an input a product SKU (unique identifier for every product) and crawl the amazon page and extract several attributes. Also implemented some api's to get more analysed data.

### Setting it up
1. Install JDK `version 12.0.2`.
2. Install mongoDB `version v4.4.1`.
3. Run `mvn spring-boot:run`
4. Tomcat server will be started on default port 8080
5. Now service is up, start your journey

### The following table shows overview of the Rest APIs that will be exported:

- GET     `api/getProductDetails/{productId}      `	     User can fetch product details by pasing productId
- GET     `api/allCrawledProducts       `                User can fetch all the crawled data
- GET     `api/priceTrend/{productId}    `               price trend of the product can be seen here by passing productId
- POST    `api/getProductDetailsHistory `                using this api, user can fetch latest productDetails to the given date
- GET     `api/getHtml/{productId}  `                    fetching source page by passing productId
