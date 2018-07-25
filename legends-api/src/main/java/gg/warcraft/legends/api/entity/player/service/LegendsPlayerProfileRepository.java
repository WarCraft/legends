package gg.warcraft.legends.api.entity.player.service;

import gg.warcraft.legends.api.entity.player.LegendsPlayerProfile;

import java.util.UUID;

public interface LegendsPlayerProfileRepository /* extends PlayerProfileRepository */ {

  //  @Override
  LegendsPlayerProfile get(UUID playerId);

  //  @Override
  void save(LegendsPlayerProfile data);
}
