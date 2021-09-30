package com.gateway.apiGateway.configure;

import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class configure {
    
    @Value("${cloud.aws.region.static}")
    private String region;
    
    @Value("${cloud.aws.endpoint.url}")
    private String url;
    
    @Bean
    public AmazonS3 s3Client() {
    	EndpointConfiguration endpointConfiguration = new EndpointConfiguration(url, region);
        return AmazonS3ClientBuilder.standard()
        		//.withEndpointConfiguration(endpointConfiguration)
        		.withPathStyleAccessEnabled(true)
                .build();
    }
}
