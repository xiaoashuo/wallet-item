package com.lovecyy.wallet.item.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class KeystoreDto {
    public KeystoreDto(String filename, String content) {
        this.filename = filename;
        this.content = content;
    }

    public KeystoreDto(String filename, String content, String mnemonic) {
        this.filename = filename;
        this.content = content;
        this.mnemonic = mnemonic;
    }

    /**
     * 文件名称
     */
    private String filename;
    /**
     * 文件内容
     */
    private String content;
    /**
     * 助记词
     */
    private String mnemonic;
}
