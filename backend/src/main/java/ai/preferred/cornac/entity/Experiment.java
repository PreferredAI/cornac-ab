package ai.preferred.cornac.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "experiment")
@Entity(name = "ITEM_ENTITY")
public class Experiment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "experiment_id")
    private Integer id;
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp startDateTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp endDateTime;
    private Long userSeed;
    private ExperimentStatus status;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "experiment")
    private List<UserAbAllocation> userAbAllocation;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "experiment")
    private List<CornacInstance> cornacInstances;

    public Experiment(Timestamp startDateTime, Timestamp endDateTime, Long userSeed, ExperimentStatus status) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.userSeed = userSeed;
        this.status = status;
        this.userAbAllocation = new ArrayList<>();
    }
}

