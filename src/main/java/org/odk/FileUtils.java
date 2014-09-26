/*
 * Copyright (C) 2009 University of Washington
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.odk;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
 * Static methods used for common file operations.
 *
 * @author Carl Hartung (carlhartung@gmail.com)
 */
public class FileUtils {
	/**
     * Modified from code by:  Carl Hartung (carlhartung@gmail.com)
     * From: ODK Collect FileUtils.java
     * @author Scott
     * @param file
     * @return
     */
    public static byte[] getFileAsBytes(File file) {
        byte[] bytes = null;
        InputStream is = null;
        try {
            is = new FileInputStream(file);

            // Get the size of the file
            long length = file.length();
            if (length > Integer.MAX_VALUE) {
                return null;
            }

            // Create the byte array to hold the data
            bytes = new byte[(int) length];

            // Read in the bytes
            int offset = 0;
            int read = 0;
            try {
                while (offset < bytes.length && read >= 0) {
                    read = is.read(bytes, offset, bytes.length - offset);
                    offset += read;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            // Ensure all the bytes have been read in
            if (offset < bytes.length) {
                try {
                    throw new IOException("Could not completely read file " + file.getName());
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            return bytes;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;

        } finally {
            // Close the input stream
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
    
    
    
	/**Sourced from code found at:
	 * http://examples.javacodegeeks.com/core-java/nio/charbuffer/convert-string-to-byte-array-utf-encoding/
	 * 
	 * By Byron Kiourtzoglou
	 *  
     * @param str
     * @return
     */
    public static byte[] stringToBytesUTFNIO(String str) {
    	char[] buffer = str.toCharArray();
    	byte[] b = new byte[buffer.length << 1];
    	CharBuffer cBuffer = ByteBuffer.wrap(b).asCharBuffer();	
    	for(int i = 0; i < buffer.length; i++)
    		cBuffer.put(buffer[i]);
    	return b;
	}
    
    public static String bytesToStringUTFNIO(byte[] bytes) {
		CharBuffer cBuffer = ByteBuffer.wrap(bytes).asCharBuffer();
    	return cBuffer.toString();
	}
    
    public static InputStream convertStringToInputStream(String input){
    	return new ByteArrayInputStream(input.getBytes(Charset.forName("UTF-8")));
    }
    
    public static DataInputStream convertStringToDataInputStream(String string){
    	InputStream inputStream = convertStringToInputStream(string);
    	return new DataInputStream(inputStream);
    }
}
