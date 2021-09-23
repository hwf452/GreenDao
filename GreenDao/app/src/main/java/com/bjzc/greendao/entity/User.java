package com.bjzc.greendao.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

@Entity
public class User {
    @Id(autoincrement = true)//设置自增长
    private Long id;
    @Index(unique = true)//设置唯一性
    private String perNo;//人员编号
    private String name;
    private int age;
    private String sex;//人员姓名
    @Generated(hash = 1053194274)
    public User(Long id, String perNo, String name, int age, String sex) {
        this.id = id;
        this.perNo = perNo;
        this.name = name;
        this.age = age;
        this.sex = sex;
    }
    @Generated(hash = 586692638)
    public User() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getPerNo() {
        return this.perNo;
    }
    public void setPerNo(String perNo) {
        this.perNo = perNo;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return this.age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public String getSex() {
        return this.sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
}
