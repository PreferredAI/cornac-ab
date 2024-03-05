




package ai.preferred.cornac.repository;

import ai.preferred.cornac.entity.DemoItem;
import org.apache.lucene.util.QueryBuilder;
import org.opensearch.action.search.SearchRequest;
import org.opensearch.action.search.SearchResponse;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.data.client.orhlc.OpenSearchRestTemplate;
import org.opensearch.index.query.MatchQueryBuilder;
import org.opensearch.index.query.QueryBuilders;
import org.opensearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.opensearch.index.query.functionscore.ScoreFunctionBuilders;
import org.opensearch.search.SearchHit;
import org.opensearch.search.SearchHits;
import org.opensearch.search.aggregations.Aggregation;
import org.opensearch.search.aggregations.AggregationBuilders;
import org.opensearch.search.aggregations.bucket.sampler.SamplerAggregationBuilder;
import org.opensearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.opensearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
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
                .field("book_id.keyword")
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
        ParsedStringTerms topItems = (ParsedStringTerms) results.get("top_items");

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
}
