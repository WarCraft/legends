package gg.warcraft.legends;

import gg.warcraft.legends.entity.LegendsRace;
import gg.warcraft.legends.player.LegendsPlayer;
import gg.warcraft.monolith.entity.Race;
import gg.warcraft.monolith.event.EventService;
import gg.warcraft.monolith.event.MonolithEntityRaceChangedEvent;
import gg.warcraft.monolith.menu.Button;
import gg.warcraft.monolith.menu.ButtonSlot;
import gg.warcraft.monolith.menu.Menu;

public class RacePicker extends Menu {

    private final EventService eventService;
    private final LegendsPlayer player;

    public RacePicker(LegendsPlayer player) {
        this(EventService.getInstance(), player);
    }

    public RacePicker(EventService eventService, LegendsPlayer player) {
        super(player, "Race picker");
        this.eventService = eventService;
        this.player = player;
        update(ButtonSlot.FIRST, new Button("eijlen", "Human", () -> setRace(LegendsRace.HUMAN)));
        update(ButtonSlot.SECOND, new Button("fishb6nes", "Orc", () -> setRace(LegendsRace.ORC)));
        // update(ButtonSlot.THIRD, new Button(Material.SKULL_ITEM, "Elf", () -> setRace(LegendsRace.ELF)));
        // update(ButtonSlot.FOURTH, new Button(Material.SKULL_ITEM, "Dwarf", () -> setRace(LegendsRace.DWARF)));
    }

    public void setRace(LegendsRace race) {
        Race previous = player.getRace();
        player.setRace(race);
        eventService.publishEvent(new MonolithEntityRaceChangedEvent(player, previous));
        close();
    }
}
