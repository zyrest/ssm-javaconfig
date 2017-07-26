package samson.common.dao;

import org.apache.ibatis.annotations.Param;
import samson.common.po.User;

public interface UserMapper {
    int deleteByPrimaryKey(Long userId);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long userId);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User selectByName(@Param("userName") String userName);
}