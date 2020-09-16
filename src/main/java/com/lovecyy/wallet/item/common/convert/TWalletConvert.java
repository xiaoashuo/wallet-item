package com.lovecyy.wallet.item.common.convert;

import com.lovecyy.wallet.item.model.dto.TWalletDto;
import com.lovecyy.wallet.item.model.pojo.TWallet;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TWalletConvert {

    TWalletConvert INSTANCE=Mappers.getMapper(TWalletConvert.class);

    TWalletDto POTODTO(TWallet tWallet);
}
