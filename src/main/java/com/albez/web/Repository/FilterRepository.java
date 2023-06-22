package com.albez.web.Repository;

import com.albez.web.Entity.Filter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FilterRepository extends JpaRepository<Filter, Integer> {
    List<Filter> findByGroupsVK_IdGroupVk(String idGroupVk);

    @Modifying
    @Query("update Filter f set f.dictionary = ?1 where f.id = ?2")
    void setDictionary(String dictionary, Long id);

    @Query("select f.dictionary from Filter f where f.groupsVK.idGroupVk = ?1")
    String getDictionary(String id);

    @Query("select f.id from Filter f where f.groupsVK.idGroupVk = ?1")
    Long getIdFilter(String id);
}
