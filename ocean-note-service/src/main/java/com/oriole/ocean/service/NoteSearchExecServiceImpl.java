package com.oriole.ocean.service;

import com.oriole.ocean.common.po.es.NoteSearchEntity;
import com.oriole.ocean.common.po.mysql.NoteEntity;
import com.oriole.ocean.common.service.NoteSearchExecService;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

@Service
public class NoteSearchExecServiceImpl implements NoteSearchExecService {

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Override
    public SearchHits<NoteEntity> searchNote(String keywords, Integer page, Integer rows) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .should(QueryBuilders.fuzzyQuery("analyzer_content", keywords).fuzziness(Fuzziness.AUTO))
                .should(QueryBuilders.fuzzyQuery("analyzer_tag", keywords).fuzziness(Fuzziness.AUTO))
                .must(QueryBuilders.multiMatchQuery(keywords, "analyzer_content", "analyzer_tag"))
                .must(QueryBuilders.matchQuery("is_deleted", false));

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withHighlightFields(
                        new HighlightBuilder.Field("analyzer_content"),
                        new HighlightBuilder.Field("analyzer_tag"))
                .withHighlightBuilder(new HighlightBuilder().preTags("<span class='highlight'>").postTags("</span>"))
                .withPageable(PageRequest.of(page - 1, rows))
                .build();

        SearchHits<NoteEntity> searchHits = (SearchHits<NoteEntity>) elasticsearchRestTemplate.search(searchQuery, NoteSearchEntity.class)
                .map( hit -> {
                    NoteEntity noteEntity = new NoteEntity();
                    BeanUtils.copyProperties(hit.getContent(), noteEntity);
                    return new SearchHit<>(
                            hit.getIndex(),
                            hit.getId(),
                            hit.getRouting(),
                            hit.getScore(),
                            hit.getSortValues().toArray(),
                            hit.getHighlightFields(),
                            noteEntity
                    );
                });
        return searchHits;
    }

    @Override
    public ArrayList<String> suggestTitle(String keyword, Integer rows) {
        return suggest("suggest_title",keyword,rows);
    }

    public ArrayList<String> suggest(String fieldName, String keyword,Integer rows) {
        HashSet<String> returnSet = new LinkedHashSet<>();

        CompletionSuggestionBuilder textBuilder = SuggestBuilders.completionSuggestion(fieldName)
                .size(rows)
                .skipDuplicates(true);

        SuggestBuilder suggestBuilder = new SuggestBuilder();
        suggestBuilder.addSuggestion("suggest_text", textBuilder)
                .setGlobalText(keyword);

        Suggest suggest = elasticsearchRestTemplate.suggest(suggestBuilder, elasticsearchRestTemplate.getIndexCoordinatesFor(NoteSearchEntity.class)).getSuggest();

        Suggest.Suggestion<Suggest.Suggestion.Entry<CompletionSuggestion.Entry.Option>> textSuggestion = suggest.getSuggestion("suggest_text");
        for (Suggest.Suggestion.Entry<CompletionSuggestion.Entry.Option> entry : textSuggestion.getEntries()) {
            List<CompletionSuggestion.Entry.Option> options = entry.getOptions();
            for (Suggest.Suggestion.Entry.Option option : options) {
                returnSet.add(option.getText().toString());
            }
        }
        return new ArrayList<>(returnSet);
    }
}
