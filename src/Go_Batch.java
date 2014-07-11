import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Scanner;


public class Go_Batch {
	public static void main(String[] args) throws ParseException {
		
		
		long AverageTime =0; // �p�⥭���B�z�ɶ��ϥ�

		long StartTime = System.currentTimeMillis(); // ���X�ثe�ɶ�	
		try {
		
				File dir=new File("preprocess/");
				String files[]=dir.list();
				for(String s:files)
				{
					s=s.replace(".txt", "");
					Qtag.tagging(s);
					Term_freq_count.counting(s);//�p��freq 
					POS_filter.filter(s);//
					new Lucene_Search1().doit(s);
					//new Google_Search1_cache().doit(j);
					google_filter1.search_filter(s);
					new Lucene_Search2().doit(s);
					//new Google_Search2().doit(j);
					google_filter2.search_filter(s);
					Stem.stemming(s);
					NGD_calculate.NGD(s);
					Result_Rank.ranking(s);
					//new K_core().K_core_cal(j);
				}
				
			
					
					
							
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("ERROR HAPPEN");
				e.printStackTrace();
			}
			long ProcessTime = System.currentTimeMillis() - StartTime; // �p��B�z�ɶ�
			//AverageTime += ProcessTime; // �ֿn�p��ɶ�
			System.out.println(ProcessTime);
	}
}
