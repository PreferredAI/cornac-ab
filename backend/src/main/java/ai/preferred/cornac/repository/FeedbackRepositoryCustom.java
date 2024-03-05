package ai.preferred.cornac.repository;

import java.util.List;

public interface FeedbackRepositoryCustom {
    public List<String> topItems(Integer experimentId, int limit);
    public List<String> randomItems(int limit);
}
