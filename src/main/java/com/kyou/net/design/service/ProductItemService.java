package com.kyou.net.design.service;

import cn.hutool.core.collection.CollUtil;
import com.kyou.net.design.composite.AbstractProductItem;
import com.kyou.net.design.composite.ProductComposite;
import com.kyou.net.design.pojo.ProductItem;
import com.kyou.net.design.repo.ProductItemRepository;
import com.kyou.net.design.util.RedisCommonProcessor;
import com.kyou.net.design.visitor.AddItemVisitor;
import com.kyou.net.design.visitor.DelItemVisitor;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductItemService {

    @Resource
    private RedisCommonProcessor redisCommonProcessor;

    @Resource
    private ProductItemRepository productItemRepository;

    @Resource
    private AddItemVisitor addItemVisitor;

    @Resource
    private DelItemVisitor delItemVisitor;

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

    public ProductComposite addItems(ProductComposite item){
        productItemRepository.addItem(item.getName(),item.getPid());
        //通过访问者模式访问树形结构，并添加新的商品类目功能
        ProductComposite addItem = ProductComposite.builder().id(productItemRepository.findByNameAndPid(item.getName(), item.getPid()).getId())
                .name(item.getName())
                .pid(item.getPid())
                .child(new ArrayList<>())
                .build();

        AbstractProductItem updatedItems = addItemVisitor.visitor(addItem);
        //再更新缓存
        //此处可做重试逻辑
        redisCommonProcessor.set("items",updatedItems);
        return (ProductComposite) updatedItems;
    }

    public ProductComposite delItems(ProductComposite item){
        productItemRepository.delItem(item.getId());
        //通过访问者模式访问树形结构，并删除商品类目功能
        ProductComposite delItem = ProductComposite.builder().id(item.getId())
                .name(item.getName())
                .pid(item.getPid())
                .child(new ArrayList<>())
                .build();

        AbstractProductItem updatedItems = delItemVisitor.visitor(delItem);
        //再更新缓存
        redisCommonProcessor.set("items",updatedItems);
        return (ProductComposite) updatedItems;
    }
    /**
     * 以上代码未涉及补偿机制及极端场景，仅做示例
     * 至少要引入MQ进行失败重试，及引入邮件进行人工介入
     */
}
