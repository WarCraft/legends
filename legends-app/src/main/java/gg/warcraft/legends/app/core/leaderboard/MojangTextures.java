package gg.warcraft.legends.app.core.leaderboard;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class MojangTextures {
    private final String timestamp;
    private final String profileId;
    private final String profileName;
    private final boolean signatureRequired;
    private final Map<String, Map<String, String>> textures;

    @JsonCreator
    public MojangTextures(@JsonProperty("timestamp") String timestamp,
                          @JsonProperty("profileId") String profileId,
                          @JsonProperty("profileName") String profileName,
                          @JsonProperty("signatureRequired") boolean signatureRequired,
                          @JsonProperty("textures") Map<String, Map<String, String>> textures) {
        this.timestamp = timestamp;
        this.profileId = profileId;
        this.profileName = profileName;
        this.signatureRequired = signatureRequired;
        this.textures = textures;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getProfileId() {
        return profileId;
    }

    public String getProfileName() {
        return profileName;
    }

    public boolean isSignatureRequired() {
        return signatureRequired;
    }

    public Map<String, Map<String, String>> getTextures() {
        return textures;
    }

    public String getSkinUrl() {
        return getTextures().get("SKIN").get("url");
    }

    public String getCapeUrl() {
        return getTextures().get("CAPE").get("url");
    }
}
