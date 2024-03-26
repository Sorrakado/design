package com.kyou.net.design.service;

import cn.hutool.core.collection.CollUtil;
import com.kyou.net.design.composite.AbstractProductItem;
import com.kyou.net.design.composite.ProductComposite;
import com.kyou.net.design.pojo.ProductItem;
import com.kyou.net.design.repo.ProductItemRepository;
import com.kyou.net.design.util.RedisCommonProcessor;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductItemService {

    @Resource
    private RedisCommonProcessor redisCommonProcessor;

    @Resource
    private ProductItemRepository productItemRepository;

    public ProductComposite fetchAllItems() {
        //先查询redis缓存
        Object cacheItems = redisCommonProcessor.get("items");
        if (cacheItems != null) {
            return (ProductComposite) cacheItems;
        }

        List<ProductItem> allItems = productItemRepository.findAll();
        ProductComposite composite = this.generateProductTree(allItems);
        if(composite == null){
            throw new UnsupportedOperationException("Should Not Be Empty");
        }
        redisCommonProcessor.set("items",composite);
        return composite;

    }

    private ProductComposite generateProductTree(List<ProductItem> allItems) {
        ArrayList<ProductComposite> composites = new ArrayList<>(allItems.size());
        allItems.forEach(item -> {
            composites.add(ProductComposite.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .pid(item.getPid())
                    .build());
        });
        Map<Integer, List<ProductComposite>> groupingList = composites.stream().collect(Collectors.groupingBy(ProductComposite::getPid));
        composites.forEach(it -> {
            List<ProductComposite> list = groupingList.get(it.getId());
            it.setChild(CollUtil.isEmpty(list) ? new ArrayList<>() : list.stream().map(k -> (AbstractProductItem) k).collect(Collectors.toList()));
        });

        return composites.isEmpty() ? null : composites.get(0);
    }
}
