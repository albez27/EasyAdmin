package com.albez.web.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "filter")
public class Filter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dictionary", length = 65555)
    private String dictionary;

    @OneToOne(mappedBy = "wordFilter", cascade = CascadeType.ALL)
    private GroupsVK groupsVK;

    public Filter() {
    }

    public Filter(String dictionary) {
        this.dictionary = dictionary;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDictionary() {
        return dictionary;
    }

    public void setDictionary(String dictionary) {
        this.dictionary = dictionary;
    }

    public GroupsVK getGroupsVK() {
        return groupsVK;
    }

    public void setGroupsVK(GroupsVK groupsVK) {
        this.groupsVK = groupsVK;
    }
}
