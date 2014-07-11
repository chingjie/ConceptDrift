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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import javax.swing.JOptionPane;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;

//import Google_Search1.ParserGetter;
public class Google_Search2_cache extends HTMLEditorKit.ParserCallback {
	class ParserGetter extends HTMLEditorKit {
		// purely to make this methods public
		public HTMLEditorKit.Parser getParser() {
			return super.getParser();
		}
	}

	// �O���O�_�N��ƦL�X
	private boolean inHeader = false;
	private static int _sn = -1;
	static int choose = 1;
	static java.util.Date d1 = new java.util.Date();
    static SimpleDateFormat sdfmt = new SimpleDateFormat("yyyy-MM-dd");
	String i1;
	int l = 1;

	public Google_Search2_cache() {
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

	public void doit(int no) throws IOException, ParseException {
		BufferedWriter bw = null;
		int flag = 0;
		String line;
		String line2;
		int l = 0, limit = 0;
		FileReader FileStream2 = null;

		FileReader FileStream = new FileReader("Pairs/" + no + "_"
				+ "pairs.txt");
		BufferedReader BufferedStream = new BufferedReader(FileStream);
		
		//���X���e��ܪ�Cache number
		FileReader choosestream = new FileReader("Cache_choose/" + no + "_" + "cache_choose1.txt");
		BufferedReader chooseBufferedStream2 = new BufferedReader(choosestream);
		Google_Search1_cache.choose = Integer.parseInt(chooseBufferedStream2.readLine());
		
		if(Google_Search1_cache.choose == 1)
			FileStream2 = new FileReader("Cache/ai_p.txt");
		else if(Google_Search1_cache.choose == 2)
			FileStream2 = new FileReader("Cache/art_p.txt");
		else if(Google_Search1_cache.choose == 3)
			FileStream2 = new FileReader("Cache/biology_p.txt");
		else if(Google_Search1_cache.choose == 4)
			FileStream2 = new FileReader("Cache/computer_science_p.txt");
		else if(Google_Search1_cache.choose == 5)
			FileStream2 = new FileReader("Cache/ir_p.txt");
		
		BufferedReader BufferedStream2 = new BufferedReader(FileStream2);
		
		while ((line = BufferedStream.readLine()) != null)
		{
			while((line2 = BufferedStream2.readLine()) != null)
			{
				//if(limit >= 200)
				//{
					//limit = 0;
					//break;
				//}
				//else
					//limit++;
				
				//Date beginDate= sdfmt.parse("2007-12-24");
				String endDateString = line2.split("=")[2];
				Date endDate= sdfmt.parse(endDateString); //�r������
				long day=(d1.getTime() - endDate.getTime())/(24*60*60*1000);
				//System.out.println("�۹j���Ѽ�=" + day);
				
				if(day <= 30) //�p�G�b�@�Ӥ뤺�~�ϥΨӤ��
				{
					long runstartTime = System.currentTimeMillis();
				
					l = 1;
					flag = 0;
					if(line2.split("=")[0].equals(line))
					{
						System.out.println("!!�PCache��!!");
						System.out.println("line1" + line);
						System.out.println("line2" + line2);
						flag = 1;
						bw = new BufferedWriter(new FileWriter("Search2/" + no + "_"
								+ "google_output2.txt", true));
						if (String.valueOf(line2.split("=")[1]).contains("���� ") || String.valueOf(line2.split("=")[1]).contains(" �����G"))
						{
							bw.write(String.valueOf(line2.split("=")[1]));
							bw.newLine();
							bw.flush(); // �M�Žw�İ�
							bw.close(); // ����BufferedWriter����
							//�إ߹B�νw�İϿ�X��Ʀ�data.txt�ɪ�BufferedWriter����
							//�A�å�bw����ѦҤޥ�
							//�N�r��g�J�ɮ�
						}
						else if (String.valueOf(line2.split("=")[1]).contains("�䤣��M�z���d��"))
						{
							bw.write("���� 0 �����G\n");
							bw.close(); // ����BufferedWriter����
						}
						System.out.println(System.currentTimeMillis() - runstartTime);
						break;
					}
				}
			}

			//�T�w�S���bcache���
			if(flag == 0 && l == 1)
			{
				if(Google_Search1_cache.choose == 1)
					bw = new BufferedWriter(new FileWriter("Cache/ai_p.txt", true));
				else if(Google_Search1_cache.choose == 2)
					bw = new BufferedWriter(new FileWriter("Cache/art_p.txt", true));
				else if(Google_Search1_cache.choose == 3)
					bw = new BufferedWriter(new FileWriter("Cache/biology_p.txt", true));
				else if(Google_Search1_cache.choose == 4)
					bw = new BufferedWriter(new FileWriter("Cache/computer_science_p.txt", true));
				else if(Google_Search1_cache.choose == 5)
					bw = new BufferedWriter(new FileWriter("Cache/ir_p.txt", true));
				bw.write(line + "=");
				bw.flush();
				bw.close();
				_sn = no;
				search_pair(line);
			}
			
			//��@�}�lcache.txt�����ɮɡA�n�d�Ĥ@��
			if(l == 0)
			{
				if(Google_Search1_cache.choose == 1)
					bw = new BufferedWriter(new FileWriter("Cache/ai_p.txt", true));
				else if(Google_Search1_cache.choose == 2)
					bw = new BufferedWriter(new FileWriter("Cache/art_p.txt", true));
				else if(Google_Search1_cache.choose == 3)
					bw = new BufferedWriter(new FileWriter("Cache/biology_p.txt", true));
				else if(Google_Search1_cache.choose == 4)
					bw = new BufferedWriter(new FileWriter("Cache/computer_science_p.txt", true));
				else if(Google_Search1_cache.choose == 5)
					bw = new BufferedWriter(new FileWriter("Cache/ir_p.txt", true));
				bw.write(line + "=");
				bw.flush();
				bw.close();
				_sn = no;
				search_pair(line);
			}
			
			//�T�O�C��line2���q�@�}�l�Q���
			if(Google_Search1_cache.choose == 1)
				FileStream2 = new FileReader("Cache/ai_p.txt");
			else if(Google_Search1_cache.choose == 2)
				FileStream2 = new FileReader("Cache/art_p.txt");
			else if(Google_Search1_cache.choose == 3)
				FileStream2 = new FileReader("Cache/biology_p.txt");
			else if(Google_Search1_cache.choose == 4)
				FileStream2 = new FileReader("Cache/computer_science_p.txt");
			else if(Google_Search1_cache.choose == 5)
				FileStream2 = new FileReader("Cache/ir_p.txt");
			BufferedStream2 = new BufferedReader(FileStream2);
		}
	}

	private void search_pair(String i1) {
		try {
			ParserGetter kit = new ParserGetter();
			HTMLEditorKit.Parser parser = kit.getParser();
			HTMLEditorKit.ParserCallback callback = new Google_Search2_cache();
			long runstartTime = System.currentTimeMillis(); // ���X�ثe�ɶ�

			System.out.println("process: " + i1 + "==>");
			// ��J�����R������
			// URL u = new URL("http://www.yam.com");
			String j = "http://www.google.com.tw/search?aq=f&sourceid=chrome&ie=UTF-8&q=";
			// String j =
			// "http://www.google.com.tw/search?aq=f&sourceid=chrome&ie=Big5&q=";
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

	public static void main(int i) throws IOException, ParseException {

		File f = new File("Search2/" + i + "_" + "google_output2.txt");
		f.delete();
		new Google_Search2_cache().doit(i);

	}

	public static void main(String args[]) throws IOException, ParseException {

		// new Google_Search1().doit(i);
		long StartTime = System.currentTimeMillis(); // ���X�ثe�ɶ�
		main(1);
		System.out.println(System.currentTimeMillis() - StartTime);

	}
}