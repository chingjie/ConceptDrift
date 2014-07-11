import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import tw.edu.ncu.sia.util.ServerUtil;


public class TOM_ComperRelateness {

	/**
	 * @param args
	 */
	/*double DecayFactor_top = 0.55; //��Ѧ]�l�W��
	double DecayFactor_botton = 0.02; //��Ѧ]�l�U��
	double DecayFactor_plus = 0.079; //��Ѧ]�l�[�t�v
	double DecayFactor_minus = 0.075; //��Ѧ]�l��w�v*/
	double relateness_threshold = 0.4; //��0.525��оǪ����絲�G
	double TP = 0, TN = 0, FP = 0, FN = 0;
	int ConceptDrift_times = 0; //�����Ʋ�����
	//update_doc�Bmaybe_update_term�Bsure_update_term�̫�����o�{�ĪG���n�A�]�������B�J���Q���ѱ�
	//HashMap<Integer,HashMap<String,Double>> update_doc = new HashMap<Integer,HashMap<String,Double>> (); //���ݳQ��s�����S�x
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HashMap<Integer,HashMap<String,Double>> User_profile_test = new HashMap<Integer,HashMap<String,Double>>();
		HashMap<Integer,HashMap<String,Double>> doc_test = new HashMap<Integer,HashMap<String,Double>>();
		HashMap<Integer,Integer> topic_test = new HashMap<Integer,Integer>();
		User_profile_test.put(0, null);
		//doc_test.put(0, null);
		topic_test.put(0, null);
		TOM_ComperRelateness TC = new TOM_ComperRelateness();
		TC.Comper_topic_profile_doc("Tom_exp/",User_profile_test,doc_test,0.2);
	}
	
	//�D�D�M�g�{�ǡA�ѼƬ������ƧX�W��, �ϥΪ̼ҫ�, ���ҫ�, ngd���e��, �ާ@���O("train"��"test")
	public HashMap<Integer,Integer> Comper_topic_profile_doc_only(String exp_dir, HashMap<Integer,HashMap<String,Double>> profile, HashMap<Integer,HashMap<String,Double>> doc, double doc_ngd, String operate){
		int doc_topic_num = 0; //�Y�@���D�D���r���ƶq
		int profile_topic_num = 0; //�Y�@�ҫ��D�D���r���ƶq
		double profile_topic_tf_sum = 0; //�Y�@�ҫ��D�D���`TF��
		double threshold = 0; //ngd���e��
		double link_num = 0; //�Y�@���D�D�P�ҫ��D�D���K���s�u�ƶq
		int how_many_topic = 0; //�ҫ����D�D��
		HashMap<Integer,Integer> topic_mapping = new HashMap<Integer,Integer>(); //�D�D�M�g���G
		//update_doc�Bmaybe_update_term�Bsure_update_term�̫�����o�{�ĪG���n�A�]�������B�J���Q���ѱ�
		/*HashMap<String,Double> maybe_update_term = new HashMap<String,Double>(); //���˥D�Di���i��Q�O�s�U�Ӫ��r��
		HashMap<String,Double> sure_update_term = new HashMap<String,Double>(); //���˥D�Di�|�Q�O�s�U�Ӫ��r��*/
		//update_doc.clear();
		
		try {
			ServerUtil.initialize();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		FileWriter FWrite;
		try{
			BufferedReader br = new BufferedReader(new FileReader(exp_dir+"user_porfile/user_profile_TR.txt"));
			how_many_topic = Integer.valueOf(br.readLine()); //�ҫ����D�D��
			br.close();
			
			FWrite = new FileWriter(exp_dir+"Comper_topic_profile_doc.txt",true); //true ��,�� Append�Ҧ�
			BufferedWriter Comper_log = new BufferedWriter(FWrite);
			
			/*�H�U���B�ʦs�A�]���p��TFIDF�ӹL�·СA���t�Υi�H��y���۷�j���B��t��A�������A�M�w�O�_�ϥΡC(���g��)
			//�]���M�g�ڭ̤��O�����Ĩ��Ǫ����r���Ӽƪ��s�u�ơA�ӬO�U�r���b�o��TFIDF���ơA�o�˧��Q�ΥD�D�S��r���A�ҥH�n����s�U�r��������
			//��s�ҫ��ɤ]�n���ܧ�s�Ӧr������Ѧ]�l�A��Ū����Ѧ]�l��������� PS. TDF = Term Decay Factor
			try {
				BufferedReader br = new BufferedReader(new FileReader(exp_dir+"user_porfile/user_profile_TDF.txt"));
				String line="";
				//�����r�����s���A�����K�󴣥X�v���P��s�ɶ��I
				HashMap<String,Integer> terms_info = new HashMap<String,Integer>(); //�r��
				int update_time = Integer.valueOf(br.readLine()); //�ثe�����s�s��
				ArrayList<Double> term_decayfactor = new ArrayList<Double>(); //�r����Ѧ]�l�}�C
				ArrayList<Integer> term_update_time = new ArrayList<Integer>(); //�r����s�ɶ��I�}�C
				ArrayList<Double> term_sum_score = new ArrayList<Double>(); //�r���b�ҫ������`��(�����D�D�έp)
				int term_src=0; //�Ȯɪ��r���s���A�K��s��term_decayfactor�Pterm_update_time�Ӧr������M��m
				while((line=br.readLine())!=null){
					terms_info.put(line.split(",")[0], term_src);
					term_decayfactor.add(Double.valueOf(line.split(",")[1]));
					term_update_time.add(Integer.valueOf(line.split(",")[2]));
					term_src++;
				}
				//��sTDF�̩Ҧ��r����T�P�ϥΪ̼ҫ��r������
				for(String s: terms_info.keySet()){
					double sum_decayfactor = 1; //�֭p����Ѧ]�l�A�᭱�����ƪ������W�o�ӿ�Ѧ]�l�Y�i
					for(int j=term_update_time.get(terms_info.get(s)); j < update_time; j++){
						sum_decayfactor = sum_decayfactor*(1-term_decayfactor.get(terms_info.get(s)));
						term_decayfactor = update_Term_Decay_Factor(terms_info,term_decayfactor,"minus",s);
					}
				}
				//��Ҧ��r����s���ơA���K�֭p�Ӧr����
			}catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
			
			//��󪺩Ҧ��D�D�P�ϥΪ̼ҫ����Ҧ��D�D�i��NGD�p��A��X���۹������D�D
			for(int i: doc.keySet()){
				doc_topic_num = doc.get(i).size();
				//maybe_update_term.clear();
				//sure_update_term.clear();
				//���פ��D�D�άO�ҫ��D�D���̧C�s�����O1�A�]����l�M�g�]0�A�b�̫�^���٬O0���ܥN��ܦ����D�D���ҫ����S�����s�D�D
				topic_mapping.put(i, 0);
				//�x�s���D�Di��M�쪺�̤j������
				double bigest_sim = 0;
				
				for(int j: profile.keySet()){
					link_num = 0;
					profile_topic_num = profile.get(j).size();
					Comper_log.write("���D�D"+i+" �r���Ƭ�"+doc_topic_num+"��");
					Comper_log.newLine();
					Comper_log.write("�ҫ��D�D"+j+" �r���Ƭ�"+profile_topic_num+"��");
					Comper_log.newLine();
					//System.out.println("���D�D"+i+" �r���Ƭ�"+doc_topic_num+"��");
					//System.out.println("�ҫ��D�D"+j+" �r���Ƭ�"+profile_topic_num+"��");
					profile_topic_tf_sum = 0;
					for(String profile_term: profile.get(j).keySet()){
						double term_tf = profile.get(j).get(profile_term);
						profile_topic_tf_sum = profile_topic_tf_sum + term_tf;
					}
					for(String doc_term: doc.get(i).keySet()){
						//Double TF = doc.get(i).get(doc_term);
						//boolean term_doc_term_inmaybe = false;
						for(String profile_term: profile.get(j).keySet()){
							double term_tf = profile.get(j).get(profile_term);
							//System.out.println(doc_term+","+profile_term+" ngd�p��");
							double a = ServerUtil.getHits("\""+doc_term+"\"");
							double b = ServerUtil.getHits("\""+profile_term+"\"");
							//System.err.println("���դ��P�ϥΪ̼Ҳշ������ Query: +\""+doc_term+"\" +\""+profile_term+"\"");
							double mValue = ServerUtil.getHits("+\""+doc_term+"\" +\""+profile_term+"\"");
							
							double NGD = NGD_calculate.NGD_cal(a,b,mValue);
							if(NGD<=doc_ngd){
								//link_num = link_num + 1; //�ֿn�s�u�Ƥ�k
								link_num = link_num + term_tf; //TF��k
								//term_doc_term_inmaybe = true;
							}
						}
						/*if(term_doc_term_inmaybe){
							maybe_update_term.put(doc_term,TF); //�ߦ���K���r�����i��Q�O�s�U��
						}*/
					}
					
					//��k2(�ֿn�s�u�Ƥ�k)�P�w�i�H�M�g���s�u���e�Ȭ���諸���D�D���r����*��諸�ҫ��D�D�r����*�����P�w���e��
					//threshold = doc_topic_num*profile_topic_num*relateness_threshold;
					//��k3(�ֿn�s�u�Ƥ�k)(�ۦ��ץ�����s�u����)�P�w�i�H�M�g���s�u���e�Ȭ���諸���D�D���r����*��諸�ҫ��D�D�r����*�����P�w���e��
					//threshold = (doc_topic_num*profile_topic_num*relateness_threshold)/(doc_topic_num*profile_topic_num);
					//link_num = (link_num / (doc_topic_num*profile_topic_num));
					
					//��k4(TF��k)�P�w�i�H�M�g���s�u���e�Ȭ���諸���D�D���r����*��諸�ҫ��D�D�r��TF���`�X*�����P�w���e��
					//threshold = doc_topic_num*profile_topic_tf_sum*relateness_threshold;
					//��k5(TF��k)(�ۦ��ץ�����s�u����)�P�w�i�H�M�g���s�u���e�Ȭ���諸���D�D���r����*��諸�ҫ��D�D�r��TF���`�X*�����P�w���e��
					threshold = (doc_topic_num*profile_topic_tf_sum*relateness_threshold)/(doc_topic_num*profile_topic_num);
					link_num = link_num / (doc_topic_num*profile_topic_num);
					
					//��k1(�Ǫ�����̨έȤ�k)�P�w�i�H�M�g���s�u���e�Ȭ�0.525�A����k�O�Q�� (�s�u�ȱo�`��/(���D�D���r����*��諸�ҫ��D�D�r��TF���`�X)) ����ҨӺ�A�g�Ǫ�����o�쪺���e��
					//threshold = relateness_threshold;
					//link_num = (link_num / (doc_topic_num*profile_topic_num));
					
					//��k6(�ֿn�s�u�Ƥ�k)(�����k2+�B�~��X��Ҫ���)�p���ȱo�s�u�ƶW�X���e�Ȫ��ƭȹ����e�Ȫ����
					//��k7(TF��k)(�����k3+�B�~��X��Ҫ���)�p���ȱo�s�u�ƶW�X���e�Ȫ��ƭȹ����e�Ȫ����
					//��k8(TF��k)(�����k4+�B�~��X��Ҫ���)�p���ȱo�s�u�ƶW�X���e�Ȫ��ƭȹ����e�Ȫ����
					//��k9(TF��k)(�����k5+�B�~��X��Ҫ���)�p���ȱo�s�u�ƶW�X���e�Ȫ��ƭȹ����e�Ȫ����
					//(�p�G���k1~5�е��ѤU��7��{���X)--(�s�u�ȱo�`��-���e��)/(���e��)�A�̤j�W�X��Ҫ��~�|�����M�g�D�D
					//link_num = (link_num-threshold)/threshold;
					Comper_log.write("���D�D"+i+"�P�ҫ��D�D"+j+"���������e�Ȭ� "+threshold);
					Comper_log.newLine();
					Comper_log.write("���D�D"+i+"�P�ҫ��D�D"+j+"����K�s�u�� "+link_num);
					Comper_log.newLine();
					//System.out.println("���D�D"+i+"�P�ҫ��D�D"+j+"���������e�Ȭ� "+threshold);
					//System.out.println("���D�D"+i+"�P�ҫ��D�D"+j+"����K�s�u�� "+link_num);
					if(link_num>threshold){
						//if(link_num>bigest_sim){
						if(((link_num-threshold)/threshold)>bigest_sim){
							Comper_log.write("���D�D"+i+" �ȩw �M�g��ҫ��D�D "+j);
							Comper_log.newLine();
							//sure_update_term.clear();
							//sure_update_term = new HashMap(maybe_update_term); //�̲׷|�Q�O�d�U�Ӫ��r���|���H�����ʳ̤j�����ӥD�D����令�G
							topic_mapping.put(i, j);
							//bigest_sim = link_num;
							bigest_sim = ((link_num-threshold)/threshold);
						}
					}
					
					//(�p�G���k6~9�е��ѤU��4��{���X)�p�G�K���s�u�ƶq�j����e�ȴN�����U�ӳo�������D�D�A�ڭ̷|��̰����A�O����O��mapping(���D�D�s��,�ҫ��D�D�s��)
					/*if(link_num>threshold && link_num>bigest_sim){
						topic_mapping.put(i, j);
						bigest_sim = link_num;
					}*/
				}
				if(topic_mapping.get(i)==0){ //�p�G�O0�N��ܷs�D�D�A�ڭ̭n��s�D�D�[�i�h
					System.out.println("�o�{�s�D�D");
					if(operate=="test"){
						Comper_log.write("�o�{�s�D�D�A���դ��_");
						Comper_log.newLine();
						/*topic_mapping.put(i, how_many_topic+1);
						how_many_topic++;*/
						break; //�]�������դ��Ө��p�G���s�D�D�N�O�N��D�ϥΪ̬����峹�A�]�������A�p��
					}else if(operate=="train"){
						Comper_log.write("�o�{�s�D�D");
						Comper_log.newLine();
						topic_mapping.put(i, how_many_topic+1);
						//update_doc.put(i, new HashMap(doc.get(i))); //�s�D�D�N�O�d�U�Ҧ��r��
						how_many_topic++;
					}
				}else{
					Comper_log.write("�¥D�D");
					Comper_log.newLine();
					System.out.println("�¥D�D");
					//update_doc.put(i, new HashMap(sure_update_term)); //�¥D�D�N�O�d�U�P�M�]�D�D��K���r��
				}
			}
			Comper_log.close();
		}catch (IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return topic_mapping;
	}
	
	/*public HashMap<Integer,HashMap<String,Double>> updata_fine_doc(){
		return update_doc;
	}*/
	
	public HashMap<Integer,Integer> Comper_topic_profile_doc(String exp_dir,HashMap<Integer,HashMap<String,Double>> profile, HashMap<Integer,HashMap<String,Double>> doc, double doc_ngd){
		/*try {
			BufferedWriter EfficacyMeasure_w = new BufferedWriter(new FileWriter(exp_dir+"EfficacyMeasure.txt"));
			EfficacyMeasure_w.write("aaa: xxxx");
			EfficacyMeasure_w.newLine();
			EfficacyMeasure_w.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		HashMap<Integer,Integer> topic_mapping = new HashMap<Integer,Integer>(); //�D�D�M�g���G
		topic_mapping = Comper_topic_profile_doc_only(exp_dir,profile,doc,doc_ngd, "train");
		update_topic_relation(exp_dir,topic_mapping);
		return topic_mapping;
	}
	
	public void update_topic_relation(String exp_dir,HashMap<Integer,Integer> topic_mapping){
		
		//�ثe���h�Ҽ{��@�Ӥ�󦳨�ӥD�D�A�]���i����]�t�F��ӥD�D�Ψ⪺�D�D��M��P�@�өΤ��P���ҫ��D�D
		try {
			//Ū���D�D���Y��� PS. TR = Topic Relation
			BufferedReader br = new BufferedReader(new FileReader(exp_dir+"user_porfile/user_profile_TR.txt"));
			String line;
			int how_many_topic = Integer.valueOf(br.readLine()); //�o���ثe�D�D��
			String topics;
			double topic_relation;
			HashMap<String,Double> TR = new HashMap<String,Double>(); //Ū���X�Ӫ��D�D���Y
			while((line=br.readLine())!=null){
				topics = line.split(",")[0];
				topic_relation = Double.valueOf(line.split(",")[1]);
				TR.put(topics, topic_relation);
			}
			//System.out.println("-->>"+topic_mapping.get(0));
			String this_update;
			if(topic_mapping.size()==1){
				//���u�]�t�@�ӥD�D
				if(topic_mapping.get(1)>how_many_topic){
					how_many_topic = topic_mapping.get(1);
				}
				this_update = topic_mapping.get(1)+"-"+topic_mapping.get(1);
				//��s�P�즳�D�D���Ӹ�Ƥ�
				if(TR.get(this_update)==null){
					ConceptDrift_times++;
					TR.put(this_update, 1.0);
				}else{
					TR.put(this_update, TR.get(this_update)+1);
				}
			}else{
				//���]�t�h�ӥD�D�A���Ʀr�p���ƫe��
				int doc_topic_num = topic_mapping.size(); //�o����󤺥D�D�ƶq
				//���t�諸�ܰ��榸�Ƭ� (�D�D�ƶq*(�D�D�ƶq-1))/2
				//���פ��D�D�άO�ҫ��D�D���̧C�s�����O1�A�ҥHj�q1�}�l�@����̫�@�Ӫ��e�@��(�]���n�Pz�ܼƨ��t��)
				for(int j=1;j<doc_topic_num;j++){
					//z�ܼƬ�j���U�@�Ӷ}�l�@����̫�@��
					for(int z=j+1;z<=doc_topic_num;z++){
						if(topic_mapping.get(j)<topic_mapping.get(z)){
							if(topic_mapping.get(z)>how_many_topic){
								how_many_topic = topic_mapping.get(z);
							}
							this_update = topic_mapping.get(j)+"-"+topic_mapping.get(z);
						}else{
							if(topic_mapping.get(j)>how_many_topic){
								how_many_topic = topic_mapping.get(j);
							}
							this_update = topic_mapping.get(z)+"-"+topic_mapping.get(j);
						}
						//��s�ܭ즳�D�D���Ӹ�Ƥ�
						if(TR.get(this_update)==null){
							ConceptDrift_times++;
							TR.put(this_update, 1.0);
						}else{
							TR.put(this_update, TR.get(this_update)+1);
						}
					}
				}
			}
			
			//��X�s���D�D���Y���
			BufferedWriter bw = new BufferedWriter(new FileWriter(exp_dir+"user_porfile/user_profile_TR.txt"));
			bw.write(""+Integer.valueOf(how_many_topic)); //�ثe�D�D��
			bw.newLine();
			bw.flush();
			for(String s: TR.keySet()){
				//�s��榡�� �D�D1-�D�D2,���Y�{��,������s�s��
				bw.write(s+","+TR.get(s));
				bw.newLine();
				bw.flush();
			}
			bw.close();
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//�������ѼƬ� String�����ƧX, hashmap���P�ҫ��D�D���M�g, String��󪺼��ҥD�D, ArrayList<String>�t�Υثe�O�������ҥD�D
	public String Comper_relateness_profile_doc(String exp_dir,HashMap<Integer,Integer> topic_mapping, String doc_label, ArrayList<String> profile_label){
		String label_result; //�x�s���ҵ���
		String label_system="0"; //�x�s�t�ΧP�_���G
		String doc_system_label=""; //�t��������ഫ������
		
		if(profile_label.contains(doc_label)){
			label_result="T"; //�����Ҫ��D�D�s�b��t�Τ�
		}else{
			label_result="F"; //�����Ҫ��D�D���s�b��t�Τ�
		}
		System.out.println("��ñ�{���O"+label_result);
		
		//Ū���D�D���Y��� PS. TR = Topic Relation
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(exp_dir+"user_porfile/user_profile_TR.txt"));
			String line;
			HashMap<String,Double> TR = new HashMap<String,Double>(); //Ū���X�Ӫ��D�D���Y
			String topics;
			double topic_relation;
			line=br.readLine(); //�Y�@�ӥD�D�`�Ƹ�T�b���N���|�Q�Ψ�A�ҥH��Ū��
			while((line=br.readLine())!=null){
				topics = line.split(",")[0];
				topic_relation = Double.valueOf(line.split(",")[1]);
				TR.put(topics, topic_relation);
			}
			br.close();
			
			/*//�Ǫ���²��P�w��k(�Ǫ��P����s�P�w��k�п�ܤ@��`��)
			int doc_topic_num = topic_mapping.size(); //�o����󤺥D�D�ƶq
			label_system="F";
			System.out.println("���D�D�ƶqtopic_mapping.size()="+doc_topic_num);
			for(int i=1;i<=doc_topic_num;i++){
				if(topic_mapping.get(i)!=0){
					label_system="T";
				}
			}*/
			
			//����s�P�w��k(�Ǫ��P����s�P�w��k�п�ܤ@��`��)
			//�p�G�D�D�M�g�o�{���դ�󤤥]�t���s�D�D�A�N���i��O�������A�p���]�s�D�D�b�P�_���Y�O�_�s�b��ҫ���
			int doc_topic_num = topic_mapping.size(); //�o����󤺥D�D�ƶq
			System.out.println("���D�D�ƶqtopic_mapping.size()="+doc_topic_num);
			if(doc_topic_num==1){
				if(topic_mapping.get(1)==0){
					System.out.println("���t���s�D�D");
					label_system="F";
				}else{
					//���u�]�t�@�ӥD�D
					doc_system_label = topic_mapping.get(1)+"-"+topic_mapping.get(1);
					if(TR.get(doc_system_label)!=null){
						label_system="T";
					}else{
						System.out.println("�ҫ����L"+doc_system_label+"�D�D���Y");
						label_system="F";
					}
				}
			}else{
				for(int i=1;i<doc_topic_num;i++){
					if(topic_mapping.get(i)==0){
						label_system="F";
					}
				}
				if(!label_system.equals("F")){
					//���t�諸�ܰ��榸�Ƭ� (�D�D�ƶq*(�D�D�ƶq-1))/2�A���]�t�h�ӥD�D�A���Ʀr�p���ƫe��
					//���פ��D�D�άO�ҫ��D�D���̧C�s�����O1�A�ҥHj�q1�}�l�@����̫�@�Ӫ��e�@��(�]���n�Pz�ܼƨ��t��)
					for(int j=1;j<doc_topic_num;j++){
						//z�ܼƬ�j���U�@�Ӷ}�l�@����̫�@��
						for(int z=j+1;z<=doc_topic_num;z++){
							if(topic_mapping.get(j)<topic_mapping.get(z)){
								doc_system_label = topic_mapping.get(j)+"-"+topic_mapping.get(z);
							}else{
								doc_system_label = topic_mapping.get(z)+"-"+topic_mapping.get(j);
							}
							if(TR.get(doc_system_label)==null){
								System.out.println("�ҫ����L"+doc_system_label+"�D�D���Y");
								label_system="F";
								break; //�p�o�{������@�����Y���s�b��ҫ����N�N�������A�����~��P�_
							}
						}
						if(label_system.equals("F")){
							break; //�p�o�{������@�����Y���s�b��ҫ����N�N�������A�����~��P�_
						}
					}
				}
				if(!label_system.equals("F")){
					label_system="T";
				}
			}
			System.out.println("�t�λ{���O"+label_system);
			
			//�̲פ������P�w
			if(label_result=="T"){
				if(label_system=="T"){
					return "TP"; //���ҵ��G�P�t�ΧP�w���G���OT
				}else{
					return "FN"; //�t�αNT�P�_��F
				}
			}else{
				if(label_system=="T"){
					return "FP"; //�t�αNF�P�_��T
				}else{
					return "TN"; //���ҵ��G�P�t�ΧP�w���G���ON
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "ERR";
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "ERR";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "ERR";
		}
	}
	
	//�N��Loperate�Ѽƪ��I�s�A�q�`�ϥάO�]���ϥΪ��O�s�r���Aoperate��J���O���򤣭��n
	/*public ArrayList<Double> update_Term_Decay_Factor(HashMap<String,Integer> terms_info, ArrayList<Double> term_decayfactor, String update_term){
		return update_Term_Decay_Factor(terms_info,term_decayfactor,"plus",update_term);
	}
	//��s��Ѧ]�l�A��J�ѼƬ���Ѧr���P�s����M��T, �ثe��Ѧ]�l, ���[�t�δ�t, �ؼЦr���s��, �ǳƧ�s���r���s��
	public ArrayList<Double> update_Term_Decay_Factor(HashMap<String,Integer> terms_info, ArrayList<Double> term_decayfactor, String operate, String update_term){
		if(terms_info.get(update_term)!=null){ //�ҫ��¦��r��
			int this_time_term_src = terms_info.get(update_term); //�o�O��s���r���s��
			System.out.println(update_term+"���ҫ��¦��r���A�ثe��Ѧ]�l��"+term_decayfactor.get(this_time_term_src));
			if(operate=="minus"){
				//��ֿ�Ѧ]�l
				//�p�G��ֿ�Ѧ]�l�|�C��U���N�]���U���A�_�h�N��������
				if((term_decayfactor.get(this_time_term_src)-DecayFactor_minus)<DecayFactor_botton){
					term_decayfactor.set(this_time_term_src, DecayFactor_botton);
				}else{
					term_decayfactor.set(this_time_term_src, term_decayfactor.get(this_time_term_src)-DecayFactor_minus);
				}
			}else if(operate=="plus"){
				//�]���������r���S�W�[�F���ƩҥH��Ѧ]�l��۴����A�p�G������]�l�|����W���N�]���W���A�_�h�N�����W�[
				if((term_decayfactor.get(this_time_term_src)+DecayFactor_plus)>DecayFactor_top){
					term_decayfactor.set(this_time_term_src, DecayFactor_top);
				}else{
					term_decayfactor.set(this_time_term_src, term_decayfactor.get(this_time_term_src)+DecayFactor_plus);
				}
			}
		}else{
			//System.out.println(update_term+"���ҫ��s�r��");
			term_decayfactor.add(DecayFactor_plus);
		}
		return term_decayfactor;
	}*/
	
	public int get_ConceptDrift_times(){
		return ConceptDrift_times;
	}
	
	//�į�Ŷq
	public void init_EfficacyMeasure(){
		TP=0; TN=0; FP=0; FN=0;
	}
	public void set_EfficacyMeasure(String result){
		System.out.println("�����Q�P�w��"+result);
		if(result=="TP"){
			TP++;
		}
		if(result=="TN"){
			TN++;
		}
		if(result=="FP"){
			FP++;
		}
		if(result=="FN"){
			FN++;
		}
	}
	public void show_all_result(){
		System.out.println("TP="+TP+", TN="+TN+", FP="+FP+", FN="+FN);
	}
	public double[] get_all_result(){
		double all_result[] = {TP, TN, FP, FN};
		return all_result;
	}
	public double get_precision(){
		return TP/(TP+FP);
	}
	public double get_recall(){
		return TP/(TP+FN);
	}
	public double get_f_measure(){
		return (2*get_precision()*get_recall())/(get_precision()+get_recall());
	}
	public double get_accuracy(){
		return (TP+TN)/(TP+TN+FP+FN);
	}
	public double get_error(){
		return (FP+FN)/(TP+TN+FP+FN);
	}
}
