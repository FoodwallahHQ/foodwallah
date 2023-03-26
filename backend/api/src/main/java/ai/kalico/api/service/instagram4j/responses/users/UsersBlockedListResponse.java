package ai.kalico.api.service.instagram4j.responses.users;

import ai.kalico.api.service.instagram4j.responses.IGResponse;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UsersBlockedListResponse extends IGResponse {
	private List<BlockedUser> blocked_list;
	
	@Getter @Setter
	public static class BlockedUser{
	    public long user_id;
	    public String username;
	    public String full_name;
	    public String profile_pic_url;
	    public long block_at;
	    public boolean is_auto_block_enabled;
	}
}
