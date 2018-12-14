/*
{*****************************************************************************
{  广州医博-基础框架 v1.0
{  版权信息 (c) 2018-2020 广州医博信息技术有限公司. 保留所有权利.
{  创建人：  高云
{  审查人：
{  模块：安全控制模块
{  功能描述:
{
{  ---------------------------------------------------------------------------
{  维护历史:
{  日期        维护人        维护类型
{  ---------------------------------------------------------------------------
{  2018-08-01  高云        新建
{
{  ---------------------------------------------------------------------------
{  注：本模块代码为底层基础框架封装的security包
{*****************************************************************************
*/

package cn.yibo.security.constant;

/**
 *  描述: 公共常量
 *  作者：高云
 *  邮箱: gogo163gao@163.com
 *  时间: 2018-09-16
 *  版本: v1.0
 */
public interface CommonConstant {
    /**
     * 用户缓存名称
     */
    String USER_CACHE = "userCache";

    /**
     * 超级管理员默认账号
     */
    String SUPER_ADMIN_ACCOUNT = "system";

    /**
     * 超级管理员菜单权重
     */
    Integer SUPER_ADMIN_PERMS_WEIGHT = 80;

    /**
     * 系统管理员菜单权重
     */
    Integer ADMIN_PERMS_WEIGHT = 60;

    /**
     *  普通用户菜单权重
     */
    Integer USER_PERMS_WEIGHT = 20;

    /**
     * 普通用户
     */
    String USER_TYPE_NORMAL = "0";

    /**
     * 管理员
     */
    String USER_TYPE_ADMIN = "1";

    /**
     * 页面类型-权限
     */
    String PERMISSION_PAGE = "0";

    /**
     * 操作类型-权限
     */
    String PERMISSION_OPERATION = "1";

    /**
     * 科室编码生成位数
     */
    Integer DEPT_CODE_NUM = 6;

    /**
     * 机构编码生成位数
     */
    Integer OFFICE_CODE_NUM = 8;

    /**
     * 正常状态
     */
    Integer STATUS_NORMAL = 1;

    /**
     * 禁用状态
     */
    Integer STATUS_DISABLE = 2;

    /**
     * 删除标志
     */
    Integer STATUS_DEL = 0;

}
