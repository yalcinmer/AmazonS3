package org.example.amazons3.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AmazonS3Config {

    @Value("${amazon.s3.accessKey}")
    private String accessKey;

    @Value("${amazon.s3.secretKey}")
    private String secretKey;

    @Bean
    public S3Client s3Client() {

        AwsCredentials awsCredentials = AwsBasicCredentials.create(accessKey, secretKey);
        return S3Client.builder().credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .region(Region.EU_NORTH_1)
                .build();
    }
}
