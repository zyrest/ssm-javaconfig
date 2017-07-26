package samson.common.dao;

import samson.common.po.UserRoleAuth;

public interface UserRoleAuthMapper {
    int insert(UserRoleAuth record);

    int insertSelective(UserRoleAuth record);
}