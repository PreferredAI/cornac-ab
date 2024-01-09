package ai.preferred.cornac.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "recommend_log")
public class RecommendLog {
    @Id
    private String id;
    @Field(type = FieldType.Keyword)
    private Integer experimentId;
    @Field(type = FieldType.Keyword)
    private String userId;
    @Field(type = FieldType.Date)
    private DateTime timestamp;
    @Field(type = FieldType.Keyword)
    private List<String> recommendations;
}
