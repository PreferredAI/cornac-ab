package ai.preferred.cornac.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
@NoArgsConstructor
@Document(indexName = "feedback")
public class Feedback {
    @Id
    private String id;
    @Field(name="experiment_id", type = FieldType.Keyword)
    private Integer experimentId;
    @Field(name="user_id", type = FieldType.Keyword)
    private String userId;
    @Field(name="item_id", type = FieldType.Keyword)
    private String itemId;
    private String model;
    private Integer rating;
    private String action; // 'click', 'rating'
    @Field(type = FieldType.Date, pattern = "uuuu-MM-dd'T'HH:mm:ss.SSSSSS||uuuu-MM-dd'T'HH:mm:ss.SSS||uuuu-MM-dd'T'HH:mm:ss||uuuu-MM-dd", format = {})
    private LocalDateTime timestamp;

    @Override
    public String toString() {
        return "Feedback{" +
                "id='" + id + '\'' +
                ", experimentId=" + experimentId +
                ", userId='" + userId + '\'' +
                ", itemId='" + itemId + '\'' +
                ", rating=" + rating +
                ", timestamp=" + timestamp +
                '}';
    }
}
