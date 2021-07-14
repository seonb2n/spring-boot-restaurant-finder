package com.example.restaurantFinder.wishlist.service;


import com.example.restaurantFinder.naver.NaverClient;
import com.example.restaurantFinder.naver.dto.SearchImageReq;
import com.example.restaurantFinder.naver.dto.SearchLocalReq;
import com.example.restaurantFinder.wishlist.dto.WishListDto;
import com.example.restaurantFinder.wishlist.entity.WishListEntity;
import com.example.restaurantFinder.wishlist.repository.WishListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishListService {

    private final NaverClient naverClient;
    private final WishListRepository wishListRepository;

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
                result.setRoadAddress(localItem.getRoadAddress());
                result.setHomePageLink(localItem.getLink());
                result.setImageLink(imageItem.getLink());

                return result;
            }

        }

        return new WishListDto();



    }

    public WishListDto add(WishListDto wishListDto) {
        //dto 를 entity 로 바꾼다음에 저장
        var entity = dtoToEntity(wishListDto);
        var saveEntity = wishListRepository.save(entity);
        return entityTODto(saveEntity);
    }

    private WishListEntity dtoToEntity(WishListDto wishListDto) {
        var entity = new WishListEntity();
        entity.setIndex(wishListDto.getIndex());
        entity.setTitle(wishListDto.getTitle());
        entity.setCategory(wishListDto.getCategory());
        entity.setAddress(wishListDto.getAddress());
        entity.setRoadAddress(wishListDto.getRoadAddress());
        entity.setHomePageLink(wishListDto.getHomePageLink());
        entity.setImageLink(wishListDto.getImageLink());
        entity.setVisit(wishListDto.isVisit());
        entity.setVisitCount(wishListDto.getVisitCount());
        entity.setLastVisitDate(wishListDto.getLastVisitDate());
        return entity;
    }

    private WishListDto entityTODto(WishListEntity wishListEntity) {
        var dto = new WishListDto();
        dto.setIndex(wishListEntity.getIndex());
        dto.setTitle(wishListEntity.getTitle());
        dto.setCategory(wishListEntity.getCategory());
        dto.setAddress(wishListEntity.getAddress());
        dto.setRoadAddress(wishListEntity.getRoadAddress());
        dto.setHomePageLink(wishListEntity.getHomePageLink());
        dto.setImageLink(wishListEntity.getImageLink());
        dto.setVisit(wishListEntity.isVisit());
        dto.setVisitCount(wishListEntity.getVisitCount());
        dto.setLastVisitDate(wishListEntity.getLastVisitDate());
        return dto;

    }

    public List<WishListDto> findAll() {
        return wishListRepository.listAll()
                .stream()
                .map(it -> entityTODto(it))
                .collect(Collectors.toList());
    }
}
