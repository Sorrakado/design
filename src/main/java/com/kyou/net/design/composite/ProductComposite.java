package com.kyou.net.design.composite;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductComposite extends AbstractProductItem implements Serializable {

    private static final long serialVersionUID = 111111L;

    private int id;

    private int pid;

    private String name;

    private List<AbstractProductItem> child = new ArrayList<>();

    @Override
    public void addProductItem(AbstractProductItem productItem) {
        this.child.add(productItem);
    }

    @Override
    public void removeProductItem(AbstractProductItem productItem) {
        ProductComposite removeItem = (ProductComposite) productItem;
        Iterator<AbstractProductItem> iterator = child.iterator();

        while(iterator.hasNext()){
            ProductComposite next = (ProductComposite)iterator.next();

            if(next.getId() == removeItem.getId()){
                iterator.remove();
                break;
            }
        }

    }
}
