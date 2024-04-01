package ai.preferred.cornac.dto;

import ai.preferred.cornac.entity.CornacInstance;
import ai.preferred.cornac.entity.ExperimentStatus;
import ai.preferred.cornac.entity.UserAbAllocation;
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
public class ExperimentDto {
    private Integer id;
    private Timestamp startDateTime;
    private Timestamp endDateTime;
    private Long userSeed;
    private ExperimentStatus status;
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "experiment")
//    private List<UserAbAllocation> userAbAllocation;
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "experiment")
    private List<CornacInstanceDto> cornacInstances;
}

