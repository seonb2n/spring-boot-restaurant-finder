package com.example.restaurantFinder.db;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class MemoryDbRepositoryAbstract<T extends MemoryDbEntity> implements MemoryDbRepositoryIfs<T>{

    private final List<T> db = new ArrayList<>();
    private int index = 0;

    @Override
    public Optional<T> findById(int index) {
        //제네릭 타입의 T 변수의 getIndex로 가져온 index 값과 db의 index 가 일치하는 값을 가져오도록
        return db.stream().filter(it -> it.getIndex() == index).findFirst();
        //있을 수도 없을수도 있는 data 를 optional 하게 return 함
    }

    @Override
    public T save(T entity) {

        var optionalEntity = db.stream().filter(it -> it.getIndex() == entity.getIndex()).findFirst();

        if(optionalEntity.isEmpty()) {
            // db에 데이터가 없는 경우
            index++;
            entity.setIndex(index);
            db.add(entity);
            return entity;
        }else {
            // db에 데이터가 있는 경우
            var preIndex = optionalEntity.get().getIndex();
            entity.setIndex(preIndex);
            deleteById(preIndex);
            db.add(entity);
            return entity;
        }
    }

    @Override
    public void deleteById(int index) {
        var optionEntity = db.stream().filter(it -> it.getIndex() == index).findFirst();
        if(optionEntity.isPresent()) {
            //optionEntity 값이 존재하는 경우
            db.remove(optionEntity.get());
        }
    }

    @Override
    public List<T> findAll() {
        return db;
    }
}
