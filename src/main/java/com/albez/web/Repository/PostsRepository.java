package com.albez.web.Repository;

import com.albez.web.Entity.GroupsVK;
import com.albez.web.Entity.Posts;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostsRepository extends JpaRepository<Posts, Integer> {

    @Query("select max(p.idPostVk) from Posts p")
    String getMaxIdPostVk();

    @Query("select max(p.idPostVk) from Posts p where p.group = ?1")
    Long getMaxPostId(GroupsVK groupsVK);

    List<Posts> getPostsByGroup(GroupsVK groupsVK);

    Posts getPostsByGroupAndIdPostVk(GroupsVK groupsVK, Long idPostVk);
    Posts getPostsById(Long id);
    @Query("select p.idPostVk from Posts p where p.id = ?1")
    Long getIdPostVkById(Long id);

    @Query("select p from Posts p where p.idPostVk = ?1")
    Posts getIdPostByVkId(Long id);


}
