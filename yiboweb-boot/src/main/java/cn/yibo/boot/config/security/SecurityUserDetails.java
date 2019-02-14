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

package cn.yibo.boot.config.security;

import cn.hutool.core.collection.CollUtil;
import cn.yibo.boot.common.constant.CommonConstant;
import cn.yibo.boot.modules.base.entity.Permission;
import cn.yibo.boot.modules.base.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *  描述: 一句话描述该类的用途
 *  作者：高云
 *  邮箱: gogo163gao@163.com
 *  时间: 2018-09-16
 *  版本: v1.0
 */
@Slf4j
public class SecurityUserDetails extends User implements UserDetails{

    public SecurityUserDetails(User user){
        if( user != null ) {
            this.setId(user.getId());
            this.setUsername(user.getUsername());
            this.setName(user.getName());
            this.setPassword(user.getPassword());
            this.setStatus(user.getStatus());

            this.setTenantId(user.getTenantId());
            this.setOfficeName(user.getOfficeName());
            this.setDeptId(user.getDeptId());
            this.setDeptName(user.getDeptName());

            this.setMgrType(user.getMgrType());
            this.setUserWeight(user.getUserWeight());

            this.setEmpCode(user.getEmpCode());
            this.setEmpStatus(user.getEmpStatus());
            this.setSex(user.getSex());
            this.setAvatar(user.getAvatar());
            this.setFirstVisitDate(user.getFirstVisitDate());
            this.setLastVisitDate(user.getLastVisitDate());

            this.setDept(user.getDept());
            this.setRoles(user.getRoles());
            this.setMenuPermissions(user.getMenuPermissions());
            this.setOperPermissions(user.getOperPermissions());
            this.setPermissions(user.getPermissions());
        }
    }

    /**
     * 获取用户拥有的权限
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        List<GrantedAuthority> authorityList = new ArrayList<>();

        // 添加请求权限
        List<Permission> permissions = this.getOperPermissions();
        if( !CollUtil.isEmpty(permissions) ){
            for(int i = 0; i < permissions.size(); i++){
                authorityList.add(new SimpleGrantedAuthority(permissions.get(i).getPermsName()));
            }
        }
        // log.info("添加的请求权限：\n" + authorityList);
        return authorityList;
    }

    /**
     * 账户是否过期
     * @return
     */
    @Override
    public boolean isAccountNonExpired(){
        return true;
    }

    /**
     * 是否锁定
     * @return
     */
    @Override
    public boolean isAccountNonLocked(){
        return CommonConstant.STATUS_DISABLE.equals(this.getStatus()) ? false : true;
    }

    /**
     * 密码是否过期
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }

    /**
     * 是否启用
     * @return
     */
    @Override
    public boolean isEnabled(){
        return CommonConstant.STATUS_NORMAL.equals(this.getStatus()) ? true : false;
    }

}