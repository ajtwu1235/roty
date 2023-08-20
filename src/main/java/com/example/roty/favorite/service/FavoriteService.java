package com.example.roty.favorite.service;

import com.example.roty.favorite.domain.Favorite;
import com.example.roty.favorite.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;


    public void create(Favorite f){

        favoriteRepository.save(f);
    }

    public Optional<Favorite> find(Long id){

        return favoriteRepository.findById(id);
    }

    public void delete(Long id){
        favoriteRepository.deleteById(id);
    }

}