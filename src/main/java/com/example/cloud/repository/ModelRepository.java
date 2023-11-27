package com.example.cloud.repository;

import com.example.cloud.domain.mapping.Model;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ModelRepository extends JpaRepository<Model, Long> {

    List<Model> findByUserId(Long user_id);

    @Query("select m from MODEL_INFO m where share_check=1 and id > :id")
    List<Model> findByUserIdWhereModelId(@Param(value = "id")Long modelId, Pageable pageable);

    @Query("select m from MODEL_INFO m where share_check=1 and id > :id and name like %:name% ")
    List<Model>findByUserIdWhereModelIdAndName(@Param(value = "id") Long modelId,Pageable pageable,@Param(value = "name") String name);

    Optional<Model> findById(Long id);

    @Modifying
    @Transactional
    @Query("update MODEL_INFO set name = :name where id = :id")
    void updateName(@Param(value = "id")Long modelId , @Param(value = "name") String name);

    @Modifying@Transactional
    @Query("update MODEL_INFO set description = :description where id = :id")
    void updateDescription(@Param(value = "id")Long modelId , @Param(value = "description") String description);

    //check
    @Modifying@Transactional
    @Query("update MODEL_INFO set share_check = :share where id = :id")
    void updateShareCheck(@Param(value = "id")Long modelId , @Param(value = "share") int share);

    //image upload
    @Modifying@Transactional
    @Query("update MODEL_INFO set image_url = :url where id = :id")
    void updateImageUrl(@Param(value = "id")Long modelId , @Param(value = "url") String url);
}
