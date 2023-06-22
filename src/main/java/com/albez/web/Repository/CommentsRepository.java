package com.albez.web.Repository;

import com.albez.web.Entity.Comments;
import com.albez.web.Entity.GroupsVK;
import com.albez.web.Entity.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentsRepository extends JpaRepository<Comments, Integer> {

    @Query
    Comments getCommentByVkIdCommentAndPosts(Long VkIdComment, Posts post);


    List<Comments> getAllByPosts(Posts posts);
    List<Comments> getAllByToDeleteAndDeletedAndPosts(boolean toDelete, boolean Deleted, Posts posts);
    @Query("select c from Comments c where c.toDelete = ?1 and c.posts.group = ?2")
    List<Comments> getToDeleteComments(boolean toDelete, GroupsVK groupsVK);

    @Query("update Comments c set c.toDelete = ?1 where c.id = ?2")
    @Modifying
    void changeToDeleteStatus(boolean flag, Long id);

    @Query("update Comments c set c.deleted = true where c.id = ?1")
    @Modifying
    void deleteComments(Long id);



    @Query("update Comments c set c.score = ?1 where c.id = ?2")
    @Modifying
    void changeScore(int score, Long id);

    @Query("select c.score from Comments c where c.id = ?1")
    int getScoreById(Long id);
}
