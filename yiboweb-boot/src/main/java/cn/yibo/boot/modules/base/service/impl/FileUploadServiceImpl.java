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

package cn.yibo.boot.modules.base.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.yibo.boot.common.constant.LoginFailEnum;
import cn.yibo.boot.config.security.context.UserContext;
import cn.yibo.boot.modules.base.dao.FileUploadDao;
import cn.yibo.boot.modules.base.entity.FileUpload;
import cn.yibo.boot.modules.base.service.FileUploadService;
import cn.yibo.common.base.service.impl.AbstractBaseService;
import cn.yibo.common.utils.ListUtils;
import cn.yibo.core.web.exception.BizException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 文件信息上传表服务实现层
 * @author 高云
 * @since 2019-03-11
 * @version v1.0
 */
@Service
@Transactional(readOnly=true)
public class FileUploadServiceImpl extends AbstractBaseService<FileUploadDao, FileUpload> implements FileUploadService {
    /**
     * 重写删除
     * @param list
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public void deleteByIds(List list){
        Map<String, Object> condition = MapUtil.newHashMap();
        condition.put("ids", list);
        List<FileUpload> fileUploadList = dao.queryList(condition, null, null);

        if( !ListUtils.isEmpty(fileUploadList) ){
            for( FileUpload fileUpload : fileUploadList ){
                if( !UserContext.getUser().isSuperAdmin() && !StrUtil.equals(UserContext.getUser().getId(), fileUpload.getCreateBy())  ){
                    throw new BizException(LoginFailEnum.UNDECLARED_ERROR.getCode(), "抱歉，您没有权限操作");
                }
            }
        }
        super.deleteByIds(list);
    }
}