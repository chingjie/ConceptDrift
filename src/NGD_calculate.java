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

	public static void NGD(String no) {
		System.out.println("�B�z�ɮ�"+no+"��...");
		try {
			//�զX�r����
			FileReader FileStream1;

			FileStream1 = new FileReader("citeulike/citeulike_Number_of_pair/" + no + "_"
					+ "number_of_pair.txt");

			BufferedReader BufferedStream1 = new BufferedReader(FileStream1);
			String e1 = "";

			ArrayList<String> pairlist = new ArrayList<String>();
			while ((e1 = BufferedStream1.readLine()) != null) {

				pairlist.add(e1);
			}

			FileReader FileStream = new FileReader("citeulike/citeulike_Stem/" + no + "_"
					+ "stem.txt");
			BufferedReader BufferedStream = new BufferedReader(FileStream);
			String e = "";

			ArrayList<String> termlist = new ArrayList<String>();
			while ((e = BufferedStream.readLine()) != null) {

				termlist.add(e);

			}

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
					//�զX�r����
					for (String o : pairlist) {
						if (o.contains("\"" + key1 + "\"+\"" + key2 + "\"")
								|| o.contains("\"" + key2 + "\"+\"" + key1
										+ "\""))
						// if(o.contains(key1+"+"+key2)||o.contains(key2+"+"+key1))
						{
							m = Double.parseDouble(o.split(",")[1]);
							// System.out.println("get m="+m);
							break;
						}
					}
					//System.out.println("x=" + x + " y=" + y + " m=" + m);
//					double logX = Math.log10(x);
//					double logY = Math.log10(y);
//					double logM = Math.log10(m);
//					double logN = 9.906;
//					// double logN = 10;
//					double NGD = (Math.max(logX, logY) - logM)
//							/ (logN - Math.min(logX, logY));
					double NGD=NGD_cal(x,y,m);
					//System.out.println(key1 + "," + key2 + ";" + NGD);
					//if(NGD<=0.15){
						//set.add(key1 + "," + key2 + ";" + NGD);
					//}else{
						set.add(key1 + "," + key2 + ";" + NGD);
					//}
				}
			}
			Object[] objs = set.toArray();
			File file = new File("citeulike/citeulike_NGD/" + no + "_" + "nNGD.txt");
			file.delete();
			BufferedWriter bw;
			bw = new BufferedWriter(new FileWriter("citeulike/citeulike_NGD/" + no + "_"
					+ "nNGD.txt", false));
			for (int j = 0; j < objs.length; j++) {

				//System.out.println(objs[j]);
				String objs_out = (String) objs[j];

				try {

					bw.write(objs_out);
					bw.newLine();
					bw.flush(); // �M�Žw�İ�

				} catch (IOException f) {
					// TODO Auto-generated catch block
					f.printStackTrace();
				}

			}
			bw.close(); // ����BufferedWriter����
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("�ɮ�"+no+"�B�z����");
	}
	
	static double NGD_cal(double x, double y, double m) {
		double logX = Math.log10(x);
		double logY = Math.log10(y);
		double logM=0.0;
		
		logM = Math.log10(m);
		//��X=0���ɭԭn�B�zLog(0)�����D�A�b�����令m��1�A��LogM=0

		//9.906�OGoogle��
		//double logN = 5.507;
		double logN = 6.627;
		//4.64�OLucnen��
		//double logN = 4.64;

		double NGD = (Math.max(logX, logY) - logM)
				/ (logN - Math.min(logX, logY));
		
		if (m == 0)
			NGD = 1;//�קK�L���j
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
