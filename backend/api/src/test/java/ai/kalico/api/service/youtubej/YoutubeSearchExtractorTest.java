package ai.kalico.api.service.youtubej;

import static org.junit.jupiter.api.Assertions.*;

import ai.kalico.api.service.ServiceTestConfiguration;
import ai.kalico.api.service.youtubej.downloader.request.RequestSearchContinuation;
import ai.kalico.api.service.youtubej.downloader.request.RequestSearchResult;
import ai.kalico.api.service.youtubej.downloader.request.RequestSearchable;
import ai.kalico.api.service.youtubej.downloader.response.Response;
import ai.kalico.api.service.youtubej.model.search.SearchResult;
import ai.kalico.api.service.youtubej.model.search.SearchResultItem;
import ai.kalico.api.service.youtubej.model.search.SearchResultItemType;
import ai.kalico.api.service.youtubej.model.search.SearchResultVideoDetails;
import ai.kalico.api.service.youtubej.model.search.field.SortField;
import ai.kalico.api.service.youtubej.model.search.field.TypeField;
import ai.kalico.api.service.youtubej.model.search.query.QueryRefinementList;
import ai.kalico.api.service.youtubej.model.search.query.QuerySuggestion;
import ai.kalico.api.service.youtubej.model.search.query.Searchable;
import lombok.extern.slf4j.Slf4j;

import ai.kalico.api.service.youtubej.downloader.request.*;
import ai.kalico.api.service.youtubej.model.search.*;
import ai.kalico.api.service.youtubej.model.search.query.*;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

@Ignore
@Slf4j
@SpringBootTest(classes = ServiceTestConfiguration.class)
public class YoutubeSearchExtractorTest extends AbstractTestNGSpringContextTests {

    private YoutubeDownloader downloader;

    @BeforeMethod
    void initDownloader() {
        downloader = new YoutubeDownloader();
    }

    @Test
    void searchContinuation_Success() {
        assertDoesNotThrow(() -> {
            SearchResult result = search(new RequestSearchResult("stir fry vegetables"));
            assertTrue(result.estimatedResults() > 20_000_000, "Estimated results should be over 20 M");
            assertFalse(result.channels().isEmpty(), "Result should contain a channel");
            assertFalse(result.shelves().isEmpty(), "Result should contain the channel latest shelf");
            assertFalse(result.videos().isEmpty(), "Result should contain at least one video");

            // first continuation
            SearchResult next = downloader.searchContinuation(new RequestSearchContinuation(result)).data();
            assertTrue(next.estimatedResults() > 20_000_000, "Next page results should also be over 20 M");

            // second continuation, asserts not empty
            downloader.searchContinuation(new RequestSearchContinuation(next)).data();
        });
    }

    @Test(enabled = false)
    void searchAllTypes_Success() {
        assertDoesNotThrow(() -> {
            RequestSearchResult request = new RequestSearchResult("star wars");
            SearchResult result;

            result = search(request.type(TypeField.VIDEO));
            for (SearchResultItem item : result.items()) {
                assertTrue(item.type() == SearchResultItemType.VIDEO || item.type() == SearchResultItemType.SHELF,
                        "Video result should only contain videos, shelf and query suggestion");
            }

            result = search(request.type(TypeField.MOVIE));
            int movieCount = 0;
            for (SearchResultItem item : result.items()) {
                assertTrue(item.type() == SearchResultItemType.VIDEO, "Movie result should only contain videos");
                if (item.asVideo().isMovie()) {
                    movieCount++;
                }
            }
            assertTrue(movieCount > 0, "Movie result should contain at least one movie");

            result = search(request.type(TypeField.PLAYLIST));
            for (SearchResultItem item : result.items()) {
                assertTrue(item.type() == SearchResultItemType.PLAYLIST, "Playlist result should only contain playlists");
            }

            result = search(request.type(TypeField.CHANNEL));
            for (SearchResultItem item : result.items()) {
                assertTrue(item.type() == SearchResultItemType.CHANNEL, "Channel result should only contain channels");
            }
        });
    }

    @Test
    void searchVideosByViewCount_Success() {
        assertDoesNotThrow(() -> {
            SearchResult result = search(new RequestSearchResult("strange")
                    .filter(TypeField.VIDEO)
                    .sortBy(SortField.VIEW_COUNT));

            SearchResultVideoDetails lastVideo = null;
            for (SearchResultVideoDetails video : result.videos()) {
                if (lastVideo != null) {
                    // 5% margin
                    long viewCount = (video.viewCount() * 95L) / 100L;
                    assertTrue(viewCount < lastVideo.viewCount(), "View count -5% should be less than previous video's view count");
                }
                lastVideo = video;
            }
        });
    }

    @Test
    void searchAutoCorrectionOrSuggestion_Success() {
        final String expectedCorrection = "lord of the rings";
        assertDoesNotThrow(() -> {
            RequestSearchResult request = new RequestSearchResult("lord of the rngs");
            SearchResult result = search(request);
            QuerySuggestion suggestion = result.suggestion();
            if (suggestion == null) {
                assertTrue(result.isAutoCorrected(), "Result should be auto corrected or contain a suggestion");
                assertEquals(expectedCorrection, result.autoCorrectedQuery(), "Auto corrected query");

                // force initial query
                result = search(request.forceExactQuery(true));
                assertFalse(result.isAutoCorrected(), "Forced result should not be auto corrected");
                Assertions.assertNotNull(result.suggestion(), "Forced result should contain a suggestion");
                assertEquals(expectedCorrection, result.suggestion().query(), "Forced result query suggestion");
            } else {
                assertEquals(expectedCorrection, suggestion.query(), "Query suggestion");
            }
        });
    }

    @Test(enabled = false)
    void searchRefinement_Success() {
        assertDoesNotThrow(() -> {
            SearchResult result;
            result = search(new RequestSearchResult("michael jackson"));
            String initialTitle = result.items().get(0).title();
            QueryRefinementList refinements = result.refinements();
            assertNotNull(refinements, "Result should contain refinements");
            if (refinements != null) {
                assertFalse(refinements.isEmpty(), "Result refinements should not be empty");

                // refinement
                result = search(refinements.get(0));
                String refinedTitle = result.items().get(0).title();
                assertNotEquals(initialTitle, refinedTitle, "Refined title should be different");
            } else {
                System.out.println("No refinement found");
            }
        });
    }

    private SearchResult search(RequestSearchResult request) {
        return check(downloader.search(request));
    }

    private SearchResult search(Searchable searchable) {
        return check(downloader.search(new RequestSearchable(searchable)));
    }

    private static SearchResult check(Response<SearchResult> response) {
        if (!response.ok()) {
            response.error().printStackTrace();
        }
        assertTrue(response.ok());
        SearchResult result = response.data();
        assertFalse(result.estimatedResults() < 1, "search results should have a positive estimated count");
        assertNotNull(result.items(), "search result should contain items");
        assertFalse(result.items().isEmpty(), "search result should contain items");
        return result;
    }
}
