package doragoso.sample;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static doragoso.sample.ItemEffect.*;
import static doragoso.sample.ItemInfo.CTask_ID;
import static doragoso.sample.StatusInfo.Cooldown;
import static doragoso.sample.StatusInfo.trickLeft;

public class ShowCooldownTask extends BukkitRunnable {

    public Player p;
    public int CD_Right;
    public int CD_Left;
    public String component;
    public ShowCooldownTask (Player p) {
        this.p = p;
        this.component = null;
        this.CD_Left = 9999;
        this.CD_Right = 9999;
    }

    public void run() {
        switch (p.getInventory().getItemInMainHand().getType()) {
            case NETHER_STAR:
                CD_Right = Math.toIntExact(Cooldown.get("trick") - (System.currentTimeMillis() - star_CD.get(p.getName())) / 1000);
                CD_Left = Math.toIntExact(trickLeft - (System.currentTimeMillis() - starleft_CD.get(p.getName())) / 1000);
                break;
            case RABBIT_FOOT:
                CD_Right = Math.toIntExact(Cooldown.get("highjump") - (System.currentTimeMillis() - highjump_CD.get(p.getName())) / 1000);
                break;
            case COMPASS:
                CD_Right = Math.toIntExact(Cooldown.get("detect") - (System.currentTimeMillis() - detect_CD.get(p.getName())) / 1000);
                break;
            case ENCHANTED_BOOK:
                CD_Right = Math.toIntExact(Cooldown.get("self_enchant") - (System.currentTimeMillis() - self_enchant_CD.get(p.getName())) / 1000);
                break;
            case IRON_BOOTS:
                CD_Right = Math.toIntExact(Cooldown.get("superjump") - (System.currentTimeMillis() - superjump_CD.get(p.getName())) / 1000);
                break;
            case DIAMOND_BOOTS:
                CD_Right = Math.toIntExact(Cooldown.get("ultrajump") - (System.currentTimeMillis() - ultrajump_CD.get(p.getName())) / 1000);
                break;
            case CLOCK:
                CD_Right = Math.toIntExact(Cooldown.get("marking") - (System.currentTimeMillis() - marking_CD.get(p.getName())) / 1000);
                break;
        }
        if (CD_Right <= 0) {
            CD_Right = 0;
        }
        if (CD_Left <= 0) {
            CD_Left = 0;
        }
        if (CD_Left == 9999 && CD_Right == 9999) {
            CTask_ID.remove(p);
            component = " ";
            cancel();
        } else if (CD_Left == 9999) {
            component = ChatColor.DARK_AQUA + "クールダウン: " + CD_Right + "秒";
        } else {
            component = ChatColor.DARK_AQUA +  "クールダウン: (左) " + CD_Left + "秒 / (右) " + CD_Right + "秒";
        }
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(component));
    }
}
