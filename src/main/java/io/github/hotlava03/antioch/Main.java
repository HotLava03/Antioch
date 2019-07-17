package io.github.hotlava03.antioch;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class Main extends JavaPlugin implements Listener {

    private HashMap<String, Boolean> map = new HashMap<>();
    private int i;

    @Override
    public void onEnable() {
        // so we can throw eggs for the first time
        map.put("achieved",true);
        // prevents nullpointerexception
        map.put("isAntioch",false);
        this.getCommand("antioch").setExecutor(new AntiochCmd(this));
        getServer().getPluginManager().registerEvents(this,this);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(!map.get("achieved")){
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED+"Please use eggs later.");
            return;
        }
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.DARK_RED+"boom boom");
        try {
            if (event.getItem().getItemMeta().getDisplayName().equals(ChatColor.DARK_GRAY+"["+ChatColor.RED+"The daughter of the angry chicken"+ChatColor.DARK_GRAY+"]") && event.getItem().getItemMeta().getLore().equals(lore)) {
                event.setCancelled(true);
                startTimer(event.getPlayer(),event);
                map.put("isAntioch", true);
                map.put("achieved", false);
                return;
            }
        } catch (NullPointerException e) {
            // happens when the egg has no metadata/no lore
            return;
        }
        map.put("isAntioch",false);
    }

    @EventHandler
    public void onPlayerThrowEgg(PlayerEggThrowEvent event) {
        // This only is triggered once the egg hits the ground.
        // Don't worry about egg conflicts, it's cooled down until the countdown stops
        if(map.get("isAntioch")) {
            map.put("achieved",true);
            event.setHatching(false);
            event.getEgg().getWorld().createExplosion(event.getEgg().getLocation(),10f,true);
        }
        map.put("isAntioch",false);
    }

    private void startTimer(Player player, PlayerInteractEvent event) {
        i = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            int time = 1; // what we start from
            @Override
            public void run() {
                if (this.time == 4){
                    map.put("isAntioch",true);
                    event.getPlayer().launchProjectile(Egg.class);
                    Bukkit.getScheduler().cancelTask(i);
                    return;
                }
                if (this.time == 3){
                    player.sendMessage(ChatColor.DARK_RED+""+ChatColor.BOLD+"5!!!");
                    this.time++;
                    return;
                }
                player.sendMessage(this.time + "...");
                this.time++;
            }
        }, 0L, 20L); // because 1 second is usually 20 ticks
    }
}
