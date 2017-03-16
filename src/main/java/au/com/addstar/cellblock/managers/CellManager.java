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
import au.com.addstar.cellblock.objects.Cell;
import au.com.addstar.cellblock.objects.CellArea;
import au.com.addstar.cellblock.utilities.Utilities;
import com.google.gson.reflect.TypeToken;
import com.intellectualcrafters.plot.commands.Auto;
import com.intellectualcrafters.plot.object.*;
import com.intellectualcrafters.plot.object.Location;
import com.intellectualcrafters.plot.util.MathMan;
import com.plotsquared.bukkit.object.BukkitPlayer;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.*;

/**
 * Created for the AddstarMC Project.
 * Created by Narimm on 8/03/2017.
 */
public class CellManager {
    private final Map<UUID, Cell> playerCells;
    private final CellBlock plugin;
    private final TypeToken<HashMap<UUID, Cell>> type = new TypeToken<HashMap<UUID, Cell>>() {
    };

    public CellManager(CellBlock plugin) {
        playerCells = new HashMap<>();
        this.plugin = plugin;
        loadcells();
    }

    private static Plot getNewCell(Player player, PlotArea plotArea) {
        PlotPlayer pplayer = new BukkitPlayer(player);
        PlotId bot = plotArea.getMin();
        PlotId top = plotArea.getMax();
        PlotId origin = new PlotId(MathMan.average(bot.x, top.x), MathMan.average(bot.y, top.y));
        PlotId id = new PlotId(0, 0);
        int width = Math.max(top.x - bot.x + 1, top.y - bot.y + 1);
        int max = width * width;
        for (int i = 0; i <= max; i++) {
            PlotId currentId = new PlotId(origin.x + id.x, origin.y + id.y);
            Plot current = plotArea.getPlotAbs(currentId);
            if (current.canClaim(pplayer)) {
                return current;
            }
            id = Auto.getNextPlotId(id, 1);
        }
        return null;
    }

    private void addCell(Player player, Cell cell) {
        playerCells.put(player.getUniqueId(), cell);
    }

    private Location claimCell(Player player, CellArea cellblock) {
        if (player.hasPermission("Plots.plot.*") && player.hasPermission("CellBlock.size.*")) {
            PlotPlayer pp = new BukkitPlayer(player);
            Plot plot = getNewCell(player, plugin.getCellAreaManager().getPlotArea(cellblock.getPlotAreaID()));
            if (plot != null) {
                if (plot.canClaim(pp)) {
                    plot.claim(pp, false, cellblock.getSchematicName());
                    Cell cell = new Cell(cellblock, plot.getId(), getCellSize(player, cellblock.getMaxPlotSize()), setDoorLocation(plot));
                    resize(cell);
                    addCell(player, cell);
                    return cell.getPlotDoor();
                }
            }
        }
        return null;
    }

    public Location getCellSpawn(Player player, CellArea cellBlock) {
        Cell cell = getCell(player.getUniqueId());
        if (cell != null) {
            return cell.getPlotDoor();
        }
        //Only need below if the cell is not registered correctly below should not run unless the player does not have a cell
        Set<Plot> plots = plugin.getCellAreaManager().getPlotArea(cellBlock.getPlotAreaID()).getPlots(player.getUniqueId());
        if (plots.size() > 1) {
            Iterator iter = plots.iterator();
            Bukkit.getLogger().warning("Player should not have more than one plot per area");
            return ((Plot)iter.next()).getDefaultHome();
        }
        if (plots.size() == 1) {
            Iterator iter = plots.iterator();
            return ((Plot)iter.next()).getDefaultHome();


        }
        if (plots.size() < 1) {
            //todo add a permission check.
            player.sendMessage("The screws have assigned you a cell on this block....good luck fish...");
            return claimCell(player, cellBlock);
        }
        return null;
    }

