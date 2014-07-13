package tw.edu.ncu.CJ102;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class POS_Filter {

	/**
	 * 
	 */
	public POS_Filter() {
		// TODO Auto-generated constructor stub
	}

	public static void filter(String f) throws IOException {
		FileReader FileStream = new FileReader("Keyword_output_freq/"+f + "_"
				+ "keyword_output_freq.txt");
		BufferedReader BufferedStream = new BufferedReader(FileStream);
		String e = "";

		ArrayList list = new ArrayList();
		while ((e = BufferedStream.readLine()) != null) {
			list.add(e);
		}

		Object[] datas = list.toArray();
		LinkedHashSet<String> set = new LinkedHashSet<String>();

		for (int i = 0; i < datas.length; i++) {
			int j, k;
			if (i == datas.length - 1)
				j = i;
			else
				j = i + 1;

			if (j == datas.length - 1)
				k = j;
			else
				k = j + 1;

			String key1 = ((String) datas[i]).split(", ")[0]; // Algorithm
			String tag1 = ((String) datas[i]).split(", ")[1]; // NN
			//String count1 = ((String) datas[i]).split(", ")[2]; // 1

			String key2 = ((String) datas[j]).split(", ")[0]; 
			String tag2 = ((String) datas[j]).split(", ")[1]; 
			//String count2 = ((String) datas[j]).split(", ")[2]; 

			String key3 = ((String) datas[k]).split(", ")[0]; 
			String tag3 = ((String) datas[k]).split(", ")[1]; 
			//String count3 = ((String) datas[k]).split(", ")[2]; 
			System.out.println("key1:" + key1 + " " + tag1);
			System.out.println("key2:" + key2 + " " + tag2);
			System.out.println("key3:" + key3 + " " + tag3);
			//��r�L�o�A�ھ�D. Tufis and O. Mason��1998���X��Qtag
			if(tag1.equals("NN") || tag1.equals("NP") ||tag1.equals("JJ")){
				set.add(key1);
				System.out.println("add:" + key1);
			}
			//�զX�r�L�o
			/*if (key1.equals(key2)) {//�̫�@�Ӧr
				if ((tag1.equals("NN") || tag1.equals("NNS") || tag1
						.equals("NP"))
						&& key1.length() > 2) {
					set.add(key1);
					// i++;
					System.out.println("add:" + key1);
				}
			} else if (key2.equals(key3)) {//�̫��Ӧr
				if ((tag1.equals("NN") || tag1.equals("NP")
						|| tag1.equals("NNS") || tag1.equals("NPS") || tag1
						.equals("JJ"))
						&& key1.length() > 2) {
					if ((key1.endsWith(",") || key1.endsWith("."))||(key2.startsWith(",") || key2.startsWith("."))) {
						set.add(key1);
						System.out.println("add:" + key1);
					} else if ((tag2.equals("NN") || tag2.equals("NP")
							|| tag2.equals("NPS") || tag2.equals("NNS"))
							&& key2.length() > 2) {

						set.add(key1 + "+" + key2);
						System.out.println("add:" + key1 + "+" + key2);
						i++;
						// System.out.println(key + "_" + key1);
					} else {
						set.add(key1);
						System.out.println("add:" + key1);
					}
				}

			} else {
				if ((tag1.equals("NN") || tag1.equals("NP")
						|| tag1.equals("NNS") || tag1.equals("JJ"))
						&& key1.length() > 2) {
					if ((key1.endsWith(",") || key1.endsWith("."))||(key2.startsWith(",") || key2.startsWith("."))) {
						set.add(key1);
						System.out.println("add:" + key1);//�걵�@�Ӧr
					} else if ((tag2.equals("NN") || tag2.equals("NP")
							|| tag2.equals("NPS") || tag2.equals("NNS"))
							&& key2.length() > 2) {
						if ((key2.endsWith(",") || key2.endsWith("."))||(key3.startsWith(",") || key3.startsWith("."))) {
							set.add(key1 + "+" + key2);
							System.out.println("add:" + key1 + "+" + key2);
							i++;//�걵��Ӧr
						} else if ((tag3.equals("NN") || tag3.equals("NP")
								|| tag3.equals("NPS") || tag3.equals("NNS"))
								&& key3.length() > 2) {
							set.add(key1 + "+" + key2 + "+" + key3);
							System.out.println("add:" + key1 + "+" + key2 + "+"
									+ key3);//�걵�T�Ӧr
							i = i + 2;
						} else {
							set.add(key1 + "+" + key2);
							System.out.println("add:" + key1 + "+" + key2);//�걵��Ӧr
							i++;
						}

					}else
					{
						set.add(key1);
						System.out.println("add:" + key1);//�걵�@�Ӧr
					}

				}
			}*/
		}


		Object[] objs = set.toArray();
		if(!new File("POS_Filter/").exists()){
			boolean mkdirSuccess = new File("POS_Filter/").mkdirs();
			
		}
		BufferedWriter bw;
		bw = new BufferedWriter(new FileWriter("POS_Filter/"+f + "_" + "filter_output1.txt",
				false));
		String objs_out = "";
		for (int i = 0; i < objs.length; i++) {

			System.out.println(objs[i]);
			objs_out = (String) objs[i];
			objs_out = objs_out.replace("]", "");
			objs_out = objs_out.replace("[", "");
			Pattern p = Pattern.compile("[(),.\"\\?!:;']");

			Matcher m = p.matcher(objs_out);

			objs_out = m.replaceAll("");
			try {
				bw.write("\"" + objs_out + "\"");
				bw.newLine();

				bw.flush(); // �M�Žw�İ�

			} catch (IOException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}

		}

		bw.close(); // ����BufferedWriter����
	}


	public static void main(int no) throws IOException {
		//filter(no);
	}

	public static void main(String args[]) throws IOException {
		String fileName="acq_0000005_qtag";
		filter(fileName);
	}

}
