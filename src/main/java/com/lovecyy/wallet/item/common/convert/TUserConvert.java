package com.lovecyy.wallet.item.common.convert;

import com.lovecyy.wallet.item.model.dto.UserDTO;
import com.lovecyy.wallet.item.model.pojo.TUsers;
import com.lovecyy.wallet.item.model.qo.UserQO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 用户转换
 * @author Yakir
 */
@Mapper
public interface TUserConvert {
    TUserConvert INSTANCE= Mappers.getMapper(TUserConvert.class);

    /**
     * qo 转po
     * @param userQO
     * @return
     */
    TUsers QOTOPO(UserQO userQO);

    /**
     * po to dto
     * @param tUsers
     * @return
     */
    UserDTO POTODTO(TUsers tUsers);
}
