import java.io.IOException;
import java.text.ParseException;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class Go_English {
	public static void main(String[] args) throws ParseException {
		
		
		long AverageTime =0; // �p�⥭���B�z�ɶ��ϥ�

		long StartTime = System.currentTimeMillis(); // ���X�ثe�ɶ�	
		try {
			Scanner input=new Scanner(System.in);
			System.out.println("Select document:");
			int j=input.nextInt();
				System.out.println("Choose the step:");
				System.out.println("1.Qtag");
				System.out.println("2.Freq");
				System.out.println("3.POS filter");
				System.out.println("4.Search");
				System.out.println("5.Google filter");
				System.out.println("6.Stem");
				System.out.println("7.NGD");
				System.out.println("8.Rank");
				System.out.println("9.K core");
				int step=input.nextInt();
				
				switch(step){
				case 1:
					//new Qtag().tagging(j);
				case 2:
					//Term_freq_count.counting(j);//�p��freq 
//					input.next();
				case 3:
					//POS_filter.filter(j);
					//new cache_choose().doit(j);
//					input.next();
					
				case 4:
					//�ڪ�����cache
					//new Google_Search1_cache().doit(j);
					
					//�ڪ�timestamp cache
					//new Google_Search1_cache_timestamp().doit(j);
					
					//�ڪ�count cache
					//new Google_Search1_cache_count().doit(j);
					
					//�ڪ�Lucene search1
					//new Lucene_Search1().doit(j);
					
					//�p�y��
					//new Google_Search1().doit(j);
					
				case 5:
					//google_filter1.search_filter(j);
					
					//�ڪ�����cache
					//new Google_Search2_cache().doit(j);
					
					//�ڪ�Lucene search2
					//new Lucene_Search2().doit(j);
					
					//�p�y��
					//new Google_Search2().doit(j);
					
					//google_filter2.search_filter(j);
				case 6:
					//Stem.stemming(j);
				case 7:
					//NGD_calculate.NGD(j);
				case 8:
				    //Result_Rank.ranking(j);
				case 9:
					//�p�y��
					K_core kcore=new K_core();
					kcore.K_core_cal(j);
					KcoreGUI gui = new KcoreGUI();
					gui.getData(j,kcore.simMin);
					
					//�ڪ�CC
					//distance dis = new distance();
					//dis.cc_cal(j);
					//distanceGUI gui = new distanceGUI();
					//gui.getData(j,dis.simMin);
					
					gui.updateGUI();
					gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					gui.setSize(1024, 768);
					gui.setVisible(true);
				}				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("ERROR HAPPEN");
				e.printStackTrace();
				//JOptionPane.showMessageDialog(null, "ERROR");
			}
			long ProcessTime = System.currentTimeMillis() - StartTime; // �p��B�z�ɶ�
			//AverageTime += ProcessTime; // �ֿn�p��ɶ�
			System.out.println("�`��O�ɶ���" + ProcessTime);
	}
}
