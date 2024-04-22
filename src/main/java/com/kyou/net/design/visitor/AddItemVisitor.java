package com.kyou.net.design.visitor;

import com.kyou.net.design.composite.AbstractProductItem;
import com.kyou.net.design.composite.ProductComposite;
import com.kyou.net.design.util.RedisCommonProcessor;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class AddItemVisitor implements ItemVisitor<AbstractProductItem> {

    @Resource
    private RedisCommonProcessor redisCommonProcessor;

    @Override
    public AbstractProductItem visitor(AbstractProductItem productItem) {
        ProductComposite currentItem = (ProductComposite) redisCommonProcessor.get("items");

        ProductComposite addItem = (ProductComposite) productItem;

        //如果新增节点的父节点为当前节点
        if(addItem.getPid() == currentItem.getId()){
            currentItem.addProductItem(addItem);
            return currentItem;
        }
        //递归寻找新类目的插入点
        addChild(addItem,currentItem);
        return currentItem;
    }

    /**
     * 递归寻找新类目的插入点
     * @param addItem
     * @param currentItem
     */
    private void addChild(ProductComposite addItem,ProductComposite currentItem){
        for(AbstractProductItem abstractProductItem : currentItem.getChild()){
            ProductComposite item = (ProductComposite) abstractProductItem;
            if(item.getId() == addItem.getPid()){
                item.addProductItem(addItem);
            }else{
                addChild(addItem,item);
            }
        }
    }
}
