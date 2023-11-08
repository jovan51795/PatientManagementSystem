package com.pms.repo;

import com.pms.models.User;
import com.pms.spel.IUserSpel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u.email as email, u.firstname as firstname, u.lastname as lastname FROM User u WHERE u.email = :email")
    Optional<IUserSpel> findUserByEmail(String email);


}
