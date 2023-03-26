package ai.kalico.api.service.instagram4j.requests.users;

import ai.kalico.api.service.instagram4j.requests.IGGetRequest;
import ai.kalico.api.service.instagram4j.responses.users.UsersBlockedListResponse;

public class UsersBlockedListRequest extends IGGetRequest<UsersBlockedListResponse> {

	@Override
    public String path() {
        return "users/blocked_list/";
    }

	@Override
	public Class<UsersBlockedListResponse> getResponseType() {
		return UsersBlockedListResponse.class;
	}

}
