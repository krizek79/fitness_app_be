package sk.krizan.fitness_app_be.common.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;
import sk.krizan.fitness_app_be.domain.user.entity.Role;
import sk.krizan.fitness_app_be.domain.user.service.api.UserService;

import java.io.Serializable;

@Component
@RequiredArgsConstructor
public class CustomPermissionEvaluator implements PermissionEvaluator {

    private final UserService userService;
    private final SecurityAccessValidator securityAccessValidator;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_" + Role.ADMIN.name()));

        if (isAdmin) {
            return true;
        }

        Profile currentProfile = userService.getOrCreateCurrentUser().getProfile();

        ResourceType resourceType = ResourceType.valueOf(targetType.toUpperCase());
        Permission mappedPermission = Permission.valueOf(permission.toString().toUpperCase());

        return switch (resourceType) {
            case PLAN -> securityAccessValidator.canAccessPlan((Long) targetId, currentProfile.getId(), mappedPermission);
            case WEEK -> securityAccessValidator.canAccessWeek((Long) targetId, currentProfile.getId(), mappedPermission);
            case WEEK_WORKOUT -> securityAccessValidator.canAccessWeekWorkout((Long) targetId, currentProfile.getId(), mappedPermission);
            case WORKOUT -> securityAccessValidator.canAccessWorkout((Long) targetId, currentProfile.getId(), mappedPermission);
            case WORKOUT_EXERCISE -> securityAccessValidator.canAccessWorkoutExercise((Long) targetId, currentProfile.getId(), mappedPermission);
            case GOAL -> securityAccessValidator.canAccessGoal((Long) targetId, currentProfile.getId(), mappedPermission);
            case DRAFT -> securityAccessValidator.canAccessDraft((Long) targetId, currentProfile.getId(), mappedPermission);
        };
    }
}
