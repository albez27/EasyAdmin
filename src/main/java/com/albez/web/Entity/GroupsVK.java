package com.albez.web.Entity;


import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "groupsVK")
public class GroupsVK {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    private Long id;

    @Column(name = "id_group_vk", nullable = false)
    private String idGroupVk;

    @Column(name = "groupName", nullable = false)
    private String groupName;

    @Column(name = "screenName")
    private String screenName;

    @Column(name = "countPosts")
    private Long countPost;

    @Column(name = "linkToPhoto")
    private String linkToPhoto;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_filter")
    private Filter wordFilter;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "group")
    private List<Posts> posts;

    public GroupsVK() {
    }

    public GroupsVK(String idGroupVk, String groupName, String screenName,Filter wordFilter) {
        this.idGroupVk = idGroupVk;
        this.groupName = groupName;
        this.screenName = screenName;
        this.wordFilter = wordFilter;
    }

    public GroupsVK(String idGroupVk, String groupName, Filter wordFilter) {
        this.idGroupVk = idGroupVk;
        this.groupName = groupName;
        this.wordFilter = wordFilter;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdGroupVk() {
        return idGroupVk;
    }

    public void setIdGroupVk(String idGroupVk) {
        this.idGroupVk = idGroupVk;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public Filter getWordFilter() {
        return wordFilter;
    }

    public void setWordFilter(Filter wordFilter) {
        this.wordFilter = wordFilter;
    }

    public List<Posts> getPosts() {
        return posts;
    }

    public void setPosts(List<Posts> posts) {
        this.posts = posts;
    }

    public Long getCountPost() {
        return countPost;
    }

    public void setCountPost(Long countPost) {
        this.countPost = countPost;
    }

    public String getLinkToPhoto() {
        return linkToPhoto;
    }

    public void setLinkToPhoto(String linkToPhoto) {
        this.linkToPhoto = linkToPhoto;
    }
}
