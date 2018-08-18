package com.imooc.service.util;


import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xyzzg on 2018/8/17.
 */
public class ZKCurator {

    //zk客户端
    private CuratorFramework client = null;
    private final static Logger log = LoggerFactory.getLogger(ZKCurator.class);

    public ZKCurator(CuratorFramework client){
        this.client = client;
    }

    //初始化
    public void init(){
        //命名空间
        client = client.usingNamespace("admin");

        try {
            // 判断在admin空间下是否有bgm节点
            if (client.checkExists().forPath("/bgm") == null){
                /**
                 * 对于zk来讲，两种类型节点：
                 * 1.持久节点：创建后一直存在，除非手动删除
                 * 2.临时节点：创建一个结点，会话断开，自动删除，也可手动删除
                 */
                client.create().creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT)
                        .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                        .forPath("/bgm");
                log.info("zookeeper初始化成功");

                log.info("zookeeper服务器状态:{0}",client.isStarted());
            }
        } catch (Exception e) {
            log.error("zookeeper客户端链接，初始化错误。。。");
            e.printStackTrace();
        }
    }

    //增加删除Bgm，向zk-server创建子节点，供小程序后端监听
    public void sendBgmOperator(String bgmId, String operType){

        try{
            client.create().creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT)
                    .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                    .forPath("/bgm/"+bgmId,operType.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
