package com.albez.web.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "comments")
public class Comments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_commentator", nullable = false)
    private Long idCommentator;

    @Column(name = "userName")
    private String userName;

    @Column(name = "comment", length = 1000, nullable = false)
    private String comment;

    @Column(name = "score", nullable = false)
    private int score;

    @Column(name = "to_delete")
    private boolean toDelete = false;

    @Column(name = "date_comment", nullable = false)
    private String dateComment;

    @Column(name = "deleted")
    private boolean deleted = false;

    @Column(name = "vkIdComment")
    private Long vkIdComment;

    @Column(name = "commentatorPhoto")
    private String commentatorPhoto;

    @Column(name = "changedScore")
    private boolean changedScore = false;
    @ManyToOne
    private Posts posts;

    public Comments(Long idCommentator, String userName, String comment, int score,
                    boolean toDelete, String dateComment, boolean deleted, Long vkIdComment,
                    String commentatorPhoto, boolean changedScore, Posts posts) {
        this.idCommentator = idCommentator;
        this.userName = userName;
        this.comment = comment;
        this.score = score;
        this.toDelete = toDelete;
        this.dateComment = dateComment;
        this.deleted = deleted;
        this.vkIdComment = vkIdComment;
        this.commentatorPhoto = commentatorPhoto;
        this.changedScore = changedScore;
        this.posts = posts;
    }

    public Comments() {

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Posts getPosts() {
        return posts;
    }

    public void setPosts(Posts posts) {
        this.posts = posts;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdCommentator() {
        return idCommentator;
    }

    public void setIdCommentator(Long idCommentator) {
        this.idCommentator = idCommentator;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isToDelete() {
        return toDelete;
    }

    public void setToDelete(boolean toDelete) {
        this.toDelete = toDelete;
    }

    public String getDateComment() {
        return dateComment;
    }

    public void setDateComment(String dateComment) {
        this.dateComment = dateComment;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Long getVkIdComment() {
        return vkIdComment;
    }

    public void setVkIdComment(Long vkIdComment) {
        this.vkIdComment = vkIdComment;
    }

    public String getCommentatorPhoto() {
        return commentatorPhoto;
    }

    public void setCommentatorPhoto(String commentatorPhoto) {
        this.commentatorPhoto = commentatorPhoto;
    }

    public boolean isChangedScore() {
        return changedScore;
    }

    public void setChangedScore(boolean changedScore) {
        this.changedScore = changedScore;
    }
}
