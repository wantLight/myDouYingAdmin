package com.imooc.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.mapper.UsersReportMapperCustom;
import com.imooc.mapper.VideosMapper;
import com.imooc.pojo.Videos;
import com.imooc.pojo.vo.Reports;
import org.springframework.stereotype.Service;
import com.imooc.enums.BGMOperatorTypeEnum;
import com.imooc.mapper.BgmMapper;
import com.imooc.pojo.Bgm;
import com.imooc.pojo.BgmExample;
import com.imooc.service.VideoService;
import com.imooc.service.util.ZKCurator;
import com.imooc.utils.PagedResult;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.ws.Action;
import java.util.List;

/**
 * Created by xyzzg on 2018/8/17.
 */
@Service
public class VideoServiceImpl implements VideoService{

    @Autowired
    private BgmMapper bgmMapper;
    @Autowired
    private Sid sid;
    @Autowired
    private VideosMapper videosMapper;
    @Autowired
    private ZKCurator zkCurator;
    @Autowired
    private UsersReportMapperCustom usersReportMapperCustom;

    @Override
    public void addBgm(Bgm bgm) {
        String bgmId = sid.nextShort();
        bgm.setId(bgmId);
        bgmMapper.insertSelective(bgm);

        zkCurator.sendBgmOperator(bgmId,BGMOperatorTypeEnum.ADD.type);


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

        zkCurator.sendBgmOperator(id,BGMOperatorTypeEnum.DELETE.type);
    }

    @Override
    public PagedResult queryReportList(Integer page, Integer pageSize) {

        PageHelper.startPage(page, pageSize);

        List<Reports> reportsList = usersReportMapperCustom.selectAllVideoReport();

        PageInfo<Reports> pageList = new PageInfo<Reports>(reportsList);

        PagedResult grid = new PagedResult();
        grid.setTotal(pageList.getPages());
        grid.setRows(reportsList);
        grid.setPage(page);
        grid.setRecords(pageList.getTotal());

        return grid;
    }

    @Override
    public void updateVideoStatus(String videoId, Integer status) {

        Videos video = new Videos();
        video.setId(videoId);
        video.setStatus(status);
        videosMapper.updateByPrimaryKeySelective(video);
    }


}
