import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class Tom_exp {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BufferedReader br,br2,br3,br4; //br�Ψ�Ū�ثe��user_profile�Abr2�Ψ�Ū���Abr3�ΨӪ������w�V�m��󪺶��ǡAbr4�ΨӪ������w���դ�󪺶���
		BufferedWriter bw2,bw3; //bw2�ΨӬ���Ū����󪺶��ǡAbw3�ΨӬ�����Ѧ]�l���P�D�D���Y���
		BufferedWriter EfficacyMeasure_w, Time_w; //EfficacyMeasure_w�ΨӬ����t�ήį�ATime_w�ΨӬ����t�ΰ���ɶ�
		double train_sum_time_read_doc = 0, test_sum_time_read_doc = 0, train_time_read_doc = 0, test_time_read_doc = 0; //Ū������`�ɶ�
		double train_sum_time_read_profile = 0, test_sum_time_read_profile = 0, train_time_read_profile = 0, test_time_read_profile = 0; //Ū���ҫ����`�ɶ�
		double train_sum_time_topic_mapping = 0, test_sum_time_topic_mapping = 0, train_time_topic_mapping = 0, test_time_topic_mapping = 0; //�D�D�M�g���`�ɶ�
		double train_sum_time_add_user_profile = 0, train_time_add_user_profile = 0; //�s�W�����ҫ����`�ɶ�(�u���V�m������)
		double test_sum_time_Comper_relateness = 0, test_time_Comper_relateness = 0; //�����P�w���`�ɶ�(�u�����ճ�����)
		double sum_time_update_OneDayTerm = 0, time_update_OneDayTerm = 0; //�ϥο�Ѧ]�l��s�ҫ����`�ɶ�(�C�Ѱ��@��)
		long StartTime;
		long EndTime;
		//�O��user_profile�U�D�D���D�D�r���A�榡<�D�D�s��,<�D�D�r��,�r������>>
		HashMap<Integer,HashMap<String,Double>> User_profile_term = new HashMap<Integer,HashMap<String,Double>>();
		//�O�����U�D�D���D�D�r���A�榡<�D�D�s��,<�D�D�r��,�r������>>
		HashMap<Integer,HashMap<String,Double>> doc_term = new HashMap<Integer,HashMap<String,Double>>();
		//�Ȧs�r���P����
		HashMap<String,Double> topic_term = new HashMap<String,Double>();
		//�x�s���P�ҫ����D�D�M�g
		HashMap<Integer,Integer> topic_mapping = new HashMap<Integer,Integer>();
		ArrayList<String> profile_label = new ArrayList<String>(); //�x�s�ΨӰV�m�����ҵ���
		int TP, TN, FP, FN;
		TOM_ComperRelateness TC = new TOM_ComperRelateness();
		Go_Training_Tom GTT = new Go_Training_Tom();
		Go_User_profile GU = new Go_User_profile();
		ConceptDrift_Forecasting CDF = new ConceptDrift_Forecasting();
		String dir = "exp_acq_DecayFactor_fs_fix0.05/";
		int days = 15; //Reuters-����Ѽ�
		int train_days = 0, test_days = -1; //citeulike-����ѼơA0�������A-1�����ϥ�
		String real_people = "626838af45efa5ca465683ab3b3f303e"; //���citeulike��Ƭy
		FileWriter FWrite;
		BufferedWriter Comper_log; //Comper_log�ΨӬ����D�D�M�g���ƭ�
		int preprocess_times = 0; //�Y�@�Ѫ���X�g�峹
		int doc_term_count; //���󤺪��r���ƶq
		double docTF; //����TF��
		double doc_ngd;
		String line = "";
		String topicname=""; //�x�s���ҵ���
		int train_times,test_times; //�]�w�C���V�m�B���դ������g��
		TC.init_EfficacyMeasure();
		
		String train_topics[] = {"acq"};
		String test_topics[] = {"acq","earn","crude","coffee","sugar","trade","cocoa"};
		//ArrayList<File> test_fs = new ArrayList<File>();
		//int test_fs_num;
		
		try {
			new File(dir).mkdirs(); //�гy�X�����ƧX
			new File(dir+"user_porfile").mkdirs(); //�гy�X����ϥΪ̼ҫ���ƧX
			Time_w = new BufferedWriter(new FileWriter(dir+"time.txt"));
			
			/*//�H�U������Ӹ�ƧX�P�V�m�B���ո�ƶ��Ыص{�����q�A�p�n����P����е��ѱ�
			StartTime = System.currentTimeMillis();
			Time_w.write("�V�m�P���ն����ͤ� :");
			Time_w.newLine();
			Time_w.flush();
			Time_w.write("�}�l�ɶ� :" + StartTime);
			Time_w.newLine();
			Time_w.flush();
			new File(dir+"training").mkdirs(); //�гy�X����V�m����ƧX
			new File(dir+"testing").mkdirs(); //�гy�X������ն���ƧX
			//�H�U��Reuters�V�m�B���ո�ƶ��Ыص{���X
			for(int i=1; i<=days; i++){
				System.out.println("��"+i+"��");
				new File(dir+"training/"+"day_"+i).mkdirs(); //�гy�X����V�m����i�Ѹ�ƧX
				new File(dir+"testing/"+"day_"+i).mkdirs(); //�гy�X������ն���i�Ѹ�ƧX
				if(i==1){
					for(int j=0; j<train_topics.length; j++){
						GTT.point_topic_doc_generateSet("Tom_reuters_0.4/single",dir+"training/"+"day_"+i,train_topics[j],3,i);
					}
				}
				//if(i==15){
					for(int j=0; j<test_topics.length; j++){
						GTT.point_topic_doc_generateSet("Tom_reuters_0.4/single",dir+"testing/"+"day_"+i,test_topics[j],1,days+i);
					}
				//}
			}
			//�H�W��Reuters�V�m�B���ո�ƶ��Ыص{���X
			//�H�U��CiteULike�V�m�B���ո�ƶ��Ыص{���X
			System.out.println("Real Word��Ƭy��: "+real_people);
			days = GTT.real_word_generateSet("citeulike/citeulike_Tom_citeulike_0.4/",dir,real_people,train_days,test_days);
			real_people = GTT.get_real_people();
			//�H�W��CiteULike�V�m�B���ո�ƶ��Ыص{���X
			EndTime = System.currentTimeMillis();
			Time_w.write("�����ɶ� :" + EndTime);
			Time_w.newLine();
			Time_w.flush();
			Time_w.write("�@�ϥήɶ�(��) :" + (EndTime-StartTime)/1000);
			Time_w.newLine();
			Time_w.flush();
			Time_w.write("��Ƭy�W��: "+real_people);
			Time_w.newLine();
			Time_w.flush();
			//�p�n����P����е��Ѧܦ�*/

			EfficacyMeasure_w = new BufferedWriter(new FileWriter(dir+"EfficacyMeasure.txt"));
			for(int day=1; day<=days; day++){
				EfficacyMeasure_w.write("��"+day+"��...");
				EfficacyMeasure_w.newLine();
				EfficacyMeasure_w.flush();
				Time_w.write("��"+day+"��...");
				Time_w.newLine();
				Time_w.flush();
				//�t�θ�ƧX�ۦ�Ū�����ճ���
				/*File test_d = new File(dir+"testing/day_"+day);
				test_fs.clear();
				for(File test_f : test_d.listFiles()){
					test_fs.add(test_f);
				}
				test_fs_num = 0;*/
				
				//~~~�H�U���V�m���q~~~//
				//�t�θ�ƧX�ۦ�Ū��
				//bw2 = new BufferedWriter(new FileWriter(dir+"processesting_order.txt"));
				File train_d = new File(dir+"training/day_"+day);
				
				if(day>7){ //�u������u�O�d7��
					profile_label.clear();
				}
				
				for(File train_f : train_d.listFiles()){
					System.out.println("�V�m�}�l");
					System.out.println("��"+day+"��, ��"+(preprocess_times+1)+"�g�峹");
					StartTime = System.currentTimeMillis();
					
				//Ū���W�w���Ǥ��
				/*br3 = new BufferedReader(new FileReader(dir+"training_order.txt"));
				String training_order;
	
				while((training_order=br3.readLine())!=null){
					File f = new File(dir+"training/"+training_order);*/
					User_profile_term.clear();
					doc_term.clear();
					topic_term.clear();
					//Ū���ثe��user_profile
					if(preprocess_times!=0){ //�D�Ĥ@���NŪ���W�@���ئn��user_profile
						br = new BufferedReader(new FileReader(dir+"user_porfile/user_profile_"+preprocess_times+".txt"));
						System.out.print("Ū���ҫ�"+dir+"user_profile_"+preprocess_times+".txt\n");
						while((line=br.readLine())!=null){
							topic_term.clear();
							String term = line.split(",")[0]; //�r��
							int group = Integer.valueOf(line.split(",")[2]); //�r�����ݸs�O
							double TFScore = Double.valueOf(line.split(",")[1]); //�r������
							//System.out.print("���X�ҫ���T=>"+term+","+TFScore+","+group+"\n");
							if(User_profile_term.get(group)==null){ //�s���D�D������r�˶i�h
								topic_term.put(term, TFScore);
							}else{ //�¥D�D�N�����X�ثe����ơA�A��s
								topic_term = User_profile_term.get(group);
								topic_term.put(term, TFScore);
							}
							User_profile_term.put(group,new HashMap(topic_term));
						}
						br.close();
					}else{ //�Ĥ@�����ҫ��N���P�O�Ū��ҥH�]�@�ӪťD�D�A�ê�l�Ƥ�r�v�����
						System.out.println("�L�o�{�ҫ��A�N�r��temp����0.0��J�ҫ�");
						//�p�GUser_profile_term���S���ȡA����B�J�|�X�{���~�T���A�ҥH�o�����@�ӡA�åB�]���Ƭ�0�A�]�����v�T�����P�w
						topic_term.put("temp", 0.0);
						User_profile_term.put(1, new HashMap(topic_term));
						//��l�ƿ�Ѧ]�l���
						bw3 = new BufferedWriter(new FileWriter(dir+"user_porfile/user_profile_TDF.txt"));
						bw3.write(""+1);
						bw3.newLine();
						//�s��榡�� �r��,�r����Ѧ]�l,������s�s��,�r���`TF����
						bw3.write("temp,0.079,1,0.0");
						bw3.newLine();
						bw3.flush();
						bw3.close();
						//��l�ƥD�D���Y���
						bw3 = new BufferedWriter(new FileWriter(dir+"user_porfile/user_profile_TR.txt"));
						bw3.write(""+0);
						bw3.newLine();
						bw3.flush();
						bw3.close();
					}
					EndTime = System.currentTimeMillis();
					train_time_read_profile = train_time_read_profile + ((EndTime-StartTime)/1000);
					test_time_read_profile = train_time_read_profile; //��]�O�]�����ծɦ]�����i�S����Ū
					
					/*if(User_profile_term.get(3)!=null){
						for(String doc_terms: User_profile_term.get(3).keySet()){
							System.out.println("test1, "+doc_terms);
						}
					}*/
					
					//Ū�����
					StartTime = System.currentTimeMillis();
					preprocess_times++; //�����ثe�O�ĴX�����
					//�}�lŪ�����
					br2 = new BufferedReader(new FileReader(train_f));
					for(int ii=0; ii<train_f.getName().split("_").length;ii++){
						if(ii==0){
							topicname=train_f.getName().split("_")[0];
						}else{
							char[] topicname_temp = train_f.getName().split("_")[ii].toCharArray();
							if(!Character.isDigit(topicname_temp[0])){ //�p�G�Ĥ@�Ӧr���O�Ʀr�N����ɦW�����F
								topicname=topicname+"_"+train_f.getName().split("_")[ii];
							}else{
								break;
							}
						}
					}
					
					if(!profile_label.contains(topicname)){
						System.out.println("�s�W���Ѽ���"+topicname);
						profile_label.add(topicname);
					}
										
					System.out.print("Ū�����"+dir+"training/day_"+day+"/"+train_f.getName()+"\n");
					doc_ngd = Double.valueOf(br2.readLine()); //���NGD���e
					System.out.print("���X���NGD="+doc_ngd+"\n");
					topic_term.clear();
					doc_term.clear();
					docTF=0;
					doc_term_count=0;
					while((line=br2.readLine())!=null){
						String term = line.split(",")[0]; //�r��
						int group = Integer.valueOf(line.split(",")[2]); //�r�����ݸs�O
						double TFScore = Integer.valueOf(line.split(",")[1]); //�r������
						docTF+=TFScore;
						doc_term_count++;
						topic_term.clear();
						//System.out.print("���X����T=>"+term+","+TFScore+","+group+"\n");
						if(doc_term.get(group)==null){ //�s���D�D������r�˶i�h
							topic_term.put(term, TFScore);
						}else{ //�¥D�D�N�����X�ثe����ơA�A��s
							topic_term = doc_term.get(group);
							topic_term.put(term, TFScore);
						}
						doc_term.put(group,new HashMap(topic_term));
					}
					GU.sum_avg_docTF(docTF);
					GU.sum_avg_termTF(docTF,doc_term_count);
					br2.close();
					//�p�G��󧹥��S���S�x�r���|�ϱo�{���X���A�]����o�ؤ���J�@��temp�r���A����A�Q��k�ѨM
					if(doc_term.get(1)==null){
						topic_term.clear();
						topic_term.put("temp", 0.0);
						doc_term.put(1, new HashMap(topic_term));
						System.out.println("�Ӥ��S������S�x");
					}
					EndTime = System.currentTimeMillis();
					train_time_read_doc = train_time_read_doc + ((EndTime-StartTime)/1000);
					
					/*if(doc_term.get(1)!=null){
						for(String doc_terms: doc_term.get(1).keySet()){
							System.out.println("test1, "+doc_terms);
						}
					}*/
					
					//�O��Ū������
					/*bw2.write(f.getName()+","+preprocess_times); 
					bw2.newLine();
					bw2.flush();*/
					
					//���P�ҫ����D�D�r����T���Ѩ��X�ӫ�N�i��D�D�����M�g�A�H�K�������D�D�O������ҫ������@��
					//���B�J�]�|�����D�D���Y
					StartTime = System.currentTimeMillis();
					//true ��,�� Append�Ҧ�
					FWrite = new FileWriter(dir+"Comper_topic_profile_doc.txt",true);
					Comper_log = new BufferedWriter(FWrite);
					Comper_log.write("�V�m���W��:"+train_f.getName());
					Comper_log.newLine();
					Comper_log.write("��M�ϥΪ̼ҫ�:"+dir+"user_profile_"+preprocess_times+".txt");
					Comper_log.newLine();
					Comper_log.close();
					topic_mapping = TC.Comper_topic_profile_doc(dir,User_profile_term, doc_term, doc_ngd);
					//updata_fine_doc()��k�̫�����o�{�ĪG���n�A�]���U���Q���ѱ�
					//doc_term.clear();
					//doc_term = new HashMap<Integer,HashMap<String,Double>>(TC.updata_fine_doc());
					EndTime = System.currentTimeMillis();
					train_time_topic_mapping = train_time_topic_mapping + ((EndTime-StartTime)/1000);
					
					FWrite = new FileWriter(dir+"Comper_topic_profile_doc.txt",true);
					Comper_log = new BufferedWriter(FWrite);
					Comper_log.write("�D�D�M�g���G�p�U");
					Comper_log.newLine();
					Comper_log.flush();
					//�[�ݥD�D�M�g���G
					for(int i: topic_mapping.keySet()){
						System.out.print("���D�D "+i+" �M�g��ҫ��D�D "+topic_mapping.get(i)+"\n");
						Comper_log.write("���D�D "+i+" �M�g��ҫ��D�D "+topic_mapping.get(i));
						Comper_log.newLine();
						Comper_log.flush();
					}
					Comper_log.write("");
					Comper_log.newLine();
					Comper_log.flush();
					Comper_log.close();
					
					//�ϥΨϥΪ̼ҫ��D�D�r����s�A�Ө��o��s�᪺�D�D�r��
					//���B�J�]�|�Ψ��Ѧ]�l
					StartTime = System.currentTimeMillis();
					User_profile_term = GU.add_user_profile_term(dir,User_profile_term, doc_term, topic_mapping);
					EndTime = System.currentTimeMillis();
					train_time_add_user_profile = train_time_add_user_profile + ((EndTime-StartTime)/1000);
					
					//��X�ϥΪ̼ҫ�
					GU.out_new_user_profile(dir,preprocess_times,User_profile_term);
					System.err.println("���"+dir+"training/day_"+day+"/"+train_f.getName()+"�B�z����");
				}
				
				//~~~�H�U�����ն��q~~~//
				File test_d = new File(dir+"testing/day_"+day);
				for(File test_f : test_d.listFiles()){
					System.out.println("���ն}�l");
					//File test_f = test_fs.get(test_fs_num);
					//test_fs_num++;
					//Ū���W�w���Ǥ��
					/*br4 = new BufferedReader(new FileReader(dir+"testing_order.txt"));
					String testing_order;
					test_times = 1; //�]�w�C���V�m���V�m����n�δX�����դ��i�����
					testing_order=br4.readLine();
					while(testing_order!=null && test_times!=0){
						test_times--;
						File test_f = new File(dir+"testing/"+testing_order);*/
						//�}�lŪ�����
						br2 = new BufferedReader(new FileReader(test_f));
						System.out.print("Ū�����"+dir+"testing/day_"+day+"/"+test_f.getName()+"\n");
						doc_ngd = Double.valueOf(br2.readLine()); //���NGD���e
						System.out.print("���X���NGD="+doc_ngd+"\n");
						//Ū�����դ��
						StartTime = System.currentTimeMillis();
						topic_term.clear();
						doc_term.clear();
						while((line=br2.readLine())!=null){
							//System.out.println("line_check: "+line);
							String term = line.split(",")[0]; //�r��
							int group = Integer.valueOf(line.split(",")[2]); //�r�����ݸs�O
							double TFScore = Integer.valueOf(line.split(",")[1]); //�r������
							topic_term.clear();
							//System.out.print("���X����T=>"+term+","+TFScore+","+group+"\n");
							if(doc_term.get(group)==null){ //�s���D�D������r�˶i�h
								topic_term.put(term, TFScore);
							}else{ //�¥D�D�N�����X�ثe����ơA�A��s
								topic_term = doc_term.get(group);
								topic_term.put(term, TFScore);
							}
							doc_term.put(group,new HashMap(topic_term));
						}
						br2.close();
						//�p�G��󧹥��S���S�x�r���|�ϱo�{���X���A�]����o�ؤ���J�@��temp�r���A����A�Q��k�ѨM
						if(doc_term.get(1)==null){
							topic_term.clear();
							topic_term.put("temp", 0.0);
							doc_term.put(1, new HashMap(topic_term));
							System.out.println("�Ӥ��S������S�x");
						}
						EndTime = System.currentTimeMillis();
						test_time_read_doc = test_time_read_doc + ((EndTime-StartTime)/1000);
						
						//���P�ҫ����D�D�r����T���Ѩ��X�ӫ�N�i��D�D�����M�g�A�H�K�������D�D�O������ҫ������@��
						StartTime = System.currentTimeMillis();
						FWrite = new FileWriter(dir+"Comper_topic_profile_doc.txt",true);
						Comper_log = new BufferedWriter(FWrite);
						Comper_log.write("���դ��W��:"+test_f.getName());
						Comper_log.newLine();
						Comper_log.write("��M�ϥΪ̼ҫ�:"+dir+"user_profile_"+preprocess_times+".txt");
						Comper_log.newLine();
						Comper_log.close();
						topic_mapping = TC.Comper_topic_profile_doc_only(dir, User_profile_term, doc_term, doc_ngd, "test");
						EndTime = System.currentTimeMillis();
						test_time_topic_mapping = test_time_topic_mapping + ((EndTime-StartTime)/1000);
						
						FWrite = new FileWriter(dir+"Comper_topic_profile_doc.txt",true);
						Comper_log = new BufferedWriter(FWrite);
						Comper_log.write("�D�D�M�g���G�p�U");
						Comper_log.newLine();
						Comper_log.flush();
						//�[�ݥD�D�M�g���G
						for(int i: topic_mapping.keySet()){
							System.out.print("���D�D "+i+" �M�g��ҫ��D�D "+topic_mapping.get(i)+"\n");
							Comper_log.write("���D�D "+i+" �M�g��ҫ��D�D "+topic_mapping.get(i));
							Comper_log.newLine();
							Comper_log.flush();
						}
						Comper_log.write("");
						Comper_log.newLine();
						Comper_log.flush();
						Comper_log.close();
						
						//���o���դ�����
						topicname="";
						for(int ii=0; ii<test_f.getName().split("_").length;ii++){
							if(ii==0){
								topicname=test_f.getName().split("_")[0];
							}else{
								char[] topicname_temp = test_f.getName().split("_")[ii].toCharArray();
								if(!Character.isDigit(topicname_temp[0])){ //�p�G�Ĥ@�Ӧr���O�Ʀr�N����ɦW�����F
									topicname=topicname+"_"+test_f.getName().split("_")[ii];
								}else{
									break;
								}
							}
						}
						System.out.println("���դ�󪺼��Ҭ�"+topicname);
						StartTime = System.currentTimeMillis();
						String relateness_result = TC.Comper_relateness_profile_doc(dir,topic_mapping,topicname,profile_label);
						EndTime = System.currentTimeMillis();
						test_time_Comper_relateness = test_time_Comper_relateness + ((EndTime-StartTime)/1000);
						
						EfficacyMeasure_w.write("���դ��: "+test_f.getName());
						EfficacyMeasure_w.newLine();
						EfficacyMeasure_w.write("�P�w��: "+relateness_result);
						EfficacyMeasure_w.newLine();
						EfficacyMeasure_w.flush();
						TC.set_EfficacyMeasure(relateness_result);
					//br4.close();
				}
				System.out.println("�ϥΪ̼ҫ��ѧ�s�B�z...");
				StartTime = System.currentTimeMillis();
				//�C��ݰ��檺��Ѧ]�l���@��
				User_profile_term = GU.update_OneDayTerm_Decay_Factor(dir, User_profile_term);
				//�C��ݰ��檺�r���h��
				User_profile_term = GU.interest_remove_term(dir, User_profile_term, day);
				//�C��ݰ��檺����h��
				User_profile_term = GU.interest_remove_doc(dir, User_profile_term, day);
				//�����Ʋ��w��
				CDF.forecasting_NGDorSIM(dir);
				
				//�N��s�᪺profile�g�J
				GU.out_new_user_profile(dir, preprocess_times, User_profile_term);
				EndTime = System.currentTimeMillis();
				sum_time_update_OneDayTerm = sum_time_update_OneDayTerm + ((EndTime-StartTime)/1000);
				
				//bw2.close();
				//br3.close();
				TC.show_all_result();
				System.out.println("���T�v�� " + TC.get_accuracy());
				System.out.println("���~�v�� " + TC.get_error());
				System.out.println("��ǲv�� " + TC.get_precision());
				System.out.println("�d���׬� " + TC.get_recall());
				System.out.println("F-measure�� " + TC.get_f_measure());
				System.out.println("�����Ʋ����Ƭ�: " + (GU.get_ConceptDrift_times()+TC.get_ConceptDrift_times()));
				System.out.println("�w���ӳs�����D�D���Y��Ƭ�: " + CDF.get_forecasting_times());
				double all_result[] = TC.get_all_result();
				EfficacyMeasure_w.write("TP="+all_result[0]+", TN="+all_result[1]+", FP="+all_result[2]+", FN="+all_result[3]);
				EfficacyMeasure_w.newLine();
				EfficacyMeasure_w.write("���T�v��: " + TC.get_accuracy());
				EfficacyMeasure_w.newLine();
				EfficacyMeasure_w.write("���~�v��: " + TC.get_error());
				EfficacyMeasure_w.newLine();
				EfficacyMeasure_w.write("��ǲv��: " + TC.get_precision());
				EfficacyMeasure_w.newLine();
				EfficacyMeasure_w.write("�d���׬�: " + TC.get_recall());
				EfficacyMeasure_w.newLine();
				EfficacyMeasure_w.write("F-measure��: " + TC.get_f_measure());
				EfficacyMeasure_w.newLine();
				EfficacyMeasure_w.write("�����Ʋ����Ƭ�: " + (GU.get_ConceptDrift_times()+TC.get_ConceptDrift_times()));
				EfficacyMeasure_w.newLine();
				EfficacyMeasure_w.write("�w���ӳs�����D�D���Y��Ƭ�: " + CDF.get_forecasting_times());
				EfficacyMeasure_w.newLine();
				EfficacyMeasure_w.newLine();
				EfficacyMeasure_w.newLine();
				EfficacyMeasure_w.flush();
				Time_w.write("�V�m��Ū���ҫ��ϥήɶ���: "+train_time_read_profile);
				Time_w.newLine();
				train_sum_time_read_profile+=train_time_read_profile;
				train_time_read_profile=0;
				Time_w.write("�V�m��Ū�����ϥήɶ���: "+train_time_read_doc);
				Time_w.newLine();
				train_sum_time_read_doc+=train_time_read_doc;
				train_time_read_doc=0;
				Time_w.write("�V�m�ϥD�D�M�g�ϥήɶ���: "+train_time_topic_mapping);
				Time_w.newLine();
				train_sum_time_topic_mapping+=train_time_topic_mapping;
				train_time_topic_mapping=0;
				Time_w.write("���հ�Ū���ҫ��ϥήɶ���: "+test_time_read_profile);
				Time_w.newLine();
				test_sum_time_read_profile+=test_time_read_profile;
				test_time_read_profile=0;
				Time_w.write("���հ�Ū�����ϥήɶ���: "+test_time_read_doc);
				Time_w.newLine();
				test_sum_time_read_doc+=test_time_read_doc;
				test_time_read_doc=0;
				Time_w.write("���հϥD�D�M�g�ϥήɶ���: "+test_time_topic_mapping);
				Time_w.newLine();
				test_sum_time_topic_mapping+=test_time_topic_mapping;
				test_time_topic_mapping=0;
				Time_w.write("�V�m�Ϸs�W����ϥήɶ���: "+train_time_add_user_profile);
				Time_w.newLine();
				train_sum_time_add_user_profile+=train_time_add_user_profile;
				train_time_add_user_profile=0;
				Time_w.write("���հϬ����P�w�ϥήɶ���: "+test_time_Comper_relateness);
				Time_w.newLine();
				test_sum_time_Comper_relateness+=test_time_Comper_relateness;
				test_time_Comper_relateness=0;
				Time_w.write("�C��ҫ���s�ϥήɶ���: "+time_update_OneDayTerm);
				Time_w.newLine();
				sum_time_update_OneDayTerm+=time_update_OneDayTerm;
				time_update_OneDayTerm=0;
				Time_w.write("");
				Time_w.newLine();
				Time_w.flush();
			}
			Time_w.write(days+"�Ѳ֭p...");
			Time_w.newLine();
			Time_w.write("�V�m��Ū���ҫ��ϥ��`�ɶ���: "+train_sum_time_read_profile);
			Time_w.newLine();
			Time_w.write("�V�m��Ū�����ϥ��`�ɶ���: "+train_sum_time_read_doc);
			Time_w.newLine();
			Time_w.write("�V�m�ϥD�D�M�g�ϥ��`�ɶ���: "+train_sum_time_topic_mapping);
			Time_w.newLine();
			Time_w.write("���հ�Ū���ҫ��ϥ��`�ɶ���: "+test_sum_time_read_profile);
			Time_w.newLine();
			Time_w.write("���հ�Ū�����ϥ��`�ɶ���: "+test_sum_time_read_doc);
			Time_w.newLine();
			Time_w.write("���հϥD�D�M�g�ϥ��`�ɶ���: "+test_sum_time_topic_mapping);
			Time_w.newLine();
			Time_w.write("�V�m�Ϸs�W����ϥ��`�ɶ���: "+train_sum_time_add_user_profile);
			Time_w.newLine();
			Time_w.write("���հϬ����P�w�ϥ��`�ɶ���: "+test_sum_time_Comper_relateness);
			Time_w.newLine();
			Time_w.write("�C��ҫ���s�ϥ��`�ɶ���: "+sum_time_update_OneDayTerm);
			Time_w.newLine();
			Time_w.flush();
			Time_w.close();
			EfficacyMeasure_w.close();
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
