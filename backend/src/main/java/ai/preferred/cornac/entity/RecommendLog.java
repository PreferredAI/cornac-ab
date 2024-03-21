package ai.preferred.cornac.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "recommendations")
public class RecommendLog {
    @Id
    private String id;
    @Field(type = FieldType.Keyword)
    private Integer experimentId;
    @Field(type = FieldType.Keyword)
    private String userId;
    @Field(type = FieldType.Date, format = {}, pattern = "uuuu-MM-dd'T'hh:mm:ss.SSSSSS")
    private LocalDateTime timestamp;
    @Field(type = FieldType.Keyword)
    private List<String> recommendations;
}
