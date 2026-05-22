package sk.krizan.fitness_app_be.domain.profile.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import sk.krizan.fitness_app_be.domain.coaching_contract.entity.CoachingContract;
import sk.krizan.fitness_app_be.domain.plan.entity.Plan;
import sk.krizan.fitness_app_be.domain.reference.entity.WeightUnit;
import sk.krizan.fitness_app_be.common.audit.AuditableEntity;
import sk.krizan.fitness_app_be.domain.user.entity.User;
import sk.krizan.fitness_app_be.domain.workout.entity.Workout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class Profile extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = User.Fields.profile)
    private User user;

    @Column(length = 64)
    private String name;

    private String profilePictureUrl;

    @Column(length = 128)
    private String bio;

    @NotNull
    @Builder.Default
    private Boolean deleted = false;

    @NotNull
    @Enumerated(EnumType.STRING)
    private WeightUnit preferredWeightUnit;

    @Builder.Default
    @OneToMany(mappedBy = CoachingContract.Fields.coach, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private Set<CoachingContract> coaching = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = CoachingContract.Fields.client, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private Set<CoachingContract> coachedBy = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = Workout.Fields.author, fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private final List<Workout> authoredWorkouts = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = Workout.Fields.trainee, fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private final List<Workout> assignedWorkouts = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = Plan.Fields.author, fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private final List<Plan> authoredPlans = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = Plan.Fields.trainee, fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private final List<Plan> assignedPlans = new ArrayList<>();

    public void addToAuthoredWorkouts(Workout workout) {
        if (workout == null) {
            return;
        }

        workout.setAuthor(this);
        this.authoredWorkouts.add(workout);
    }

    public void removeFromAuthoredWorkouts(Workout workout) {
        if (workout == null) {
            return;
        }

        workout.setAuthor(null);
        this.authoredWorkouts.remove(workout);
    }

    public void addToAssignedWorkouts(Workout workout) {
        if (workout == null) {
            return;
        }

        workout.setTrainee(this);
        this.assignedWorkouts.add(workout);
    }

    public void removeFromAssignedWorkouts(Workout workout) {
        if (workout == null) {
            return;
        }

        workout.setTrainee(null);
        this.assignedWorkouts.remove(workout);
    }

    public void addToAuthoredPlans(Plan plan) {
        if (plan == null) {
            return;
        }

        plan.setAuthor(this);
        this.authoredPlans.add(plan);
    }

    public void addToAssignedPlans(Plan plan) {
        if (plan == null) {
            return;
        }

        plan.setTrainee(this);
        this.assignedPlans.add(plan);
    }

    public void removeFromAssignedPlans(Plan plan) {
        if (plan == null) {
            return;
        }

        plan.setTrainee(null);
        this.assignedPlans.remove(plan);
    }

    public void addToCoaching(CoachingContract coachingContract) {
        if (coachingContract == null) {
            return;
        }

        coachingContract.setCoach(this);
        this.coaching.add(coachingContract);
    }

    public void removeFromCoaching(CoachingContract coachingContract) {
        if (coachingContract == null) {
            return;
        }

        coachingContract.setCoach(null);
        this.coaching.remove(coachingContract);
    }

    public void addToCoachedBy(CoachingContract coachingContract) {
        if (coachingContract == null) {
            return;
        }

        coachingContract.setClient(this);
        this.coachedBy.add(coachingContract);
    }

    public void removeFromCoachedBy(CoachingContract coachingContract) {
        if (coachingContract == null) {
            return;
        }

        coachingContract.setClient(null);
        this.coachedBy.remove(coachingContract);
    }

}
