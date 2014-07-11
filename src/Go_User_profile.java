import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class Go_User_profile {

	/**
	 * @param args
	 */
	/*//�ʺA��Ѧ]�l�Ҧ�
	double DecayFactor_top = 0.55; //��Ѧ]�l�W��
	double DecayFactor_botton = 0.02; //��Ѧ]�l�U��
	double DecayFactor_plus = 0.079; //��Ѧ]�l�[�t��
	double DecayFactor_minus = 0.075; //��Ѧ]�l��w��
	double DecayFactor_init = DecayFactor_plus*2; //��Ѧ]�l��l��*/
	
	//�T�w��Ѧ]�l�Ҧ� 0 0.285�Atop�Bbotton�Binit���n�դ@�˪��A��L��0
	double DecayFactor_top = 0.05; //��Ѧ]�l�W��
	double DecayFactor_botton = 0.05; //��Ѧ]�l�U��
	double DecayFactor_plus = 0; //��Ѧ]�l�[�t��
	double DecayFactor_minus = 0; //��Ѧ]�l��w��
	double DecayFactor_init = 0.05; //��Ѧ]�l��l��*/
	
	double sum_avg_docTF = 0; //�֭p���������`TF��
	double sum_avg_termTF = 0; //�֭p������r��TF��
	double remove_rate = 0.6; //����h�����
	double interest_remove_rate = remove_rate; //�D�D�h�����֭p���������`TF�Ȥ��
	double term_remove_rate = remove_rate; //�r���h�����֭p������r��TF�Ȥ��
	static ArrayList<String> term_had_changed = new ArrayList<String>();
	int ConceptDrift_times = 0; //�����Ʋ�����
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HashMap<Integer,HashMap<String,Double>> User_profile_test = new HashMap<Integer,HashMap<String,Double>>();
		HashMap<Integer,HashMap<String,Double>> doc_test = new HashMap<Integer,HashMap<String,Double>>();
		HashMap<Integer,Integer> topic_test = new HashMap<Integer,Integer>();
		User_profile_test.put(0, null);
		doc_test.put(0, null);
		topic_test.put(0, null);
		Go_User_profile GU = new Go_User_profile();
		GU.add_user_profile_term("Tom_exp/",User_profile_test,doc_test,topic_test);
	}
	
	//��J�ѼƬOString�����ƧX�B��ϥΪ̼ҫ�(�r��)�B��󤺮e��T�B�D�D���M�g�A�^�Ǫ��Ƨ�s�᪺�ϥγo�ҫ�(�r��)
	public HashMap<Integer,HashMap<String,Double>> add_user_profile_term(String exp_dir,HashMap<Integer,HashMap<String,Double>> User_profile_term, HashMap<Integer,HashMap<String,Double>> doc_term, HashMap<Integer,Integer> topic_mapping){
		//�N�Ӥ��D�D���r��TF���ƨ̾ڱo�쪺�D�D�M�g���Y�s�J�ϥΪ̼ҫ���
		for(int i: doc_term.keySet()){
			//�����X��Ӭ۹�M���D�D
			HashMap<String, Double> doc_topic;// = new HashMap<String, Double>();
			doc_topic = new HashMap(doc_term.get(i));
			HashMap<String, Double> User_profile_topic;// = new HashMap<String, Double>();
			//User_profile_topic = User_profile_term.get(topic_mapping.get(i));
			if(User_profile_term.get(topic_mapping.get(i))==null){
				User_profile_topic = new HashMap<String, Double>();
				User_profile_topic.put("temp", 0.0); //�s�D�D����l��
				term_had_changed.add("temp");
			}else{
				User_profile_topic = new HashMap( User_profile_term.get(topic_mapping.get(i)));
			}
			
			//�N�D�D�����r�����Ʀs�J�ϥΪ̼ҫ� (��s)
			for(String s: doc_topic.keySet()){
				if(!term_had_changed.contains(s)){
					//System.out.println("�N�r��"+s+"�s�J��ʦr���C��");
					term_had_changed.add(s); //�N�r���s�J�A�H�K�C�Ѧr�����ƻP��Ѧ]�l�`��z�ɴ��X
				}
				if(User_profile_topic.get(s)!=null){ //�D�D���¦r���N�H�֥[���覡�s�J
					User_profile_topic.put(s,User_profile_topic.get(s)+doc_topic.get(s)); //��s���r������
				}else{ //�D�D���s�r���N�����s�J
					User_profile_topic.put(s,doc_topic.get(s));
				}
			}
			System.out.print("��s�ϥΪ̼ҫ��D�D "+topic_mapping.get(i)+",�r���ƶq��"+User_profile_topic.size()+"\n");
			//�N��s�᪺�ҫ���^�h
			User_profile_term.put(topic_mapping.get(i), new HashMap(User_profile_topic));
		}
		return User_profile_term;
	}
	
	//�C��ݰ��檺�ϥΪ̼ҫ���s�A�]�t��Ѧ]�l���@�λP�D�D�B�r�����h��
	//�ѼƬ������ƧX��m�W��, �ϥΪ̼ҫ�
	public HashMap<Integer,HashMap<String,Double>> update_OneDayTerm_Decay_Factor(String exp_dir, HashMap<Integer,HashMap<String,Double>> User_profile_term){
		//��s�ҫ��ɤ]�n���ܧ�s�Ӧr������Ѧ]�l�A��Ū����Ѧ]�l��������� PS. TDF = Term Decay Factor
		String line="";
		//double sum_decayfactor; //�����Y�r�����ӭ��W����Ѧ]�l
		int this_term_src; //�����Ӧ��B�z���r���s��
		//int this_term_update_time; //�����Ӧ��B�z���r������s�ɶ�
		double this_term_tf = 0; //�����Ӧ��B�z���Y�D�D���Y�r����TF����
		double new_this_term_tf = 0; //��s�᪺���Y�D�D���Y�r����TF����
		try{
			BufferedReader br = new BufferedReader(new FileReader(exp_dir+"user_porfile/user_profile_TDF.txt"));
			//�����r�����s���A�����K�󴣥X�v���P��s�ɶ��I
			HashMap<String,Integer> terms_info = new HashMap<String,Integer>(); //�r��
			int update_time = Integer.valueOf(br.readLine()); //�ثe�����s�s��
			update_time++; //�o������s�s���A�N�O�W�@�����A�[1
			ArrayList<Double> term_decayfactor = new ArrayList<Double>(); //�r����Ѧ]�l�}�C
			ArrayList<Integer> term_update_time = new ArrayList<Integer>(); //�r����s�ɶ��I�}�C
			ArrayList<Double> term_sum_score = new ArrayList<Double>(); //�r���b�ҫ������`��(�����D�D�έp)
			int term_src=0; //�Ȯɪ��r���s���A�K��s��term_decayfactor�Pterm_update_time�Ӧr������M��m
			while((line=br.readLine())!=null){
				terms_info.put(line.split(",")[0], term_src);
				term_decayfactor.add(Double.valueOf(line.split(",")[1]));
				term_update_time.add(Integer.valueOf(line.split(",")[2]));
				term_sum_score.add(Double.valueOf(line.split(",")[3]));
				term_src++;
			}
			
			//���N�s�W��(��user_profile_TDF.txt�S����)�r���[�J
			for(int i=0; i<term_had_changed.size(); i++){
				if(terms_info.get(term_had_changed.get(i))==null){
					//System.out.println("�s�W�r��"+term_had_changed.get(i)+"�i�JTDF");
					terms_info.put(term_had_changed.get(i), term_src);
					term_decayfactor.add(DecayFactor_init);
					term_update_time.add(update_time);
					term_sum_score.add(0.0);
					term_src++;
				}
			}
			
			//��s�ϥΪ̼ҫ��Ҧ��r�������ƻP��Ѧ]�l
			for(int i: User_profile_term.keySet()){
				for(String s: User_profile_term.get(i).keySet()){
					
					this_term_src = terms_info.get(s);
					term_update_time.set(this_term_src, update_time); //��s�r����s�ɶ��ܥثe�ɶ�
					//��s�r��TF����
					this_term_tf = User_profile_term.get(i).get(s);
					new_this_term_tf = this_term_tf * (1-term_decayfactor.get(this_term_src));
					System.out.println("��s�ϥΪ̼ҫ��D�D"+i+"�r��"+s+"��Ѧ]�l"+term_decayfactor.get(this_term_src)+"��s����Ƭ�"+new_this_term_tf);
					//�p�G�O�즳�r���N��s�r�����`���Ƭ��ثe����-�¤���+�s���ơA�s�r�����`���ƫh�����s�Jnew_this_term_tf
					if(term_sum_score.get(this_term_src)==0){
						term_sum_score.set(this_term_src,new_this_term_tf);
					}else{
						term_sum_score.set(this_term_src, term_sum_score.get(this_term_src)-this_term_tf+new_this_term_tf);
					}
					User_profile_term.get(i).put(s, new_this_term_tf);
					//��s��Ѧ]�l�A�p�G���o�ͧ��ܱo�N�W�[�A�S���N���
					if(term_had_changed.contains(s)){
						term_decayfactor = update_Term_Decay_Factor(terms_info,term_decayfactor,"plus",s);
					}else{
						term_decayfactor = update_Term_Decay_Factor(terms_info,term_decayfactor,"minus",s);
					}
				}
			}
			
			/*//��s��Ѧ��ܰʪ��r������T
			for(int i=0; i<term_had_changed.size(); i++){
				//��s�Y�r������Ѧ]�l�A���K�֭p��s���Ʃһݭn���W���Ʀr
				sum_decayfactor = 1;
				if(terms_info.get(term_had_changed.get(i))!=null){
					this_term_src = terms_info.get(term_had_changed.get(i));
					this_term_update_time = term_update_time.get(this_term_src);
					for(int p = this_term_update_time; this_term_update_time<(update_time-1); this_term_update_time++){
						sum_decayfactor = sum_decayfactor*term_decayfactor.get(this_term_src);
						term_decayfactor = update_Term_Decay_Factor(terms_info,term_decayfactor,"minus",term_had_changed.get(i));
					}
					term_decayfactor = update_Term_Decay_Factor(terms_info,term_decayfactor,"plus",term_had_changed.get(i));
					term_update_time.set(this_term_src, update_time); //��s�r����s�ɶ��ܥثe�ɶ�
					
					for(int j: User_profile_term.keySet()){
						//�p�G�Y�Ӧr���s�b��Y�Ӽҫ��D�D�����ܡA�ڭ̴N��s�L������
						if(User_profile_term.get(j).get(term_had_changed.get(i))!=null){
							this_term_tf = User_profile_term.get(j).get(term_had_changed.get(i));
							new_this_term_tf = this_term_tf * sum_decayfactor;
							//��s�����Ƭ��ثe����-�¤���+�s����
							term_sum_score.set(this_term_src, term_sum_score.get(this_term_src)-this_term_tf+new_this_term_tf);
							User_profile_term.get(j).put(term_had_changed.get(i), new_this_term_tf);
						}
					}
				}else{
					terms_info.put(term_had_changed.get(i), term_src);
					term_decayfactor.add(DecayFactor_plus);
					term_update_time.add(update_time);
					for(int j: User_profile_term.keySet()){
						if(User_profile_term.get(j).get(term_had_changed.get(i))!=null){
							new_this_term_tf = new_this_term_tf + User_profile_term.get(j).get(term_had_changed.get(i));
							User_profile_term.get(j).put(term_had_changed.get(i), new_this_term_tf);
						}
					}
					term_sum_score.add(new_this_term_tf);
				}
			}*/
			
			//��X�s����Ѧ]�l�������
			BufferedWriter bw = new BufferedWriter(new FileWriter(exp_dir+"user_porfile/user_profile_TDF.txt"));
			bw.write(""+Integer.valueOf(update_time));
			bw.newLine();
			bw.flush();
			for(String s: terms_info.keySet()){
				//System.out.println("....."+s+".....");
				//�s��榡�� �r��,�r����Ѧ]�l,������s�s��,�r���`TF����
				bw.write(s+","+term_decayfactor.get(terms_info.get(s))+","+term_update_time.get(terms_info.get(s))+","+term_sum_score.get(terms_info.get(s)));
				bw.newLine();
				bw.flush();
			}
			bw.close();
			term_had_changed.clear();
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return User_profile_term;
	}
	
	//�N��Loperate�Ѽƪ��I�s�A�q�`�ϥάO�]���ϥΪ��O�s�r���Aoperate��J���O���򤣭��n
	public ArrayList<Double> update_Term_Decay_Factor(HashMap<String,Integer> terms_info, ArrayList<Double> term_decayfactor, String update_term){
		return update_Term_Decay_Factor(terms_info,term_decayfactor,"plus",update_term);
	}
	//��s��Ѧ]�l�A��J�ѼƬ���Ѧr���P�s����M��T, �ثe��Ѧ]�l, ���[�t�δ�t, �ؼЦr���s��, �ǳƧ�s���r���s��
	public ArrayList<Double> update_Term_Decay_Factor(HashMap<String,Integer> terms_info, ArrayList<Double> term_decayfactor, String operate, String update_term){
		if(terms_info.get(update_term)!=null){ //�ҫ��¦��r��
			int this_time_term_src = terms_info.get(update_term); //�o�O��s���r���s��
			//System.out.println(update_term+"���ҫ��¦��r���A�ثe��Ѧ]�l��"+term_decayfactor.get(this_time_term_src));
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
	}
	
	//��l�Ʋ֭p���������`TF��
	public void init_docTF(){
		sum_avg_docTF = 0;
	}
	//����h�����֭p���������`TF�ȡA��J�ѼƬ��������`TF��
	public void sum_avg_docTF(double docTF){
		sum_avg_docTF = (sum_avg_docTF+docTF)/2;
	}
	//����h���{�ǡA��J�ѼƬ������ƧX�W��, �ϥΪ̼ҫ�, ��s��
	public HashMap<Integer,HashMap<String,Double>> interest_remove_doc(String exp_dir, HashMap<Integer,HashMap<String,Double>> User_profile_term, int day){
		double removal_threshold = sum_avg_docTF*interest_remove_rate;
		double topic_sum_score; //�ҫ����D�D���`��
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(exp_dir+"user_porfile/interst_Remove_Recorder.txt",true));
			System.out.println("�ثe�D�D�������e�Ȭ�"+removal_threshold);
			for (Iterator iterator = User_profile_term.keySet().iterator(); iterator.hasNext();){
				int j = (Integer)iterator.next();
				topic_sum_score = 0;
				for(String s: User_profile_term.get(j).keySet()){
					topic_sum_score+=User_profile_term.get(j).get(s);
				}
				if(topic_sum_score<removal_threshold){
					bw.write("��"+day+"��A�D�D"+j+"���Ƭ� "+topic_sum_score+" �]�p����e�� "+removal_threshold+" �Q����");
					bw.newLine();
					bw.flush();
					System.out.println("�D�D"+j+"���Ƭ�"+topic_sum_score+"�]�p����e�ȳQ����");
					iterator.remove();
					User_profile_term.remove(j); //�p����e�ȴN������
					remove_topic(exp_dir, String.valueOf(j));
				}
			}
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return User_profile_term;
	}
	
	//��l�Ʋ֭p������r��TF��
	public void init_termTF(){
		sum_avg_termTF = 0;
	}
	//�r���h�����֭p������r��TF�ȡA��J�ѼƬ���r����TF��
	public void sum_avg_termTF(double docTF, int term_count){
		sum_avg_termTF = (sum_avg_termTF+(docTF/term_count))/2;
	}
	//�r���h���{�ǡA��J�ѼƬ������ƧX�W��, �ϥΪ̼ҫ�, ��s��
	public HashMap<Integer,HashMap<String,Double>> interest_remove_term(String exp_dir, HashMap<Integer,HashMap<String,Double>> User_profile_term, int day){
		double removal_threshold = sum_avg_termTF*term_remove_rate;
		BufferedWriter bw;
		try {
			bw = new BufferedWriter(new FileWriter(exp_dir+"user_porfile/interst_Remove_Recorder.txt",true));
			System.out.println("�ثe�r���������e�Ȭ�"+removal_threshold);
			for(int j: User_profile_term.keySet()){
				for (Iterator iterator = User_profile_term.get(j).keySet().iterator(); iterator.hasNext();){
					String s = (String)iterator.next();
					if(User_profile_term.get(j).get(s)<removal_threshold){
						bw.write("��"+day+"��A�D�D"+j+"���r��"+s+"���Ƭ� "+User_profile_term.get(j).get(s)+" �]�p����e�� "+removal_threshold+" �Q����");
						bw.newLine();
						bw.flush();
						System.out.println("�D�D"+j+"���r��"+s+"���Ƭ�"+User_profile_term.get(j).get(s)+"�]�p����e�ȳQ����");
						iterator.remove();
						User_profile_term.get(j).remove(s); //�p����e�ȴN������
					}
				}
			}
			bw.close();
			//�������ϥΪ̼ҫ����w���s�b���r��
			remove_term(exp_dir, User_profile_term);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return User_profile_term;
	}
	
	//��X�C�����ϥΪ̼ҫ����G�A��J�ѼƬ������ƧX�W��, �ϥΪ̼ҫ��ǦC�s��, �ϥΪ̼ҫ�
	public void out_new_user_profile(String exp_dir, int preprocess_times, HashMap<Integer,HashMap<String,Double>> User_profile_term){
		BufferedWriter bw; //bw�ΨӼguser_profile
		//�Ȧs�r���P����
		HashMap<String,Double> topic_term = new HashMap<String,Double>();
		try {
			bw = new BufferedWriter(new FileWriter(exp_dir+"user_porfile/user_profile_"+preprocess_times+".txt"));
			for(int i: User_profile_term.keySet()){
				topic_term.clear();
				topic_term = new HashMap(User_profile_term.get(i));
				//System.out.print("�ϥΪ̼ҫ��D�D "+i+",�r���ƶq��"+topic_term.size()+"\n");
				for(String s: topic_term.keySet()){
					//�x�s�榡�� �r��,�r������,�r���D�D�s��
					//System.out.print("�إߨϥΪ̼ҫ��D�D "+i+",�r��"+s+",����"+topic_term.get(s)+"\n");
					bw.write(s+","+topic_term.get(s)+","+i);
					bw.newLine();
					bw.flush();
				}
			}
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//�quser_profile_TDF.txt�������ϥΪ̼ҫ����w���s�b���r��
	public void remove_term(String exp_dir, HashMap<Integer,HashMap<String,Double>> User_profile_term){
		try {
			String line="", v;
			ArrayList<String> TDF = new ArrayList<String>();
			BufferedReader br;
			br = new BufferedReader(new FileReader(exp_dir+"user_porfile/user_profile_TDF.txt"));
			String un = br.readLine(); //�Ĥ@�椣���n
			boolean exis = false;
			while((line=br.readLine())!=null){
				v=line.split(",")[0];
				for(int i: User_profile_term.keySet()){
					if(User_profile_term.get(i).containsKey(v)){
						exis = true;
					}
				}
				if(exis){
					TDF.add(line);
				}
				exis=false;
			}
			br.close();
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(exp_dir+"user_porfile/user_profile_TDF.txt"));
			bw.write(un);
			bw.newLine();
			bw.flush();
			for(int i=0;i<TDF.size();i++){
				bw.write(TDF.get(i));
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
	
	//�quser_profile_TR.txt�������Q�R�����D�D
	public void remove_topic(String exp_dir, String old_topic){
		try {
			String line="", v1, v2;
			ArrayList<String> TR = new ArrayList<String>();
			BufferedReader br = new BufferedReader(new FileReader(exp_dir+"user_porfile/user_profile_TR.txt"));
			String max_topic_index = br.readLine(); //�Ĥ@�欰�̤j�D�D�s��
			while((line=br.readLine())!=null){
				TR.add(line);
			}
			br.close();
			for(int i=0;i<TR.size();i++){
				v1 = TR.get(i).split(",")[0].split("-")[0];
				v2 = TR.get(i).split(",")[0].split("-")[1];
				if(v1.equals(old_topic) || v2.equals(old_topic)){
					ConceptDrift_times++;
					TR.remove(i);
				}
			}
			BufferedWriter bw = new BufferedWriter(new FileWriter(exp_dir+"user_porfile/user_profile_TR.txt"));
			bw.write(max_topic_index);
			bw.newLine();
			bw.flush();
			for(int i=0;i<TR.size();i++){
				bw.write(TR.get(i));
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
	
	public int get_ConceptDrift_times(){
		return ConceptDrift_times;
	}
}
