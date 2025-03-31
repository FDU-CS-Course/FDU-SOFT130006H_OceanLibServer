package com.oriole.ocean.service;

import co.elastic.clients.elasticsearch.core.search.CompletionSuggester;
import co.elastic.clients.elasticsearch.core.search.Suggester;
import com.oriole.ocean.common.po.es.FileSearchEntity;
import jakarta.annotation.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.HighlightQuery;
import org.springframework.data.elasticsearch.core.query.highlight.Highlight;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightField;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightFieldParameters;
import org.springframework.data.elasticsearch.core.suggest.response.Suggest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

@Service
public class ESearchServiceImpl {

    @Resource
    private ElasticsearchOperations elasticsearchOperations;

    public SearchHits<FileSearchEntity> searchFile(String keywords, Integer page, Integer rows) {

        HighlightFieldParameters highlightFieldParameters = HighlightFieldParameters.builder()
                .withMatchedFields(
                        "analyzer_title",
                        "analyzer_abstract_content",
                        "analyzer_content"
                )
                .withPreTags("<span class='highlight'>")
                .withPostTags("</span>")
                .build();

        //构建高亮查询
        Highlight highlight = new Highlight(
            List.of(
                new HighlightField("analyzer_title",
                        highlightFieldParameters),
                new HighlightField("analyzer_abstract_content",
                        highlightFieldParameters),
                new HighlightField("analyzer_content",
                        highlightFieldParameters)
            )
        );
        HighlightQuery highlightQuery = new HighlightQuery(highlight, FileSearchEntity.class);

        NativeQuery searchQuery = new NativeQueryBuilder()
                .withQuery(q -> q
                        .multiMatch(m -> m
                                .query(keywords)
                                .fields(
                                        "analyzer_title",
                                        "analyzer_content",
                                        "analyzer_abstract_content")))
                .withQuery(q -> q
                        .match(m -> m
                                .query("true")
                                .field("is_approved")))
                .withHighlightQuery(highlightQuery)
                .withPageable(PageRequest.of(page - 1, rows))
                .build();

        return elasticsearchOperations.search(searchQuery, FileSearchEntity.class);
    }
    public ArrayList<String> suggestTitle(String keyword,Integer rows) {
        return suggest("suggest_title",keyword,rows);
    }

    public ArrayList<String> suggest(String fieldName, String keyword,Integer rows) {
        HashSet<String> returnSet = new LinkedHashSet<>(); // 用于存储查询到的结果
        // 创建CompletionSuggestionBuilder
        CompletionSuggester.Builder textBuilder = new CompletionSuggester.Builder()
                .field(fieldName)
                .size(rows)
                .skipDuplicates(true);

        // 创建nativeQuery并将completionBuilder添加进去
        NativeQuery nativeQuery = new NativeQueryBuilder()
                .withSuggester(Suggester.of(s -> s
                        .suggesters("suggest_text", a -> a
                                .completion(textBuilder.build())
                                .prefix(keyword))))
                .build();

        // 执行请求
        SearchHits<FileSearchEntity> searchHits = elasticsearchOperations.search(nativeQuery, FileSearchEntity.class);

        // 取出结果
        if(searchHits.hasSuggest()) {
            Suggest suggest = searchHits.getSuggest();
            var textSuggestion = suggest.getSuggestion("suggest_text");
            for (var entry : textSuggestion.getEntries()) {
                for (Suggest.Suggestion.Entry.Option option : entry.getOptions()) {
                    returnSet.add(option.getText());
                }
            }
        }
        return new ArrayList<>(returnSet);
    }
}
