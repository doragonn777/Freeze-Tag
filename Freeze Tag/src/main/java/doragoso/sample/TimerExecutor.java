package doragoso.sample;

import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static doragoso.sample.GameSystem.*;
import static doragoso.sample.ItemEffect.CDReset;
import static doragoso.sample.MenuInfo.FClass;
import static doragoso.sample.StatusInfo.Damage;


public class TimerExecutor extends BukkitRunnable {
    public static BossBar prepare = Bukkit.createBossBar("準備期間: ", BarColor.PINK, BarStyle.SEGMENTED_6);
    public static BossBar pregame = Bukkit.createBossBar("ゲーム開始まで: ", BarColor.PINK, BarStyle.SEGMENTED_6);
    public static BossBar time_left = Bukkit.createBossBar("残り時間: ", BarColor.PINK, BarStyle.SEGMENTED_10);
    static int time = 660;

    @Override
    public void run() {
        if (time == 660) {
            Bukkit.broadcastMessage(ChatColor.AQUA + "[Freeze Tag] " + ChatColor.GOLD + "-----" + ChatColor.WHITE + "準備期間" + ChatColor.GOLD + "----------");
            Bukkit.broadcastMessage(ChatColor.GOLD + "[クラス選択メニュー]" + ChatColor.WHITE + "からクラスを選択してください");
            Bukkit.broadcastMessage(ChatColor.WHITE + "クラスの名前がついたアイテムを左クリックで決定できます(選び直し可)");
            Bukkit.broadcastMessage(ChatColor.WHITE + "また、右クリックでクラスの所持アイテムやスキル等を確認できます");
            prepare.setProgress(1.0);
            for (Player p : Bukkit.getOnlinePlayers()) {
                prepare.addPlayer(p);
            }
        }
        if (time == 630) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.getInventory().remove(Material.DIAMOND);
                if (runner.getEntries().contains(p.getName())) {
                    p.teleport(new Location(p.getWorld(), 1001.0, 6, 1001.0));
                }
                GiveClass(p);
            }
            prepare.removeAll();
            pregame.setProgress(1.0);
            for (Player p : Bukkit.getOnlinePlayers()) {
                pregame.addPlayer(p);
            }
        }
        if (time == 600) {
            pregame.removeAll();
            time_left.setProgress(1.0);
            for (Player p : Bukkit.getOnlinePlayers()) {
                Bukkit.broadcastMessage(ChatColor.AQUA + "[Freeze Tag] " + ChatColor.WHITE + "ゲームスタート！");
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 2.0F);
                CDReset(p.getName());
                time_left.addPlayer(p);
                if (oni.getEntries().contains(p.getName())) {
                    p.teleport(new Location(p.getWorld(), 1001.0, 6, 1001.0));
                }
            }
        }

        if (630 < time && time <= 660) {
            prepare.setProgress((time - 630) / 30.0);
            prepare.setTitle("準備期間: " + (time - 630) + "秒");
            if (time <= 635) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.1F);
                }
            }
        }
        if (600 < time && time <= 630) {
            pregame.setProgress((time - 600) / 30.0);
            pregame.setTitle("ゲーム開始まで: " + (time - 600) + "秒");
            if (time <= 605) {
                Bukkit.broadcastMessage(ChatColor.AQUA + "[Freeze Tag] " + ChatColor.WHITE + "ゲーム開始まで " + (time - 600) + "...");
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 1.0F);
                }
            }
        }
        if (time <= 600) {
            time_left.setProgress(time / 600.0);
            time_left.setTitle("残り時間: " + time / 60 + "分 " + time % 60 + "秒");
            if (time % 30 == 0 && time != 600) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    switch (FClass.get(p.getName())) {
                        case "ボルト":
                            if (runner.getEntries().contains(p.getName())) {
                                p.setWalkSpeed(p.getWalkSpeed() + 0.01F);
                                p.playSound(p.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0F, 1.5F);
                                p.sendMessage(ChatColor.AQUA + "[Freeze Tag] " + ChatColor.WHITE + "移動速度が5%増加した！");
                            }
                    }
                    if (time % 60 == 0) {   //1分毎の処理
                        switch (FClass.get(p.getName())) {
                            case "レートキャリー":
                                p.setWalkSpeed(p.getWalkSpeed() + 0.01F);
                                p.playSound(p.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0F, 1.5F);
                                p.sendMessage(ChatColor.AQUA + "[Freeze Tag] " + ChatColor.WHITE + "移動速度が5%増加した！");
                                if (time == 180) {
                                    Damage.put(p.getName(), 4.0);
                                    p.sendMessage(ChatColor.AQUA + "[Freeze Tag] " + ChatColor.RED + "ランナーを捕まえた時に与えるダメージが2倍になった！");
                                }
                        }
                    }
                }
            }
        }

        if (time <= 0) {
            if (runner.getSize() >= 1) {
                StopGame();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                    for (int i = 0; i < 10 ; i++) {
                        new PlaySoundLater(player, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1.0F, 1.0F).runTaskLater(Main.getPlugin(), 20 * i);
                    }
                    player.sendTitle(ChatColor.AQUA + "ランナーの勝利！！！", " ", 10, 60, 10);
                }
            } //ゲーム終了
            this.cancel();
        }

        time--;
    }
}
