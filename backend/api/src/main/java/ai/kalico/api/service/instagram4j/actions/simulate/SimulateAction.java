package ai.kalico.api.service.instagram4j.actions.simulate;

import ai.kalico.api.service.instagram4j.IGClient;
import ai.kalico.api.service.instagram4j.actions.async.AsyncAction;
import ai.kalico.api.service.instagram4j.requests.IGRequest;
import ai.kalico.api.service.instagram4j.requests.accounts.AccountsContactPointPrefillRequest;
import ai.kalico.api.service.instagram4j.requests.accounts.AccountsGetPrefillCandidatesRequest;
import ai.kalico.api.service.instagram4j.requests.direct.DirectGetPresenceRequest;
import ai.kalico.api.service.instagram4j.requests.direct.DirectInboxRequest;
import ai.kalico.api.service.instagram4j.requests.discover.DiscoverTopicalExploreRequest;
import ai.kalico.api.service.instagram4j.requests.feed.FeedReelsTrayRequest;
import ai.kalico.api.service.instagram4j.requests.feed.FeedTimelineRequest;
import ai.kalico.api.service.instagram4j.requests.launcher.LauncherSyncRequest;
import ai.kalico.api.service.instagram4j.requests.linkedaccounts.LinkedAccountsGetLinkageStatusRequest;
import ai.kalico.api.service.instagram4j.requests.loom.LoomFetchConfigRequest;
import ai.kalico.api.service.instagram4j.requests.media.MediaBlockedRequest;
import ai.kalico.api.service.instagram4j.requests.multipleaccounts.MultipleAccountsGetAccountFamilyRequest;
import ai.kalico.api.service.instagram4j.requests.news.NewsInboxRequest;
import ai.kalico.api.service.instagram4j.requests.qe.QeSyncRequest;
import ai.kalico.api.service.instagram4j.requests.qp.QpGetCooldowns;
import ai.kalico.api.service.instagram4j.requests.status.StatusGetViewableStatusesRequest;
import ai.kalico.api.service.instagram4j.requests.users.UsersArlinkDownloadInfoRequest;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SimulateAction {
    @NonNull
    private IGClient client;

    private static final IGRequest<?>[] preLoginFlow = {
            new LauncherSyncRequest(true),
            new QeSyncRequest(true),
            new AccountsContactPointPrefillRequest(),
            new AccountsGetPrefillCandidatesRequest()
    };

    private static final IGRequest<?>[] postLoginFlow = {
            new LauncherSyncRequest(),
            new QpGetCooldowns(),
            new MultipleAccountsGetAccountFamilyRequest(),
            new LinkedAccountsGetLinkageStatusRequest(),
            new LoomFetchConfigRequest(),
            new MediaBlockedRequest(),
            new FeedTimelineRequest(),
            new FeedReelsTrayRequest(),
            new UsersArlinkDownloadInfoRequest(),
            new DiscoverTopicalExploreRequest().is_prefetch(true),
            new NewsInboxRequest(false),
            new DirectGetPresenceRequest(),
            new DirectInboxRequest().limit(0).visual_message_return_type("unseen")
                    .persistent_badging(true),
            new DirectInboxRequest().limit(20).fetch_reason("initial_snapshot")
                    .thread_message_limit(10).visual_message_return_type("unseen")
                    .persistent_badging(true),
            new StatusGetViewableStatusesRequest()
    };

    public List<CompletableFuture<?>> preLoginFlow() {
        return AsyncAction.executeRequestsAsync(client, preLoginFlow);
    }

    public List<CompletableFuture<?>> postLoginFlow() {
        return AsyncAction.executeRequestsAsync(client, postLoginFlow);
    }
}
