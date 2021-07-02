package doragoso.sample;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static doragoso.sample.GameSystem.frozen;

public class FreezeTimer extends BukkitRunnable {
    public Player player;
    public FreezeTimer (Player p) {
        this.player = p;
    }
    public void run() {
        if (frozen.getEntries().contains(player.getName())) {
            player.spawnParticle(Particle.BLOCK_CRACK, player.getLocation(), 50, 0.5, 1.0, 0.5, Material.PACKED_ICE.createBlockData());
            player.playSound(player.getLocation(), Sound.BLOCK_GLASS_BREAK, 1.0F, 1.0F);
            if (player.getHealth() > 1.0) {
                player.setHealth(player.getHealth() - 1.0);
            } else {
                player.setGameMode(GameMode.SPECTATOR);
                player.sendMessage(ChatColor.RED + "あなたは氷漬けにされてしまった！");
                cancel();
            }
        }
    }
}
