package io.github.hotlava03.antioch;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class AntiochCmd implements CommandExecutor {

    private Plugin plugin;

    AntiochCmd(Plugin instance){
        plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("[Antioch] Sorry, but this command is only available to in-game users.");
            return true;
        }
        Player player = (Player) sender;
        final PlayerInventory INV = player.getInventory();
        // If inventory is full
        if(INV.firstEmpty() == -1){
            player.sendMessage(ChatColor.RED+"Cannot add item to your inventory as it is full. Please empty at least 1 slot in order to use this command.");
            return true;
        }
        // if the user's current hand is empty, then just add the item in it
        ItemStack item = new ItemStack(Material.EGG);
        List<String> lore = new ArrayList<>();
        ItemMeta meta = item.getItemMeta();
        lore.add(ChatColor.DARK_RED+"boom boom");
        meta.setLore(lore);
        meta.setDisplayName(ChatColor.DARK_GRAY+"["+ChatColor.RED+"The daughter of the angry chicken"+ChatColor.DARK_GRAY+"]");
        item.setItemMeta(meta);
        INV.addItem(new ItemStack(item));
        player.sendMessage(ChatColor.GREEN+"Have fun! Never play with fire, though :P");
        return true;
    }
}
