package com.lovecyy.wallet.item.common.convert;

import com.lovecyy.wallet.item.model.pojo.ContractInfo;
import com.lovecyy.wallet.item.model.pojo.TContract;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TContractConvert {
    TContractConvert INSTANCE=Mappers.getMapper(TContractConvert.class);

    TContract ContractInfoTOContract(ContractInfo contractInfo);
    ContractInfo ContractTOContractInfo(TContract tContract);
}
