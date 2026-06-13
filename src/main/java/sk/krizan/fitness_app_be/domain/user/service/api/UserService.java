package sk.krizan.fitness_app_be.domain.user.service.api;

import sk.krizan.fitness_app_be.domain.user.entity.User;

public interface UserService {

    User getOrCreateCurrentUser();

    boolean isUserAdmin(User user);

}
