package com.felixstudio.automationcontrol.controller.shop;

import com.felixstudio.automationcontrol.common.ApiResponse;
import com.felixstudio.automationcontrol.entity.shop.ShopInfo;
import com.felixstudio.automationcontrol.service.shop.ShopInfoService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shops")
public class ShopInfoController {
    private final ShopInfoService shopInfoService;

    public ShopInfoController(ShopInfoService shopInfoService) {
        this.shopInfoService = shopInfoService;
    }

    @PostMapping("/list/{type}")
    public ApiResponse<?> listShopsByType(@PathVariable String type) {
        return ApiResponse.success(shopInfoService.getShopsByType(type));
    }

    @PostMapping("/add/{type}")
    public ApiResponse<?> addShopInfo(@RequestBody ShopInfo shopInfo, @PathVariable String type) {
        shopInfo.setBusinessType(type);
        return ApiResponse.success(shopInfoService.insertShopInfo(shopInfo));
    }

    @PostMapping("/update")
    public ApiResponse<?> updateShopInfo(@RequestBody ShopInfo shopInfo) {
        return ApiResponse.success(shopInfoService.updateShopInfo(shopInfo));
    }

    @PostMapping("/disable/{id}/{visible}")
    public ApiResponse<?> disableShopInfo(@PathVariable Long id, @PathVariable Integer visible) {
        return ApiResponse.success(shopInfoService.disabledShopInfo(id, visible));
    }
    @PostMapping("/delete/{id}")
    public ApiResponse<?> deleteShopInfo(@PathVariable Long id) {
        return ApiResponse.success(shopInfoService.deleteShopInfo(id));
    }
}
