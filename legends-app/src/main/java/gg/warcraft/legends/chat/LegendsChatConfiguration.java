package gg.warcraft.legends.chat;

import com.google.inject.Inject;
import gg.warcraft.monolith.api.chat.Channel;
import gg.warcraft.monolith.api.chat.ChannelFactory;
import gg.warcraft.monolith.api.chat.ChatService;
import gg.warcraft.monolith.api.chat.MessageFormatter;
import gg.warcraft.monolith.api.chat.MessageFormatterFactory;
import gg.warcraft.monolith.api.core.AuthorizationService;
import gg.warcraft.monolith.api.core.EventService;
import gg.warcraft.monolith.api.util.ColorCode;
import gg.warcraft.monolith.app.chat.event.PlayerHomeChannelPersister;

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
    private final ChannelFactory channelFactory;
    private final MessageFormatterFactory formatterFactory;

    @Inject
    public LegendsChatConfiguration(ChatService chatService, EventService eventService,
                                    AuthorizationService authorizationService, ChannelFactory channelFactory,
                                    MessageFormatterFactory formatterFactory, PlayerHomeChannelPersister homeChannelPersister) {
        this.authorizationService = authorizationService;
        this.channelFactory = channelFactory;
        this.formatterFactory = formatterFactory;

        // Global channels
        Channel globalChannel = createGlobalChannel();
        chatService.registerChannel(globalChannel);
        eventService.subscribe(globalChannel);

        Channel staffChannel = createStaffChannel();
        chatService.registerChannel(staffChannel);
        eventService.subscribe(staffChannel);

        Channel humanChannel = createHumanChannel();
        chatService.registerChannel(humanChannel);
        eventService.subscribe(humanChannel);

        Channel orcChannel = createOrcChannel();
        chatService.registerChannel(orcChannel);
        eventService.subscribe(orcChannel);

        // Local channels
        Channel localChannel = createLocalChannel();
        chatService.registerChannel(localChannel);

        Channel emoteChannel = createEmoteChannel();
        chatService.registerChannel(emoteChannel);

        Channel yellChannel = createYellChannel();
        chatService.registerChannel(yellChannel);

        Channel whisperChannel = createWhisperChannel();
        chatService.registerChannel(whisperChannel);

        // Remaining setup
        homeChannelPersister.setDefaultChannel(globalChannel);
        eventService.subscribe(homeChannelPersister);
    }

    private Channel createGlobalChannel() {
        MessageFormatter formatter = formatterFactory.createMessageFormatter(DEFAULT_GLOBAL_FORMATTING_STRING);
        Predicate<UUID> joinCondition = playerId -> true; // TODO check if id has left channel in jedis
        return channelFactory.createGlobalChannel("Global", Collections.emptyList(), null,
                ColorCode.GRAY, formatter, joinCondition);
    }

    private Channel createStaffChannel() {
        MessageFormatter formatter = formatterFactory.createMessageFormatter(NO_RANK_GLOBAL_FORMATTING_STRING);
        return channelFactory.createGlobalChannel("Staff", Collections.emptyList(), null,
                ColorCode.LIGHT_PURPLE, formatter, authorizationService::isStaff);
    }

    private Channel createHumanChannel() {
        MessageFormatter formatter = formatterFactory.createMessageFormatter(DEFAULT_GLOBAL_FORMATTING_STRING);
        Predicate<UUID> joinCondition = id -> true; // TODO check player race == HUMAN
        return channelFactory.createGlobalChannel("Human", Collections.emptyList(), null,
                ColorCode.GRAY, formatter, joinCondition);
    }

    private Channel createOrcChannel() {
        MessageFormatter formatter = formatterFactory.createMessageFormatter(DEFAULT_GLOBAL_FORMATTING_STRING);
        Predicate<UUID> joinCondition = id -> true; // TODO check player race == ORC
        return channelFactory.createGlobalChannel("Orc", Collections.emptyList(), null,
                ColorCode.GRAY, formatter, joinCondition);
    }

    private Channel createLocalChannel() {
        MessageFormatter formatter = formatterFactory.createMessageFormatter(DEFAULT_LOCAL_FORMATTING_STRING);
        return channelFactory.createLocalChannel("Local", Arrays.asList("say", "s", "rp"), "((",
                ColorCode.WHITE, formatter, DEFAULT_LOCAL_RADIUS);
    }

    private Channel createEmoteChannel() {
        MessageFormatter formatter = formatterFactory.createMessageFormatter(EMOTE_LOCAL_FORMATTING_STRING);
        return channelFactory.createLocalChannel("Emote", Arrays.asList("e", "pose"), "*",
                ColorCode.GOLD, formatter, DEFAULT_LOCAL_RADIUS);
    }

    private Channel createYellChannel() {
        MessageFormatter formatter = formatterFactory.createMessageFormatter(DEFAULT_LOCAL_FORMATTING_STRING);
        return channelFactory.createLocalChannel("Yell", Arrays.asList("y", "shout"), null,
                ColorCode.RED, formatter, FAR_LOCAL_RADIUS);
    }

    private Channel createWhisperChannel() {
        MessageFormatter formatter = formatterFactory.createMessageFormatter(DEFAULT_LOCAL_FORMATTING_STRING);
        return channelFactory.createLocalChannel("Whisper", Arrays.asList("w"), null,
                ColorCode.AQUA, formatter, NEAR_LOCAL_RADIUS);
    }
}
