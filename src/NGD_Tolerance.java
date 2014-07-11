import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class NGD_Tolerance {

	static double tolerance_rate=0.4;
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Tolerance_main("citeulike/citeulike_sTF_score/", "citeulike/citeulike_NGD_Tolerance_0.4/");
		//Tolerance_main("sTF_scoretest/", "source_dir/");
	}
	
	public static void Tolerance_main(String source_dir){
		String resultDir=source_dir;
		Tolerance_main(source_dir, resultDir);
	}
	
	public static void Tolerance_main(String source_dir, String resultDir){
		System.out.print("Tol_NGD�}�l\n");
		File dir = new File(source_dir); //�ӷ���Ƨ�
		Tolerance_work(dir,resultDir);
		System.out.print("End\n");
	}
	
	public static void Tolerance_work(File source_dir, String resultDir){
		Map<String, Double> ngds = null;
		File[] fileslist;
		String filename="";
		System.out.print("�ӷ���ƧX"+source_dir.getName()+"\n");
		if(source_dir.isDirectory()){
			fileslist = source_dir.listFiles(); //��Ƨ��Ҧ�
		}else{ //���ɼҦ�
			fileslist = new File[1];
			fileslist[0] = source_dir;
		}
		
		for (File files : fileslist){
			if(files.isDirectory()){
				Tolerance_work(files,resultDir);
			}else if(files.isFile()){
				try {
					filename="";
					/*//reuters��ƶ����ɦW�Ѩ���k
					for(int i=0; i<files.getName().split("_").length;i++){
						//System.out.println("filename = "+ filename);
						if(i==0){
							filename=files.getName().split("_")[0];
						}else{
							char[] filename_temp = files.getName().split("_")[i].toCharArray();
							if(!Character.isDigit(filename_temp[0])){ //�p�G�Ĥ@�Ӧr���O�Ʀr�N����ɦW�����F
								filename=filename+"_"+files.getName().split("_")[i];
							}else{
								filename=filename+"_"+files.getName().split("_")[i];
								break;
							}
						}
					}*/
					
					//citeulike��ƶ����ɦW�Ѩ���k
					filename = files.getName().split("_")[0];
					
					File brfile = new File("citeulike/citeulike_Rank/"+filename+"_Rank.txt");
					File br2file = new File(source_dir+"/"+filename+"_Term_TFcalculate.txt");
					BufferedWriter bw;
					BufferedReader br = new BufferedReader(new FileReader(brfile));
					BufferedReader br2 = new BufferedReader(new FileReader(br2file));
					List<Tolerance_object> tolerance_list = new LinkedList<Tolerance_object>();
					List<Map.Entry<String, Double>> list_data = new ArrayList<Map.Entry<String, Double>>();
					HashMap<String,Integer> TF_term = new HashMap<String,Integer>();
					ngds = new HashMap<String, Double>();
					String line, v1, v2, bigTF, smallTF, word="";
					double temp_ngd;
					int small_than_tolerance_rate_num; //�ݤp��ngd���e�Ȫ���Ʀ��X��
					
					System.out.print("Ū���ɮ�"+br2file.getName()+"\n");
					//���X���ɮת��Ҧ��r���P�v��
					while((line=br2.readLine())!=null){
						v1 = line.split(",")[0];
						TF_term.put(v1,Integer.valueOf(line.split(",")[1]));
					}
					
					System.out.print("Ū���ɮ�"+brfile.getName()+"\n");
					
					small_than_tolerance_rate_num=0;
					//���X�Ҧ�_Rank�����
					while((line=br.readLine())!=null){
						temp_ngd = Double.parseDouble(line.split(",")[2]);
						if(temp_ngd<=tolerance_rate){
							small_than_tolerance_rate_num++;
						}
						//�h��NDG���Ƥj��1�����G
						if(temp_ngd<1){
							v1 = line.split(",")[0];
							v2 = line.split(",")[1];
							word = v1+","+v2;
							Map.Entry<String, Double> temp_entry = new AbstractMap.SimpleEntry<String, Double>(word, temp_ngd);
							list_data.add(temp_entry);
						}
					}

					//�}�lNGD�e�t�B�J
					for(int i=1;i<=small_than_tolerance_rate_num;i++){
						Map.Entry<String, Double> entry = list_data.get(i-1);	
						temp_ngd = entry.getValue();
						word = entry.getKey();
						//System.out.print("Ū��"+ word + "," + temp_ngd + "\n");
						v1 = word.split(",")[0];
						v2 = word.split(",")[1];
						//�r�������O�bTF��󤤦��������r��
						if(TF_term.get(v1)!=null && TF_term.get(v2)!=null){
							//�e�t�L��esmall_than_tolerance_rate_num�椺���`�I���ӬۦP�A�Y���ۦP�h�i��e�t
							if(!v1.equals(v2)){
								//��TF�ȸ������r�����N���C��
								if(TF_term.get(v1)>=TF_term.get(v2)){
									//System.out.print("�s�W���N�W�h"+v1+"�N���N"+v2+"\n");
									bigTF = v1;
									smallTF = v2;
								}else{
									//System.out.print("�s�W���N�W�h"+v2+"�N���N"+v1+"\n");
									bigTF = v2;
									smallTF = v1;
								}
								
								//System.out.println("bigTF="+bigTF+" ,smallTF="+smallTF);
								
								//bigTF�}�l��Ҧ�smallTF�`�I�i����N
								int temp_index=0;
								for(Map.Entry<String, Double> entry2 : list_data){
									if(entry2.getKey().split(",")[0].equals(smallTF)){
										Map.Entry<String, Double> temp_entry = new AbstractMap.SimpleEntry<String, Double>(bigTF+","+entry2.getKey().split(",")[1],entry2.getValue());
										list_data.set(temp_index, temp_entry);
										//System.out.println("�s��"+temp_index+" ���N�e = "+ entry2.getKey() +","+ entry2.getValue());
										//System.out.println("�s��"+temp_index+" ���N�� = "+ list_data.get(temp_index).getKey() +","+ list_data.get(temp_index).getValue());
										entry2=list_data.get(temp_index);
									}
									if(entry2.getKey().split(",")[1].equals(smallTF)){
										Map.Entry<String, Double> temp_entry = new AbstractMap.SimpleEntry<String, Double>(entry2.getKey().split(",")[0]+","+bigTF,entry2.getValue());
										list_data.set(temp_index, temp_entry);
										//System.out.println("�s��"+temp_index+" ���N�e = "+ entry2.getKey() +","+ entry2.getValue());
										//System.out.println("�s��"+temp_index+" ���N�� = "+ list_data.get(temp_index).getKey() +","+ list_data.get(temp_index).getValue());
									}
									temp_index++;
								}
							}
						}
					}
					br.close();
					
					bw = new BufferedWriter(new FileWriter(resultDir+filename+"_TolNGD.txt"));
					for(Map.Entry<String, Double> entry : list_data){
						bw.write(entry.getKey()+","+entry.getValue());
						bw.newLine();
						bw.flush();
					}
					bw.close();
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}

/*class StrLenComparator implements Comparator<String>{
    public int compare(String s1, String s2){
        if(s1.length() > s2.length())
            return 1;
        if(s1.length() < s2.length())
            return -1;
        return s1.compareTo(s2);
    }
}*/
