/*
 * Copyright (c) 2018-2028 广州医博信息技术有限公司 All Rights Reserved.
 * ProjectName：yiboweb-framework
 * FileName:：SecurityUserDetails.java
 * @author:：gogo163gao@163.com
 * @version：1.0
 */

package cn.yibo.security;

import cn.yibo.common.collect.ListUtils;
import cn.yibo.common.lang.StringUtils;
import cn.yibo.security.constant.CommonConstant;
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
public class SecurityUserDetails extends User implements UserDetails {
    public SecurityUserDetails(User user){
        if( user != null ) {
            this.setId(user.getId());
            this.setUsername(user.getUsername());
            this.setPassword(user.getPassword());
            this.setStatus(user.getStatus());
            this.setDeptId(user.getDeptId());
            this.setRoles(user.getRoles());
            this.setPermissions(user.getPermissions());
        }
    }

    /**
     * 添加用户拥有的权限和角色
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        List<GrantedAuthority> authorityList = new ArrayList<>();
        List<Permission> permissions = this.getPermissions();

        // 添加请求权限
        if( permissions != null && permissions.size() > 0 ){
            for( Permission permission : permissions ){
                if( CommonConstant.PERMISSION_OPERATION.equals(permission.getType() )
                        &&StringUtils.isNotBlank(permission.getTitle())
                        &&StringUtils.isNotBlank(permission.getPath()) ){
                    authorityList.add(new SimpleGrantedAuthority(permission.getTitle()));
                }
            }
        }

        // 添加角色
        List<Role> roles = this.getRoles();
        if( !ListUtils.isEmpty(roles) ){
            roles.forEach(item -> {
                if(StringUtils.isNotBlank(item.getName())){
                    authorityList.add(new SimpleGrantedAuthority(item.getName()));
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