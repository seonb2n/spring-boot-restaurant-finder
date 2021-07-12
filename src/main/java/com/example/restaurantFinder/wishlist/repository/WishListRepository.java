package com.example.restaurantFinder.wishlist.repository;

import com.example.restaurantFinder.db.MemoryDbRepositoryAbstract;
import com.example.restaurantFinder.wishlist.entity.WishListEntity;
import org.springframework.stereotype.Component;

@Component
public class WishListRepository extends MemoryDbRepositoryAbstract<WishListEntity> {
}
