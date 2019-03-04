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

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.yibo.boot.base.entity.TreeBuild;
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
import cn.yibo.common.base.service.impl.AbstractBaseService;
import cn.yibo.common.utils.ListUtils;
import cn.yibo.common.utils.ObjectUtils;
import cn.yibo.core.web.exception.BizException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${webapp.allow-multi-identity}")
    private Boolean allowMultiIdentity;

    /**
     * 重写新增
     * @param user
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public void insert(User user){
        super.insert(user);
        this.assignRoles(user);
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
        String authorizeType = ObjectUtils.toString(params.get("authorizeType"));
        if( !CommonConstant.USER_MGR_TYPE_ADMIN.equals(mgrType) || StrUtil.isNotBlank(authorizeType) ){
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
            if( !ListUtils.isEmpty(roles) ){
                // 单角色使用系统时，默认角色为权重最大的角色
                if( !allowMultiIdentity ){
                    user.setRole(roles.get(0));
                }
                user.setRoles(roles);
            }
            // 关联权限
            List<Permission> permissions = permUtils.getUserAllPermissions(user);
            if( !ListUtils.isEmpty(permissions) ){
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
     * 授权角色
     * @param user
     */
    @Override
    @Transactional(readOnly = false)
    public void assignRoles(User user){
        dao.assignRoles(user);
        clearUsersCacheByUserId(ListUtils.newArrayList(user.getId()));
    }

    /**
     * 分配指定角色
     * @param roleId
     * @param userId
     * @param flag
     */
    @Override
    public void assignRole(String roleId, String userId, boolean flag){
        roleService.assignUser(roleId, userId, flag);
        clearUsersCacheByUserId(ListUtils.newArrayList(userId));
    }

    /**
     * 修改个人信息
     * @param user
     */
    @Transactional(readOnly = false)
    public void editUserInfo(User user){
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
        clearUsersCacheByUserId(ListUtils.newArrayList(UserContext.getUser().getId()));
    }

    /**
     * 修改个人密码
     * @param newPassword
     */
    @Transactional(readOnly = false)
    public void editUserPwd(String newPassword){
        Map<String, Object> map = MapUtil.newHashMap();
        map.put("id", UserContext.getUser().getId());
        map.put("password", new BCryptPasswordEncoder().encode(newPassword));
        map.put("updateBy", UserContext.getUser().getId());
        map.put("updateDate", new Date());
        dao.updateMap(map);
        clearUsersCacheByUserId(ListUtils.newArrayList(UserContext.getUser().getId()));
    }

    /**
     * 获取用户相关信息
     * @param type
     * @return
     */
    @Override
    public Map<String, Object> userInfo(String type){
        if( StrUtil.equals("basic", type) ){
            User user = dao.fetch(UserContext.getUser().getId());
            Map<String, Object> map = MapUtil.newHashMap();
            if( user != null ){
                map.put("userId", UserContext.getUser().getId());
                map.put("userName", UserContext.getUser().getUsername());
                map.put("empCode", UserContext.getUser().getEmpCode());
                map.put("deptName", UserContext.getUser().getDeptName());
                map.put("organName", UserContext.getUser().getOrganName());
                map.put("roleNames", UserContext.getUser().getRoleNames());
                map.put("name", user.getName());
                map.put("avatar", user.getAvatar());
                map.put("sex", user.getSex());
                map.put("email", user.getEmail());
                map.put("mobile", user.getMobile());
                map.put("lastVisitDate", DateUtil.format(user.getLastVisitDate(), DatePattern.NORM_DATETIME_MINUTE_FORMAT));
            }
            return map;
        }else{
            return loginInfo();
        }
    }

    /**
     * 用户登录信息
     * @return
     */
    public Map<String, Object> loginInfo(){
        User user = UserContext.getUser();
        Map<String, Object> map = MapUtil.newHashMap();
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
        List<Permission> menuPermissions = PermUtils.filterPermissionsByType(user.getPermissions(), CommonConstant.PERMISSION_PAGE);
        if( !ListUtils.isEmpty(menuPermissions) ){
            map.put("menuAuthorities", new TreeBuild(menuPermissions).getTreeList());
        }

        // 操作权限(用于前端按钮权限控制)
        Map<String, List<String>> btnPermissions = PermUtils.getButtonPermissions(user.getPermissions());
        if( !CollectionUtil.isEmpty(btnPermissions) ){
            map.put("authorities", btnPermissions);
        }

        // 所属角色
        List<Role> roles = user.getRoles();
        if( !ListUtils.isEmpty(roles) ){
            // 单角色使用系统
            if( !allowMultiIdentity ){
                map.put("currRole", user.getRole());
            }
            map.put("roles", roles);
        }
        return map;
    }

    /**
     * 根据用户ID清除用户缓存
     * @param userIdList
     */
    public void clearUsersCacheByUserId(List userIdList){
        ClearUserCacheThread clearUserCacheThread = new ClearUserCacheThread();
        clearUserCacheThread.setUserIdList(userIdList);
        ThreadUtil.execute(clearUserCacheThread);
    }
}