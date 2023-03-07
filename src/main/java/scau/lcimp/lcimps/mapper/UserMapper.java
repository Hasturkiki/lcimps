package scau.lcimp.lcimps.mapper;

import org.apache.ibatis.annotations.Mapper;
import scau.lcimp.lcimps.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author Hastur kiki
* @description 针对表【user(用户)】的数据库操作Mapper
* @createTime 2023-02-20 00:42:08
* @Entity scau.lcimp.lcimps.domain.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




