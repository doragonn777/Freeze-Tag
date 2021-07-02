package doragoso.sample;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static doragoso.sample.GameSystem.frozen;
import static doragoso.sample.GameSystem.runner;
import static doragoso.sample.ItemInfo.*;
import static doragoso.sample.MenuInfo.FClass;
import static doragoso.sample.StatusInfo.Cooldown;
import static doragoso.sample.StatusInfo.trickLeft;

public final class ItemEffect implements Listener {
    static Map<String, Long> bone_CD = new HashMap<>();
    static Map<String, Long> feather_CD = new HashMap<>();
    public static Map<String, Long> star_CD = new HashMap<>();
    public static Map<String, Long> starleft_CD = new HashMap<>();
    public static Map<String, Boolean> star_cantp = new HashMap<>();
    public static Map<String, Item> star_save = new HashMap<>();
    public static Map<String, Long> highjump_CD = new HashMap<>();
    static Map<String, Long> medikit_CD = new HashMap<>();
    public static Map<String, Long> detect_CD = new HashMap<>();
    public static Map<String, Long> self_enchant_CD = new HashMap<>();
    public static Map<String, Long> superjump_CD = new HashMap<>();
    public static Map<String, Long> ultrajump_CD = new HashMap<>();
    public static Map<String, Long> marking_CD = new HashMap<>();

    public static void CDReset (String name) {
        bone_CD.put(name, 0L);
        feather_CD.put(name, 0L);
        star_CD.put(name, 0L);
        starleft_CD.put(name, 0L);
        star_cantp.put(name, false);
        highjump_CD.put(name, 0L);
        medikit_CD.put(name, 0L);
        detect_CD.put(name, 0L);
        self_enchant_CD.put(name, 0L);
        superjump_CD.put(name, 0L);
        ultrajump_CD.put(name, 0L);
        marking_CD.put(name, 0L);
    }

    @EventHandler
    public void OnInteract(PlayerInteractEntityEvent e) {
        Player p = e.getPlayer();
        if (e.getRightClicked() instanceof LivingEntity && !frozen.getEntries().contains(p.getName())) {
            if (p.getInventory().getItemInMainHand().getType() == Material.IRON_INGOT && System.currentTimeMillis() - medikit_CD.get(p.getName()) >= Cooldown.get("medikit") * 1000) {
                LivingEntity entity = (LivingEntity) e.getRightClicked();
                if (entity.getHealth() != entity.getMaxHealth()) {
                    if (entity.getHealth() <= entity.getMaxHealth() - 4.0) {
                        entity.setHealth(entity.getHealth() + 4.0);
                    } else if (entity.getHealth() < entity.getMaxHealth()) {
                        entity.setHealth(entity.getMaxHealth());
                    }
                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                    p.getInventory().removeItem(medikit);
                    p.updateInventory();
                } else {
                    p.sendMessage(ChatColor.RED + "対象の体力が満タンです");
                    p.playSound(p.getLocation(), Sound.BLOCK_LAVA_POP, 1.0F, 1.0F);
                }
                medikit_CD.put(p.getName(), System.currentTimeMillis());
            }
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && !frozen.getEntries().contains(p.getName())) {
            switch (p.getInventory().getItemInMainHand().getType()) {
                case BONE:
                    e.setCancelled(true);
                    if (System.currentTimeMillis() - bone_CD.get(p.getName()) >= Cooldown.get("bone") * 1000) {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100, 0));
                        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_WITHER_SHOOT, 1.0F, 1.0F);
                        p.getInventory().removeItem(bone);
                        p.updateInventory();
                        bone_CD.put(p.getName(), System.currentTimeMillis());
                    }
                    break;

