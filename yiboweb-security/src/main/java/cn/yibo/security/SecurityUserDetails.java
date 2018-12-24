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

package cn.yibo.security;

import cn.yibo.common.collect.ListUtils;
import cn.yibo.common.lang.StringUtils;
import com.yibo.modules.base.constant.CommonConstant;
import com.yibo.modules.base.entity.Permission;
import com.yibo.modules.base.entity.Role;
import com.yibo.modules.base.entity.User;
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
            this.setPassword(user.getPassword());
            this.setStatus(user.getStatus());
            this.setDeptId(user.getDeptId());
            this.setTenantId(user.getTenantId());
            this.setUserWeight(user.getUserWeight());
            this.setRoles(user.getRoles());
            this.setPermissions(user.getPermissions());
        }
    }

    /**
     * 获取用户拥有的权限和角色
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        List<GrantedAuthority> authorityList = new ArrayList<>();

        // 添加请求权限
        List<Permission> permissions = this.getPermissions();
        if( !ListUtils.isEmpty(permissions) ){
            permissions.forEach(item -> {
                String permsUrl = item.getPermsUrl();
                String permsName = item.getPermsName();
                String permsType = item.getPermsType();

                // 如果是操作权限
                if( CommonConstant.PERMISSION_OPERATION.equals(permsType) && StringUtils.isNotBlank(permsName) && StringUtils.isNotBlank(permsUrl) ){
                    authorityList.add(new SimpleGrantedAuthority(permsName));
                }
            });
        }

        // 添加角色
        List<Role> roles = this.getRoles();
        if( !ListUtils.isEmpty(roles) ){
            roles.forEach(item -> {
                String roleName = item.getRoleName();

                if( StringUtils.isNotBlank(roleName) ){
                    authorityList.add(new SimpleGrantedAuthority(roleName));
                }
            });
        }
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