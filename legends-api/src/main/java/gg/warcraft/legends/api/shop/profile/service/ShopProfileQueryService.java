package gg.warcraft.legends.api.shop.profile.service;

import gg.warcraft.legends.api.shop.profile.ShopProfile;

import java.util.UUID;

public interface ShopProfileQueryService {

    ShopProfile getProfile(UUID playerId);
}
