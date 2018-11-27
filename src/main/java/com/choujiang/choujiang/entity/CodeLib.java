package com.choujiang.choujiang.entity;

import javax.persistence.*;

@Entity
@Table(name = "lj_codelib")
@PrimaryKeyJoinColumn(name = "id")
public class CodeLib {
    private String id;
    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
