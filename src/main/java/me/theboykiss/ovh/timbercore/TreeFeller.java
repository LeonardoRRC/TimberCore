package me.theboykiss.ovh.timbercore;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public class TreeFeller implements Listener {
    private final JavaPlugin plugin;
    private final Random random = new Random();
    private final TreeFellerCommand treeFellerCommand;

    public TreeFeller(JavaPlugin plugin, TreeFellerCommand treeFellerCommand) {
        this.plugin = plugin;
        this.treeFellerCommand = treeFellerCommand;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (isLog(block.getType()) || isLeaves(block.getType())) {
            if (player.hasPermission("treefeller.use") && player.getGameMode() == GameMode.SURVIVAL && isAxe(item.getType()) && treeFellerCommand.isAutoFellingEnabled(player.getUniqueId())) {
                fellTree(block);
                // Desgasta el hacha
                damageItem(player, item);
            }
        }
    }

    private void fellTree(Block block) {
        // Si el bloque es de madera, rompe y repite
        if (isLog(block.getType())) {
            block.breakNaturally();
            // Comprueba los bloques directamente encima y a los lados del bloque actual
            for (int x = -1; x <= 1; x++) {
                for (int y = 0; y <= 1; y++) {
                    for (int z = -1; z <= 1; z++) {
                        // Ignora el bloque actual
                        if (x != 0 || y != 0 || z != 0) {
                            Block relative = block.getRelative(x, y, z);
                            fellTree(relative);
                        }
                    }
                }
            }
        }
        else if (isLeaves(block.getType())) {
            if (random.nextInt(100) < 1) {
                block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.APPLE));
            }
            block.breakNaturally();
        }
    }


    private void damageItem(Player player, ItemStack item) {
        int unbreakingLevel = item.getEnchantmentLevel(Enchantment.DURABILITY);
        double damagePercent = 5.0;
        switch (unbreakingLevel) {
            case 1:
                damagePercent = 3.0;
                break;
            case 2:
                damagePercent = 2.0;
                break;
            case 3:
                damagePercent = 1.0;
                break;
        }
        ItemMeta meta = item.getItemMeta();
        if (meta instanceof Damageable) {
            Damageable damageable = (Damageable) meta;
            int damage = (int) (item.getType().getMaxDurability() * damagePercent / 100);
            damageable.setDamage(damageable.getDamage() + damage);
            item.setItemMeta(meta);
            if (damageable.getDamage() > item.getType().getMaxDurability()) {
                player.getInventory().remove(item);
                player.getWorld().playSound(player.getLocation(), "entity.item.break", 1.0f, 1.0f);
            }
        }
    }

    private boolean isLog(Material material) {
        return material == Material.OAK_LOG || material == Material.BIRCH_LOG || material == Material.SPRUCE_LOG || material == Material.JUNGLE_LOG || material == Material.ACACIA_LOG || material == Material.DARK_OAK_LOG;
    }

    private boolean isLeaves(Material material) {
        return material == Material.OAK_LEAVES || material == Material.BIRCH_LEAVES || material == Material.SPRUCE_LEAVES || material == Material.JUNGLE_LEAVES || material == Material.ACACIA_LEAVES || material == Material.DARK_OAK_LEAVES;
    }

    private boolean isAxe(Material material) {
        return material == Material.WOODEN_AXE || material == Material.STONE_AXE || material == Material.IRON_AXE || material == Material.DIAMOND_AXE || material == Material.GOLDEN_AXE;
    }
}
