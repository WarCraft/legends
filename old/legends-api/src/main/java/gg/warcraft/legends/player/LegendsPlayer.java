package gg.warcraft.legends.player;

import gg.warcraft.legends.combat.Specialization;
import gg.warcraft.legends.entity.LegendsEntity;
import gg.warcraft.monolith.player.MonolithPlayer;

public interface LegendsPlayer extends MonolithPlayer, LegendsEntity {

    Specialization getSpecialization();

    void setSpecialization(Specialization spec);

    String getDescription();

    void setDescription(String description);
}
