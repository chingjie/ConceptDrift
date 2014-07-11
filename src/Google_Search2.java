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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.JOptionPane;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;

//import Google_Search1.ParserGetter;

public class Google_Search2 extends HTMLEditorKit.ParserCallback {
	class ParserGetter extends HTMLEditorKit {
		// purely to make this methods public
		public HTMLEditorKit.Parser getParser() {
			return super.getParser();
		}
	}

	// �O���O�_�N��ƦL�X
	private boolean inHeader = false;
	private static int _sn = -1;
	String i1;
	int l = 1;

	public Google_Search2() {
	}

	// �N Parse HTML �᪺��ƦL�X
	public void handleText(char[] text, int position) {
		if (inHeader) {
			// �L�X xxxx => <A HREF = ....> xxxx </A>
			// xxxx => HTML Tag A ����r (text)
			// System.out.println("handleText: " + new String(text));

			BufferedWriter bw;
			try {
				// File out = new File(_sn + "_" + "google_output1.txt");
				// FileOutputStream outFile = new FileOutputStream(out, true);
				// OutputStreamWriter bw = new OutputStreamWriter(outFile,
				// "UTF-8");
				// System.out.println(String.valueOf(text).contains("���� "));
				bw = new BufferedWriter(new FileWriter("Search2/" + _sn + "_"
						+ "google_output2.txt", true));
				if (String.valueOf(text).contains("���� ")
						|| String.valueOf(text).contains(" �����G")) {

					System.out.println("write==>" + l);
					bw.write(String.valueOf(text));
					bw.newLine();
					// bw.write("\r\n");

					l++;
					bw.flush(); // �M�Žw�İ�
					bw.close(); // ����BufferedWriter����
					// �إ߹B�νw�İϿ�X��Ʀ�data.txt�ɪ�BufferedWriter����
					// �A�å�bw����ѦҤޥ�
					// �N�r��g�J�ɮ�

				} else if (String.valueOf(text).contains("�䤣��M�z���d��")) {

					bw.write("���� 0 �����G\n");
					bw.close(); // ����BufferedWriter����
					l++;
				}

			} catch (IOException e) {
				e.printStackTrace();
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

	public void doit(int no) throws IOException {
		// try {

		FileReader FileStream = new FileReader("Pairs/" + no + "_"
				+ "pairs.txt");
		BufferedReader BufferedStream = new BufferedReader(FileStream);
		String line;
		// ArrayList<String> stop_list = Stop_Loader.loadList("stop_list.txt");
		// int l=1;
		while ((line = BufferedStream.readLine()) != null) {
			// line = BufferedStream.readLine();
			// if (stop_list.contains(line.toLowerCase())) {
			// System.out.println("skip:"+line+"============");
			// } else {
			// String i1 = "\"" + line + "\"";
			i1 = line;
			_sn = no;
			line = URLEncoder.encode(line, "UTF-8");
			search_pair(line);
			// in.close();
			// r.close();
			// if(l%150==0)
			// {
			// String ip1=InetAddress.getLocalHost().toString().split("/")[1];
			// //URL test=new URL("http://www.google.com/");
			// //boolean flag;
			// System.out.println("ip1="+ip1);
			// InetAddress test=InetAddress.getByName("140.115.1.254");
			// while(ip1.equals(InetAddress.getLocalHost().toString().split("/")[1])||!test.isReachable(5000))
			// {
			// try {
			// Process p=Runtime.getRuntime().exec("IP_1.bat");
			// System.out.println(InetAddress.getLocalHost().toString().split("/")[1]);
			// } catch (IOException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// Thread.sleep(5000);
			// }
			// System.out.println("final ip="+InetAddress.getLocalHost().toString().split("/")[1]);
			// }
			// //Thread.sleep(1000);// �j1��j�M�@��
			// System.out.println("finish: " + i1);
			// l++;
			// System.out.println(System.currentTimeMillis()-runstartTime);
			// }

		}

		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
	}

	private void search_pair(String i1) {
		try {
			ParserGetter kit = new ParserGetter();
			HTMLEditorKit.Parser parser = kit.getParser();
			HTMLEditorKit.ParserCallback callback = new Google_Search2();
			long runstartTime = System.currentTimeMillis(); // ���X�ثe�ɶ�

			System.out.println("process: " + i1 + "==>");
			// ��J�����R������
			// URL u = new URL("http://www.yam.com");
			String j = "http://www.google.com.tw/search?aq=f&sourceid=chrome&ie=UTF-8&q=";
//			String j = "http://www.google.com.tw/search?aq=f&sourceid=chrome&ie=Big5&q=";
			// String f = "&btnG=�j�M&aq=f&aqi=&aql=&oq=&gs_rfai=";
			String http = j + i1;
			// System.out.println(http);

			URL u = new URL(http);
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
			// in.close();
			// r.close();
			Thread.sleep(0);// �j1��j�M�@��
			System.out.println("finish: " + i1);
			System.out.println(System.currentTimeMillis() - runstartTime);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("IP�Q��A���s���oIP");

			IP_Operation.IP_change();// ��IP
			System.out.println("���s���o�r��(" + i1 + ")�j�M���G");

			search_pair(i1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(int i) throws IOException {

		File f = new File("Search2/" + i + "_" + "google_output2.txt");
		f.delete();
		new Google_Search2().doit(i);

	}

	public static void main(String args[]) throws IOException {

		// new Google_Search1().doit(i);
		long StartTime = System.currentTimeMillis(); // ���X�ثe�ɶ�
		main(1);
		System.out.println(System.currentTimeMillis() - StartTime);

	}
}
