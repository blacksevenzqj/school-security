package school.admin.modules.sys.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import school.admin.modules.sys.dao.SysRoleDao;
import school.admin.modules.sys.entity.SysRoleEntity;
import school.db.service.CrudService;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;


/**
 * 角色
 */
@Service("sysRoleServiceImpl")
public class SysRoleServiceImpl extends CrudService<SysRoleDao, SysRoleEntity, Long> {

	@Autowired
	private SysRoleMenuServiceImpl sysRoleMenuServiceImpl;

	@Autowired
	private SysUserRoleServiceImpl sysUserRoleServiceImpl;

//	@DataFilter(subDept = true, user = false)
//	public PageUtils queryPage(Map<String, Object> params) {
//		String roleName = (String)params.get("roleName");
//
//		Page<SysRoleEntity> page = this.selectPage(
//			new Query<SysRoleEntity>(params).getPage(),
//			new EntityWrapper<SysRoleEntity>()
//				.like(StringUtils.isNotBlank(roleName),"role_name", roleName)
//				.addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String)params.get(Constant.SQL_FILTER))
//		);
//
//		for(SysRoleEntity sysRoleEntity : page.getRecords()){
//			SysDeptEntity sysDeptEntity = sysDeptService.selectById(sysRoleEntity.getDeptId());
//			if(sysDeptEntity != null){
//				sysRoleEntity.setDeptName(sysDeptEntity.getName());
//			}
//		}
//
//		return new PageUtils(page);
//	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveSysRole(SysRoleEntity role) {
		role.setCreateTime(new Date());
		this.save(role);
		//保存角色与菜单关系
		sysRoleMenuServiceImpl.saveOrUpdate(role.getRoleId(), role.getMenuIdList());
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void update(SysRoleEntity role) {
		this.save(role);
		//更新角色与菜单关系：还是 删除 -> 新增
		sysRoleMenuServiceImpl.saveOrUpdate(role.getRoleId(), role.getMenuIdList());
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deleteBatch(Long[] roleIds) {
		//删除角色
		this.deleteBatchByIds(roleIds);

		//删除角色与菜单关联
		sysRoleMenuServiceImpl.deleteBatch(roleIds);

		//删除角色与用户关联
		sysUserRoleServiceImpl.deleteBatch(roleIds);
	}


}