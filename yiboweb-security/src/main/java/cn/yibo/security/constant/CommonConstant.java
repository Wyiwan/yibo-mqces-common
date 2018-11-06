/*
 * Copyright (c) 2018-2028 广州医博信息技术有限公司 All Rights Reserved.
 * ProjectName：yiboweb-framework
 * FileName:：CommonConstant.java
 * @author:：gogo163gao@163.com
 * @version：1.0
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
     * 普通用户
     */
    Integer USER_TYPE_NORMAL = 0;

    /**
     * 管理员
     */
    Integer USER_TYPE_ADMIN = 1;

    /**
     * 页面类型-权限
     */
    Integer PERMISSION_PAGE = 0;

    /**
     * 操作类型-权限
     */
    Integer PERMISSION_OPERATION = 1;

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

    /**
     * 顶级菜单
     */
    String PARENT_ID = "0";

    /**
     * 1级菜单
     */
    Integer LEVEL_ONE = 1;

    /**
     * 2级菜单
     */
    Integer LEVEL_TWO = 2;

    /**
     * 3级菜单
     */
    Integer LEVEL_THREE = 3;

}
