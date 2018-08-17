package com.imooc.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.mapper.BgmMapper;
import com.imooc.pojo.Bgm;
import com.imooc.pojo.BgmExample;
import com.imooc.service.VideoService;
import com.imooc.utils.PagedResult;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.ws.Action;
import java.util.List;

/**
 * Created by xyzzg on 2018/8/17.
 */
public class VideoServiceImpl implements VideoService{

    @Autowired
    private BgmMapper bgmMapper;
    @Autowired
    private Sid sid;

    @Override
    public void addBgm(Bgm bgm) {
        String bgmId = sid.nextShort();
        bgm.setId(bgmId);
        bgmMapper.insertSelective(bgm);
    }

    @Override
    public PagedResult queryBgmList(Integer page,Integer pageSize) {

        PageHelper.startPage(page,pageSize);

        BgmExample bgmExample = new BgmExample();
        List<Bgm> list = bgmMapper.selectByExample(bgmExample);

        PageInfo<Bgm> pageInfo = new PageInfo <>(list);

        PagedResult pagedResult = new PagedResult();
        // 总页数
        pagedResult.setTotal(pageInfo.getPages());
        // 每行显示的内容
        pagedResult.setRows(list);
        // 当前页数
        pagedResult.setPage(page);
        // 总记录数
        pagedResult.setRecords(pageInfo.getTotal());

        return pagedResult;
    }

    @Override
    public void deleteBgm(String id) {
        bgmMapper.deleteByPrimaryKey(id);
    }
}
