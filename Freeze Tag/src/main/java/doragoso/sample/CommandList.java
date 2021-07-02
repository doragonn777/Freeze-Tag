package doragoso.sample;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Random;

import static doragoso.sample.GameSystem.*;
import static doragoso.sample.MenuInfo.FClass;
import static doragoso.sample.MenuInfo.createGuiItem;
import static doragoso.sample.StatusInfo.Damage;
import static doragoso.sample.TimerExecutor.time;

public final class CommandList implements CommandExecutor, Listener {

    public int number_oni = 10;
    public static int GTaskID = -1;
    ItemStack[] armor = new ItemStack[4];
    {
        armor[0] = new ItemStack(Material.DIAMOND_BOOTS, 1);
        armor[1] = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
        armor[2] = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
        armor[3] = new ItemStack(Material.DIAMOND_HELMET, 1);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        e.setJoinMessage(e.getPlayer().getName() + "が参加したニャン");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;

        if (label.equalsIgnoreCase("sethp")) {
            p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(Double.parseDouble(args[0]));
            p.sendMessage("最大体力を" + p.getAttribute(Attribute.GENERIC_MAX_HEALTH) + "に設定しました");
        }

        if (label.equalsIgnoreCase("push")) {
            double x = Double.parseDouble(args[0]);
            double y = Double.parseDouble(args[1]);
            double z = Double.parseDouble(args[2]);
            Vector v = new Vector(x, y, z);
            p.setVelocity(v);
        }

        if (label.equalsIgnoreCase("speed")) {
            if (args[0].equalsIgnoreCase("check")) {
                p.sendMessage("Speed: " + p.getWalkSpeed());
            } else {
                p.setWalkSpeed(Float.parseFloat(args[0]));
            }
        }

        if (label.equalsIgnoreCase("saturation")) {
            p.setFoodLevel(Integer.parseInt(args[0]));
        }

        if (label.equalsIgnoreCase("ft")) {
            if (args[0].equalsIgnoreCase("start")) {
                if (GTaskID != -1) {
                    p.sendMessage(ChatColor.AQUA + "[Freeze Tag] " + ChatColor.RED + "既にゲームが開始されています");
                    return true;
                }
                ArrayList<Player> allPlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
                if (number_oni > allPlayers.size()) {
                    p.sendMessage(ChatColor.AQUA + "[Freeze Tag] " + ChatColor.RED + "鬼の人数が多すぎます");
                    return true;
                }
                for (int i = 0 ; i < number_oni ; i++){
                    int random = new Random().nextInt(allPlayers.size());
                    Player picked = allPlayers.get(random);
                    picked.sendMessage(ChatColor.AQUA + "[Freeze Tag] " + ChatColor.WHITE + "あなたは" + ChatColor.RED + "鬼" + ChatColor.WHITE + "に選ばれました");
                    picked.playSound(picked.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 1.0F);
                    allPlayers.remove(random);
                    oni.addEntry(picked.getName());
                    picked.getInventory().setArmorContents(armor);
                    FClass.put(picked.getName(), "ベーシック");
                    Damage.put(picked.getName(), 2.0);
                    picked.getInventory().addItem(createGuiItem(Material.DIAMOND, ChatColor.GOLD + "クラス選択メニュー" + ChatColor.GRAY + "(右クリックで開く)"));
                }
                for (Player rest : allPlayers) {
                    runner.addEntry(rest.getName());
                    FClass.put(rest.getName(), "ベーシック");
                    rest.playSound(rest.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 1.0F);
                    rest.sendMessage(ChatColor.AQUA + "[Freeze Tag] " + ChatColor.WHITE + "あなたは" + ChatColor.AQUA + "プレイヤー" + ChatColor.WHITE + "に選ばれました");
                    rest.getInventory().addItem(createGuiItem(Material.DIAMOND, ChatColor.GOLD + "クラス選択メニュー" + ChatColor.GRAY + "(右クリックで開く)"));
                }
                time = 660;
                BukkitTask GTask = new TimerExecutor().runTaskTimer(Main.getPlugin(), 0, 20);
                GTaskID = GTask.getTaskId();
            } else if (args[0].equalsIgnoreCase("oni")) {
                number_oni = Integer.parseInt(args[1]);
                p.sendMessage(ChatColor.AQUA + "[Freeze Tag] " + ChatColor.WHITE + "鬼の人数を" + number_oni + "人に設定しました。");
            } else if (args[0].equalsIgnoreCase("stop")) {
                if (GTaskID == -1) {
                    p.sendMessage(ChatColor.AQUA + "[Freeze Tag] " + ChatColor.RED + "まだゲームが開始されていません");
                } else {
                    StopGame();
                    p.sendMessage(ChatColor.AQUA + "[Freeze Tag] " + ChatColor.WHITE + "ゲームを停止しました");
                }
            }
        }
    return true;
}

}
