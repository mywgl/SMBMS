package com.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @Description
 * 角色表实体类
 * @Date 2021/1/10 16:01
 * @Version 1.0
 */
@Data
public class Role {
    private Integer id;   //id
    private String roleCode; //角色编码
    private String roleName; //角色名称
    private Integer createdBy; //创建者
    private Date creationDate; //创建时间
    private Integer modifyBy; //更新者
    private Date modifyDate;//更新时间
}
