package com.justpickup.storeservice.domain.favoritestore.web;

import com.justpickup.storeservice.domain.favoritestore.dto.GetFavoriteStoreByStoreIdDto;
import com.justpickup.storeservice.domain.favoritestore.service.FavoriteStoreService;
import com.justpickup.storeservice.global.dto.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/customer/favoriteStore")
public class FavoriteStoreCustomerApiController {

    private final FavoriteStoreService favoriteStoreService;

    @GetMapping("/{storeId}")
    public ResponseEntity getFavoriteStoreByStoreId(@RequestHeader(value = "user-id") String userId, @PathVariable Long storeId){

        GetFavoriteStoreByStoreIdDto favoriteStoreByStoreId = favoriteStoreService.getFavoriteStoreByStoreId(Long.parseLong(userId), storeId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Result.createSuccessResult(favoriteStoreByStoreId));
    }


    @PatchMapping("/{storeId}")
    public ResponseEntity patchFavoriteStore(@RequestHeader(value = "user-id") String userId, @PathVariable Long storeId){

        favoriteStoreService.patchFavoriteStore(Long.parseLong(userId),storeId);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(Result.success());
    }
}
