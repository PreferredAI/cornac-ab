package ai.preferred.cornac.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.DateTime;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
@NoArgsConstructor
@Document(indexName = "feedback")
public class Feedback {
    @Field(type = FieldType.Keyword)
    private Integer experimentId;
    @Field(type = FieldType.Keyword)
    private String userId;
    @Field(type = FieldType.Keyword)
    private String itemId;
    private Integer rating;
    @Field(type = FieldType.Date)
    private DateTime timestamp;
}
