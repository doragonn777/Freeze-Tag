package doragoso.sample;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PlaySoundLater extends BukkitRunnable {

    private final Player p;
    private final Sound s;
    private final float vol;
    private final float pitch;
    public PlaySoundLater (Player p, Sound s, float vol, float pitch) {
        this.p = p;
        this.s = s;
        this.vol = vol;
        this.pitch = pitch;
    }
    
    @Override
    public void run() {
        p.playSound(p.getLocation(), s, vol, pitch);
    }
}
