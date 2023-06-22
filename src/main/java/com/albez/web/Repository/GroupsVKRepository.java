package com.albez.web.Repository;

import com.albez.web.Entity.GroupsVK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface GroupsVKRepository extends JpaRepository<GroupsVK, Integer> {

    @Modifying
    @Query("update GroupsVK g set g.screenName = ?1 where g.idGroupVk = ?2")
    void setScreenName(String screenName, String idGroupVk);

    @Modifying
    @Query("update GroupsVK g set g.linkToPhoto = ?1 where g.idGroupVk = ?2")
    void setLinkToPost(String linkToPost, String idGroupVk);

    @Query("select g.screenName from GroupsVK g where g.idGroupVk = ?1")
    String getScreenName(String idGroupVk);

    @Modifying
    @Query("update GroupsVK g set g.countPost = ?1 where g.idGroupVk = ?2")
    void setCountPosts(Long countPosts, String idGroupVk);

    @Query("select g.countPost from GroupsVK g where g.idGroupVk = ?1")
    String getCountPosts(String idGroupVk);

    @Query("select g from GroupsVK g where g.idGroupVk = ?1")
    GroupsVK getIdByIdGroup(String idGroupVk);

}
