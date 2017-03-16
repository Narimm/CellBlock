/*
 * Copyright (c) 2017
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package au.com.addstar.cellblock.managers;

import au.com.addstar.cellblock.CellBlock;
import au.com.addstar.cellblock.objects.CellArea;
import au.com.addstar.cellblock.objects.CellDoor;
import au.com.addstar.cellblock.utilities.Utilities;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.material.Door;

import javax.annotation.Nullable;

import java.io.File;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created for the AddstarMC Project.
 * Created by Narimm on 9/03/2017.
 */
public class CellDoorManager {
    private final TypeToken<ArrayList<CellDoor>> type = new TypeToken<ArrayList<CellDoor>>() {
    };
    private List<CellDoor> doors;
    private final CellBlock plugin;

    public CellDoorManager(CellBlock plugin) {
        this.plugin = plugin;
        doors = new ArrayList<>();
        load();
    }

    public List<CellDoor> getDoors() {
        return doors;
    }

    public void setDoors(List<CellDoor> doors) {
        this.doors = doors;
    }

    public void addDoor(CellDoor door) {
        if (door != null) {
            doors.add(door);
        }
    }


    public boolean isCellDoor(Block block) {
        CellDoor door;
        if (block.hasMetadata("celldoor")) {
            Object value = block.getMetadata("celldoor");
            UUID celldoorUuid = UUID.fromString(value.toString());
            door = getCellDoor(celldoorUuid);
        } else {
            door = getCellDoor(block.getLocation());
        }
        return door != null;
    }

    public CellDoor getCellDoor(Block block) {
        CellDoor door = null;
        if (block.hasMetadata("celldoor")) {
            Object value = block.getMetadata("celldoor");
            UUID celldoorUuid = UUID.fromString(value.toString());
            door = getCellDoor(celldoorUuid);
        }
        if (door == null) {
            door = getCellDoor(block.getLocation());
        }
        return door;
    }


    @Nullable
    private CellDoor getCellDoor(Location location) {
        for (CellDoor door : doors) {
            if (door.getDoorBlockLocation() == location || door.getTopDoorBlockLocation() == location) {
                return door;
            }
        }
        return null;
    }

    private CellDoor getCellDoor(UUID uuid) {
        for (CellDoor door : doors) {
            if (door.getUuid() == uuid) {
                return door;
            }
        }
        return null;
    }

    public void listCellDoors(CommandSender sender) {
        sender.sendMessage("----- Door List -------");
        for (CellDoor door : doors) {
            sender.sendMessage(door.toString());
        }
    }


    public void createNewCellDoor(CommandSender sender, CellArea area, Location location, Material material) {
        Block clicked = location.getBlock();
        if (clicked.getType() != Material.AIR && (clicked.getRelative(BlockFace.UP).getType() == Material.AIR)) {
            Location doorBottom = clicked.getRelative(BlockFace.UP).getLocation();
            Block block = doorBottom.getBlock();
            Door door = new Door(material);
            door.setTopHalf(false);
            door.setHinge(true);
            door.setOpen(false);
            block.getState().setData(door);
            Door doortop = door.clone();
            doortop.setTopHalf(true);
            doortop.setHinge(true);
            door.setOpen(false);
            Block topBlock = block.getRelative(BlockFace.UP);
            topBlock.getState().setData(doortop);
            CellDoor cellDoor = new CellDoor(block.getLocation(), area, material);
            cellDoor.setBlockMeta(plugin);
            doors.add(cellDoor);
            sender.sendMessage("New Door created at " + block.getLocation().toString());
        } else {
            sender.sendMessage("Must have 2 blocks of air above selected block to create a door.");
        }
    }


    public void save() {
        File parent = new File("saves");
        File file = new File(parent, "cellDoors.json");
        Utilities.saveFile(file, (ArrayList<CellDoor>) doors, type);
        plugin.getLogger().info("Door Save Completed...");
    }

    private void load() {
        File parent = new File("saves");
        File file = new File(parent, "celldoors.json");
        if (!parent.exists()) return;
        List<CellDoor> celldoors = Utilities.loadFile(file, type);
        int added = 0;
        if (celldoors != null && celldoors.size() > 0) {
            if (doors.size() == 0) {
                doors.addAll(celldoors);
                added = doors.size();
            } else {
                added = doors.size();
                boolean exists = false;
                for (CellDoor door : celldoors) {
                    for (CellDoor existing : doors) {
                        if (door.getUuid() == existing.getUuid()) {
                            exists = true;
                            break;
                        }
                    }
                    if (exists) break;
                    doors.add(door);
                    added++;
                }
            }
        }
        plugin.getLogger().info("Loading CellDoors Completed loaded: " + added);
    }

}
