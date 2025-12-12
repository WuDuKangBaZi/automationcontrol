package com.felixstudio.automationcontrol.service.shop;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.felixstudio.automationcontrol.entity.shop.ShopInfo;
import com.felixstudio.automationcontrol.mapper.shop.ShopInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ShopInfoService {
    private final ShopInfoMapper shopInfoMapper;

    public ShopInfoService(ShopInfoMapper shopInfoMapper) {
        this.shopInfoMapper = shopInfoMapper;
    }


    public List<ShopInfo> getShopsByType(String type) {
        return shopInfoMapper.selectList(new LambdaQueryWrapper<ShopInfo>().eq(ShopInfo::getBusinessType,type).orderByAsc(ShopInfo::getCreatedAt));
    }

    public int insertShopInfo(ShopInfo shopInfo) {
        return shopInfoMapper.insert(shopInfo);
    }

    public int updateShopInfo(ShopInfo shopInfo) {
        return shopInfoMapper.updateById(shopInfo);
    }

    public Object disabledShopInfo(Long id, Integer visible) {
        ShopInfo shopInfo = shopInfoMapper.selectById(id);
        shopInfo.setStatus(visible);
        return shopInfoMapper.updateById(shopInfo);
    }

    public Object deleteShopInfo(Long id) {
        return shopInfoMapper.deleteById(id);
    }

    public ShopInfo getShopByNameAndBusinessType(String shopName,String businessType) {
        ShopInfo shopInfo = shopInfoMapper.selectOne(new LambdaQueryWrapper<ShopInfo>()
                .eq(ShopInfo::getShopName, shopName)
                .eq(ShopInfo::getBusinessType, businessType)
        );
        return shopInfo;
    }
}
