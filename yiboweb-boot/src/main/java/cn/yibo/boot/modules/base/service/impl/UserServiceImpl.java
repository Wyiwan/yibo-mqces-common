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
{  2018-12-03  高云        新建
{
{  ---------------------------------------------------------------------------
{  注：本模块代码由医博代码生成工具辅助生成
{*****************************************************************************
*/

package cn.yibo.boot.modules.base.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.yibo.boot.common.aync.ClearUserCacheThread;
import cn.yibo.boot.common.constant.CommonConstant;
import cn.yibo.boot.common.constant.LoginFailEnum;
import cn.yibo.boot.common.utils.MsgUtils;
import cn.yibo.boot.common.utils.PermUtils;
import cn.yibo.boot.config.security.context.UserContext;
import cn.yibo.boot.modules.base.dao.UserDao;
import cn.yibo.boot.modules.base.entity.*;
import cn.yibo.boot.modules.base.service.DeptService;
import cn.yibo.boot.modules.base.service.OrganService;
import cn.yibo.boot.modules.base.service.RoleService;
import cn.yibo.boot.modules.base.service.UserService;
import cn.yibo.common.base.controller.BaseForm;
import cn.yibo.common.base.entity.TreeBuild;
import cn.yibo.common.base.service.impl.AbstractBaseService;
import cn.yibo.common.utils.ObjectUtils;
import cn.yibo.core.web.exception.BizException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户表服务实现层
 * @author 高云
 * @since 2018-12-03
 * @version v1.0
 */
@Service
@Transactional(readOnly=true)
public class UserServiceImpl extends AbstractBaseService<UserDao, User> implements UserService {
    @Autowired
    private DeptService deptService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private OrganService organService;

    @Autowired
    private PermUtils permUtils;

    @Autowired
    private MsgUtils msgUtils;


    /**
     * 重写新增
     * @param user
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public void insert(User user){
        super.insert(user);
        if( !CollUtil.isEmpty(user.getRoleIdList()) ){
            this.roleAuthorized(user);
        }
        msgUtils.sendMessage(user.getId());
    }

    /**
     * 重写更新
     * 只更新用户基本信息，不更新用户角色
     * @param user
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    @CacheEvict(key = "#user.username")
    public void updateNull(User user){
        super.updateNull(user);
    }

    /**
     * 重写删除
     * @param list
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public void deleteByIds(List list){
        dao.deleteByIdsExt(list);
        clearUsersCacheByUserId(list);
    }

    /**
     * 重写分页查询
     * @param baseForm
     * @param <T>
     * @return
     */
    @Override
    public <T>PageInfo<T> queryPage(BaseForm<T> baseForm){
        // 设置分页参数
        PageHelper.startPage(baseForm.getPageNo(), baseForm.getPageSize());

        // 获取查询参数
        Map<String, Object> params = baseForm.getParameters();

        // 用户管理：查询管理员用户和普通用户的标识
        String mgrType = ObjectUtils.toString(params.get("mgrType"));
        params.put("mgrType", StrUtil.emptyToDefault(mgrType, CommonConstant.USER_MGR_TYPE_NORMAL));

        // 角色管理：查询授权用户的标识
        String queryType = ObjectUtils.toString(params.get("queryType"));
        if( !CommonConstant.USER_MGR_TYPE_ADMIN.equals(mgrType) || StrUtil.isNotBlank(queryType) ){
            params.put("tenantId", UserContext.getUser().getTenantId());
        }
        this.logger.info("分页请求参数："+params);

        List list = dao.queryPageExt(params);
        return new PageInfo<T>(list);
    }

    /**
     * 通过用户名查询
     * @param username
     * @return
     */
    @Override
    public User findByUsername(String username){
        User user = dao.findOne("username", username);

        if( user != null ){
            // 关联机构
            Organ organ = organService.fetch(user.getTenantId());
            if( organ != null ){
                user.setOrganName(organ.getOrganName());
            }
            // 关联科室
            Dept dept = deptService.fetch(user.getDeptId());
            if( dept != null ){
                user.setDeptName(dept.getDeptName());
                user.setDept(dept);
            }
            // 关联角色
            List<Role> roles = roleService.findByUserId(user.getId());
            if( !CollUtil.isEmpty(roles) ){
                user.setRoles(roles);
            }
            // 关联权限
            List<Permission> permissions = permUtils.getUserAllPermissions(user);
            if( !CollUtil.isEmpty(permissions) ){
                user.setPermissions(permissions);
            }
        }
        return user;
    }

    /**
     * 根据ID查询用户信息
     * condition：用户ID[ids]、角色ID[roleIds]、机构ID[organIds]、科室ID[deptIds]
     * @param condition
     * @return
     */
    @Override
    public List<User> findByIds(Map<String, Object> condition){
        return  dao.findByIds(condition);
    }

    /**
     * 根据ID查询用户，会抛出数据校验异常
     * @param id
     * @return
     * @throws BizException
     */
    public User fetched(String id) throws BizException{
        User user = dao.fetch(id);
        if( user != null && !UserContext.getUser().isSuperAdmin() ){
            if( CommonConstant.USER_MGR_TYPE_ADMIN.equals(user.getMgrType()) || !UserContext.getUser().getTenantId().equals(user.getTenantId()) ){
                throw new BizException(LoginFailEnum.UNDECLARED_ERROR.getCode(), "抱歉，您没有权限操作此数据");
            }
        }
        return user;
    }

    /**
     * 获取用户相关信息
     * @param type
     * @return
     */
    @Override
    public Map<String, Object> userInfo(String type) {
        if("login".equals(type)){
            return loginInfo();
        }else if("personal".equals(type)){
            return personalInfo();
        }
        return MapUtil.newHashMap();
    }

    /**
     * 保存用户个人信息
     * @param user
     */
    @Transactional(readOnly = false)
    public void saveUserInfo(User user){
        Map<String, Object> map = MapUtil.newHashMap();
        map.put("id", UserContext.getUser().getId());
        map.put("name", user.getName());
        map.put("avatar", user.getAvatar());
        map.put("sex", user.getSex());
        map.put("mobile", user.getMobile());
        map.put("email", user.getEmail());
        map.put("updateBy", UserContext.getUser().getId());
        map.put("updateDate", new Date());
        dao.updateMap(map);
        clearUsersCacheByUserId(CollUtil.newArrayList(UserContext.getUser().getId()));
    }

    /**
     * 用户密码修改
     * @param newPassword
     */
    @Transactional(readOnly = false)
    public void saveUserPassword(String newPassword){
        Map<String, Object> map = MapUtil.newHashMap();
        map.put("id", UserContext.getUser().getId());
        map.put("password", new BCryptPasswordEncoder().encode(newPassword));
        map.put("updateBy", UserContext.getUser().getId());
        map.put("updateDate", new Date());
        dao.updateMap(map);
        clearUsersCacheByUserId(CollUtil.newArrayList(UserContext.getUser().getId()));
    }

    /**
     * 角色授权
     * @param user
     */
    @Override
    @Transactional(readOnly = false)
    public void roleAuthorized(User user){
        dao.roleAuthorized(user);
        clearUsersCacheByUserId(CollUtil.newArrayList(user.getId()));
    }

    /**
     * 用户登录信息
     * @return
     */
    public Map<String, Object> loginInfo(){
        Map<String, Object> map = MapUtil.newHashMap();

        User user = UserContext.getUser();
        if( user != null ){
            map.put("userId", user.getId());
            map.put("userName", user.getUsername());
            map.put("name", user.getName());

            map.put("organId", user.getTenantId());
            map.put("organName", user.getOrganName());
            map.put("deptId", user.getDeptId());
            map.put("deptName", user.getDeptName());

            map.put("admin", user.isAdmin());
            map.put("superAdmin", user.isSuperAdmin());
            map.put("userWeight", user.getUserWeight());

            map.put("empCode", user.getEmpCode());
            map.put("empStatus", user.getEmpStatus());
            map.put("sex", user.getSex());
            map.put("avatar", user.getAvatar());
            map.put("lastVisitDate", user.getLastVisitDate());

            // 菜单权限
            List<Permission> menuPermissions = PermUtils.getPermissionsByType(user.getPermissions(), CommonConstant.PERMISSION_PAGE);
            if( !CollUtil.isEmpty(menuPermissions) ){
                map.put("menuAuthorities", new TreeBuild(menuPermissions).getTreeList());
            }

            // 操作权限(用于前端按钮权限控制)
            Map<String, List<String>> btnPermissions = PermUtils.getButtonPermissions(user.getPermissions());
            if( !CollUtil.isEmpty(btnPermissions) ){
                map.put("authorities", btnPermissions);
            }

            // 所属角色
            List<Role> roles = user.getRoles();
            if( !CollUtil.isEmpty(roles) ){
                map.put("roles", roles);
            }
        }
        return map;
    }

    /**
     * 用户个人信息
     * @return
     */
    public Map<String, Object> personalInfo(){
        Map<String, Object> map = MapUtil.newHashMap();
        if( UserContext.getUser() != null ){
            User user = dao.fetch(UserContext.getUser().getId());

            if( user != null ){
                map.put("userId", UserContext.getUser().getId());
                map.put("userName", UserContext.getUser().getUsername());
                map.put("name", user.getName());
                map.put("avatar", user.getAvatar());
                map.put("empCode", UserContext.getUser().getEmpCode());
                map.put("deptName", UserContext.getUser().getDeptName());
                map.put("organName", UserContext.getUser().getOrganName());
                map.put("roleNames", UserContext.getUser().getRoleNames());
                map.put("sex", user.getSex());
                map.put("email", user.getEmail());
                map.put("mobile", user.getMobile());
                map.put("lastVisitDate", DateUtil.format(user.getLastVisitDate(), DatePattern.NORM_DATETIME_MINUTE_FORMAT));
            }
        }
        return map;
    }

    /**
     * 根据用户ID清除用户缓存
     * @param userIdList
     */
    public void clearUsersCacheByUserId(List userIdList){
        if( !CollUtil.isEmpty(userIdList) ){
            ClearUserCacheThread clearUserCacheThread = new ClearUserCacheThread();
            clearUserCacheThread.setUserIdList(userIdList);
            ThreadUtil.execute(clearUserCacheThread);
        }
    }
}