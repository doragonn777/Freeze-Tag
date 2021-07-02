package doragoso.sample;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static doragoso.sample.GameSystem.*;
import static doragoso.sample.ItemInfo.*;

public class MenuInfo implements Listener {
    public static Map<String, String> FClass = new HashMap<>();

    Inventory runner_class_menu = Bukkit.createInventory(null, 9, ChatColor.BLUE + "クラス一覧 " + ChatColor.DARK_GRAY + "(右クリックで詳細確認)");
    Inventory oni_class_menu = Bukkit.createInventory(null, 9, ChatColor.RED + "クラス一覧 " + ChatColor.DARK_GRAY + "(右クリックで詳細確認)");

    Inventory oni_basic = Bukkit.createInventory(null, 9, ChatColor.RED + "ベーシック");
    Inventory oni_jumper = Bukkit.createInventory(null, 9, ChatColor.RED + "ジャンパー");
    Inventory oni_tracker = Bukkit.createInventory(null, 9, ChatColor.RED + "トラッカー");
    Inventory oni_latecarry = Bukkit.createInventory(null, 9, ChatColor.RED + "レートキャリー");

    Inventory runner_basic = Bukkit.createInventory(null, 9, ChatColor.BLUE + "ベーシック");
    Inventory runner_trickstar = Bukkit.createInventory(null, 9, ChatColor.BLUE + "トリックスター");
    Inventory runner_medic = Bukkit.createInventory(null, 9, ChatColor.BLUE + "メディック");
    Inventory runner_detective = Bukkit.createInventory(null, 9, ChatColor.BLUE + "ディテクティブ");
    Inventory runner_rabbit = Bukkit.createInventory(null, 9, ChatColor.BLUE + "ラビット");
    Inventory runner_enchanter = Bukkit.createInventory(null, 9, ChatColor.BLUE + "エンチャンター");
    Inventory runner_bolt = Bukkit.createInventory(null, 9, ChatColor.BLUE + "ボルト");

    protected static ItemStack createGuiItem(final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        return item;
    }

