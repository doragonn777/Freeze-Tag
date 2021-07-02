package doragoso.sample;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class LaterEffect extends BukkitRunnable {

    private final Player player;

    public LaterEffect(Player p) {
        this.player = p;
    }

    @Override
    public void run() {
        player.spawnParticle(Particle.DRAGON_BREATH, player.getLocation(), 100, 0.1, 1.0, 0.1, 0.2);
        player.playSound(player.getLocation(), Sound.BLOCK_PORTAL_TRAVEL, 0.2F, 1.5F);
    }
}