                case FEATHER:
                    e.setCancelled(true);
                    if ((System.currentTimeMillis() - feather_CD.get(p.getName())) >= Cooldown.get("feather") * 1000) {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 160, 1));
                        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1.0F, 1.0F);
                        p.getInventory().removeItem(feather);
                        p.updateInventory();
                        feather_CD.put(p.getName(), System.currentTimeMillis());
                    }
                    break;

                case NETHER_STAR:
                    e.setCancelled(true);
                    if (System.currentTimeMillis() - star_CD.get(p.getName()) >= Cooldown.get("trick") * 1000) {
                        star_CD.put(p.getName(), System.currentTimeMillis());
                        star_cantp.put(p.getName(), true);
                        p.playSound(p.getLocation(), Sound.ENTITY_ENDER_PEARL_THROW, 1.0F, 0.0F);
                        Item star = p.getWorld().dropItem(p.getEyeLocation(), trick);
                        star.setVelocity(p.getLocation().getDirection());
                        new ThrowTask(star, p).runTaskLater(Main.getPlugin(), 100);
                        star_save.put(p.getName(), star);
                    } else if (star_cantp.get(p.getName()) && System.currentTimeMillis() - star_CD.get(p.getName()) >= 100) {
                        star_cantp.put(p.getName(), false);
                        Location l = star_save.get(p.getName()).getLocation();
                        star_save.get(p.getName()).remove();
                        l.setYaw(p.getLocation().getYaw());
                        l.setPitch(p.getLocation().getPitch());
                        p.teleport(l);
                        p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
                    }
                    break;

                case RABBIT_FOOT:
                    e.setCancelled(true);
                    if (System.currentTimeMillis() - highjump_CD.get(p.getName()) >= Cooldown.get("highjump") * 1000) {
                        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1.0F, 1.0F);
                        p.setVelocity(new Vector(0.0, 0.75, 0.0));
                        highjump_CD.put(p.getName(), System.currentTimeMillis());
                    }
                    break;

                case IRON_INGOT:
                    e.setCancelled(true);
                    if (System.currentTimeMillis() - medikit_CD.get(p.getName()) >= Cooldown.get("medikit") * 1000) {
                        if (p.isSneaking()) {
                            if (p.getHealth() <= 16.0) {
                                p.setHealth(p.getHealth() + 4.0);
                            } else if (p.getHealth() < 20.0) {
                                p.setHealth(20.0);
                            } else {
                                p.sendMessage(ChatColor.RED + "体力が満タンです");
                                p.playSound(p.getLocation(), Sound.BLOCK_LAVA_POP, 1.0F, 1.0F);
                                medikit_CD.put(p.getName(), System.currentTimeMillis());
                                break;
                            }
                            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                            p.getInventory().removeItem(medikit);
                            p.updateInventory();
                            medikit_CD.put(p.getName(), System.currentTimeMillis());
                        }
                    }
                    break;

                case COMPASS:
                    e.setCancelled(true);
                    if (System.currentTimeMillis() - detect_CD.get(p.getName()) >= Cooldown.get("detect") * 1000) {
                        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                        List<Entity> entityList = e.getPlayer().getNearbyEntities(30, 30, 30);
                        for (Entity value : entityList) {
                            if (value instanceof LivingEntity && p.getLocation().distance(value.getLocation()) < 24 ) {
                                LivingEntity entity = (LivingEntity) value;
                                entity.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 400, 0));
                            }
                        }
                        detect_CD.put(p.getName(), System.currentTimeMillis());
                    }
                    break;

                case ENCHANTED_BOOK:
                    e.setCancelled(true);
                    if (System.currentTimeMillis() - self_enchant_CD.get(p.getName()) >= Cooldown.get("self_enchant") * 1000) {
                        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 2.0F);
                        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 1));
                        self_enchant_CD.put(p.getName(), System.currentTimeMillis());
                    }
                    break;

                case IRON_BOOTS:
                    e.setCancelled(true);
                    if (System.currentTimeMillis() - superjump_CD.get(p.getName()) >= Cooldown.get("superjump") * 1000) {
                        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1.0F, 1.0F);
                        p.setVelocity(new Vector(0.0, 1.4, 0.0));
                        superjump_CD.put(p.getName(), System.currentTimeMillis());
                    }
                    break;

                case DIAMOND_BOOTS:
                    e.setCancelled(true);
                    if (System.currentTimeMillis() - ultrajump_CD.get(p.getName()) >= Cooldown.get("ultrajump") * 1000) {
                        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0F, 2.0F);
                        p.setVelocity(new Vector(0.0, 10.0, 0.0));
                        ultrajump_CD.put(p.getName(), System.currentTimeMillis());
                    }
                    break;

                case CLOCK:
                    e.setCancelled(true);
                    if (System.currentTimeMillis() - marking_CD.get(p.getName()) >= Cooldown.get("marking") * 1000) {
                        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                        new PlaySoundLater(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F).runTaskLater(Main.getPlugin(), 2);
                        new PlaySoundLater(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F).runTaskLater(Main.getPlugin(), 4);
                        int allplayers = runner.getSize() + frozen.getSize();
                        int players = frozen.getSize();
                        if (allplayers == 0) {
                            allplayers = 1;
                        }
                        double duration = (double) players / (double) allplayers;
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            if (runner.getEntries().contains(player.getName())) {
                                player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 10 + (int) (90 * duration), 0));
                            }
                        }
                        marking_CD.put(p.getName(), System.currentTimeMillis());
                    }
                    break;

                case STICK:
                    p.sendMessage(FClass.get(p.getName()));
            }


        } else if ((e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) && !frozen.getEntries().contains(p.getName())) {
            switch (p.getInventory().getItemInMainHand().getType()) {
                case NETHER_STAR:
                    e.setCancelled(true);
                    if (System.currentTimeMillis() - starleft_CD.get(p.getName()) >= trickLeft * 1000) {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 40, 0));
                        p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 40, 0));
                        p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 2.0F);
                        starleft_CD.put(p.getName(), System.currentTimeMillis());
                    }
                    break;

                case ENCHANTED_BOOK:
                    e.setCancelled(true);
                    if (System.currentTimeMillis() - self_enchant_CD.get(p.getName()) >= Cooldown.get("self_enchant") * 1000) {
                        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 2.0F);
                        p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 120, 0));
                        self_enchant_CD.put(p.getName(), System.currentTimeMillis());
                    }
                    break;
            }
        }
    }
}

