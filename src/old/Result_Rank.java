import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Result_Rank {
	public static void main(String args[])
	{
		File F = new File("citeulike/citeulike_NGD/");
		for(File f : F.listFiles()){
			//System.out.println(f.getName().split("_")[0]);
			ranking(f.getName().split("_")[0]);
		}
		//ranking(1);
	}
	public static void ranking(String no) {
		System.out.println("處理檔案"+no+"中...");
		try {
			
			BufferedReader br = new BufferedReader(new FileReader("citeulike/citeulike_NGD/"+no + "_" + 
					"nNGD.txt"));
			Map<String, Double> map_Data = new HashMap<String, Double>();
			String line = "";
			while ((line = br.readLine()) != null) {
				String key = line.split(";")[0];
				String dupKey=key.split(",")[1]+","+key.split(",")[0];
				Double value = Double.parseDouble(line.split(";")[1]);
				if(!map_Data.containsKey(dupKey)&&!key.split(",")[1].equals(key.split(",")[0]))
				map_Data.put(key, value);
			}

			List<Map.Entry<String, Double>> list_Data = new ArrayList<Map.Entry<String, Double>>(
					map_Data.entrySet());
			Iterator<Map.Entry<String, Double>> iterator = list_Data.iterator();

			Collections.sort(list_Data,
					new Comparator<Map.Entry<String, Double>>() {
						public int compare(Map.Entry<String, Double> o1,
								Map.Entry<String, Double> o2) {
							return (int) ((o1.getValue() - o2.getValue()) * 1000.0);
						}
					});
			File file=new File("citeulike/citeulike_Rank/"+no + "_" + "Rank.txt");
	file.delete();
			BufferedWriter bw = new BufferedWriter(new FileWriter("citeulike/citeulike_Rank/"+no + "_" + "Rank.txt"));
			//bw.write("");
			//bw.newLine();

			while (iterator.hasNext()) {
				Map.Entry<String, Double> entry = iterator.next();
				//System.out.println(entry.getKey() + "," + entry.getValue());
				bw.write(entry.getKey() + "," + entry.getValue());
//				rel_loader.rel_map.put(entry.getKey(), entry.getValue());
				bw.newLine();
			}
			bw.flush();
			bw.close();
//			rel_loader.updateBase();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("檔案"+no+"處理完畢");
	}
}

