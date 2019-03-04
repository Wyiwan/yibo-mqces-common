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
{  注：本模块代码为底层基础框架封装的boot包
{*****************************************************************************
*/

package cn.yibo.boot.config.security;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.yibo.boot.common.constant.LoginFailEnum;
import cn.yibo.boot.common.exception.LoginFailLimitException;
import cn.yibo.boot.common.utils.LogUtils;
import cn.yibo.boot.modules.base.entity.Log;
import cn.yibo.boot.modules.base.entity.Role;
import cn.yibo.boot.modules.base.entity.User;
import cn.yibo.boot.modules.base.service.PermissionService;
import cn.yibo.boot.modules.base.service.UserService;
import cn.yibo.common.utils.ListUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 *  描述: 一句话描述该类的用途
 *  作者：高云
 *  邮箱: gogo163gao@163.com
 *  时间: 2018-09-16
 *  版本: v1.0
 */
@Slf4j
@Component
public class UserDetailsServiceImpl implements UserDetailsService{
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private PermissionService permissionService;

    @Value("${webapp.allow-multi-identity}")
    private Boolean allowMultiIdentity;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String flagKey = "loginFailFlag:"+username;
        String value = redisTemplate.opsForValue().get(flagKey);
        Long timeRest = redisTemplate.getExpire(flagKey, TimeUnit.MINUTES);

        // 超过限制次数
        if( StrUtil.isNotBlank(value) ){
            throw new LoginFailLimitException("登录错误次数超过限制，请"+timeRest+"分钟后再试");
        }

        User user = userService.findByUsername(username);
        if( user == null ){
            throw new UsernameNotFoundException(LoginFailEnum.INCORRECT_ERROR.getDesc());
        }
        return new SecurityUserDetails(user);
    }

    /**
     * 根据登录名查询用户对象
     * @param username
     * @param roleId
     * @return
     * @throws UsernameNotFoundException
     */
    public UserDetails loadUserByUsername(String username, String roleId) throws UsernameNotFoundException {
        // 缓存获取用户对象(引用对象)
        User user = userService.findByUsername(username);
        if( user == null ){
            throw new UsernameNotFoundException(LoginFailEnum.INCORRECT_ERROR.getDesc());
        }

        // 单身份使用系统
        if( !allowMultiIdentity && ListUtils.isNotEmpty(user.getRoles()) ){
            // 切换身份
            if( !StrUtil.isEmptyOrUndefined(roleId) ){
                List<String> condition = Lists.newArrayList(roleId);
                List<Role> resultList = user.getRoles().stream().filter((Role role) -> condition.contains((role.getId()))).collect(Collectors.toList());

                if( ListUtils.isNotEmpty(resultList) ){
                    user.setRole(resultList.get(0));
                    user.setPermissions(permissionService.findByRoleId(roleId));
                }
            // 默认身份
            }else{
                Role dftRole = user.getRoles().get(0);
                user.setRole(dftRole);
                user.setPermissions(permissionService.findByRoleId(dftRole.getId()));
            }
        }
        return new SecurityUserDetails(user);
    }

    public void saveLoginInfo(HttpServletRequest request, User user){
        Map<String, Object> entityMap = MapUtil.newHashMap();
        if( user.getFirstVisitDate() == null ){
            entityMap.put("firstVisitDate", new Date());
        }
        entityMap.put("id", user.getId());
        entityMap.put("lastVisitDate", new Date());
        userService.updateMap(entityMap);

        LogUtils.saveLog(user, request, "系统登录", Log.TYPE_LOGIN);
    }
}
