package gg.warcraft.legends.objective;

import gg.warcraft.monolith.util.Timer;
import org.bukkit.Bukkit;

import java.util.Observable;
import java.util.Observer;

public class SkirmishCountdownTimer implements Observer {

    public SkirmishCountdownTimer() {
        Timer timer = new Timer(60, false);
        timer.addObserver(this);
    }

    @Override
    public void update(Observable observable, Object arg) {
        Timer timer = (Timer) observable;
        String timerAsString = timer.toString();
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.sendTitle("", "Skirmish in " + timerAsString);
        });
    }
}
