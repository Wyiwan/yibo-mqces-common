/*
{*****************************************************************************
{  广州医博-基础框架 v1.0													
{  版权信息 (c) 2018-2020 广州医博信息技术有限公司. 保留所有权利.					
{  创建人：  高云
{  审查人：
{  模块：系统管理模块										
{  功能描述:										
{		 													
{  ---------------------------------------------------------------------------	
{  维护历史:													
{  日期        维护人        维护类型						
{  ---------------------------------------------------------------------------	
{  2019-03-11  高云        新建
{ 	                                                                     
{  ---------------------------------------------------------------------------
{  注：本模块代码由医博代码生成工具辅助生成
{*****************************************************************************	
*/

package cn.yibo.boot.modules.base.controller;

import cn.hutool.core.io.FileUtil;
import cn.yibo.boot.common.utils.FileUploadUtils;
import cn.yibo.boot.modules.base.entity.FileUpload;
import cn.yibo.boot.modules.base.service.FileUploadService;
import cn.yibo.common.base.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * 文件上传控制器层
 * @author 高云
 * @since 2019-03-11
 * @version v1.0
 */
@RestController
@RequestMapping("/api/file")
@Api(tags = "9011.文件上传管理")
public class FileUploadController extends BaseController {
    @Autowired
    FileUploadService fileUploadService;

    /**
     * 文件上传
     * 上传文件大小统一在配置文件配置
     * 上传文件类型限制后续优化...
     * @param request
     * @return
     * @throws Exception
     */
    @ApiOperation("文件上传")
    @PostMapping({"upload"})
    public String upload(HttpServletRequest request) throws Exception{
        FileUpload fileUpload = new FileUpload();
        if( request instanceof MultipartHttpServletRequest ){
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)request;

            if( multipartRequest.getFileNames().hasNext() ){
                MultipartFile file = multipartRequest.getFile(multipartRequest.getFileNames().next());

                if( file != null && !file.isEmpty() && file.getOriginalFilename() != null ){
                    fileUpload.setFileName(file.getOriginalFilename());
                    fileUpload.setFileSize(file.getSize());
                    fileUpload.setFileContentType(file.getContentType());
                    fileUpload.setFilePath( FileUtil.normalize(FileUpload.getRelativePath()) );
                    fileUpload.setFileExtension( FileUpload.getExtension(file.getOriginalFilename()) );
                    fileUploadService.save(fileUpload);

                    File tmpFile = new File(fileUpload.getAbsolutePath());
                    if( !tmpFile.getParentFile().exists() ){
                        tmpFile.getParentFile().mkdirs();
                    }
                    file.transferTo(tmpFile);
                }
            }
        }
        return fileUpload.getId();
    }

    /**
     * 文件下载
     * @param fileUploadId
     * @param request
     * @param response
     */
    @ApiOperation("文件下载")
    @ApiImplicitParam(name = "fileUploadId", value = "文件ID", paramType = "query", required = true, dataType = "String")
    @GetMapping("/download/{fileUploadId}")
    public void download(@PathVariable("fileUploadId") String fileUploadId, HttpServletRequest request, HttpServletResponse response){
        FileUpload fileUpload = fileUploadService.fetch(fileUploadId);
        if( fileUpload != null ){
            File file = new File(fileUpload.getAbsolutePath());
            if( file.exists() ){
                FileUploadUtils.downFile(file, request, response, fileUpload.getFileName());
            }
        }
    }

    /**
     * 文件列表查询
     * @return
     */
    @ApiOperation("文件列表查询")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "bizKey", value = "业务主键", paramType = "query", required = true, dataType = "String"),
            @ApiImplicitParam(name = "bizType", value = "业务类型", paramType = "query", required = true, dataType = "String")
    })
    @GetMapping("/list")
    public List list() throws Exception{
        return fileUploadService.queryList(getParamMap());
    }

    /**
     * 文件下载
     * @param ids
     * @return
     */
    @ApiOperation("文件删除")
    @ApiImplicitParam(name = "ids", value = "文件ID(多个以逗号隔开)", paramType = "query", required = true, dataType = "String")
    @PostMapping("/deleted")
    protected String deleted(@RequestBody String ids){
        fileUploadService.deleteByIds(Arrays.asList(ids.split(",")));
        return DELETE_SUCCEED;
    }
}