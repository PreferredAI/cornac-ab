package ai.preferred.cornac.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "experiment")
public class Experiment {
    @Id
    private String id;
    private ExperimentType type;
    @Field(type = FieldType.Date, format = DateFormat.basic_date_time)
    private Date startDateTime;
    @Field(type = FieldType.Date, format = DateFormat.basic_date_time)
    private Date endDateTime;
    private Long userSeed;
    private Integer timeHoursSwitch;

    private ExperimentStatus status;

}

