package com.kyou.net.design.config;

import com.kyou.net.design.bridge.AbstractRegisterLoginComponent;
import com.kyou.net.design.bridge.RegisterLoginComponent;
import com.kyou.net.design.bridge.RegisterLoginFuncInterface;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RegisterLoginComponentFactory {
    /**
     * 缓存左路对象
     */
    public static Map<String, AbstractRegisterLoginComponent> componentMap = new ConcurrentHashMap<>();

    public static Map<String, RegisterLoginFuncInterface> funcMap = new ConcurrentHashMap<>();

    public static AbstractRegisterLoginComponent getComponent(String type){
        AbstractRegisterLoginComponent component = componentMap.get(type);
        if(component == null){
            synchronized (componentMap){
                component = componentMap.get(type);
                if(component == null){
                    component = new RegisterLoginComponent(funcMap.get(type));
                    componentMap.put(type, component);
                }
            }
        }
        return component;
    }
}
