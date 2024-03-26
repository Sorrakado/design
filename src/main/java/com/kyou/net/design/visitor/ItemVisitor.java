package com.kyou.net.design.visitor;

import com.kyou.net.design.composite.AbstractProductItem;

public interface ItemVisitor<T> {

    T visitor(AbstractProductItem productItem);
}
