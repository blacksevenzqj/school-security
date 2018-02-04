package school.admin.modules.sys.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import school.admin.modules.sys.dao.SysMenuDao;
import school.admin.modules.sys.entity.SysMenuEntity;
import school.common.utils.Constant;
import school.common.utils.MapUtils;
import school.db.service.CrudService;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service(value = "sysMenuServiceImpl")
public class SysMenuServiceImpl extends CrudService<SysMenuDao, SysMenuEntity, Long> {

    @Autowired
    private SysUserServiceImpl sysUserServiceImpl;

    @Autowired
    private SysRoleMenuServiceImpl sysRoleMenuServiceImpl;


    public List<SysMenuEntity> queryListParentId(Long parentId, List<Long> menuIdList) {
        List<SysMenuEntity> menuList = queryListParentId(parentId);
        if(menuIdList == null){
            return menuList;
        }

        List<SysMenuEntity> userMenuList = new ArrayList<>();
        for(SysMenuEntity menu : menuList){
            if(menuIdList.contains(menu.getMenuId())){
                userMenuList.add(menu);
            }
        }
        return userMenuList;
    }

    public List<SysMenuEntity> queryListParentId(Long parentId) {
        return getDao().queryListParentId(parentId);
    }

    public List<SysMenuEntity> queryNotButtonList() {
        return getDao().queryNotButtonList();
    }

    public List<SysMenuEntity> getUserMenuList(Long userId) {
        //系统管理员，拥有最高权限
        if(userId == Constant.SUPER_ADMIN){
            return getAllMenuList(null);
        }

        //用户菜单列表
        List<Long> menuIdList = sysUserServiceImpl.queryAllMenuId(userId);
        return getAllMenuList(menuIdList);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void delete(Long menuId){
        //删除菜单
        deleteById(menuId);
        //删除菜单与角色关联
//        sysRoleMenuServiceImpl.deleteByMap(new MapUtils().put("menu_id", menuId));
    }

    /**
     * 获取所有菜单列表
     */
    private List<SysMenuEntity> getAllMenuList(List<Long> menuIdList){
        //查询根菜单列表
        List<SysMenuEntity> menuList = queryListParentId(0L, menuIdList);
        //递归获取子菜单
        getMenuTreeList(menuList, menuIdList);

        return menuList;
    }

    /**
     * 递归
     */
    private List<SysMenuEntity> getMenuTreeList(List<SysMenuEntity> menuList, List<Long> menuIdList){
        List<SysMenuEntity> subMenuList = new ArrayList<SysMenuEntity>();

        for(SysMenuEntity entity : menuList){
            //目录
            if(entity.getType() == Constant.MenuType.CATALOG.getValue()){
                entity.setList(getMenuTreeList(queryListParentId(entity.getMenuId(), menuIdList), menuIdList));
            }
            subMenuList.add(entity);
        }

        return subMenuList;
    }

}