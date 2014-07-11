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
import java.util.Enumeration;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;

public class Google_Search1 extends HTMLEditorKit.ParserCallback {
	class ParserGetter extends HTMLEditorKit {
		// purely to make this methods public
		public HTMLEditorKit.Parser getParser() {
			return super.getParser();
		}
	}

	// �O���O�_�N��ƦL�X
	private boolean inHeader = false;
	private static int _sn = -1;

	public Google_Search1() {
	}

	// �N Parse HTML �᪺��ƦL�X
	public void handleText(char[] text, int position) {
		if (inHeader) {

			BufferedWriter bw;

			try {
				bw = new BufferedWriter(new FileWriter("Search1/" + _sn + "_"
						+ "google_output1.txt", true));
				if (String.valueOf(text).contains("���� ")
						|| String.valueOf(text).contains(" �����G")) {
					System.out.println(String.valueOf(text));
					bw.write(String.valueOf(text));
					bw.newLine();
					bw.flush(); // �M�Žw�İ�
					bw.close(); // ����BufferedWriter����
					// �إ߹B�νw�İϿ�X��Ʀ�data.txt�ɪ�BufferedWriter����
					// �A�å�bw����ѦҤޥ�
					// �N�r��g�J�ɮ�

				} else if (String.valueOf(text).contains("�䤣��M�z���d��")) {

					bw.write("���� 0 �����G\n");
					bw.close(); // ����BufferedWriter����
					// l++;
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
		File f = new File("Search1/" + no + "_" + "google_output1.txt");
		f.delete();
		// try {
		ParserGetter kit = new ParserGetter();
		HTMLEditorKit.Parser parser = kit.getParser();
		HTMLEditorKit.ParserCallback callback = new Google_Search1();
		FileReader FileStream = new FileReader("POS_filter/" + no + "_"
				+ "filter_output1.txt");
		BufferedReader BufferedStream = new BufferedReader(FileStream);
		String line;
		int l = 1;
		// ArrayList<String> stop_list = Stop_Loader.loadList("stop_list.txt");
		while ((line = BufferedStream.readLine()) != null) {
			_sn = no;
			line = URLEncoder.encode(line, "UTF-8");
			search_term(line);
		}

	}

	private void search_term(String i1) {
		ParserGetter kit = new ParserGetter();
		HTMLEditorKit.Parser parser = kit.getParser();
		HTMLEditorKit.ParserCallback callback = new Google_Search1();
		long runstartTime = System.currentTimeMillis(); // ���X�ثe�ɶ�
		System.out.println("process: " + i1);
		// ��J�����R������
//		String j = "http://www.google.com.tw/search?aq=f&sourceid=chrome&ie=Big5&q=";
		String j = "http://www.google.com.tw/search?aq=f&sourceid=chrome&ie=UTF-8&q=";
		String http = j + i1; //i1�����d�ߪ��r
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
			BufferedInputStream in = new BufferedInputStream(urlConnection
					.getInputStream());
			InputStreamReader r = new InputStreamReader(in, "UTF-8");
			parser.parse(r, callback, true);
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
			System.out.println("IP�Q��A���s���oIP");
			
				IP_Operation.IP_change();//��IP
				System.out.println("���s���o�r��("+i1+")�j�M���G");
			search_term(i1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
	public static void main(int i) throws IOException {

		new Google_Search1().doit(i);

	}

	public static void main(String args[]) throws IOException {

		long StartTime = System.currentTimeMillis(); // ���X�ثe�ɶ�
		main(1);
		System.out.println(System.currentTimeMillis() - StartTime);
	}
}
