package gg.warcraft.legends.entity;

import gg.warcraft.monolith.entity.MonolithEntity;

public interface LegendsEntity extends MonolithEntity {

    @Override
    LegendsRace getRace();

    void setRace(LegendsRace race);
}
