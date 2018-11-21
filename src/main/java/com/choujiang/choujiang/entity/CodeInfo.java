package com.choujiang.choujiang.entity;


import javax.persistence.*;

@Entity
@Table(name = "lj_code")
@PrimaryKeyJoinColumn(name = "id")
public class CodeInfo {
    private Integer id;
    private String code;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
