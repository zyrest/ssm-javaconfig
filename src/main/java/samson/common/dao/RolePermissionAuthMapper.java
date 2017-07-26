package samson.common.dao;

import samson.common.po.RolePermissionAuth;

public interface RolePermissionAuthMapper {
    int insert(RolePermissionAuth record);

    int insertSelective(RolePermissionAuth record);
}