    private int getCellSize(Player player, int maxPlotSize) {
        int max = 0;

        if (player.hasPermission("cellblock.cell.size.*") || player.hasPermission("cellblock.cell.size.*")) {
            return maxPlotSize;
        } else {
            for (int ctr = 0; ctr < maxPlotSize; ctr++) {
                if (player.hasPermission("cellblock.cell.size." + ctr)) {
                    max = ctr;
                }
            }
        }
        return max;
    }

    private Cell getCell(UUID uuid) {
        if (playerCells.containsKey(uuid)) {
            return playerCells.get(uuid);
        }
        return null;
    }

    private Location setDoorLocation(Plot plot) {
        RegionWrapper region = plot.getLargestRegion();
        int plotSize = region.maxX - region.minX;
        int halfx = region.minX + (plotSize / 2);
        return new Location(plot.getCenter().getWorld(), region.minX + halfx, 64, region.minZ, 0, 0);
    }

    private void resize(Cell cell) {
        resize(cell, cell.getSize());
    }

    private void resize(Cell cell, int newSize) {
        if (newSize > cell.getArea().getMaxPlotSize()) newSize = cell.getArea().getMaxPlotSize();
        int cellrad;
        if (newSize % 2 == 0) {
            cellrad = newSize % 2;
            newSize++; //newsize must be odd..
        } else {
            cellrad = (newSize - 1) / 2; //this is the number of blocks either side of the door....
        }
        cell.setSize(newSize);
        org.bukkit.Location door = cell.getBukkitDoor();
        org.bukkit.Location loc1 = new org.bukkit.Location(door.getWorld(), door.getX() + cellrad, door.getY(), door.getZ());
        org.bukkit.Location loc2 = new org.bukkit.Location(door.getWorld(), door.getX() - cellrad, door.getY() + newSize, door.getZ() + newSize);
        doReplace(loc1, loc2, cell.getArea().getCellFiller(), cell.getArea().getCellWalls());
    }

    private void doReplace(org.bukkit.Location min, org.bukkit.Location max, Material replace, Material with) {
        for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
            for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                    Block blk = min.getWorld().getBlockAt(new org.bukkit.Location(min.getWorld(), x, y, z));
                    if (blk.getType() == replace) {
                        blk.setType(with);
                    }
                }
            }
        }
    }

    public void saveCells() {
        File parent = new File("saves");
        File file = new File(parent, "cells.json");
        Utilities.saveFile(file, (HashMap<UUID, Cell>) playerCells, type);
        plugin.getLogger().info("Door Save Completed...");


    }

    private void loadcells() {
        File parent = new File("saves");
        File file = new File(parent, "cellAreas.json");
        if (!parent.exists()) return;
        Map<UUID, Cell> loadedCells = Utilities.loadFile(file, type);
        int added = 0;
        if (loadedCells != null && loadedCells.size() > 0) {
            if (playerCells.size() == 0) {
                playerCells.putAll(loadedCells);
                added = playerCells.size();
            } else {
                added = playerCells.size();
                boolean exists = false;
                UUID eUuid = null;
                Cell eCell = null;
                for (Map.Entry<UUID, Cell> entry : loadedCells.entrySet()) {
                    for (Map.Entry<UUID, Cell> existing : playerCells.entrySet()) {
                        if (entry.getKey() == existing.getKey()) {
                            eUuid = entry.getKey();
                            eCell = entry.getValue();
                            exists = true;
                            break;
                        }
                    }
                    if (exists) break;
                }
                playerCells.put(eUuid, eCell);
                added++;
            }
        }
        plugin.getLogger().info("Loading Cells Completed. Loaded: " + added);
    }

    public void listCells(CommandSender sender) {
        sender.sendMessage("----- Cell List -------");
        for (Map.Entry<UUID, Cell> entry : playerCells.entrySet()) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(entry.getKey());
            sender.sendMessage("Ownner: " + player.getName() + "Cell:" + entry.getValue().toString());
        }
    }

}
