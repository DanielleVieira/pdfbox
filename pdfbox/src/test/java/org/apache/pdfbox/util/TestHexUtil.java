/*
 * Copyright 2016 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.pdfbox.util;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 * @author Michael Doswald
 */
class TestHexUtil
{
    
    /**
     * Test conversion from short to char[]
     */
    @Test
    void testGetCharsFromShortWithoutPassingInABuffer()
    {
        assertArrayEquals(new char[]{'0','0','0','0'}, Hex.getChars((short)0x0000), "new char does equals HEX char");
        assertArrayEquals(new char[]{'0','0','0','F'}, Hex.getChars((short)0x000F), "new char does equals HEX char");
        assertArrayEquals(new char[]{'A','B','C','D'}, Hex.getChars((short)0xABCD), "new char does equals HEX char");
        assertArrayEquals(new char[]{'B','A','B','E'}, Hex.getChars((short)0xCAFEBABE), "new char does equals HEX char");
    }

    /**
     * Check conversion from String to a char[] which contains the UTF16-BE encoded
     * bytes of the string as hex digits
     *
     */
    @Test
    void testGetCharsUTF16BE()
    {
        assertArrayEquals(new char[]{'0','0','6','1','0','0','6','2'}, Hex.getCharsUTF16BE("ab"), "new char does equals HEX char UTF-16");
        assertArrayEquals(new char[]{'5','E','2','E','5','2','A','9'}, Hex.getCharsUTF16BE("帮助"), "new char does equals HEX char UTF-16");
    }

    /**
     * Test getBytes() and getString() and decodeHex()
     */
    @Test
    void testMisc()
    {
        byte[] byteSrcArray = new byte[256];
        for (int i = 0; i < 256; ++i)
        {
            byteSrcArray[i] = (byte) i;

            byte[] bytes = Hex.getBytes((byte) i);
            assertEquals(2, bytes.length, "lengh of HEX bytes does equals 2");
            String s2 = String.format(Locale.US, "%02X", i);
            assertArrayEquals(s2.getBytes(StandardCharsets.US_ASCII), bytes, "s2 bytes (Local format) does equals HEX bytes");
            s2 = Hex.getString((byte) i);
            assertArrayEquals(s2.getBytes(StandardCharsets.US_ASCII), bytes, "s2 bytes does equals HEX bytes");
            
            assertArrayEquals(new byte[]{(byte) i}, Hex.decodeHex(s2), "new bytedoes equals decode Hex s2 byte");
        }
        byte[] byteDstArray = Hex.getBytes(byteSrcArray);
        assertEquals(byteDstArray.length, byteSrcArray.length * 2, "byteDstArray does is twice as large as byteSrcArray");

        String dstString = Hex.getString(byteSrcArray);
        assertEquals(dstString.length(), byteSrcArray.length * 2, "dstString does is twice as large as byteSrcArray");

        assertArrayEquals(dstString.getBytes(StandardCharsets.US_ASCII), byteDstArray, "dstString bytes does equals byteDstArray");
        
        assertArrayEquals(byteSrcArray, Hex.decodeHex(dstString), "byteSrcArray does equals decode HEX dstString");
    }

}
