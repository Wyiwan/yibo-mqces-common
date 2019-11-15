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
{  2018-12-10  高云        新建
{
{  ---------------------------------------------------------------------------
{  注：本模块代码为底层基础框架封装的boot包
{*****************************************************************************
*/

package cn.yibo.boot.common.constant;

/**
 *  描述: 公共常量
 *  作者：高云
 *  邮箱: gogo163gao@163.com
 *  时间: 2018-12-17
 *  版本: v1.0
 */
public interface CommonConstant {
    /**
     * 超级管理员默认账号
     */
    String SUPER_ADMIN_ACCOUNT = "system";

    //------------------------------------------------------------------------------------------------------------------
    // @菜单相关默认配置
    //------------------------------------------------------------------------------------------------------------------
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
     * 菜单权限类型：页面菜单
     */
    String PERMISSION_PAGE = "0";

    /**
     * 菜单权限类型：操作按钮
     */
    String PERMISSION_OPERATION = "1";

    //------------------------------------------------------------------------------------------------------------------
    // @用户相关默认配置
    //------------------------------------------------------------------------------------------------------------------
    /**
     * 用户初始化密码
     */
    String USER_INIT_PASSWORD = "123456";

    /**
     * 用户类型：普通用户
     */
    String USER_TYPE_NORMAL = "normal";

    /**
     * 用户类型：专家
     */
    String USER_TYPE_EXPERT = "expert";

    /**
     * 管理员类型：非管理员
     */
    String USER_MGR_TYPE_NORMAL = "0";

    /**
     * 管理员类型：管理员
     */
    String USER_MGR_TYPE_ADMIN = "1";

    //------------------------------------------------------------------------------------------------------------------
    // @消息推送默认配置
    //------------------------------------------------------------------------------------------------------------------
    /**
     * 消息发送范围(全部用户)
     */
    Integer MESSAGE_RANGE_ALL = 0;

    /**
     * 消息发送范围(指定机构)
     */
    Integer MESSAGE_RANGE_ORGAN = 1;

    /**
     * 未读
     */
    Integer MESSAGE_STATUS_UNREAD = 3;

    /**
     * 已读
     */
    Integer MESSAGE_STATUS_READ = 4;

    //------------------------------------------------------------------------------------------------------------------
    // @其他默认配置
    //------------------------------------------------------------------------------------------------------------------
    /**
     * 否
     */
    String NO = "0";

    /**
     * 是
     */
    String YES = "1";

    /**
     * 科室编码位数
     */
    Integer DEPT_CODE_NUM = 6;

    /**
     * 机构编码位数
     */
    Integer ORGAN_CODE_NUM = 8;

}
