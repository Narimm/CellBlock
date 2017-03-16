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

package au.com.addstar.cellblock.utilities;

import com.google.gson.Gson;

import java.io.*;

import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

/**
 * Created for use for the Add5tar MC Minecraft server
 * Created by benjamincharlton on 16/03/2017.
 */
public class Utilities {

    private static final Gson gsonEncoder = new Gson();

    /**
     * The objects class must much the TypeTokens underlying class.
     *
     * @param file   the file to save to
     * @param object the object to save
     * @param type   a TypeToken that is defined using the same class as the object.
     */
    public static <T extends Serializable> void saveFile(File file, T object, TypeToken<T> type) {
        try {
            if (file.exists() || file.createNewFile()) {
                OutputStream out = new FileOutputStream(file);
                String encoded = gsonEncoder.toJson(object, type.getType());
                out.write(encoded.getBytes());
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static <T extends Serializable> T loadFile(File file, TypeToken<T> type) {
        if (!file.exists()) {
            return null;
        }
        try {
            InputStream in = new FileInputStream(file);
            InputStreamReader inread = new InputStreamReader(in);
            JsonReader reader = new JsonReader(inread);
            return gsonEncoder.fromJson(reader, type.getType());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
