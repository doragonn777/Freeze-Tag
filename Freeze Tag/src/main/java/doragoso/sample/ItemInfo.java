package doragoso.sample;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static doragoso.sample.StatusInfo.Cooldown;
import static doragoso.sample.StatusInfo.trickLeft;

public final class ItemInfo implements Listener {

    public static Map<Player, Integer> CTask_ID = new HashMap<>();
    static ItemMeta meta;

    public static ItemStack feather = new ItemStack(Material.FEATHER, 1);
    static {
        meta = feather.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "加速の羽");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.BLUE + "[アイテム]");
        lore.add(ChatColor.AQUA + "右クリック: " + ChatColor.GRAY + "8秒間移動速度上昇Ⅱを得る。");
        lore.add(ChatColor.DARK_GRAY + "クールダウン: " + Cooldown.get("feather") + "秒");
        meta.setLore(lore);
        feather.setItemMeta(meta);
    }

    public static ItemStack bone = new ItemStack(Material.BONE, 1);
    static {
        meta = bone.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "身隠しの骨");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.BLUE + "[アイテム]");
        lore.add(ChatColor.AQUA + "右クリック: " + ChatColor.GRAY + "5秒間透明化する。");
        lore.add(ChatColor.DARK_GRAY + "クールダウン: " + Cooldown.get("bone") + "秒");
        meta.setLore(lore);
        bone.setItemMeta(meta);
    }

    public static ItemStack trick = new ItemStack(Material.NETHER_STAR, 1);
    static {
        meta = trick.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_PURPLE + "トリック");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.BLUE + "[スキル]");
        lore.add(ChatColor.AQUA + "左クリック: " + ChatColor.GRAY + "2秒間透明化する、この間は鬼に捕まらない。");
        lore.add(ChatColor.AQUA + "右クリック: " + ChatColor.GRAY + "ネザースターを投げ、5秒以内に");
        lore.add(ChatColor.GRAY + "再度右クリックするとその位置にテレポートする。");
        lore.add(ChatColor.DARK_GRAY + "クールダウン: (左) " + trickLeft + "秒 / (右) " + Cooldown.get("trick") + "秒");
        meta.setLore(lore);
        trick.setItemMeta(meta);
    }

    public static ItemStack highjump = new ItemStack(Material.RABBIT_FOOT, 1);
    static {
        meta = highjump.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "ハイジャンプ");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.BLUE + "[スキル]");
        lore.add(ChatColor.AQUA + "右クリック: " + ChatColor.GRAY + "3ｍ相当のジャンプをする。");
        lore.add(ChatColor.DARK_GRAY + "クールダウン: " + Cooldown.get("highjump") + "秒");
        meta.setLore(lore);
        highjump.setItemMeta(meta);
    }

    public static ItemStack medikit = new ItemStack(Material.IRON_INGOT, 1);
    static {
        meta = medikit.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "メディキット");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.BLUE + "[アイテム]");
        lore.add(ChatColor.AQUA + "右クリック: " + ChatColor.GRAY + "対象のプレイヤーの体力を4回復する");
        lore.add(ChatColor.AQUA + "Shift+右クリック: " + ChatColor.GRAY + "自身の体力を4回復する");
        lore.add(ChatColor.DARK_GRAY + "クールダウン: " + Cooldown.get("medikit") + "秒");
        meta.setLore(lore);
        medikit.setItemMeta(meta);
    }

    public static ItemStack detect = new ItemStack(Material.COMPASS, 1);
    static {
        meta = detect.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_AQUA + "探知");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.BLUE + "[スキル]");
        lore.add(ChatColor.AQUA + "右クリック: " + ChatColor.GRAY + "半径24ｍ以内の鬼を20秒間可視化する。");
        lore.add(ChatColor.DARK_GRAY + "クールダウン: " + Cooldown.get("detect") + "秒");
        meta.setLore(lore);
        detect.setItemMeta(meta);
    }

    public static ItemStack self_enchant = new ItemStack(Material.ENCHANTED_BOOK, 1);
    static {
        meta = self_enchant.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_PURPLE + "セルフエンチャント");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.BLUE + "[スキル]");
        lore.add(ChatColor.AQUA + "左クリック: " + ChatColor.GRAY + "6秒間透明化する。");
        lore.add(ChatColor.AQUA + "右クリック: " + ChatColor.GRAY + "10秒間移動速度上昇Ⅱを得る。");
        lore.add(ChatColor.DARK_GRAY + "クールダウン: " + Cooldown.get("self_enchant") + "秒 (左右共通)");
        meta.setLore(lore);
        self_enchant.setItemMeta(meta);
    }

    public static ItemStack superjump = new ItemStack(Material.IRON_BOOTS, 1);
    static {
        meta = superjump.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "スーパージャンプ");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.BLUE + "[スキル]");
        lore.add(ChatColor.AQUA + "右クリック: " + ChatColor.GRAY + "10ｍ相当のジャンプをする。");
        lore.add(ChatColor.DARK_GRAY + "クールダウン: " + Cooldown.get("superjump") + "秒");
        meta.setLore(lore);
        superjump.setItemMeta(meta);
    }

    public static ItemStack ultrajump = new ItemStack(Material.DIAMOND_BOOTS, 1);
    static {
        meta = ultrajump.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "ウルトラジャンプ");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.BLUE + "[スキル]");
        lore.add(ChatColor.AQUA + "右クリック: " + ChatColor.GRAY + "天井まで届く大ジャンプ！");
        lore.add(ChatColor.DARK_GRAY + "クールダウン: " + Cooldown.get("ultrajump") + "秒");
        meta.setLore(lore);
        ultrajump.setItemMeta(meta);
    }

    public static ItemStack marking = new ItemStack(Material.CLOCK, 1);
    static {
        meta = marking.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "マーキング");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.BLUE + "[スキル]");
        lore.add(ChatColor.AQUA + "右クリック: " + ChatColor.GRAY + "全プレイヤーを0.5秒間可視化する。");
        lore.add(ChatColor.DARK_GRAY + "この効果時間は捕まったプレイヤーが多いほど");
        lore.add(ChatColor.DARK_GRAY + "その割合に応じて最大5秒まで増加する。");
        lore.add(ChatColor.DARK_GRAY + "クールダウン: " + Cooldown.get("marking") + "秒");
        meta.setLore(lore);
        marking.setItemMeta(meta);
    }

    public static Inventory bag = Bukkit.createInventory(null, 9, ChatColor.GOLD + "Item Set");
    static {
        bag.setItem(0, feather);
        bag.setItem(1, bone);
    }

    @EventHandler
    public void OnItemHeld(PlayerItemHeldEvent e) {
        Player p = e.getPlayer();
        ItemStack i = p.getInventory().getItem(e.getNewSlot());
        if (CTask_ID.get(p) != null) {
            Bukkit.getScheduler().cancelTask(CTask_ID.get(p));
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(" "));
            CTask_ID.remove(p);
        }
        if (i != null) {
            switch (i.getType()) {
                case FEATHER:
                    i.setItemMeta(feather.getItemMeta());
                    break;
                case BONE:
                    i.setItemMeta(bone.getItemMeta());
                    break;
                case NETHER_STAR:
                    i.setItemMeta(trick.getItemMeta());
                    break;
                case RABBIT_FOOT:
                    i.setItemMeta(highjump.getItemMeta());
                    break;
                case IRON_INGOT:
                    i.setItemMeta(medikit.getItemMeta());
                    break;
                case COMPASS:
                    i.setItemMeta(detect.getItemMeta());
                    break;
                case ENCHANTED_BOOK:
                    i.setItemMeta(self_enchant.getItemMeta());
                    break;
                case IRON_BOOTS:
                    i.setItemMeta(superjump.getItemMeta());
                    break;
                case DIAMOND_BOOTS:
                    i.setItemMeta(ultrajump.getItemMeta());
                    break;
                case CLOCK:
                    i.setItemMeta(marking.getItemMeta());
                    break;
            }
            BukkitTask task = new ShowCooldownTask(p).runTaskTimer(Main.getPlugin(), 0, 20);
            CTask_ID.put(p, task.getTaskId());
        }
    }
}
