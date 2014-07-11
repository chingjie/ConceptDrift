import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Term_Freq_and_POS_filterx {
	private static Map concordance;
	private static File file;
	private static FileReader reader;
	private static BufferedReader in;
	
	public static void main(String args[]) {
		// TODO Auto-generated method stub
		counting();
	}
	
	public static void counting() {
		File dir = new File( "Qtag/");
		System.out.println("�W�v�p��P�ϩʹL�o�{�Ƕ}�l");
		System.out.println("�ӷ���ƧX��"+dir.getName());
		File[] fileslist = dir.listFiles();
		for (File files : fileslist){
			System.out.println("�B�z���"+files.getName()+"��...");
			loadMap(files);
		}
		/*File f = new File("Qtag/003_qtag.txt");
		loadMap(f);*/
	}

	private static void loadMap(File file){
		try{
			concordance = new TreeMap();
			reader = new FileReader(file);
			in = new BufferedReader(reader);
			String line = null;
			String word = "";
			String frequency = "";
			String filename = "";
			for(int i=0; i<file.getName().split("_").length;i++){
				//System.out.println("filename = "+ filename);
				if(i==0){
					filename=file.getName().split("_")[0];
				}else{
					char[] filename_temp = file.getName().split("_")[i].toCharArray();
					if(!Character.isDigit(filename_temp[0])){ //�p�G�Ĥ@�Ӧr���O�Ʀr�N����ɦW�����F
						filename=filename+"_"+file.getName().split("_")[i];
					}else{
						filename=filename+"_"+file.getName().split("_")[i];
						break;
					}
				}
			}
			int lineNumber = 0;
			String key1 = ""; //�r��1
			String tag1 = ""; //�r��1������
			String key2 = ""; //�r��1
			String tag2 = ""; //�r��1������
			String key3 = ""; //�r��1
			String tag3 = ""; //�r��1������
			LinkedHashSet<String> set = new LinkedHashSet<String>(); //�x�s�ŦX���ʹL�o���r��
			BufferedWriter bw;
			ArrayList list = new ArrayList(); //�x�sqtag��󤺮e
			bw = new BufferedWriter(new FileWriter("Keyword_output_freq/"+filename+"_"+"keyword_output_freq.txt",false));
			while((line = in.readLine()) != null){
				list.add(line);
			}
			Object[] datas = list.toArray();
			for(int i = 0; i < datas.length; i++){
				int j, k;
				if(i == datas.length-1){ //�̫�@�Ӧr
					j = i;
				}else{
					j = i + 1;
				}
				if(j == datas.length-1){ //�̫��Ӧr
					k = j;
				}else{
					k = j + 1;
				}
				if(i==0){ //�Ĥ@�Ӧr
					key1 = ((String) datas[i]).split(", ")[0].toUpperCase();
					tag1 = ((String) datas[i]).split(", ")[1];
				}else if(i==1){ //�ĤG�Ӧr
					key1 = ((String) datas[i]).split(", ")[0].toUpperCase();
					tag1 = ((String) datas[i]).split(", ")[1];

					key2 = ((String) datas[j]).split(", ")[0].toUpperCase(); 
					tag2 = ((String) datas[j]).split(", ")[1]; 
				}else{
					key1 = ((String) datas[i]).split(", ")[0].toUpperCase();
					tag1 = ((String) datas[i]).split(", ")[1];

					key2 = ((String) datas[j]).split(", ")[0].toUpperCase(); 
					tag2 = ((String) datas[j]).split(", ")[1]; 

					key3 = ((String) datas[k]).split(", ")[0].toUpperCase(); 
					tag3 = ((String) datas[k]).split(", ")[1];
				}
				System.out.println("key1:" + key1 + " " + tag1);
				System.out.println("key2:" + key2 + " " + tag2);
				System.out.println("key3:" + key3 + " " + tag3);
				
				//�զX�r�L�o
				if(key1.equals(key2)){ //�̫�@�Ӧr
					if ((tag1.equals("NN") || tag1.equals("NNS") || tag1.equals("NP")) && key1.length()>2){
						word = key1;
					}
				}else if(key2.equals(key3)){ //�̫��Ӧr
					if ((tag1.equals("NN") || tag1.equals("NP") || tag1.equals("NNS") || tag1.equals("NPS") || tag1.equals("JJ")) && key1.length()>2){
						if ((key1.endsWith(",") || key1.endsWith("."))||(key2.startsWith(",") || key2.startsWith("."))) {
							word = key1;
						}else if((tag2.equals("NN") || tag2.equals("NP") || tag2.equals("NPS") || tag2.equals("NNS")) && key2.length()>2){
							word = key1 + "+" + key2;
							i++;
						}else{
							word = key1;
						}
					}
				}else{
					if((tag1.equals("NN") || tag1.equals("NP") || tag1.equals("NNS") || tag1.equals("JJ")) && key1.length()>2){
						if ((key1.endsWith(",") || key1.endsWith("."))||(key2.startsWith(",") || key2.startsWith("."))) {
							word = key1; //�걵�@�Ӧr
						}else if((tag2.equals("NN") || tag2.equals("NP") || tag2.equals("NPS") || tag2.equals("NNS")) && key2.length()>2){
							if((key2.endsWith(",") || key2.endsWith("."))|| (key3.startsWith(",") || key3.startsWith("."))){
								word = key1 + "+" + key2; //�걵��Ӧr
								i++;
							}else if((tag3.equals("NN") || tag3.equals("NP") || tag3.equals("NPS") || tag3.equals("NNS")) && key3.length()>2){
								word = key1 + "+" + key2 + "+" + key3; //�걵�T�Ӧr
								i = i+2;
							}else{
								word = key1 + "+" + key2; //�걵��Ӧr
								i++;
							}
						}else{
							word = key1;
						}
					}
				}
				if(word!=""){
					set.add(word);
					System.out.println("add:" + word);
					frequency = (String) concordance.get(word);
					if(frequency == null){
						frequency = "1"; //�Y�Ӧr�S�X�{�h���Ƭ�0
					}else{
						int n = Integer.parseInt(frequency);
						++n; //�Y�X�{�h++
						frequency = "" + n;
					}
					concordance.put(word, frequency);
					bw.write(word + ", " + frequency);
					bw.newLine();
					bw.flush();
					word="";
				}
			}
			bw.close(); // ����BufferedWriter����
			bw = new BufferedWriter(new FileWriter("POS_filter/"+filename + "_" + "filter_output1.txt", false));
			String objs_out = "";
			Object[] objs = set.toArray();
			for (int i = 0; i < objs.length; i++){
				System.out.println(objs[i]);
				objs_out = (String) objs[i];
				objs_out = objs_out.replace("]", "");
				objs_out = objs_out.replace("[", "");
				Pattern p = Pattern.compile("[(),.\"\\?!:;']");
				Matcher m = p.matcher(objs_out);
				objs_out = m.replaceAll("");
				bw.write("\"" + objs_out + "\"");
				bw.newLine();
				bw.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
