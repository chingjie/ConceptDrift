import java.io.*;

public class parser
{
	public static void main(String[] args)
	{
		String find1 = "<doc ";//���^���r��
		String find2 = "</doc>";//���^���r��

		String path = "F:/extracted";//Ū�����|
		String out_path ="F:/enwiki-pages-articles";//��X���|
		String name = "wiki";//���W��

		int i = 0;
		String value;
		
		BufferedReader br = null;
		BufferedWriter bw = null;
		
				
		
		File dir = new File(path);	
		String line;

		for (String d : dir.list()){
			String files[] = new File(path+"/"+d).list();
			
			for (String f : files){
				try {
					System.out.println("Ū��"+path+"/"+d+"/"+f);
					br = new BufferedReader(new InputStreamReader(new FileInputStream(path+"/"+d+"/"+f), "UTF8") );
					new File(out_path+"/" + d ).mkdir();					
					//bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out_path+"/" + d + "/" + name + "_output" + i + ".htm"), "UTF8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				try {
					while((line = br.readLine()) != null){
						if (String.valueOf(line).contains(find1))
						{
							value = line.replaceAll(find1, "");
							value = value.replaceAll(">", "");
							
							System.out.println("�g�J: "+value);
							
							bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out_path+"/" + d + "/" + name + "_output" + i + ".htm"), "UTF8"));
							
							bw.write(value);
							bw.newLine();
							bw.flush(); // �M�Žw�İ�
						}
						else if (String.valueOf(line).contains(find2))
						{
							bw.flush();
							bw.close();
							System.out.println(path+"/"+d+"/"+f+" �w����!");
							
							i++;
							//bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out_path+"/" + d + "/" + name + "_output" + i + ".htm"), "UTF8"));
						}
						else
						{
							bw.write(line);
							System.out.println("�g�J: "+line);
							bw.newLine();
							bw.flush();
						}
						
						
					}
					
					br.close();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}	
		}
			
		
		/*

		try
		{
			for(nameidc=0;nameidc<=99;nameidc++)
			{
				if(nameidc <= 9)
					nameid = "0" + Integer.toString(nameidc);
				else
					nameid = Integer.toString(nameidc);
				
				System.out.println(nameid);
				FileReader FileStream = new FileReader("F:/extracted/" + file + "/" + name +"_" + nameid);
				BufferedWriter bw = new BufferedWriter(new FileWriter("F:/enwiki-pages-articles/" + file + "/" + name + "_output" + i + ".htm", true));
			
				BufferedReader BufferedStream = new BufferedReader(FileStream);
				String line;

				while ((line = BufferedStream.readLine()) != null)
				{
					if (String.valueOf(line).contains(find1))
					{
						value = line.replaceAll(find1, "");
						value = value.replaceAll(">", "");
						bw.write(value);
						bw.newLine();
						bw.flush(); // �M�Žw�İ�
					}
					else if (String.valueOf(line).contains(find2))
					{
						bw.close();
						i++;
						bw = new BufferedWriter(new FileWriter("F:/enwiki-pages-articles/" + file + "/" + name + "_output" + i + ".htm", true));
					}
					else
					{
						bw.write(line);
						bw.newLine();
						bw.flush();
					}
				}
			}
		}
			
		catch(IOException exp)
		{
			System.out.println("IOException");
		}
		
		*/
		
	}
}