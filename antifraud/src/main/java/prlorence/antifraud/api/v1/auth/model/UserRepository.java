package prlorence.antifraud.api.v1.auth.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import prlorence.antifraud.api.v1.auth.entities.User;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    User findByUsername(String username);
    void deleteByUsername(String username);
    List<User> findAllByRoleEqualsIgnoreCase(String role);
}
