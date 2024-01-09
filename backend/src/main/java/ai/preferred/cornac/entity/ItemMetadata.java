package ai.preferred.cornac.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class ItemMetadata {
    private String itemId;
    private String title;
    private String description;
    private String imageUrl;
    private Map<String, String> additionalInfo;
}
