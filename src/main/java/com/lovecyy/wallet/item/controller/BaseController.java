package com.lovecyy.wallet.item.controller;

import com.lovecyy.wallet.item.model.pojo.TUsers;

public class BaseController {
    public TUsers getUser(){
        return TUsers.builder().id(1).build();
    }
}
