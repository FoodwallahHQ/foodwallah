package ai.kalico.api.service.instagram4j.actions;

import ai.kalico.api.service.instagram4j.IGClient;
import ai.kalico.api.service.instagram4j.actions.account.AccountAction;
import ai.kalico.api.service.instagram4j.actions.igtv.IgtvAction;
import ai.kalico.api.service.instagram4j.actions.search.SearchAction;
import ai.kalico.api.service.instagram4j.actions.simulate.SimulateAction;
import ai.kalico.api.service.instagram4j.actions.status.StatusAction;
import ai.kalico.api.service.instagram4j.actions.story.StoryAction;
import ai.kalico.api.service.instagram4j.actions.timeline.TimelineAction;
import ai.kalico.api.service.instagram4j.actions.upload.UploadAction;
import ai.kalico.api.service.instagram4j.actions.users.UsersAction;
import java.lang.reflect.Field;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

@Accessors(fluent = true, prefix = "_")
@Getter
public class IGClientActions {
    private UploadAction _upload;
    private TimelineAction _timeline;
    private StoryAction _story;
    private UsersAction _users;
    private SimulateAction _simulate;
    private IgtvAction _igtv;
    private AccountAction _account;
    private SearchAction _search;
    private StatusAction _status;

    @SneakyThrows
    public IGClientActions(IGClient client) {
        for (Field field : this.getClass().getDeclaredFields())
            if (field.getName().startsWith("_"))
                field.set(this, field.getType().getConstructor(IGClient.class).newInstance(client));
    }

}
