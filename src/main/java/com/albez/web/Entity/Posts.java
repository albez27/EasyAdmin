package com.albez.web.Entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "posts")
public class Posts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_post_vk", nullable = false)
    private Long idPostVk;

    @Column(name = "link_to_post", nullable = false)
    private String linkToPost;

    @Column(name = "countComments")
    private Long countComments;

    @Column(name = "datePost")
    private String datePost;

    @Column(name = "text", columnDefinition = "LONGTEXT")
    private String text;

    @Column(name = "likes")
    private Long likes;

    @ManyToOne
    private GroupsVK group;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "posts")
    private List<Comments> comments;


    public Posts() {
    }

    public Posts(Long idPostVk, String linkToPost) {
        this.idPostVk = idPostVk;
        this.linkToPost = linkToPost;
    }

    public Posts(Long idPostVk, String linkToPost, Long countComments, String datePost, String text, Long likes, GroupsVK group) {
        this.idPostVk = idPostVk;
        this.linkToPost = linkToPost;
        this.countComments = countComments;
        this.datePost = datePost;
        this.text = text;
        this.likes = likes;
        this.group = group;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdPostVk() {
        return idPostVk;
    }

    public void setIdPostVk(Long idPostVk) {
        this.idPostVk = idPostVk;
    }

    public String getLinkToPost() {
        return linkToPost;
    }

    public void setLinkToPost(String linkToPost) {
        this.linkToPost = linkToPost;
    }

    public List<Comments> getComments() {
        return comments;
    }

    public void setComments(List<Comments> comments) {
        this.comments = comments;
    }

    public Long getCountComments() {
        return countComments;
    }

    public void setCountComments(Long countComments) {
        this.countComments = countComments;
    }

    public String getDatePost() {
        return datePost;
    }

    public void setDatePost(String datePost) {
        this.datePost = datePost;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getLikes() {
        return likes;
    }

    public void setLikes(Long likes) {
        this.likes = likes;
    }

    @Override
    public String toString() {
        return "Posts{" +
                "id=" + id +
                ", idPostVk=" + idPostVk +
                ", linkToPost='" + linkToPost + '\'' +
                ", countComments=" + countComments +
                ", datePost='" + datePost + '\'' +
                ", group=" + group +
                ", comments=" + comments +
                '}';
    }
}
