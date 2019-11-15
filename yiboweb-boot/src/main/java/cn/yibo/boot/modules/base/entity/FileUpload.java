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

package cn.yibo.boot.modules.base.entity;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.yibo.boot.base.entity.DataEntity;
import cn.yibo.common.utils.PropertiesUtils;
import cn.yibo.common.utils.ServletUtils;
import cn.yibo.common.utils.StringUtils;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

/**
 * 文件信息上传表实体类(sys_file_upload)
 * @author 高云
 * @since 2019-03-11
 * @version v1.0
 */
@Data
@ApiModel(value = "文件信息上传表实体类")
public class FileUpload extends DataEntity<String>{
    @NotEmpty(message="文件名称不能为空")
    @ApiModelProperty(value = "文件名称")
    private String fileName;
    
    @ApiModelProperty(value = "文件分类")
    private String fileType;

    @ApiModelProperty(value = "文件内容类型")
    private String fileContentType;
    
    @NotEmpty(message="文件相对路径不能为空")
    @ApiModelProperty(value = "文件相对路径")
    private String filePath;
    
    @ApiModelProperty(value = "文件后缀扩展名")
    private String fileExtension;
    
    @ApiModelProperty(value = "文件大小(单位B)")
    private Long fileSize;
    
    @ApiModelProperty(value = "业务主键")
    private String bizKey;
    
    @ApiModelProperty(value = "业务类型")
    private String bizType;

    /**
     * 获取扩展名
     * @param fileName
     * @return
     */
    @JsonIgnore
    @JSONField(serialize = false)
    public static String getExtension(String fileName){
        return fileName != null && fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != fileName.length() - 1 ? (fileName.substring(fileName.lastIndexOf(".") + 1)).toLowerCase() : null;
    }

    /**
     * 获取相对路径
     * @return
     */
    @JsonIgnore
    @JSONField(serialize = false)
    public static String getRelativePath(){
        String uploadPath = PropertiesUtils.getInstance().getProperty("webapp.file.uploadPath");
        String[] tempArr = StringUtils.subBetween(uploadPath, "{", "}");
        if( tempArr != null ){
            for( int i = 0; i< tempArr.length; i++ ){
                uploadPath = StringUtils.replace(uploadPath, "{"+tempArr[i]+"}", DateUtil.format(new Date(), tempArr[i]));
            }
        }
        return uploadPath;
    }

    /**
     * 获取绝对路径
     * @return
     */
    @JsonIgnore
    @JSONField(serialize = false)
    public String getAbsolutePath(){
        return getUserfilesBaseDir(new StringBuilder().append(this.filePath).append(this.id+"."+this.fileExtension).toString());
    }

    @JsonIgnore
    @JSONField(serialize = false)
    public static String getUserfilesBaseDir(String path){
        String baseDir = PropertiesUtils.getInstance().getProperty("webapp.file.baseDir");

        if( StringUtils.isBlank(baseDir) ){
            baseDir = FileUtil.getWebRoot().getParentFile().getAbsolutePath();
        }

        if( StringUtils.isBlank(baseDir) ){
            baseDir = FileUtil.getUserHomePath();
        }
        return path != null ? FileUtil.normalize((new StringBuilder()).insert(0, baseDir).append("/userfiles/").append(path).toString()) : FileUtil.normalize(baseDir);
    }

}