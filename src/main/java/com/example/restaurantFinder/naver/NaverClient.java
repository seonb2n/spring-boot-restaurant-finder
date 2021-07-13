package com.example.restaurantFinder.naver;

import com.example.restaurantFinder.naver.dto.SearchImageReq;
import com.example.restaurantFinder.naver.dto.SearchImageRes;
import com.example.restaurantFinder.naver.dto.SearchLocalReq;
import com.example.restaurantFinder.naver.dto.SearchLocalRes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Parameter;

@Component
public class NaverClient {

    @Value("${naver.client.id}")
    private String naverClientId;

    @Value("${naver.client.secret}")
    private String naverSecret;

    @Value("${naver.url.search.local}")
    private String naverLocalSearchUrl;

    @Value("${naver.url.search.image}")
    private String naverImageSearchUrl;

    public SearchLocalRes searchLocal(SearchLocalReq searchLocalReq) {
        var uri = UriComponentsBuilder.fromUriString(naverLocalSearchUrl)
                .queryParams(searchLocalReq.toMultiValueMap())
                .build()
                .encode()
                .toUri();

        var headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id",naverClientId);
        headers.set("X-Naver-Client-Secret",naverSecret);
        headers.setContentType(MediaType.APPLICATION_JSON);

        var httpEntity = new HttpEntity<>(headers);

        var responseType = new ParameterizedTypeReference<SearchLocalRes>(){};

        var responseEntity= new RestTemplate().exchange(
                uri,
                HttpMethod.GET,
                httpEntity,
                responseType
        );

        return responseEntity.getBody();
    }

    public SearchImageRes searchImage(SearchImageReq searchImageReq) {
        var uri = UriComponentsBuilder.fromUriString(naverImageSearchUrl)
                .queryParams(searchImageReq.toMultiValueMap())
                .build()
                .encode()
                .toUri();

        var headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id",naverClientId);
        headers.set("X-Naver-Client-Secret",naverSecret);
        headers.setContentType(MediaType.APPLICATION_JSON);

        var httpEntity = new HttpEntity<>(headers);

        var responseType = new ParameterizedTypeReference<SearchImageRes>(){};

        var responseEntity= new RestTemplate().exchange(
                uri,
                HttpMethod.GET,
                httpEntity,
                responseType
        );

        return responseEntity.getBody();
    }

}
