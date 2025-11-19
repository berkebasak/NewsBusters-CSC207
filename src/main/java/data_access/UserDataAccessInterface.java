package data_access;

import entity.User;

import java.util.Collection;

public interface UserDataAccessInterface {
    boolean existsByName(String username);

    void save(User user);

    void update(User user);

    User get(String username);

    Collection<User> getAll();
}
