package com.code.vo;

import lombok.Data;
import lombok.ToString;

/**
 * @author fxs
 * @Date 2021/12/30 15:34
 * @Description:
 */
@Data
@ToString
public class User {
    private String username;
    private String password;
    private Boolean rememberMe;
}
