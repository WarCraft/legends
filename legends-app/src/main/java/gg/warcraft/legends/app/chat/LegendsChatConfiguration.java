package gg.warcraft.legends.app.chat;

import com.google.inject.Inject;
import gg.warcraft.monolith.api.chat.ChatRouter;
import gg.warcraft.monolith.api.chat.channel.Channel;
import gg.warcraft.monolith.api.chat.channel.ChannelCommandService;
import gg.warcraft.monolith.api.core.AuthorizationService;
import gg.warcraft.monolith.api.event.EventService;
import gg.warcraft.monolith.api.util.ColorCode;
import gg.warcraft.monolith.app.chat.profile.handler.ChatProfileInitializationHandler;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;
import java.util.function.Predicate;

public class LegendsChatConfiguration {
    private static final String DEFAULT_GLOBAL_FORMATTING_STRING = "[{channel.name}] {sender.color}[{sender.tag}] {channel.color}{sender.name}: {text}";
    private static final String NO_RANK_GLOBAL_FORMATTING_STRING = "[{channel.name}] {sender.name}: {text}";
    private static final String DEFAULT_LOCAL_FORMATTING_STRING = "{sender.color}[{sender.tag}] {channel.color}{sender.name}: {text}";
    private static final String EMOTE_LOCAL_FORMATTING_STRING = "{sender.color}[{sender.tag}] {channel.color}{sender.name} {text}";
    private static final float DEFAULT_LOCAL_RADIUS = 30;
    private static final float FAR_LOCAL_RADIUS = 60;
    private static final float NEAR_LOCAL_RADIUS = 5;

    private final AuthorizationService authorizationService;
    private final ChannelCommandService channelCommandService;

    @Inject
    public LegendsChatConfiguration(ChatRouter chatRouter, ChannelCommandService channelCommandService,
                                    ChatProfileInitializationHandler chatProfileHandler,
                                    EventService eventService, AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
        this.channelCommandService = channelCommandService;

        // Global channels
        Channel globalChannel = createGlobalChannel();
        Channel staffChannel = createStaffChannel();
        Channel humanChannel = createHumanChannel();
        Channel orcChannel = createOrcChannel();

        // Local channels
        Channel localChannel = createLocalChannel();
        Channel emoteChannel = createEmoteChannel();
        Channel yellChannel = createYellChannel();
        Channel whisperChannel = createWhisperChannel();

        // Remaining setup
        chatProfileHandler.setDefaultChannel(globalChannel);
        eventService.subscribe(chatProfileHandler);
        eventService.subscribe(chatRouter);
    }

    private Channel createGlobalChannel() {
        Predicate<UUID> joinCondition = playerId -> true; // TODO check if id has left channel in jedis
        return channelCommandService.createGlobalChannel("Global", Collections.emptyList(), null,
                ColorCode.GRAY, DEFAULT_GLOBAL_FORMATTING_STRING, joinCondition);
    }

    private Channel createStaffChannel() {
        return channelCommandService.createGlobalChannel("Staff", Collections.emptyList(), null,
                ColorCode.LIGHT_PURPLE, NO_RANK_GLOBAL_FORMATTING_STRING, authorizationService::isStaff);
    }

    private Channel createHumanChannel() {
        Predicate<UUID> joinCondition = id -> true; // TODO check player race == HUMAN
        return channelCommandService.createGlobalChannel("Human", Collections.emptyList(), null,
                ColorCode.GRAY, DEFAULT_GLOBAL_FORMATTING_STRING, joinCondition);
    }

    private Channel createOrcChannel() {
        Predicate<UUID> joinCondition = id -> true; // TODO check player race == ORC
        return channelCommandService.createGlobalChannel("Orc", Collections.emptyList(), null,
                ColorCode.GRAY, DEFAULT_GLOBAL_FORMATTING_STRING, joinCondition);
    }

    private Channel createLocalChannel() {
        return channelCommandService.createLocalChannel("Local", Arrays.asList("say", "s", "rp"), "((",
                ColorCode.WHITE, DEFAULT_LOCAL_FORMATTING_STRING, DEFAULT_LOCAL_RADIUS);
    }

    private Channel createEmoteChannel() {
        return channelCommandService.createLocalChannel("Emote", Arrays.asList("e", "pose"), "*",
                ColorCode.GOLD, EMOTE_LOCAL_FORMATTING_STRING, DEFAULT_LOCAL_RADIUS);
    }

    private Channel createYellChannel() {
        return channelCommandService.createLocalChannel("Yell", Arrays.asList("y", "shout"), null,
                ColorCode.RED, DEFAULT_LOCAL_FORMATTING_STRING, FAR_LOCAL_RADIUS);
    }

    private Channel createWhisperChannel() {
        return channelCommandService.createLocalChannel("Whisper", Arrays.asList("w"), null,
                ColorCode.AQUA, DEFAULT_LOCAL_FORMATTING_STRING, NEAR_LOCAL_RADIUS);
    }
}
