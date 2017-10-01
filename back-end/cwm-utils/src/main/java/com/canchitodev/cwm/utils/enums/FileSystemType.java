/**
 * This content is released under the MIT License (MIT)
 *
 * Copyright (c) 2017, canchito-dev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * 
 * @author 		José Carlos Mendoza Prego
 * @copyright	Copyright (c) 2017, canchito-dev (http://www.canchito-dev.com)
 * @license		http://opensource.org/licenses/MIT	MIT License
 * @link		https://github.com/canchito-dev/canchito-workflow-manager
 **/
package com.canchitodev.cwm.utils.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum FileSystemType {
	undefined(0),
	FTP(1),
	LOCAL(2),
	SMB(3),
	AMAZON_S3(4),
	AMAZON_GLACIER(5);
	
	private static final Map<Integer,FileSystemType> lookup 
		= new HashMap<Integer, FileSystemType>();

	static {
	    for(FileSystemType s : EnumSet.allOf(FileSystemType.class))
	         lookup.put(s.getType(), s);
	}
	
	private int type;
	
	private FileSystemType(int type) {
	    this.type = type;
	}
	
	public int getType() { return type; }
	
	public static FileSystemType get(int type) { 
	    return lookup.getOrDefault(type, FileSystemType.undefined); 
	}
}