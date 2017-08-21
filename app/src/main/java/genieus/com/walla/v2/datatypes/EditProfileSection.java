package genieus.com.walla.v2.datatypes;

import com.firebase.client.annotations.Nullable;
import com.google.common.base.Optional;

/**
 * Created by anesu on 8/7/17.
 */

public class EditProfileSection {
    private String title;
    private Optional<String> description;
    private boolean showCharCount;
    private Optional<String> hintText;
    private Action onFinishAction;

    public interface Action {
        void onFinishAction(final String data);
    }

    public EditProfileSection(final String title,
                              final Optional<String> description,
                              final boolean showCharCount,
                              final Optional<String> hintText,
                              final @Nullable Action onFinishAction) {
        this.title = title;
        this.description = description;
        this.showCharCount = showCharCount;
        this.hintText = hintText;
        this.onFinishAction = onFinishAction;
    }

    public String getTitle() {
        return title;
    }

    public Optional<String> getDescription() {
        return description;
    }

    public boolean isShowCharCount() {
        return showCharCount;
    }

    public Optional<String> getHintText() {
        return hintText;
    }

    public Action getOnFinishAction() {
        return onFinishAction;
    }
}
