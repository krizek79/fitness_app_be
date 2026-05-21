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
import sk.krizan.fitness_app_be.domain.reference.entity.WeightUnit;
import sk.krizan.fitness_app_be.domain.coach_client.entity.CoachClient;
import sk.krizan.fitness_app_be.domain.cycle.entity.Cycle;
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
    @OneToMany(mappedBy = CoachClient.Fields.coach, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private Set<CoachClient> coaching = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = CoachClient.Fields.client, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private Set<CoachClient> coachedBy = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = Workout.Fields.author, fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private final List<Workout> authoredWorkouts = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = Workout.Fields.trainee, fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private final List<Workout> assignedWorkouts = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = Cycle.Fields.author, fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private final List<Cycle> authoredCycles = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = Cycle.Fields.trainee, fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private final List<Cycle> assignedCycles = new ArrayList<>();

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

    public void addToAuthoredCycles(Cycle cycle) {
        if (cycle == null) {
            return;
        }

        cycle.setAuthor(this);
        this.authoredCycles.add(cycle);
    }

    public void addToAssignedCycles(Cycle cycle) {
        if (cycle == null) {
            return;
        }

        cycle.setTrainee(this);
        this.assignedCycles.add(cycle);
    }

    public void removeFromAssignedCycles(Cycle cycle) {
        if (cycle == null) {
            return;
        }

        cycle.setTrainee(null);
        this.assignedCycles.remove(cycle);
    }

    public void addToCoaching(CoachClient coachClient) {
        if (coachClient == null) {
            return;
        }

        coachClient.setCoach(this);
        this.coaching.add(coachClient);
    }

    public void removeFromCoaching(CoachClient coachClient) {
        if (coachClient == null) {
            return;
        }

        coachClient.setCoach(null);
        this.coaching.remove(coachClient);
    }

    public void addToCoachedBy(CoachClient coachClient) {
        if (coachClient == null) {
            return;
        }

        coachClient.setClient(this);
        this.coachedBy.add(coachClient);
    }

    public void removeFromCoachedBy(CoachClient coachClient) {
        if (coachClient == null) {
            return;
        }

        coachClient.setClient(null);
        this.coachedBy.remove(coachClient);
    }

}
