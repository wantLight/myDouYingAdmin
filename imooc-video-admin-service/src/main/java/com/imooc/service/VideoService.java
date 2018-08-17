package com.imooc.service;

import com.imooc.pojo.Bgm;
import com.imooc.utils.PagedResult;

/**
 * Created by xyzzg on 2018/8/17.
 */
public interface VideoService {

    void addBgm(Bgm bgm);

    PagedResult queryBgmList(Integer page,Integer pageSize);

    void deleteBgm(String id);
}
