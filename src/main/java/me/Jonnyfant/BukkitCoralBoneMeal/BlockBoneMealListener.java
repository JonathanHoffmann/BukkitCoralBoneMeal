package me.Jonnyfant.BukkitCoralBoneMeal;

import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.CoralWallFan;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.inventory.ItemStack;

public class BlockBoneMealListener implements Listener {
    private final List<Material> CORAL_BLOCKS = new LinkedList<Material>(Arrays.asList(Material.HORN_CORAL_BLOCK, Material.BUBBLE_CORAL_BLOCK, Material.FIRE_CORAL_BLOCK, Material.BRAIN_CORAL_BLOCK, Material.TUBE_CORAL_BLOCK));
    private final Map<Material, Material> CORAL_FANS = new HashMap<Material, Material>() {{
        put(Material.HORN_CORAL_BLOCK, Material.HORN_CORAL_FAN);
        put(Material.BRAIN_CORAL_BLOCK, Material.BRAIN_CORAL_FAN);
        put(Material.BUBBLE_CORAL_BLOCK, Material.BUBBLE_CORAL_FAN);
        put(Material.FIRE_CORAL_BLOCK, Material.FIRE_CORAL_FAN);
        put(Material.TUBE_CORAL_BLOCK, Material.TUBE_CORAL_FAN);
    }};
    private final Map<Material, Material> CORALS = new HashMap<Material, Material>() {{
        put(Material.HORN_CORAL_BLOCK, Material.HORN_CORAL);
        put(Material.BRAIN_CORAL_BLOCK, Material.BRAIN_CORAL);
        put(Material.BUBBLE_CORAL_BLOCK, Material.BUBBLE_CORAL);
        put(Material.FIRE_CORAL_BLOCK, Material.FIRE_CORAL);
        put(Material.TUBE_CORAL_BLOCK, Material.TUBE_CORAL);
    }};
    @EventHandler
    public void onBlockBreak(BlockFertilizeEvent event) {
        if (applyBoneMeal(event.getBlock()))
            event.setCancelled(true);
    }

    public boolean applyBoneMeal(Block b) {
        boolean changed = false;
        if (CORAL_BLOCKS.contains(b.getType())) {
            List<Block> neighbouringBlocks = new LinkedList<Block>();
            neighbouringBlocks.add(b.getRelative(0, -1, 0));
            neighbouringBlocks.add(b.getRelative(0, 1, 0));
            neighbouringBlocks.add(b.getRelative(-1, 0, 0));
            neighbouringBlocks.add(b.getRelative(1, 0, 0));
            neighbouringBlocks.add(b.getRelative(0, 0, -1));
            neighbouringBlocks.add(b.getRelative(0, 0, 1));
            Random r = new Random();

            for (int i = 0; i < 10; i++) {
                int ra = r.nextInt(neighbouringBlocks.size());
                Block newCoral = neighbouringBlocks.get(ra);
                Bukkit.broadcastMessage("Selected " + ra + "Block: " + newCoral.getType());
                if (newCoral.getType().equals(Material.WATER)) {
                    if(ra==1 &&r.nextDouble()>0.5)
                    {
                        changed = true;
                        newCoral.setType(CORALS.get(b.getType()));
                        break;
                    }
                    changed = true;
                    newCoral.setType(CORAL_FANS.get(b.getType()));
                    CoralWallFan d = (CoralWallFan) newCoral.getBlockData();
                    d.setFacing(BlockFace.WEST);
                    newCoral.setBlockData(d);
                    break;
                }
            }
        }
        return changed;
    }
}
