package com.kyou.net.design.composite;

public abstract class AbstractProductItem {
    protected void addProductItem(AbstractProductItem item){
        throw new UnsupportedOperationException("Not Support Child Add");
    }
    protected void removeProductItem(AbstractProductItem item){
        throw new UnsupportedOperationException("Not Support Child Remove");
    }
}
