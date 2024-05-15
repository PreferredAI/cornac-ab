




package ai.preferred.cornac.repository;

import ai.preferred.cornac.entity.FeedbackModelSummary;
import org.opensearch.action.search.SearchRequest;
import org.opensearch.action.search.SearchResponse;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.index.query.*;
import org.opensearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.opensearch.index.query.functionscore.ScoreFunctionBuilders;
import org.opensearch.search.aggregations.Aggregation;
import org.opensearch.search.aggregations.AggregationBuilders;
import org.opensearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.opensearch.search.aggregations.bucket.terms.ParsedTerms;
import org.opensearch.search.aggregations.bucket.terms.Terms;
import org.opensearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.opensearch.search.aggregations.metrics.CardinalityAggregationBuilder;
import org.opensearch.search.aggregations.metrics.ParsedCardinality;
import org.opensearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class FeedbackRepositoryImpl implements FeedbackRepositoryCustom {

    private final static Logger LOGGER = LoggerFactory.getLogger(FeedbackRepositoryImpl.class);

    @Autowired
    private RestHighLevelClient openSearchClient;

    @Override
    public List<String> topItems(Integer experimentId, int limit) {

        MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery("experiment_id", experimentId);

        TermsAggregationBuilder aggregation = AggregationBuilders.terms("top_items")
                .field("item_id.keyword")
                .size(limit);

        SearchSourceBuilder builder = new SearchSourceBuilder().query(queryBuilder).aggregation(aggregation);

        SearchRequest searchRequest = new SearchRequest().indices("feedback").source(builder);
        SearchResponse response = null;
        try {
            response = openSearchClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            LOGGER.error("Error occurred while fetching top items", e);
            return null;
        }

        Map<String, Aggregation> results = response.getAggregations().asMap();
        ParsedTerms topItems = (ParsedTerms) results.get("top_items");

        List<String> keys = topItems.getBuckets()
                .stream()
                .map(bucket -> bucket.getKeyAsString())
                .collect(Collectors.toList());

        return keys;
    }

    @Override
    public List<String> randomItems(int limit) {
        // random sample items from index
        FunctionScoreQueryBuilder functionScore = QueryBuilders.functionScoreQuery(QueryBuilders.matchAllQuery(), ScoreFunctionBuilders.randomFunction());
        SearchSourceBuilder builder = new SearchSourceBuilder().query(functionScore).size(limit);
        SearchRequest searchRequest = new SearchRequest().indices("items").source(builder);

        SearchResponse response = null;

        try {
            response = openSearchClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            LOGGER.error("Error occurred while fetching random items", e);
            return new ArrayList<>();
        }

        List<String> ids = Arrays.stream(response.getHits().getHits())
                .map(hit -> hit.getId())
                .toList();

        return ids;
    }

    @Override
    public List<FeedbackModelSummary> getFeedbackModelSummary(Integer experimentId, List<String> models, String dateFrom, String dateTo) {
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("experiment_id", experimentId);

        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("timestamp")
                .gte(dateFrom)
                .lte(dateTo);

        TermsQueryBuilder termsQueryBuilder = QueryBuilders.termsQuery("model.keyword", models);

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(matchQueryBuilder).must(rangeQueryBuilder).filter(termsQueryBuilder);


        CardinalityAggregationBuilder userCountAggregation = AggregationBuilders.cardinality("distinct_users").field("user_id.keyword");
        CardinalityAggregationBuilder itemCountAggregation = AggregationBuilders.cardinality("distinct_items").field("item_id.keyword");

        TermsAggregationBuilder modelAggregation = AggregationBuilders.terms("feedbacks").field("model.keyword")
                .subAggregation(userCountAggregation).subAggregation(itemCountAggregation);

        SearchSourceBuilder builder = new SearchSourceBuilder()
                .query(boolQueryBuilder)
                .aggregation(modelAggregation);

        SearchRequest searchRequest = new SearchRequest().indices("feedback").source(builder);

        SearchResponse response;
        try {
            response = openSearchClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            LOGGER.error("Error occurred while fetching feedback model summary", e);
            return null;
        }

        Map<String, Aggregation> results = response.getAggregations().asMap();
        ParsedStringTerms modelAgg = (ParsedStringTerms) results.get("feedbacks");

        List<FeedbackModelSummary> summaries = new ArrayList<>();
        for (Terms.Bucket bucket : modelAgg.getBuckets()) {
            ParsedCardinality userCount = bucket.getAggregations().get("distinct_users");
            ParsedCardinality itemCount = bucket.getAggregations().get("distinct_items");
            FeedbackModelSummary summary = new FeedbackModelSummary(bucket.getKeyAsString(), bucket.getDocCount(), userCount.getValue(), itemCount.getValue());
            summaries.add(summary);
        }

        return summaries;
    }
}
