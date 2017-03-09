package com.hubert.solrTest;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import com.chenlb.mmseg4j.ComplexSeg;
import com.chenlb.mmseg4j.Dictionary;
import com.chenlb.mmseg4j.MMSeg;
import com.chenlb.mmseg4j.Seg;
import com.chenlb.mmseg4j.Word;

public class MMSeg4jTest {
	public static void main(String[] args) throws IOException{
		File file = new File("/Lucene/mmseg4j/data");
		Dictionary dic = Dictionary.getInstance(file);
		String txt = "哦， 那个是我本机上测的， 没打包到jar里。 ";
		Seg seg = new ComplexSeg(dic); 
		MMSeg mmSeg = new MMSeg(new StringReader(txt), seg); 
		Word word = null;
		while((word = mmSeg.next())!=null) {
				if(word != null) { 
					System.out.print(word + "|"); 
			} 
		}
	}
}
