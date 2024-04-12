package ai.preferred.cornac.repository;

import ai.preferred.cornac.entity.FeedbackModelSummary;

import java.util.List;

public interface FeedbackRepositoryCustom {
    public List<String> topItems(Integer experimentId, int limit);
    public List<String> randomItems(int limit);

    List<FeedbackModelSummary> getFeedbackModelSummary(Integer experimentId, List<String> models, String dateFrom, String dateTo);
}
