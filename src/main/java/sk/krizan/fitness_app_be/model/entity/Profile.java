package sk.krizan.fitness_app_be.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import sk.krizan.fitness_app_be.model.enums.WeightUnit;

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
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(nullable = false)
    private User user;

    @Column(length = 64)
    private String name;

    private String profilePictureUrl;

    @Column(length = 128)
    private String bio;

    @NotNull
    private Boolean deleted = false;

    @NotNull
    @Enumerated(EnumType.STRING)
    private WeightUnit preferredWeightUnit;

    @OneToMany(mappedBy = CoachClient.Fields.coach, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CoachClient> coachingSet = new HashSet<>();

    @OneToMany(mappedBy = CoachClient.Fields.client, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CoachClient> beingCoachedSet = new HashSet<>();

    @OneToMany(mappedBy = Workout.Fields.author, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private final List<Workout> authoredWorkoutList = new ArrayList<>();

    @OneToMany(mappedBy = Workout.Fields.trainee, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private final List<Workout> assignedWorkoutList = new ArrayList<>();

    @OneToMany(mappedBy = Cycle.Fields.author, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private final List<Cycle> authoredCycleList = new ArrayList<>();

    @OneToMany(mappedBy = Cycle.Fields.trainee, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private final List<Cycle> assignedCycleList = new ArrayList<>();

    public void addToCoachingSet(Set<CoachClient> coachClientSet) {
        coachClientSet.forEach(coachClient -> coachClient.setCoach(this));
        coachingSet.addAll(coachClientSet);
    }

    public void addToBeingCoachedSet(Set<CoachClient> coachClientSet) {
        coachClientSet.forEach(coachClient -> coachClient.setClient(this));
        beingCoachedSet.addAll(coachClientSet);
    }

    public void addToAuthoredWorkoutList(List<Workout> workoutList) {
        workoutList.forEach(workout -> workout.setAuthor(this));
        this.getAuthoredWorkoutList().addAll(workoutList);
    }

    public void addToAssignedWorkoutList(List<Workout> workoutList) {
        workoutList.forEach(workout -> workout.setTrainee(this));
        this.getAssignedWorkoutList().addAll(workoutList);
    }

    public void addToAuthoredCycleList(List<Cycle> cycleList) {
        cycleList.forEach(cycle -> cycle.setAuthor(this));
        this.getAuthoredCycleList().addAll(cycleList);
    }

    public void addToAssignedCycleList(List<Cycle> cycleList) {
        cycleList.forEach(cycle -> cycle.setTrainee(this));
        this.assignedCycleList.addAll(cycleList);
    }
}
