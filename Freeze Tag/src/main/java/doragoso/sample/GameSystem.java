package doragoso.sample;

import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;

import static doragoso.sample.CommandList.GTaskID;
import static doragoso.sample.ItemEffect.CDReset;
import static doragoso.sample.ItemInfo.*;
import static doragoso.sample.MenuInfo.FClass;
import static doragoso.sample.StatusInfo.Damage;
import static doragoso.sample.StatusInfo.Speed;
import static doragoso.sample.TimerExecutor.*;

public class GameSystem implements Listener {

    public static ScoreboardManager manager = Bukkit.getScoreboardManager();
    public static Scoreboard board = manager.getMainScoreboard();
    public static Team oni = board.getTeam("oni");
    public static Team runner = board.getTeam("runner");
    public static Team frozen = board.getTeam("frozen");
    public static Map<Player, Integer> FTaskID = new HashMap<>();
    Map<String, Long> Last_melted = new HashMap<>();
    static {
        if (oni != null) {
            oni.unregister();
        }
        oni = board.registerNewTeam("oni");
        oni.setDisplayName("鬼");
        oni.setColor(ChatColor.RED);
        oni.setAllowFriendlyFire(false);
        oni.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        oni.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.FOR_OTHER_TEAMS);

        if (runner != null) {
            runner.unregister();
        }
        runner = board.registerNewTeam("runner");
        runner.setDisplayName("プレイヤー");
        runner.setColor(ChatColor.AQUA);
        runner.setAllowFriendlyFire(false);
        runner.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        runner.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.FOR_OTHER_TEAMS);

        if (frozen != null) {
            frozen.unregister();
        }
        frozen = board.registerNewTeam("frozen");
        frozen.setDisplayName("氷漬け");
        frozen.setColor(ChatColor.GRAY);
        frozen.setAllowFriendlyFire(false);
        frozen.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        frozen.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
    } //チームの初期化

    public static void GiveClass (Player p) {
        ItemStack f = feather.clone();
        ItemStack b = bone.clone();
        ItemStack m = medikit.clone();
        p.playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1.0F, 1.0F);
        switch (FClass.get(p.getName())) {
            case "ベーシック":
                if (runner.getEntries().contains(p.getName())) {
                    f.setAmount(5); b.setAmount(5);
                    p.getInventory().addItem(f, b);
                } else {
                    p.setWalkSpeed(0.2F * 1.2F);
                }
                break;
            case "トリックスター":
                p.getInventory().addItem(trick);
                break;
            case "メディック":
                f.setAmount(3); b.setAmount(3); m.setAmount(5);
                p.getInventory().addItem(f, b, m);
                break;
            case "ディテクティブ":
                f.setAmount(2); b.setAmount(2);
                p.getInventory().addItem(f, b, detect);
                break;
            case "ラビット":
                f.setAmount(3);
                p.getInventory().addItem(f, highjump);
                p.setWalkSpeed(0.2F * 1.05F);
                break;
            case "エンチャンター":
                p.getInventory().addItem(self_enchant);
                break;
            case "ボルト":
                p.setWalkSpeed(0.2F * 1.1F);
                break;
            case "ジャンパー":
                p.getInventory().addItem(superjump, ultrajump);
                p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 100000, 1));
                p.setWalkSpeed(0.2F * 1.05F);
                break;
            case "トラッカー":
                p.getInventory().addItem(marking);
                p.setWalkSpeed(0.2F * 1.1F);
                break;
            case "レートキャリー":
                p.setWalkSpeed(0.2F * 0.9F);
                break;
        }
    } //クラスアイテムやスキルを配布する

    @EventHandler
    public void OnPlayerJoin (PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (GTaskID == -1) {
            Last_melted.put(p.getName(), 0L);
            p.teleport(new Location(p.getWorld(), 24, 34, 315));
            p.setHealth(20.0);
            p.setFoodLevel(20);
            p.setWalkSpeed(0.2F);
            p.setGameMode(GameMode.ADVENTURE);
            CDReset(p.getName());
            p.getInventory().clear();
            for (PotionEffect effect : p.getActivePotionEffects()) {
                p.removePotionEffect(effect.getType());
            }
        } else {
            if (630 < time && time <= 660) {
                prepare.addPlayer(p);
            } else if (600 < time && time <= 630) {
                pregame.addPlayer(p);
                if (p.getInventory().contains(Material.DIAMOND)) {
                    p.getInventory().remove(Material.DIAMOND);
                    GiveClass(p);
                    if (runner.getEntries().contains(p.getName())) {
                        p.teleport(new Location(p.getWorld(), 1001.0, 6, 1001.0));
                    }
                }
            } else if (time <= 600) {
                time_left.addPlayer(p);
                if (p.getInventory().contains(Material.DIAMOND)) {
                    p.getInventory().remove(Material.DIAMOND);
                    GiveClass(p);
                    p.teleport(new Location(p.getWorld(), 1001.0, 6, 1001.0));
                }
                if (runner.getEntries().contains(p.getName())) {
                    p.spawnParticle(Particle.BLOCK_CRACK, p.getLocation(), 50, 0.5, 1.0, 0.5, Material.PACKED_ICE.createBlockData());
                    p.playSound(p.getLocation(), Sound.BLOCK_GLASS_BREAK, 1.0F, 1.0F);
                    frozen.addEntry(p.getName());
                    p.setWalkSpeed(0);
                    p.setFoodLevel(6);
                    p.addPotionEffect(PotionEffectType.JUMP.createEffect(12000, 200));
                    BukkitTask FTask = new FreezeTimer(p).runTaskTimer(Main.getPlugin(), 600, 600);
                    FTaskID.put(p, FTask.getTaskId());
                } else if (frozen.getEntries().contains(p.getName())) {
                    p.addPotionEffect(PotionEffectType.JUMP.createEffect(12000, 200));
                    BukkitTask FTask = new FreezeTimer(p).runTaskTimer(Main.getPlugin(), 0, 600);
                    FTaskID.put(p, FTask.getTaskId());
                }
            }
        }
    }

    @EventHandler
    public void OnDamage (EntityDamageEvent e) {
        if (e.getEntityType() == EntityType.PLAYER) {
            e.setCancelled(true);
            if (e instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent)e).getDamager().getType() == EntityType.PLAYER) {
                Player attacker = (Player) ((EntityDamageByEntityEvent)e).getDamager();
                Player victim = (Player) e.getEntity();
                if (runner.getEntries().contains(victim.getName()) && oni.getEntries().contains(attacker.getName()) && !victim.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
                    victim.spawnParticle(Particle.BLOCK_CRACK, victim.getLocation(), 50, 0.5, 1.0, 0.5, Material.PACKED_ICE.createBlockData());
                    victim.playSound(victim.getLocation(), Sound.BLOCK_GLASS_BREAK, 1.0F, 1.0F);
                    frozen.addEntry(victim.getName());
                    victim.setWalkSpeed(0);
                    victim.setFoodLevel(6);
                    victim.addPotionEffect(PotionEffectType.JUMP.createEffect(12000, 200));
                    BukkitTask FTask = new FreezeTimer(victim).runTaskTimer(Main.getPlugin(), 600, 600);
                    FTaskID.put(victim, FTask.getTaskId());
                    if (System.currentTimeMillis() - Last_melted.get(victim.getName()) >= 10 * 1000) {
                        if (victim.getHealth() > Damage.get(attacker.getName())) {
                            victim.setHealth(victim.getHealth() - Damage.get(attacker.getName()));
                        } else {
                            victim.setGameMode(GameMode.SPECTATOR);
                            victim.sendMessage(ChatColor.RED + "あなたは氷漬けにされてしまった！");
                            Bukkit.getScheduler().cancelTask(FTaskID.get(victim));
                        }
                    } //ダメージ処理
                    if (runner.getSize() <= 0) {
                        StopGame();
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                            for (int i = 0; i < 10 ; i++) {
                                new PlaySoundLater(player, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1.0F, 1.0F).runTaskLater(Main.getPlugin(), 20 * i);
                            }
                            player.sendTitle(ChatColor.RED + "鬼の勝利！！！", " ", 10, 60, 10);
                        }
                    } //ゲーム終了
                } else if (frozen.getEntries().contains(victim.getName()) && runner.getEntries().contains(attacker.getName())) {
                    if (FClass.get(attacker.getName()).equalsIgnoreCase("エンチャンター")) {
                        victim.playSound(victim.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                        victim.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 0));
                        victim.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20, 0));
                    }
                    victim.spawnParticle(Particle.FLAME, victim.getLocation(),50, 0.3, 1.0, 0.3, 0.03);
                    victim.playSound(victim.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                    runner.addEntry(victim.getName());
                    Last_melted.put(victim.getName(), System.currentTimeMillis());
                    Bukkit.getScheduler().cancelTask(FTaskID.get(victim));
                    victim.setWalkSpeed(0.2F * Speed.get(FClass.get(victim.getName())));
                    victim.setFoodLevel(20);
                    victim.removePotionEffect(PotionEffectType.JUMP);
                }
            } else {
                Player victim = (Player) e.getEntity();
                if (runner.getEntries().contains(victim.getName()) && !victim.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
                    victim.spawnParticle(Particle.BLOCK_CRACK, victim.getLocation(), 50, 0.5, 1.0, 0.5, Material.PACKED_ICE.createBlockData());
                    victim.playSound(victim.getLocation(), Sound.BLOCK_GLASS_BREAK, 1.0F, 1.0F);
                    frozen.addEntry(victim.getName());
                    victim.setWalkSpeed(0);
                    victim.setFoodLevel(6);
                    victim.addPotionEffect(PotionEffectType.JUMP.createEffect(12000, 200));
                    victim.addPotionEffect(PotionEffectType.DAMAGE_RESISTANCE.createEffect(100, 2));
                    BukkitTask FTask = new FreezeTimer(victim).runTaskTimer(Main.getPlugin(), 600, 600);
                    FTaskID.put(victim, FTask.getTaskId());
                    if (System.currentTimeMillis() - Last_melted.get(victim.getName()) >= 10 * 1000) {
                        if (victim.getHealth() > 2.0) {
                            victim.setHealth(victim.getHealth() - 2.0);
                        } else {
                            victim.setGameMode(GameMode.SPECTATOR);
                            victim.sendMessage(ChatColor.RED + "あなたは氷漬けにされてしまった！");
                            Bukkit.getScheduler().cancelTask(FTaskID.get(victim));
                        }
                    }
                } else if (frozen.getEntries().contains(victim.getName()) && !victim.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
                    victim.spawnParticle(Particle.FLAME, victim.getLocation(),50, 0.3, 1.0, 0.3, 0.03);
                    victim.playSound(victim.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                    runner.addEntry(victim.getName());
                    Last_melted.put(victim.getName(), System.currentTimeMillis());
                    Bukkit.getScheduler().cancelTask(FTaskID.get(victim));
                    victim.setWalkSpeed(0.2F * Speed.get(FClass.get(victim.getName())));
                    victim.setFoodLevel(20);
                    victim.removePotionEffect(PotionEffectType.JUMP);
                }
            } //デバッグ用
        }
    }

    @EventHandler
    public void OnPlayerLeave (PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (GTaskID != -1 && runner.getEntries().contains(p.getName())) {
            if (runner.getSize() <= 1) {
                StopGame();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                    for (int i = 0; i < 10; i++) {
                        new PlaySoundLater(player, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1.0F, 1.0F).runTaskLater(Main.getPlugin(), 20 * i);
                    }
                    player.sendTitle(ChatColor.RED + "鬼の勝利！！！", " ", 10, 60, 10);
                }
            } //ゲーム終了
        } else if (GTaskID != -1 && frozen.getEntries().contains(p.getName())) {
            Bukkit.getScheduler().cancelTask(FTaskID.get(p));
        }
    }

    @EventHandler
    public void OnItemTeleportEnd (EntityPortalEvent e) {
        if (e.getEntityType() != EntityType.PLAYER) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void OnFoodLevelChange (FoodLevelChangeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent e) {
        if (e.getItem().getItemStack().getType() == Material.NETHER_STAR) {
            e.setCancelled(true);
        }
    }

    public static void StopGame () {
        Bukkit.getScheduler().cancelTask(GTaskID);
        GTaskID = -1;
        prepare.removeAll();
        pregame.removeAll();
        time_left.removeAll();
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.getInventory().clear();
            player.setWalkSpeed(0.2F);
            player.setFoodLevel(20);
            player.setHealth(player.getMaxHealth());
            if (frozen.getEntries().contains(player.getName())) {
                Bukkit.getScheduler().cancelTask(FTaskID.get(player));
            }
            for (PotionEffect effect : player.getActivePotionEffects()) {
                player.removePotionEffect(effect.getType());
            }
            player.setGameMode(GameMode.ADVENTURE);
            player.teleport(new Location(player.getWorld(), 24, 34, 342));
        }
        for (Team team : board.getTeams()) {
            for (String name : team.getEntries()) {
                team.removeEntry(name);
            }
        }
    }

}
