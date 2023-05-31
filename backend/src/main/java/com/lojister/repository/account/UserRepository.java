package com.lojister.repository.account;

import com.lojister.model.abroudModel.abroudBid;
import com.lojister.model.enums.Role;
import com.lojister.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByPhone(String phone);

    Optional<User> findByEmail(String email);

    Long countByRole(Role role);

    @Query("SELECT u FROM User u WHERE u.role = :role ORDER BY u.id DESC")
    List<User> userRole(Role role);


}
