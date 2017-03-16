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

import au.com.addstar.cellblock.configuration.AutoConfig;
import au.com.addstar.cellblock.configuration.ConfigField;

import java.io.File;

/**
 * Created for use for the Add5tar MC Minecraft server
 * Created by benjamincharlton on 11/03/2017.
 */
public class Config extends AutoConfig {

    @ConfigField(comment = "The world name we use to create the Cell Block World")
    public final String cellWorldName = "CellBlock";
    @ConfigField(comment = "The world name we use to create the Cell Block World")
    public final int plotMaxSize = 40;
    @ConfigField(comment = "The Name of the Schematic you want in each plot...make sure it fits the size of the plot exactly")
    public final String schematicName = "cellBlockSchematic";

    Config(File file) {
        super(file);
    }
}
