package com.example.restaurantFinder.wishlist.service;


import com.example.restaurantFinder.naver.NaverClient;
import com.example.restaurantFinder.naver.dto.SearchImageReq;
import com.example.restaurantFinder.naver.dto.SearchLocalReq;
import com.example.restaurantFinder.wishlist.dto.WishListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class WishListService {

    private final NaverClient naverClient;

    public WishListDto search(String query) {
        //지역 검색
        var searchLocalReq = new SearchLocalReq();
        searchLocalReq.setQuery(query);

        var searchLocalRes = naverClient.searchLocal(searchLocalReq);

        if(searchLocalRes.getTotal() > 0 ) {
            var localItem = searchLocalRes.getItems().stream().findFirst().get();

            //결과를 정규식을 이용해 변환
            var imageQuery = localItem.getTitle().replaceAll("<[^>]*>","");

            //이미지 검색

            var searchImageReq = new SearchImageReq();
            searchImageReq.setQuery(imageQuery);

            var searchImageRes = naverClient.searchImage(searchImageReq);

            if(searchImageRes.getTotal() > 0) {
                var imageItem = searchImageRes.getItems().stream().findFirst().get();
                //결과 리턴
                var result = new WishListDto();
                result.setTitle(localItem.getTitle());
                result.setCategory(localItem.getCategory());
                result.setAddress(localItem.getAddress());
                result.setReadAddress(localItem.getRoadAddress());
                result.setHomePageLink(localItem.getLink());
                result.setImageLink(imageItem.getLink());

                return result;
            }

        }

        return new WishListDto();



    }

}
