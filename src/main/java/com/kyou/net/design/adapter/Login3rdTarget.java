package com.kyou.net.design.adapter;

import java.io.IOException;

public interface Login3rdTarget {
    public String loginByGitee(String code,String state) throws IOException;
}
