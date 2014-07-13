package tw.edu.ncu.CJ102;
import java.io.*;
import java.util.*;

public class TermFreqCount {
	private static Map concordance;
	private static File file;
	private static FileReader reader;
	private static BufferedReader in;

	public static void main(String args[]) {
		counting("1");
	}
	public static void counting(String DirPath) {
		for(File file:new File(DirPath).listFiles()){
			loadMap(file,null,null);
			dumpMap();
		}
	}

	private static void loadMap(File file, String word, String frequency) {
		try {
			concordance = new TreeMap();
			reader = new FileReader(file);
			in = new BufferedReader(reader);
			String line = null;
			StringTokenizer parser = null;
			int lineNumber = 0;
			BufferedWriter bw;
			if(!new File("Keyword_output_freq/").exists()){
				boolean mkdirSuccess = new File("Keyword_output_freq/").mkdirs();
				if (!mkdirSuccess) {
					System.out.println("Directory creation failed");
				}
			}
			bw = new BufferedWriter(new FileWriter("Keyword_output_freq/"+file.getName().split("\\.")[0] + "_"
					+ "keyword_output_freq.txt", false));
			while ((line = in.readLine()) != null)

			{
				++lineNumber;
				parser = new StringTokenizer(line, " ");
				
				while (parser.hasMoreTokens())// Ū�J�C�ӳ�r
				{
					word = parser.nextToken("\n").toUpperCase();
					frequency = (String) concordance.get(word);
					if (frequency == null)
						frequency = "1";// �Y�Ӧr�S�X�{�h���Ƭ�0
					else {
						int n = Integer.parseInt(frequency);
						++n;// �Y�X�{�h++
						frequency = "" + n;
					}
					concordance.put(word, frequency);

				}
				
				try {
					
					
					bw.write(word + ", " + frequency);
					bw.newLine();
					bw.flush(); // �M�Žw�İ�
					
					// �إ߹B�νw�İϿ�X��Ʀ�data.txt�ɪ�BufferedWriter����
					// �A�å�bw����ѦҤޥ�
					// �N�r��g�J�ɮ�
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			bw.close(); // ����BufferedWriter����
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private static void dumpMap() {
		Set set = concordance.entrySet();
		for (Iterator it = set.iterator(); it.hasNext();)
			System.out.println(it.next());
	}
}