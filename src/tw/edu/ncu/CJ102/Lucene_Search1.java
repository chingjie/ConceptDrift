﻿package tw.edu.ncu.CJ102;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;

import javax.swing.JTextArea;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;

import tw.edu.ncu.sia.util.ServerUtil;

public class Lucene_Search1 extends HTMLEditorKit.ParserCallback {
	/**
	 * 
	 * @author 某屆學長
	 * 搜尋Solr Server以取得NGD分數
	 * 注意有可能會有ReadTimeout的例外可能！目前需要自行handle Exception!
	 * 
	 */
	static class ParserGetter extends HTMLEditorKit {
		// purely to make this methods public
		public HTMLEditorKit.Parser getParser() {
			return super.getParser();
		}
	}

	// 記錄是否將資料印出
	private boolean inHeader = false;
	private static String _sn = ""; //舊式檔名命名法

	public Lucene_Search1() {
		
	}
	// one file at a time
	public static void doit(String fileName,String path) throws IOException {
		//做實驗時務必清空舊有資料不保證IO讀取乾淨
		ParserGetter kit = new ParserGetter();
		HTMLEditorKit.Parser parser = kit.getParser();
		//HTMLEditorKit.ParserCallback callback = new Lucene_Search1();
		BufferedReader BufferedStream = new BufferedReader(new FileReader(path + fileName));
		
		
		String line;
		long t1 =0 , t2 = 0;
		long sum = 0;
		
		int l = 0;
		// ArrayList<String> stop_list = Stop_Loader.loadList("stop_list.txt");
		try {
			ServerUtil.initialize();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while ((line = BufferedStream.readLine()) != null) {
			_sn = fileName;
			t1 = System.currentTimeMillis();
			SolrSearcher solr = new SolrSearcher();
			try {
				solr.searchIndexed(line, _sn);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			t2 = System.currentTimeMillis();
			sum += (t2-t1);
			
			l++;
			
		}
		BufferedStream.close();
		BufferedWriter bw = new BufferedWriter(new FileWriter("Util/time/google_output.txt", true));
		bw.write(l+":"+sum);
		bw.newLine();
		bw.close();
		System.out.println("處理檔案"+fileName+"處理完畢");
	}

	/*public static void main(String i) throws IOException{
		System.out.println(i);
		new Lucene_Search1().doit(i);
	}*/

	public static void main(String args[]) throws IOException {
		long StartTime = System.currentTimeMillis(); // 取出目前時間
		int count=0;
		
		/*File F = new File("citeulike/citeulike_POS_filter/");
		for(File f : F.listFiles()){
			//System.out.println(f.getName().split("_")[0]);
			main(f.getName().split("_")[0]);
		}*/
		
		//找出缺少的文件來處理
//		File F = new File("citeulike/citeulike_POS_filter/");
//		for(File f : F.listFiles()){
//			File file = new File("citeulike/citeulike_Search1/"+f.getName().split("_")[0]+"_google_output1.txt");
//			if(!file.exists()){
//				System.out.println(file.getName().split("_")[0]);
//				doit(file.getName().split("_")[0]);
//				count++;
//			}
//		}
//		System.out.println(count);
		//System.out.println(System.currentTimeMillis() - StartTime);
	}
}