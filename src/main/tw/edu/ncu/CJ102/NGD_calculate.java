package tw.edu.ncu.CJ102;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;

public class NGD_calculate {
	
	
	public static void NGD(String fileName) {
		String pairPath = SettingManager.getSetting(SettingManager.IndexMultiTermDir);//should be the GoogleFilter2 result
		String stemmedPath = SettingManager.getSetting(SettingManager.stemmedDir);
		String writePath = SettingManager.getSetting(SettingManager.NGDCalcDir);
//		System.out.println("處理檔案"+fileName+"中...");
		try {
			//組合字部分
			//NumberOfPair
			BufferedReader numberOfPairReader = new BufferedReader(new FileReader(pairPath + fileName));
			String e1 = "";

			ArrayList<String> pairlist = new ArrayList<String>();
			while ((e1 = numberOfPairReader.readLine()) != null) {

				pairlist.add(e1);
			}
			numberOfPairReader.close();
			FileReader FileStream = new FileReader(stemmedPath + fileName);
			BufferedReader stemmedReader = new BufferedReader(FileStream);
			String e = "";

			ArrayList<String> termlist = new ArrayList<String>();
			while ((e = stemmedReader.readLine()) != null) {

				termlist.add(e);

			}
			stemmedReader.close();

			Object[] datas = termlist.toArray();
			LinkedHashSet<String> set = new LinkedHashSet<String>();
			for (int i = 0; i < datas.length; i++) {
				String key1 = String.valueOf(datas[i]).split(",")[0];
				double x = Double.parseDouble(String.valueOf(datas[i]).split(
						",")[1]);
				for (int j = i + 1; j < datas.length; j++) {
					String key2 = String.valueOf(datas[j]).split(",")[0];
					double y = Double.parseDouble(String.valueOf(datas[j])
							.split(",")[1]);
					double m = 0;
					//組合字部分
					for (String o : pairlist) {
						if (o.contains("\"" + key1 + "\"+\"" + key2 + "\"")
								|| o.contains("\"" + key2 + "\"+\"" + key1
										+ "\""))
						{
							m = Double.parseDouble(o.split(",")[1]);
							break;
						}
					}

					double NGD=NGD_cal(x,y,m);
					set.add(key1 + "," + key2 + ";" + NGD);
				}
			}
			Object[] objs = set.toArray();
			//TODO clean old File
//			File file = new File("citeulike/citeulike_NGD/" + fileName + "_" + "nNGD.txt");
//			file.delete();
			BufferedWriter bw;
			bw = new BufferedWriter(new FileWriter(writePath + fileName, false));
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
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
//		System.out.println("檔案"+fileName+"處理完畢");
	}
	
	public static double NGD_cal(double x, double y, double m) {
		double logX = Math.log10(x);
		double logY = Math.log10(y);
		double logM=0.0;
		
		logM = Math.log10(m);
		//當X=0的時候要處理Log(0)的問題，在此先改成m為1，讓LogM=0

		//9.906是Google的
		//double logN = 5.507;
		double logN = 6.627;//LogN 為總體文件(N)的Log10值 TODO 應該設定自動取得此值(用Solr *:* query應該即可)
		//4.64是Lucnen的
		//double logN = 4.64;

		double NGD = (Math.max(logX, logY) - logM)
				/ (logN - Math.min(logX, logY));
		
		if (m == 0)
			NGD = 1;//避免無限大
		if (NGD > 1)
			NGD = 1;
		if (NGD < 0)
			NGD = 0;
		return NGD;
	}

	public static void main(String args[]) throws IOException {
		File F = new File("citeulike/citeulike_Stem/");
		for(File f : F.listFiles()){
			//System.out.println(f.getName().split("_")[0]);
			NGD(f.getName().split("_")[0]);
		}
		//NGD(500);
	}

}
