import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;

//import ConceptCompare.ParserGetter;

public class CompareCenter extends HTMLEditorKit.ParserCallback {
	Map<String, Double> center = new HashMap<String, Double>();
	Map<String, Double> concept = new HashMap<String, Double>();
	Map<String, Integer> degree = new HashMap<String, Integer>();
	Map<String, Integer> core = new HashMap<String, Integer>();
	ArrayList<String> newAdd = new ArrayList<String>();
static String dirPath="PsyTopic/";
	class ParserGetter extends HTMLEditorKit {
		// purely to make this methods public
		public HTMLEditorKit.Parser getParser() {
			return super.getParser();
		}
	}

	// �O���O�_�N��ƦL�X
	private boolean inHeader = false;
	private static int _sn = -1;
	// static private ArrayList<Double> num=new ArrayList<Double>();
	static double mValue = 0;

	public CompareCenter() {
	}

	// �N Parse HTML �᪺��ƦL�X
	public void handleText(char[] text, int position) {
		if (inHeader) {
			// �L�X xxxx => <A HREF = ....> xxxx </A>
			// xxxx => HTML Tag A ����r (text)
			// System.out.println("handleText: " + new String(text));

			BufferedWriter bw;

			// File out = new File(_sn + "_" + "google_output1.txt");
			// FileOutputStream outFile = new FileOutputStream(out, true);
			// OutputStreamWriter bw = new OutputStreamWriter(outFile,
			// "UTF-8");
			if (String.valueOf(text).contains("���� ")
					|| String.valueOf(text).contains(" �����G")) {
				String value = String.valueOf(text).replaceAll(",", ""); // ����
				// 173,000
				// �����G

				double value1 = Double.parseDouble(value.split(" ")[value
						.split(" ").length - 2]);

				// num.add(value1);
				// System.out.println("update1" + value1);
				mValue = value1;

				// set.add(value1);
				// String key = ((String) datas[count]);
				// set.add(key + "," + value1);
				// System.out.println(key+"==>"+value1);
				// count++;
			} else if (String.valueOf(text).contains("�䤣��M�z���d��")) {
				double value1 = Double.parseDouble("0");
				// System.out.println("update2");
				mValue = 0;
			}

		}
	}

	// Parse HTML Start Tag
	public void handleStartTag(HTML.Tag tag, MutableAttributeSet attributes,
			int position) {

		// ���R Tag �����I�b�o��
		if (tag == HTML.Tag.DIV) {
			Enumeration e = attributes.getAttributeNames();
			while (e.hasMoreElements()) {
				Object name = e.nextElement();
				String value = (String) attributes.getAttribute(name);

				// �ŦX <A HREF = "xxxx"> �ݩʪ��r��Axxxx �|�Q�L�X
				if (name == HTML.Attribute.ID && value.equals("resultStats")) {
					this.inHeader = true;
				}
			}
		} else if (tag == HTML.Tag.SPAN) {
			Enumeration e = attributes.getAttributeNames();
			while (e.hasMoreElements()) {
				Object name = e.nextElement();
				String value = (String) attributes.getAttribute(name);

				// �ŦX <A HREF = "xxxx"> �ݩʪ��r��Axxxx �|�Q�L�X
				if (name == HTML.Attribute.ID) {
					this.inHeader = true;
				}
			}
		}
	}

