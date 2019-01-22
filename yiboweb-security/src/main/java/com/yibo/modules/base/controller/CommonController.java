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
{  2018-08-01  高云        新建
{
{  ---------------------------------------------------------------------------
{  注：本模块代码由医博代码生成工具辅助生成
{*****************************************************************************
*/

package com.yibo.modules.base.controller;

import cn.yibo.common.utils.PinyinUtils;
import com.yibo.modules.base.config.annotation.IgnoredLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  描述: 公共控制器层
 *  作者：高云
 *  邮箱: gogo163gao@163.com
 *  时间: 2018-09-16
 *  版本: v1.0
 */
@RestController
@RequestMapping("/common")
@Api(tags = "9000.系统公用接口")
public class CommonController {
    /**
     * 获取汉字拼音首字母
     * @return
     */
    @IgnoredLog
    @ApiOperation("获取汉字拼音首字母")
    @ApiImplicitParam(name = "name", value = "中文汉字", paramType = "query", required = true, dataType = "String")
    @GetMapping("/first-pinyin-code")
    public String getChineseFirstLetter(String name){
        return PinyinUtils.getFirstLettersUp(name);
    }

}
