package me.Jonnyfant.BukkitCoralBoneMeal;

import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.block.data.type.CoralWallFan;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class BlockBoneMealListener implements Listener {
    private BukkitCoralBoneMeal plug;
    private final Map<Material, Material> CORAL_WALL_FANS = new HashMap<Material, Material>() {
        {
            put(Material.HORN_CORAL_BLOCK, Material.HORN_CORAL_WALL_FAN);
            put(Material.BRAIN_CORAL_BLOCK, Material.BRAIN_CORAL_WALL_FAN);
            put(Material.BUBBLE_CORAL_BLOCK, Material.BUBBLE_CORAL_WALL_FAN);
            put(Material.FIRE_CORAL_BLOCK, Material.FIRE_CORAL_WALL_FAN);
            put(Material.TUBE_CORAL_BLOCK, Material.TUBE_CORAL_WALL_FAN);
        }
    };
    private final Map<Material, Material> CORAL_FANS = new HashMap<Material, Material>() {
        {
            put(Material.HORN_CORAL_BLOCK, Material.HORN_CORAL_FAN);
            put(Material.BRAIN_CORAL_BLOCK, Material.BRAIN_CORAL_FAN);
            put(Material.BUBBLE_CORAL_BLOCK, Material.BUBBLE_CORAL_FAN);
            put(Material.FIRE_CORAL_BLOCK, Material.FIRE_CORAL_FAN);
            put(Material.TUBE_CORAL_BLOCK, Material.TUBE_CORAL_FAN);
        }
    };
    private final Map<Material, Material> CORALS = new HashMap<Material, Material>() {
        {
            put(Material.HORN_CORAL_BLOCK, Material.HORN_CORAL);
            put(Material.BRAIN_CORAL_BLOCK, Material.BRAIN_CORAL);
            put(Material.BUBBLE_CORAL_BLOCK, Material.BUBBLE_CORAL);
            put(Material.FIRE_CORAL_BLOCK, Material.FIRE_CORAL);
            put(Material.TUBE_CORAL_BLOCK, Material.TUBE_CORAL);
        }
    };

    public BlockBoneMealListener (BukkitCoralBoneMeal p)
    {
        plug=p;
    }

    @EventHandler
    public void onBlockBonemeal(BlockFertilizeEvent event) {
        if (applyBoneMeal(event.getBlock()))
            event.setCancelled(true);
    }

    public boolean applyBoneMeal(Block b) {
        // Not a Coral Block
        if (!CORALS.containsKey(b.getType())) {
            return false;
        }
        ArrayList<BlockFace> neighbourDirections = new ArrayList<BlockFace>(Arrays.asList(
                //BlockFace.DOWN, Corals cannot grow down
                BlockFace.UP,
                BlockFace.EAST,
                BlockFace.WEST,
                BlockFace.NORTH,
                BlockFace.SOUTH));
        Random r = new Random();
        Collections.shuffle(neighbourDirections);

        for (int i = 0; i <= neighbourDirections.size(); i++) {
            BlockFace direction = neighbourDirections.get(i);
            Block newCoral = b.getRelative(direction);

            Bukkit.broadcastMessage("Selected " + direction + " Block: " + newCoral.getType());

            // Not Water, next face
            if (!newCoral.getType().equals(Material.WATER)) {
                continue;
            }

            //Chance for coral instead of fan
            if (direction == BlockFace.UP) {
                if(r.nextDouble() <= plug.getConfig().getDouble(plug.CFG_NOT_FAN_CHANCE_KEY)) {
                    newCoral.setType(CORALS.get(b.getType()));
                    //Waterlogged bd = (Waterlogged) newCoral.getBlockData();
                    //bd.setWaterlogged(true);
                    //newCoral.setBlockData(bd);
                    return true;
                }
                newCoral.setType(CORAL_FANS.get(b.getType()));
                return true;
            }

            // Create coral fan
            newCoral.setType(CORAL_WALL_FANS.get(b.getType()));
            CoralWallFan coralWallFan = (CoralWallFan) CORAL_WALL_FANS.get(b.getType()).createBlockData();
            //BlockData coralWallFan = Bukkit.createBlockData(CORAL_FANS.get(b.getType()));
            coralWallFan.setFacing(direction);
            newCoral.setBlockData(coralWallFan);

            return true;
        }
        return false;
    }
}
