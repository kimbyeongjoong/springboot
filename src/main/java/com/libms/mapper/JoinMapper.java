package com.libms.mapper;

import com.libms.vo.User_emailVo;
import com.libms.vo.User_infoVo;
import com.libms.vo.User_telVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface JoinMapper {

    void userJoin(User_infoVo user_infoVo);
    void insertUserTel(User_telVo user_telVo);
    void insertUserEmail(User_emailVo user_emailVo);
    String idDuplicateCheck(String user_id);
    String emailDuplicateCheck(User_emailVo user_emailVo);
}
