package com.imooc.web.util;

import org.apache.curator.framework.CuratorFramework;

/**
 * Created by xyzzg on 2018/8/17.
 */
public class ZKCurator {

    //zk客户端
    private CuratorFramework client = null;

    public ZKCurator(CuratorFramework client){
        this.client = client;
    }
}
