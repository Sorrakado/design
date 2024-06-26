package com.kyou.net.design.controller;

import com.kyou.net.design.composite.ProductComposite;
import com.kyou.net.design.service.ProductItemService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductItemController {

    @Resource
    ProductItemService productItemService;

    @PostMapping("/fetchAllItems")
    public ProductComposite fetchAllItems(){
        return productItemService.fetchAllItems();
    }

    @PostMapping("/addItems")
    public ProductComposite addItems(@RequestBody ProductComposite item){
        return productItemService.addItems(item);
    }

    @PostMapping("/delItems")
    public ProductComposite delItems(@RequestBody ProductComposite item){
        return productItemService.delItems(item);
    }
}
