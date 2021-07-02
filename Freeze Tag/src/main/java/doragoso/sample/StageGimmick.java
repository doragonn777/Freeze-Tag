package doragoso.sample;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class StageGimmick implements Listener {
    World world = Bukkit.getWorld("world");
    Location EnderChestLoc = new Location(world, 1044.0, 35.0, 969.5, 90, 0);
    Location EndLoc = new Location(world, 1001.0, 6.0, 1001.0, 90, 0);

    @EventHandler
    public void OnEndPortalEnter (PlayerTeleportEvent e) {
        if (e.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL) {
            Player p = e.getPlayer();
            e.setCancelled(true);
            int x = (int) p.getLocation().getX();
            int y = (int) p.getLocation().getY();
            int z = (int) p.getLocation().getZ();

            if (world.getBlockAt(x, y - 1, z).getType() == Material.BEDROCK) {
                p.teleport(EndLoc);
                p.playSound(EndLoc, Sound.BLOCK_PORTAL_TRAVEL, 0.3F, 2F);
            }

            if (world.getBlockAt(x, y - 1, z).getType() == Material.PURPUR_SLAB) {
                p.teleport(EnderChestLoc);
                p.playSound(EnderChestLoc, Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0F, 0.8F);
                p.setVelocity(new Vector(-1, 0, 0));
            }

        }
    }

    @EventHandler
    public void OnPress (PlayerInteractEvent e) {
        if (e.getAction() == Action.PHYSICAL) {
            Player p = e.getPlayer();
            int x = (int) e.getClickedBlock().getX();
            int y = (int) e.getClickedBlock().getY();
            int z = (int) e.getClickedBlock().getZ();
            if (world.getBlockAt(x, y - 1, z).getType() == Material.COAL_BLOCK) {
                p.setVelocity(new Vector(-1.8, 2.4, 0.0));
                p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.5F, 2.0F);
                BukkitRunnable task = new LaterEffect(p);
                task.runTaskLater(Main.getPlugin(), 35);
            }
            if (world.getBlockAt(x, y - 1, z).getType() == Material.REDSTONE_BLOCK) {
                p.setVelocity(new Vector(0.0, 1.75, 0.0));
                p.playSound(p.getLocation(), Sound.BLOCK_PISTON_EXTEND, 1.0F, 0.8F);
                new PlaySoundLater(p, Sound.BLOCK_PISTON_CONTRACT, 1.0F, 0.8F).runTaskLater(Main.getPlugin(), 20);
            }
        }
    }

}