    public void registerClass (Player p, String s) {
        FClass.put(p.getName(), s);
        p.closeInventory();
        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 2.0F);
        p.sendMessage(ChatColor.AQUA + "[Freeze Tag] " + ChatColor.WHITE + "クラス: " + ChatColor.YELLOW + s + ChatColor.WHITE + "を選択しました");
    }

    {
        oni_class_menu.addItem(createGuiItem(Material.GRASS_BLOCK, ChatColor.YELLOW + "ベーシック", ChatColor.BLUE + "[移動速度]", ChatColor.GRAY + "120%"));
        oni_class_menu.addItem(createGuiItem(Material.DIAMOND_BOOTS, ChatColor.YELLOW + "ジャンパー", ChatColor.BLUE + "[スキル]", ChatColor.GRAY + "スーパージャンプ", ChatColor.GRAY + "ウルトラジャンプ", ChatColor.BLUE + "[パッシブ]", ChatColor.GRAY + "ハイジャンプ", ChatColor.BLUE + "[移動速度]", ChatColor.GRAY + "105%"));
        oni_class_menu.addItem(createGuiItem(Material.CLOCK, ChatColor.YELLOW + "トラッカー", ChatColor.BLUE + "[スキル]", ChatColor.GRAY + "マーキング", ChatColor.BLUE + "[移動速度]", ChatColor.GRAY + "110%"));
        oni_class_menu.addItem(createGuiItem(Material.GOLDEN_BOOTS, ChatColor.YELLOW + "レートキャリー", ChatColor.BLUE + "[パッシブ]", ChatColor.GRAY + "レートゲームキャリー", ChatColor.BLUE + "[移動速度]", ChatColor.GRAY + "90%"));
    } //鬼のクラス一覧

    {
        oni_basic.setItem(8, createGuiItem(Material.BARRIER, ChatColor.RED + "クラス選択画面へ戻る"));
    } //ベーシッククラス(鬼)

    {
        oni_jumper.addItem(superjump,ultrajump);
        oni_jumper.setItem(7, createGuiItem(Material.LEATHER_BOOTS, ChatColor.YELLOW + "ハイジャンプ", ChatColor.BLUE + "[パッシブ]", ChatColor.GRAY + "常に跳躍力上昇Ⅱ(2ｍ相当のジャンプ力)を得る。"));
        oni_jumper.setItem(8, createGuiItem(Material.BARRIER, ChatColor.RED + "クラス選択画面へ戻る"));
    } //ジャンパー

    {
        oni_tracker.addItem(marking);
        oni_tracker.setItem(8, createGuiItem(Material.BARRIER, ChatColor.RED + "クラス選択画面へ戻る"));
    } //トラッカー

    {
        oni_latecarry.setItem(7, createGuiItem(Material.BLAZE_POWDER, ChatColor.RED + "レートゲームキャリー", ChatColor.BLUE + "[パッシブ]", ChatColor.GRAY + "ゲーム開始から1分毎に移動速度が5%上昇し、", ChatColor.GRAY + "残り時間が3分になるとプレイヤーを捕まえた時に", ChatColor.GRAY + "与えるダメージが2倍になる。"));
        oni_latecarry.setItem(8, createGuiItem(Material.BARRIER, ChatColor.RED + "クラス選択画面へ戻る"));
    } //レートキャリー

    {
        runner_class_menu.addItem(createGuiItem(Material.GRASS_BLOCK, ChatColor.YELLOW + "ベーシック", ChatColor.BLUE + "[アイテム]", ChatColor.GRAY + "加速の羽x5", ChatColor.GRAY + "身隠しの骨x5", ChatColor.BLUE + "[移動速度]", ChatColor.GRAY + "100%"));
        runner_class_menu.addItem(createGuiItem(Material.NETHER_STAR, ChatColor.YELLOW + "トリックスター", ChatColor.BLUE + "[スキル]", ChatColor.GRAY + "トリック", ChatColor.BLUE + "[移動速度]", ChatColor.GRAY + "100%"));
        runner_class_menu.addItem(createGuiItem(Material.GOLDEN_APPLE, ChatColor.YELLOW + "メディック", ChatColor.BLUE + "[アイテム]", ChatColor.GRAY + "加速の羽x3", ChatColor.GRAY + "身隠しの骨x3", ChatColor.GRAY + "メディキットx5", ChatColor.BLUE + "[移動速度]", ChatColor.GRAY + "100%"));
        runner_class_menu.addItem(createGuiItem(Material.COMPASS, ChatColor.YELLOW + "ディテクティブ", ChatColor.BLUE + "[アイテム]", ChatColor.GRAY + "加速の羽x2", ChatColor.GRAY + "身隠しの骨x2", ChatColor.BLUE + "[スキル]", ChatColor.GRAY + "探知", ChatColor.BLUE + "[移動速度]", ChatColor.GRAY + "100%"));
        runner_class_menu.addItem(createGuiItem(Material.RABBIT_FOOT, ChatColor.YELLOW + "ラビット", ChatColor.BLUE + "[アイテム]", ChatColor.GRAY + "加速の羽x3", ChatColor.BLUE + "[スキル]", ChatColor.GRAY + "ハイジャンプ", ChatColor.BLUE + "[移動速度]", ChatColor.GRAY + "105%"));
        runner_class_menu.addItem(createGuiItem(Material.ENCHANTING_TABLE, ChatColor.YELLOW + "エンチャンター", ChatColor.BLUE + "[スキル]", ChatColor.GRAY + "セルフエンチャント", ChatColor.BLUE + "[パッシブ]", ChatColor.GRAY + "エンチャント", ChatColor.BLUE + "[移動速度]", ChatColor.GRAY + "100%"));
        runner_class_menu.addItem(createGuiItem(Material.GOLDEN_BOOTS, ChatColor.YELLOW + "ボルト", ChatColor.BLUE + "[パッシブ]", ChatColor.GRAY + "ヒートアップ", ChatColor.BLUE + "[移動速度]", ChatColor.GRAY + "110%"));

    } //プレイヤーのクラス一覧

    ItemStack f = feather.clone();
    ItemStack b = bone.clone();

    {
        f.setAmount(5); b.setAmount(5);
        runner_basic.addItem(f, b);
        runner_basic.setItem(8, createGuiItem(Material.BARRIER, ChatColor.RED + "クラス選択画面へ戻る"));
    } //ベーシッククラス(プレイヤー)

    {
        runner_trickstar.addItem(trick);
        runner_trickstar.setItem(8, createGuiItem(Material.BARRIER, ChatColor.RED + "クラス選択画面へ戻る"));
    } //トリックスター

    {
        f.setAmount(3); b.setAmount(3);
        ItemStack m = medikit.clone();
        m.setAmount(5);
        runner_medic.addItem(f, b, m);
        runner_medic.setItem(8, createGuiItem(Material.BARRIER, ChatColor.RED + "クラス選択画面へ戻る"));
    } //メディック

    {
        f.setAmount(2); b.setAmount(2);
        runner_detective.addItem(f, b, detect);
        runner_detective.setItem(8, createGuiItem(Material.BARRIER, ChatColor.RED + "クラス選択画面へ戻る"));
    } //ディテクティブ

    {
        f.setAmount(3);
        runner_rabbit.addItem(f, highjump);
        runner_rabbit.setItem(8, createGuiItem(Material.BARRIER, ChatColor.RED + "クラス選択画面へ戻る"));
    } //ラビット

    {
        runner_enchanter.addItem(self_enchant);
        runner_enchanter.setItem(7, createGuiItem(Material.ENCHANTED_BOOK, ChatColor.GREEN + "エンチャント", ChatColor.BLUE + "[パッシブ]", ChatColor.GRAY + "凍っている味方を助けた時、その味方に", ChatColor.GRAY + "5秒間移動速度上昇Ⅰを与え、1秒間透明化させる。"));
        runner_enchanter.setItem(8, createGuiItem(Material.BARRIER, ChatColor.RED + "クラス選択画面へ戻る"));
    } //エンチャンター

    {
        runner_bolt.setItem(7, createGuiItem(Material.BLAZE_POWDER, ChatColor.RED + "ヒートアップ",ChatColor.BLUE + "[パッシブ]", ChatColor.GRAY + "30秒毎に移動速度が5%増加する", ChatColor.GRAY + "鬼に捕まるとこの効果はリセットされる。"));
        runner_bolt.setItem(8, createGuiItem(Material.BARRIER, ChatColor.RED + "クラス選択画面へ戻る"));
    } //ボルト

    @EventHandler
    public void onRightClick (PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
        if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.DIAMOND)
        if (runner.getEntries().contains(p.getName())) {
            p.openInventory(runner_class_menu);
        } else if (oni.getEntries().contains(p.getName())) {
            p.openInventory(oni_class_menu);
        }
    }

    @EventHandler
    public void OnInventoryClick (InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getCurrentItem() != null && e.getWhoClicked().getGameMode() != GameMode.CREATIVE) {
            e.setCancelled(true);
            if (e.getInventory() == oni_class_menu) {
                switch (e.getCurrentItem().getType()) {
                    case GRASS_BLOCK:
                        if (e.getClick() == ClickType.RIGHT) {
                            p.closeInventory();
                            p.openInventory(oni_basic);
                        } else {
                            registerClass(p, "ベーシック");
                        }
                        break;
                    case DIAMOND_BOOTS:
                        if (e.getClick() == ClickType.RIGHT) {
                            p.closeInventory();
                            p.openInventory(oni_jumper);
                        } else {
                            registerClass(p, "ジャンパー");
                        }
                        break;
                    case CLOCK:
                        if (e.getClick() == ClickType.RIGHT) {
                            p.closeInventory();
                            p.openInventory(oni_tracker);
                        } else {
                            registerClass(p, "トラッカー");
                        }
                        break;
                    case GOLDEN_BOOTS:
                        if (e.getClick() == ClickType.RIGHT) {
                            p.closeInventory();
                            p.openInventory(oni_latecarry);
                        } else {
                            registerClass(p, "レートキャリー");
                        }
                        break;
                }
            } else if (e.getInventory() == runner_class_menu) {
                switch (e.getCurrentItem().getType()) {
                    case GRASS_BLOCK:
                        if (e.getClick() == ClickType.RIGHT) {
                            p.closeInventory();
                            p.openInventory(runner_basic);
                        } else {
                            registerClass(p, "ベーシック");
                        }
                        break;
                    case NETHER_STAR:
                        if (e.getClick() == ClickType.RIGHT) {
                            p.closeInventory();
                            p.openInventory(runner_trickstar);
                        } else {
                            registerClass(p, "トリックスター");
                        }
                        break;
                    case GOLDEN_APPLE:
                        if (e.getClick() == ClickType.RIGHT) {
                            p.closeInventory();
                            p.openInventory(runner_medic);
                        } else {
                            registerClass(p, "メディック");
                        }
                        break;
                    case COMPASS:
                        if (e.getClick() == ClickType.RIGHT) {
                            p.closeInventory();
                            p.openInventory(runner_detective);
                        } else {
                            registerClass(p, "ディテクティブ");
                        }
                        break;
                    case RABBIT_FOOT:
                        if (e.getClick() == ClickType.RIGHT) {
                            p.closeInventory();
                            p.openInventory(runner_rabbit);
                        } else {
                            registerClass(p, "ラビット");
                        }
                        break;
                    case ENCHANTING_TABLE:
                        if (e.getClick() == ClickType.RIGHT) {
                            p.closeInventory();
                            p.openInventory(runner_enchanter);
                        } else {
                            registerClass(p, "エンチャンター");
                        }
                        break;
                    case GOLDEN_BOOTS:
                        if (e.getClick() == ClickType.RIGHT) {
                            p.closeInventory();
                            p.openInventory(runner_bolt);
                        } else {
                            registerClass(p, "ボルト");
                        }
                        break;
                }

            } else if (e.getInventory() != p.getInventory() && e.getCurrentItem().getType() == Material.BARRIER) {
                if (runner.getEntries().contains(p.getName())) {
                    p.closeInventory();
                    p.openInventory(runner_class_menu);
                } else if (oni.getEntries().contains(p.getName())) {
                    p.closeInventory();
                    p.openInventory(oni_class_menu);
                }
            }
        }
    }
}
