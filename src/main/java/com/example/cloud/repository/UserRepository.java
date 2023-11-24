package com.example.cloud.repository;

import com.example.demo.domain.mapping.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByEmail(String email);

    Optional<UserInfoMapping> findStatusByEmail(String email);

    @Modifying@Transactional
    @Query("update User set instagram_id = :instagram_id where id = :userId")
    void updateUserInstagramId(@Param(value = "instagram_id")String instagram_id, @Param(value = "userId") Long userId);

    //@Modifying@Transactional
   // @Query("update User set boardImage.id = :board_id where id = :userId")
   // void updateUserBoardId(@Param(value = "board_id") Long board_id, @Param(value = "userId") Long userId);

    @Modifying@Transactional
    @Query("update User set createdAt = current_timestamp , updatedAt = current_timestamp where id = :userId ")
    void updateUserCreateAt(@Param(value = "userId") Long userId);

    @Modifying @Transactional
    @Query("update User set createdAt = current_timestamp , updatedAt  = current_timestamp where id = :userId")
    void updateUserUpdatedAt(@Param(value = "userId")Long userId);

    @Modifying @Transactional
    @Query("update User set refresh_token = :refresh_token , updatedAt = current_timestamp where id = :userId")
    void updateUserRefreshToken(@Param(value = "refresh_token")String refresh_token, @Param(value = "userId")Long userId);

    @Modifying @Transactional
    @Query("update User set nickname = :nickname , updatedAt  = current_timestamp where id = :userId")
    void updateUserNickname(@Param(value = "nickname") String nickname, @Param(value = "userId") Long userId);

    @Modifying @Transactional
    @Query("update User set status = :status , updatedAt  = current_timestamp where id = :userId")
    void updateUser_Status(@Param(value = "status") int i, @Param(value = "userId") Long userId);


    @Query("select count(id) from User where status = 3 and datediff(current_timestamp, updatedAt ) = 60")
    Long countAllByStatusAndUpdated_At();

    @Modifying
    @Transactional
    @Query("delete from User where status = 3 and datediff(current_timestamp, updatedAt ) = 60")
    void deleteAllByDay();
}
