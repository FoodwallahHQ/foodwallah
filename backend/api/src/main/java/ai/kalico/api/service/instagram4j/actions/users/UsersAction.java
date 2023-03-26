package ai.kalico.api.service.instagram4j.actions.users;

import ai.kalico.api.service.instagram4j.IGClient;
import ai.kalico.api.service.instagram4j.models.user.User;
import ai.kalico.api.service.instagram4j.requests.users.UsersInfoRequest;
import ai.kalico.api.service.instagram4j.requests.users.UsersSearchRequest;
import ai.kalico.api.service.instagram4j.requests.users.UsersUsernameInfoRequest;
import ai.kalico.api.service.instagram4j.responses.users.UserResponse;
import ai.kalico.api.service.instagram4j.responses.users.UsersSearchResponse;
import java.util.concurrent.CompletableFuture;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UsersAction {
    @NonNull
    private IGClient client;

    public CompletableFuture<UserAction> findByUsername(String username) {
        return new UsersUsernameInfoRequest(username).execute(client)
                .thenApply(response -> new UserAction(client, response.getUser()));
    }

    public CompletableFuture<User> info(long pk) {
        return new UsersInfoRequest(pk).execute(client)
                .thenApply(UserResponse::getUser);
    }

    public CompletableFuture<UsersSearchResponse> search(String query) {
        return new UsersSearchRequest(query).execute(client);
    }

}
