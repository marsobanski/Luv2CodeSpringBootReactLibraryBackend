package com.marcin.library_spring_boot.config;

import com.marcin.library_spring_boot.entity.Book;
import com.marcin.library_spring_boot.entity.Checkout;
import com.marcin.library_spring_boot.entity.Review;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

    private String allowedOrigin = "http://localhost:3000";

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        HttpMethod[] unsupportedActions = {
                HttpMethod.POST,
                HttpMethod.PUT,
                HttpMethod.DELETE,
                HttpMethod.PATCH};

        config.exposeIdsFor(Book.class);
        disableHttpMethods(Book.class, config, unsupportedActions);
        config.exposeIdsFor(Review.class);
        disableHttpMethods(Review.class, config, unsupportedActions);
        config.exposeIdsFor(Checkout.class);
        disableHttpMethods(Checkout.class, config, unsupportedActions);


        /* Configure CORS mapping */
        /* zezwala na komunikację dowolny endpoint ale tylko z http://localhost:3000*/
        cors.addMapping(config.getBasePath() + "/**")
                .allowedOrigins(allowedOrigin);

    }

    private void disableHttpMethods(Class clz,
                                    RepositoryRestConfiguration config,
                                    HttpMethod[] unsupportedActions) {
        config.getExposureConfiguration()
                .forDomainType(clz)
                .withItemExposure((item, httpMethods)
                        -> httpMethods.disable(unsupportedActions))
                .withCollectionExposure((item, httpMethods)
                        -> httpMethods.disable(unsupportedActions));
    }
}
