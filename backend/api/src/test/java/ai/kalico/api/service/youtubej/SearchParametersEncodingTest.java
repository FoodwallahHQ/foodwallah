package ai.kalico.api.service.youtubej;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ai.kalico.api.service.ServiceTestConfiguration;
import ai.kalico.api.service.youtubej.downloader.request.RequestSearchResult;
import ai.kalico.api.service.youtubej.model.search.field.DurationField;
import ai.kalico.api.service.youtubej.model.search.field.FeatureField;
import ai.kalico.api.service.youtubej.model.search.field.FormatField;
import ai.kalico.api.service.youtubej.model.search.field.SearchField;
import ai.kalico.api.service.youtubej.model.search.field.SortField;
import ai.kalico.api.service.youtubej.model.search.field.TypeField;
import ai.kalico.api.service.youtubej.model.search.field.UploadDateField;
import lombok.extern.slf4j.Slf4j;

import ai.kalico.api.service.youtubej.model.search.field.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@Slf4j
@SpringBootTest(classes = ServiceTestConfiguration.class)
public class SearchParametersEncodingTest extends AbstractTestNGSpringContextTests  {

    @Test
    void encodeUploadDate_Success() {
        assertFieldEncoding(UploadDateField.HOUR,  "EgIIAQ%253D%253D");
        assertFieldEncoding(UploadDateField.DAY,   "EgIIAg%253D%253D");
        assertFieldEncoding(UploadDateField.WEEK,  "EgIIAw%253D%253D");
        assertFieldEncoding(UploadDateField.MONTH, "EgIIBA%253D%253D");
        assertFieldEncoding(UploadDateField.YEAR,  "EgIIBQ%253D%253D");
    }

    @Test
    void encodeType_Success() {
        assertFieldEncoding(TypeField.VIDEO,    "EgIQAQ%253D%253D");
        assertFieldEncoding(TypeField.CHANNEL,  "EgIQAg%253D%253D");
        assertFieldEncoding(TypeField.PLAYLIST, "EgIQAw%253D%253D");
        assertFieldEncoding(TypeField.MOVIE,    "EgIQBA%253D%253D");
    }

    @Test
    void encodeDuration_Success() {
        assertFieldEncoding(DurationField.UNDER_4_MINUTES,      "EgIYAQ%253D%253D");
        assertFieldEncoding(DurationField.FROM_4_TO_20_MINUTES, "EgIYAw%253D%253D");
        assertFieldEncoding(DurationField.OVER_20_MINUTES,      "EgIYAg%253D%253D");
    }

    @Test
    void encodeFeature_Success() {
        assertFieldEncoding(FeatureField.LIVE,      "EgJAAQ%253D%253D");
        assertFieldEncoding(FeatureField.SUBTITLES, "EgIoAQ%253D%253D");
        assertFieldEncoding(
                FeatureField.CREATIVE_COMMONS,      "EgIwAQ%253D%253D");
        assertFieldEncoding(FeatureField.LOCATION,  "EgO4AQE%253D");
        assertFieldEncoding(FeatureField.PURCHASED, "EgJIAQ%253D%253D");
    }

    @Test
    void encodeFormat_Success() {
        assertFieldEncoding(FormatField._4K,       "EgJwAQ%253D%253D");
        assertFieldEncoding(FormatField.HD,        "EgIgAQ%253D%253D");
        assertFieldEncoding(FormatField._360,      "EgJ4AQ%253D%253D");
        assertFieldEncoding(FormatField.VR180,     "EgPQAQE%253D");
        assertFieldEncoding(FormatField._3D,       "EgI4AQ%253D%253D");
        assertFieldEncoding(FormatField.HDR,       "EgPIAQE%253D");
    }

    private static void assertFieldEncoding(SearchField field, String expected) {
        String actual = new RequestSearchResult("a")
                .filter(field)
                .encodeParameters();
        assertEquals(expected, actual, "Filter on " + field.name());
    }

    @Test
    void encodeSortField_Success() {
        // Relevance is the default sort field
//        assertSortFieldEncoding(SearchSortField.RELEVANCE,   "CAA%253D");
        assertSortFieldEncoding(SortField.RATING,      "CAE%253D");
        assertSortFieldEncoding(SortField.UPLOAD_DATE, "CAI%253D");
        assertSortFieldEncoding(SortField.VIEW_COUNT,  "CAM%253D");
    }

    private static void assertSortFieldEncoding(SortField sortField, String expected) {
        String actual = new RequestSearchResult("a")
                .sortBy(sortField)
                .encodeParameters();
        assertEquals(expected, actual, "Sort by " + sortField.name());
    }

    @Test
    void encodeCombinations_Success() {
        assertCombinationEncoding(
                "Video/less than 4 minutes/by upload date",
                new RequestSearchResult("a")
                        .filter(TypeField.VIDEO, DurationField.UNDER_4_MINUTES)
                        .sortBy(SortField.UPLOAD_DATE),
                "CAISBBABGAE%253D");

        assertCombinationEncoding(
                "Video/today/20 minutes +/by view count",
                new RequestSearchResult("a")
                        .filter(
                                TypeField.VIDEO,
                                UploadDateField.DAY,
                                DurationField.OVER_20_MINUTES)
                        .sortBy(SortField.VIEW_COUNT),
                "CAMSBggCEAEYAg%253D%253D");

        assertCombinationEncoding(
                "Video/4K/360/3D/HDR/by view count",
                new RequestSearchResult("a")
                        .filter(
                                TypeField.VIDEO,
                                FormatField._4K,
                                FormatField._360,
                                FormatField._3D,
                                FormatField.HDR)
                        .sortBy(SortField.VIEW_COUNT),
                "CAMSCxABOAFwAXgByAEB");
    }

    @Test
    void encodeForcedCombinations_Success() {
        assertCombinationEncoding(
                "Forced",
                new RequestSearchResult("a")
                        .forceExactQuery(true),
                "QgIIAQ%253D%253D");

        assertCombinationEncoding(
                "Forced video by upload date",
                new RequestSearchResult("a")
                        .forceExactQuery(true)
                        .type(TypeField.VIDEO)
                        .sortBy(SortField.UPLOAD_DATE),
                "CAISAhABQgIIAQ%253D%253D");
    }

    private static void assertCombinationEncoding(String label, RequestSearchResult request, String expected) {
        String actual = request.encodeParameters();
        assertEquals(expected, actual, label);
    }
}
