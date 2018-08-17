package com.imooc.controller;

import com.imooc.enums.VideoStatusEnum;
import com.imooc.pojo.Bgm;
import com.imooc.service.VideoService;
import com.imooc.utils.IMoocJSONResult;
import com.imooc.utils.PagedResult;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Bgm自动下载：
 * 1.Springboot开放接口，ssm调用让其下载（耦合度高）
 * 2.使用MQ消息队列，生产者（SSM）调用消费者（SpringBoot）
 * 3.使用Zookeeper，Springboot监听并且下载。集群
 *
 * Zookeeper:
 * 中间件，提供协调服务。
 * 作用于分布式服务，可为大数据服务
 *
 * Apache curator:Zookeeper客户端
 * 创建重试策略 -retryPolicy
 * 创建客户端 -client
 * 初始化客户端
 *
 */
@Controller
@RequestMapping("/video")
public class VideoController {

	@Autowired
	private VideoService videoService;

    @GetMapping("/showReportList")
    public String showReportList() {
        return "video/reportList";
    }

    @PostMapping("/reportList")
    @ResponseBody
    public PagedResult reportList(Integer page) {

        PagedResult result = videoService.queryReportList(page, 10);
        return result;
    }

    @PostMapping("/forbidVideo")
    @ResponseBody
    public IMoocJSONResult forbidVideo(String videoId) {

        videoService.updateVideoStatus(videoId, VideoStatusEnum.FORBID.value);
        return IMoocJSONResult.ok();
    }

    @GetMapping("/showBgmList")
    public String showBgmList() {
        return "video/bgmList";
    }

    @PostMapping("/queryBgmList")
    @ResponseBody
    //这里需要直接返回PagedResult（jqGrid框架认识）
    public PagedResult queryBgmList(Integer page) {
        return videoService.queryBgmList(page,10);
    }

    @GetMapping("/showAddBgm")
    public String login() {
        return "video/addBgm";
    }

    @PostMapping("/addBgm")
    @ResponseBody
    public IMoocJSONResult addBgm(Bgm bgm) {

        videoService.addBgm(bgm);
        return IMoocJSONResult.ok();
    }

    @PostMapping("/delBgm")
    @ResponseBody
    public IMoocJSONResult delBgm(String bgmId) {
        videoService.deleteBgm(bgmId);
        return IMoocJSONResult.ok();
    }

    @PostMapping("/bgmUpload")
    @ResponseBody
    public IMoocJSONResult bgmUpload(@RequestParam("file") MultipartFile[] files) throws Exception {

        // 文件保存的命名空间
//		String fileSpace = File.separator + "imooc_videos_dev" + File.separator + "mvc-bgm";
        String fileSpace = "C:" + File.separator + "imooc_videos_dev" + File.separator + "mvc-bgm";
        // 保存到数据库中的相对路径
        String uploadPathDB = File.separator + "bgm";

        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        try {
            if (files != null && files.length > 0) {

                String fileName = files[0].getOriginalFilename();
                if (StringUtils.isNotBlank(fileName)) {
                    // 文件上传的最终保存路径
                    String finalPath = fileSpace + uploadPathDB + File.separator + fileName;
                    // 设置数据库保存的路径
                    uploadPathDB += (File.separator + fileName);

                    File outFile = new File(finalPath);
                    if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
                        // 创建父文件夹
                        outFile.getParentFile().mkdirs();
                    }

                    fileOutputStream = new FileOutputStream(outFile);
                    inputStream = files[0].getInputStream();
                    IOUtils.copy(inputStream, fileOutputStream);
                }

            } else {
                return IMoocJSONResult.errorMsg("上传出错...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return IMoocJSONResult.errorMsg("上传出错...");
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        }

        return IMoocJSONResult.ok(uploadPathDB);
    }



}
