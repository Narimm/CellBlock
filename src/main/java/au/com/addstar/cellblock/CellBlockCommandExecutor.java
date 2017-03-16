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

package au.com.addstar.cellblock;

import au.com.addstar.cellblock.objects.CellArea;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;


public class CellBlockCommandExecutor implements CommandExecutor {

    private final CellBlock plugin;

    CellBlockCommandExecutor(CellBlock plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args != null) {
            if (args.length == 0) {
                displayHelp(sender);
                return true;
            }
            String commandType = args[0];
            switch (commandType) {
                case "createdoor":
                    if (args[1] == null) {
                        displayHelp(sender);
                        break;
                    } else {
                        String areaId = args[1];
                        CellArea area = plugin.getCellAreaManager().getCellArea(areaId);
                        Location loc;
                        if (sender instanceof Player) {
                            Set<Material> transparent = new HashSet<>();
                            transparent.add(Material.AIR);
                            loc = ((Player) sender).getTargetBlock(transparent, 4).getLocation();
                        } else {
                            if (args.length != 4) {
                                sender.sendMessage("Must be used as a player...");
                                displayHelp(sender);
                                break;
                            }
                            loc = new Location(Bukkit.getWorld(plugin.config.cellWorldName), Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]));

                        }
                        plugin.getCellDoorManager().createNewCellDoor(sender, area, loc, Material.IRON_DOOR);
                    }
                    break;
                case "list":
                    if (args[1] == null) {
                        displayHelp(sender);
                        break;
                    }
                    switch (args[1]) {
                        case "areas":
                            plugin.getCellAreaManager().listCellAreas(sender);
                            break;
                        case "cells":
                            plugin.getCellManager().listCells(sender);
                            break;
                        case "doors":
                            plugin.getCellDoorManager().listCellDoors(sender);
                            break;
                        default:
                            displayHelp(sender);
                            break;
                    }
                    break;
                case "area":
                    if (args[1] == null) {
                        displayHelp(sender);
                        break;
                    }
                    switch (args[1]) {
                        case "setfiller":
                            if (sender instanceof Player) {
                                Set<Material> transparent = new HashSet<>();
                                transparent.add(Material.AIR);
                                Block block = ((Player) sender).getTargetBlock(transparent, 4);
                                CellArea cellArea = plugin.getCellAreaManager().findCellArea(block.getLocation());
                                if(cellArea != null){
                                    cellArea.setCellFiller(block.getType());
                                    sender.sendMessage("Filler Block set to " + block.getType().name());
                                }else{
                                    sender.sendMessage("No Cell Area found in this location.");
                                }
                            } else {
                                sender.sendMessage("Must run in game as a player with a block targetted from within a CellBlock");
                                displayHelp(sender);
                            }
                        case "setwalls":
                            if (sender instanceof Player) {
                                Set<Material> transparent = new HashSet<>();
                                transparent.add(Material.AIR);
                                Block block = ((Player) sender).getTargetBlock(transparent, 4);
                                CellArea cellArea = plugin.getCellAreaManager().findCellArea(block.getLocation());
                                if(cellArea != null) {
                                    cellArea.setCellWalls(block.getType());
                                    sender.sendMessage("Wall Replacement Block set to " + block.getType().name());
                                }else{
                                sender.sendMessage("No Cell Area found in this location.");
                            }
                            } else {
                                sender.sendMessage("Must run in game as a player with a block targetted from within a CellBlock");
                                displayHelp(sender);
                            }
                        case "setrespawn":
                            String areaId = args[2];
                            CellArea area = plugin.getCellAreaManager().getCellArea(areaId);
                            Location loc;
                            if (sender instanceof Player) {
                                loc = ((Player) sender).getLocation();
                            } else {
                                if (args.length != 4) {
                                    sender.sendMessage("Must be used as a player...");
                                    displayHelp(sender);
                                    break;
                                }
                                loc = new Location(Bukkit.getWorld(plugin.config.cellWorldName), Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]));
                            }
                            plugin.getCellAreaManager().setCellAreaRespawn(sender, area, loc);
                        default:
                            displayHelp(sender);
                            break;
                    }
                    break;
                default:
                    displayHelp(sender);
                    break;
            }
        } else {
            displayHelp(sender);
            return true;
        }
        return false;
    }

    private void displayHelp(CommandSender sender) {
        sender.sendMessage("/cellblock createdoor <cellAreaId> [<x> <y> <z>]");
        sender.sendMessage("/cellblock list areas");
        sender.sendMessage("/cellblock list cells");
        sender.sendMessage("/cellblock list doors");
        sender.sendMessage("/cellblock area setfiller  {Player Only}");
        sender.sendMessage("/cellblock area setwalls   {Player Only}");
        sender.sendMessage("/cellblock area setrespawn <cellAreaId> [<x> <y> <z>]");

    }

}
