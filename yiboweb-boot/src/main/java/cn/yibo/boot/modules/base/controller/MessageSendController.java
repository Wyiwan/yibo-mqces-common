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
{  2019-02-20  高云        新建	
{ 	                                                                     
{  ---------------------------------------------------------------------------
{  注：本模块代码由医博代码生成工具辅助生成
{*****************************************************************************	
*/

package cn.yibo.boot.modules.base.controller;

import cn.yibo.boot.modules.base.entity.MessageSend;
import cn.yibo.boot.modules.base.service.MessageSendService;
import cn.yibo.common.base.controller.BaseController;
import cn.yibo.common.base.controller.BaseForm;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * 消息已推送表控制器层
 * @author 高云
 * @since 2019-02-20
 * @version v1.0
 */
@RestController
@RequestMapping("/api/message-send")
@Api(tags = "90091.已推送消息管理")
public class MessageSendController extends BaseController {
    @Autowired
    MessageSendService messageSendService;

    /**
     * 分页查询
     * @return
     */
    @ApiOperation("分页查询")
    @GetMapping("/paged")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "pageNum", value = "当前页", paramType = "query", dataType = "Number"),
            @ApiImplicitParam(name = "pageSize", value = "页大小", paramType = "query", dataType = "Number"),
            @ApiImplicitParam(name = "username", value = "发送用户", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "status", value = "状态", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "organId", value = "机构ID", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "messageId", value = "消息ID", paramType = "query", dataType = "String", required = true)
    })
    protected PageInfo<MessageSend> paged(){
        return messageSendService.queryPage(new BaseForm<MessageSend>());
    }

    /**
     * 删除
     * @param ids
     * @return
     */
    @ApiOperation("删除")
    @ApiImplicitParam(name = "ids", value = "标识ID(多个以逗号隔开)", paramType = "query", required = true, dataType = "String")
    @PostMapping("/deleted")
    protected String deleted(@RequestBody String ids){
        messageSendService.deleteByIds( Arrays.asList(ids.split(",")) );
        return DELETE_SUCCEED;
    }
}