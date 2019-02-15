package gg.warcraft.legends.api.core.challenge.service;

import gg.warcraft.legends.api.core.challenge.Challenge;

import java.util.List;

public interface ChallengeRepository {

    List<Challenge> getDailies();

    void setDailies(List<Challenge> challenges);

    List<Challenge> getWarries();

    void setWarries(List<Challenge> challenges);
}
