package doragoso.sample;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static doragoso.sample.ItemEffect.star_cantp;

class ThrowTask extends BukkitRunnable {

    public Item item;
    public Player player;

    public ThrowTask(Item i, Player p) {
        this.item = i;
        this.player = p;
    }

    @Override
    public void run() {
        star_cantp.put(player.getName(), false);
        item.remove();
    }

}
