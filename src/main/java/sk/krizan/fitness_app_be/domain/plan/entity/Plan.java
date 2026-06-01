package sk.krizan.fitness_app_be.domain.plan.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import sk.krizan.fitness_app_be.common.audit.AuditableEntity;
import sk.krizan.fitness_app_be.domain.profile.entity.Profile;
import sk.krizan.fitness_app_be.domain.week.entity.Week;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class Plan extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    private Profile author;

    @ManyToOne
    private Profile trainee;

    @NotEmpty
    private String title;

    @Size(max = 2000)
    private String description;

    @Builder.Default
    @OneToMany(mappedBy = Week.Fields.plan, cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private final List<Week> weeks = new ArrayList<>();

    public void addToWeeks(Week week) {
        if (week == null) {
            return;
        }

        week.setPlan(this);
        this.weeks.add(week);
    }

}
