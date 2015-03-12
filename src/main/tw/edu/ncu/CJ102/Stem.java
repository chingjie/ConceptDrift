package tw.edu.ncu.CJ102;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Stem {

	/**
	 * 
	 */
	public Stem() {
		// TODO Auto-generated constructor stub
	}

	public static void stemming(String fileName) throws IOException {
		System.out.println("Stem:處理檔案"+fileName+"中...");
		String numberOfTermPath = SettingManager.getSetting(SettingManager.NumOfTermDir);
		String stemmedPath = SettingManager.getSetting(SettingManager.stemmedDir);
		
		BufferedReader termReader = new BufferedReader(new FileReader(numberOfTermPath +fileName));
		//String line;

		ArrayList<String> list = new ArrayList<String>();
		for (String line=termReader.readLine();line!=null;line=termReader.readLine()){
			list.add(line);
		}

		termReader.close();
		String[] datas = (String[]) list.toArray();
		LinkedHashSet<String> set = new LinkedHashSet<String>();
		for (String line:datas){
			String key = line.split(",")[0]; //first keyword
			String value = line.split(",")[1];
			
			Pattern p= Pattern.compile("[(),\"\\?!:;=]");

	         Matcher m=p.matcher(key);
	         String first=m.replaceAll("");
	         
	         Porter_Stemmer stemmer = new Porter_Stemmer();
	         String stemmedKey = stemmer.stemming(first);
			 set.add (stemmedKey + "," + value);
			
		}			  
		
		String[] objs = (String[]) set.toArray();
		BufferedWriter bw;
		bw = new BufferedWriter(new FileWriter(stemmedPath + fileName, false));
		for (int j = 0; j < objs.length; j++) {

			String objs_out = (String) objs[j];

				try {
					bw.write(objs_out);
					bw.newLine();
					bw.flush(); // 清空緩衝區
					
				} catch (IOException f) {
					f.printStackTrace();
				}
		}
		bw.close(); // 關閉BufferedWriter物件
		System.out.println("處理檔案"+fileName+"處理完畢");
	}

}