	void pairTest(String s) {
		ParserGetter kit = new ParserGetter();
		HTMLEditorKit.Parser parser = kit.getParser();
		HTMLEditorKit.ParserCallback callback = new CompareCenter();
		String i1 = s;
		// long runstartTime = System.currentTimeMillis(); // ���X�ثe�ɶ�
		// System.out.println("process: " + i1);
		// ��J�����R������
		// URL u = new URL("http://www.yam.com");
		String j = "http://www.google.com.tw/search?aq=f&sourceid=chrome&ie=UTF-8&q=";
		// String f = "&btnG=�j�M&aq=f&aqi=&aql=&oq=&gs_rfai=";
		String http = j + i1;
		// System.out.println(http);

		URL u;
		try {
			u = new URL(http);
			HttpURLConnection urlConnection = (HttpURLConnection) u
					.openConnection();
			urlConnection
					.addRequestProperty(
							"user-agent",
							"Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-TW; rv:1.8.1.14) Gecko/20080404 Firefox/2.0.0.14"
									+ "SV1; .NET CLR 1.1.4322; .NET CLR 2.0.50727)");

			// Ū�J����
			// InputStream in = u.openStream();
			BufferedInputStream in = new BufferedInputStream(urlConnection
					.getInputStream());
			InputStreamReader r = new InputStreamReader(in, "UTF-8");
			// System.out.println(r);

			// �I�s parse method �}�l�i�� Parse HTML
			// _sn = no;
			parser.parse(r, callback, true);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				IP_change();
				pairTest(s);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	double NGDandGCD(double x, double y, double m) {
		double a = Math.log10(x) / Math.PI; // ��@�b�|
		double b = Math.log10(y) / Math.PI; // ��G�b�|

		double r1 = 1.78 - Math.sqrt(a);
		double r2 = 1.78 - Math.sqrt(b);
		double d = Math.pow(r1, 2) + Math.pow(r2, 2);
		double GCD = Math.sqrt(d);
		// System.out.println("GCD="+GCD);

		double logX = Math.log10(x);
		double logY = Math.log10(y);
		double logM = Math.log10(m);
		double logN = 9.906;

		double NGD = (Math.max(logX, logY) - logM)
				/ (logN - Math.min(logX, logY));
		// System.out.println("NGD="+NGD);
		// }
		return NGD;
	}

	void getCenter(String path) {

		BufferedReader br;

		// br2 = new BufferedReader(new FileReader(no + "_" + "stem.txt"));
		String line = "";
		// try {
		double sum = 0;
		center.clear();
		try {
			br = new BufferedReader(new FileReader(path));
			while ((line = br.readLine()) != null) {
				// c1.put();
				// sum += Double.parseDouble(line.split(",")[2]);
				center.put(line.split(",")[0], Double.parseDouble(line
						.split(",")[2]));
				// core.put(line.split(",")[0], Integer
				// .parseInt(line.split(",")[2]));
				// degree.put(line.split(",")[0], Integer
				// .parseInt(line.split(",")[3]));
				// }
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}

	void getConcept(String s) {

		BufferedReader br;

		// br2 = new BufferedReader(new FileReader(no + "_" + "stem.txt"));
		String line = "";
		// try {
		concept.clear();
		double sum = 0;
		try {
			br = new BufferedReader(new FileReader(dirPath+"Set/" + s));
			while ((line = br.readLine()) != null) {
				// c1.put();
				// sum += Double.parseDouble(line.split(",")[2]);
				concept.put(line.split(",")[0], Double.parseDouble(line
						.split(",")[1]));
				// }
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String args[]) throws IOException,
			InterruptedException {
		long StartTime = System.currentTimeMillis(); // ���X�ثe�ɶ�
		CompareCenter test = new CompareCenter();
		String[] s = new String[3];
		Scanner input = new Scanner(System.in);

		test.getCenter(dirPath+"center.txt");
		File dir = new File(dirPath+"Set/");
		BufferedWriter bw = new BufferedWriter(new FileWriter(
				dirPath+"centerCP.txt", true));
		double total = 0;
		for (String f : dir.list()) {
			test.getConcept(f);
			// test.g
			// System.out.println(test.c1.keySet().iterator().next());
//			test.mValue = 100;
			Iterator<String> i1 = test.center.keySet().iterator();
			double bsum = 0;
			double csum = 0;
			double sum = 0;
			double bbase = 0;
			double cbase = 0;
			double[] di = new double[test.center.keySet().size()];
			int d = 0;
			// Rel_Loader rel_loader = new Rel_Loader();
			// rel_loader.loadList("RelationBase.txt");

			while (i1.hasNext()) {
				s[0] = i1.next();
				Iterator<String> i2 = test.concept.keySet().iterator();

				while (i2.hasNext()) {
					s[1] = i2.next();
					System.out.println(s[0] + "," + s[1]);
					s[2] = "\"" + s[0] + "\",\"" + s[1] + "\"";
					test.pairTest(s[2].replace(",", "+"));
					// if (rel_loader.Rel_exist(s[2]) != -1) {
					double NGD = test.NGDandGCD(test.center.get(s[0]),
							test.concept.get(s[1]), mValue);
					 System.out.println(mValue);
					if (NGD > 1)
						NGD = 1;
					if (NGD < 0)
						NGD = 0;
					// bsum = bsum + (NGD * (test.degree.get(s[0])));
					// csum = csum + (NGD * (test.core.get(s[0])));
					sum = sum + NGD;
					// bbase = bbase + (test.degree.get(s[0]));
					// cbase = cbase + (test.core.get(s[0]));
					// di[d] = di[d] + NGD;
					if (NGD < 0.5) {
						test.newAdd.add(s[1]);
					}
					// System.out.println(s[2] + ":" + (test.degree.get(s[0]))
					// + "==>" + NGD);
					// d++;

					// }else
					// {
					// //i2.
					// System.out.println(mValue);
					// test.pairTest(s[2].replace(",", "+"));
					// rel_loader.rel_map.put(s[2], mValue);
					// rel_loader.updateBase();
					// }
				}

			}

			double avg1 = sum
					/ (test.center.keySet().size() * test.concept.keySet()
							.size());
			// double avg2 = bsum / bbase;
			// double avg3 = csum / cbase;

			// double avg=sum/base;
			double[] tol = new double[di.length];
			for (int i = 0; i < di.length; i++) {
				tol[i] = di[i] / test.concept.keySet().size();
			}
			double sum1 = 0;
			for (double a : tol) {
				sum1 = sum1 + (a * a);
			}
			// System.out.println("SUM1=" + sum);
			// double avgdis = Math.pow(sum1, 0.5);
			// avgdis = avgdis / Math.pow(test.center.keySet().size(), 0.5);
			// System.out.println("AVGD=" + avgdis);
			System.out.println(f + ":" + avg1);
			bw.write(f + ":" + avg1);
			bw.newLine();
			bw.flush(); // �M�Žw�İ�
			// IP_change();
			total = total + avg1;
		}
		bw.write("T:" + total / dir.list().length);
		bw.close();
		// System.out.println("AVG2=" + avg2);
		// System.out.println("AVG3=" + avg3);
		// System.out.println(test.newAdd);

		// s[0]="\""+input.next()+"\"";
		// s[1]="\""+input.next()+"\"";

		// s[2]=s[0]+"+"+s[1];
		// long ProcessTime = System.currentTimeMillis() - StartTime; // �p��B�z�ɶ�
		// AverageTime += ProcessTime; // �ֿn�p��ɶ�
		// System.out.println(ProcessTime);
	}

	private static void IP_change() throws IOException, InterruptedException {
		String ip1 = InetAddress.getLocalHost().toString().split("/")[1];
		// URL test=new URL("http://www.google.com/");
		// boolean flag;
		System.out.println("ip1=" + ip1);
		InetAddress test = InetAddress.getByName("140.115.1.254");
		while (ip1.equals(InetAddress.getLocalHost().toString().split("/")[1])
				|| !test.isReachable(5000)) {
			try {
				Process p = Runtime.getRuntime().exec("IP_1.bat");
				System.out.println(InetAddress.getLocalHost().toString().split(
						"/")[1]);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Thread.sleep(5000);
		}
		System.out.println("final ip="
				+ InetAddress.getLocalHost().toString().split("/")[1]);
	}
}
