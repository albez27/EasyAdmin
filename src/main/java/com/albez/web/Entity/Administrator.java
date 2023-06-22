package com.albez.web.Entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "administrator")
public class Administrator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    private Long id;

    @Column(name = "userName", nullable = false)
    private String userName;

    @Column(name = "idVk", nullable = false)
    private String idVk;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idSettings")
    private Settings setting;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "idAdmin")
    private List<GroupsVK> groups;

    public Administrator(String userName) {
        this.userName = userName;
    }

    public Administrator() {

    }

    public Administrator(String userName, String idVk) {
        this.userName = userName;
        this.idVk = idVk;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUser_name(String userName) {
        this.userName = userName;
    }

    public Settings getSettings() {
        return setting;
    }

    public void setSettings(Settings settings) {
        this.setting = settings;
    }


    public List<GroupsVK> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupsVK> groups) {
        this.groups = groups;
    }

}
