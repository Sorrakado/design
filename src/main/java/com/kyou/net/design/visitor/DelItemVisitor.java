package com.kyou.net.design.visitor;

import com.kyou.net.design.composite.AbstractProductItem;
import com.kyou.net.design.composite.ProductComposite;
import com.kyou.net.design.util.RedisCommonProcessor;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class DelItemVisitor implements ItemVisitor<AbstractProductItem>{

    @Resource
    private RedisCommonProcessor redisCommonProcessor;
    @Override
    public AbstractProductItem visitor(AbstractProductItem productItem) {

        ProductComposite currentItem = (ProductComposite) redisCommonProcessor.get("items");

        ProductComposite delItem = (ProductComposite) productItem;
        if(delItem.getId() == currentItem.getId()){
            throw new UnsupportedOperationException("Root Node cannot be deleted!");
        }
        //如果被删除节点的父节点为当前节点，则直接删除
        if(delItem.getPid() == currentItem.getId()){
            currentItem.removeProductItem(delItem);
            return currentItem;
        }
        delChild(delItem,currentItem);
        return currentItem;
    }

    private void delChild(ProductComposite productItem,ProductComposite currentItem){
        for(AbstractProductItem abstractProductItem: currentItem.getChild()){
            ProductComposite item = (ProductComposite) abstractProductItem;

            if(item.getId() == productItem.getPid()){
                item.removeProductItem(productItem);
            }else{
                delChild(productItem,item);
            }
        }
    }
}
