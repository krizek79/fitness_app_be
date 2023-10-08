package sk.krizan.fitness_app_be.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
@Builder
@Getter
@Setter
@Entity
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

    @OneToMany(mappedBy = "author")
    private List<Workout> authoredWorkouts = new ArrayList<>();
}
