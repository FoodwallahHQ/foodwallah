package ai.kalico.api.service.instagram4j.actions.users;

import ai.kalico.api.service.instagram4j.IGClient;
import ai.kalico.api.service.instagram4j.actions.feed.FeedIterable;
import ai.kalico.api.service.instagram4j.models.friendships.Friendship;
import ai.kalico.api.service.instagram4j.models.user.User;
import ai.kalico.api.service.instagram4j.requests.friendships.FriendshipsActionRequest;
import ai.kalico.api.service.instagram4j.requests.friendships.FriendshipsActionRequest.FriendshipsAction;
import ai.kalico.api.service.instagram4j.requests.friendships.FriendshipsFeedsRequest;
import ai.kalico.api.service.instagram4j.requests.friendships.FriendshipsFeedsRequest.FriendshipsFeeds;
import ai.kalico.api.service.instagram4j.requests.friendships.FriendshipsShowRequest;
import ai.kalico.api.service.instagram4j.responses.feed.FeedUsersResponse;
import ai.kalico.api.service.instagram4j.responses.friendships.FriendshipStatusResponse;
import ai.kalico.api.service.instagram4j.responses.friendships.FriendshipsShowResponse;
import java.util.concurrent.CompletableFuture;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserAction {
    @NonNull
    private IGClient client;
    @NonNull
    @Getter
    private User user;

    public FeedIterable<FriendshipsFeedsRequest, FeedUsersResponse> followersFeed() {
        return new FeedIterable<>(client, () ->
                new FriendshipsFeedsRequest(user.getPk(), FriendshipsFeeds.FOLLOWERS));
    }

    public FeedIterable<FriendshipsFeedsRequest, FeedUsersResponse> followingFeed() {
        return new FeedIterable<>(client, () ->
                new FriendshipsFeedsRequest(user.getPk(), FriendshipsFeeds.FOLLOWING));
    }

    public CompletableFuture<Friendship> getFriendship() {
        return new FriendshipsShowRequest(user.getPk()).execute(client)
                .thenApply(FriendshipsShowResponse::getFriendship);
    }

    public CompletableFuture<FriendshipStatusResponse> action(FriendshipsAction action) {
        return new FriendshipsActionRequest(user.getPk(), action).execute(client);
    }
}
