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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Go_Training_Tom {

	/**
	 * @param args
	 */
	static String real_real_people="";
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*//�H�U��Reuters�V�m�B���ո�ƶ��Ыص{���X
		String dir = "Tom_exp2/";
		int days = 3; //����Ѽ�
		new File(dir).mkdirs(); //�гy�X�����ƧX
		new File(dir+"user_porfile").mkdirs(); //�гy�X����ϥΪ̼ҫ���ƧX
		new File(dir+"training").mkdirs(); //�гy�X����V�m����ƧX
		new File(dir+"testing").mkdirs(); //�гy�X������ն���ƧX
		for(int i=1; i<=days; i++){
			System.out.println("��"+i+"��");
			new File(dir+"training/"+"day_"+i).mkdirs(); //�гy�X����V�m����i�Ѹ�ƧX
			new File(dir+"testing/"+"day_"+i).mkdirs(); //�гy�X������ն���i�Ѹ�ƧX
			topic_doc_generateTrainSet("Tom_reuters/single",dir+"training/"+"day_"+i,"acq");
			topic_doc_generateTestSet("Tom_reuters/single",dir+"testing/"+"day_"+i,"acq");
		}
		//�H�W��Reuters�V�m�B���ո�ƶ��Ыص{���X*/
		
		//�H�U��CiteULike�V�m�B���ո�ƶ��Ыص{���X
		String dir = "Tom_exp5/", line="", real_people="";
		int train_days = 30, test_days = 10; //citeulike-����ѼơA0�������A-1�����ϥ�
		//real_people = "06c159908900abf05b5ab975b9766f0a"; //���citeulike��Ƭy
		try {
			BufferedReader br = new BufferedReader(new FileReader("citeulike/used_file_into.txt"));
			while((line=br.readLine())!=null){
				real_people = line.split("-")[0]; //���citeulike��Ƭy
				real_word_generateSet("citeulike/citeulike_Tom_citeulike_0.4/",dir,real_people,train_days,test_days);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//�H�W��CiteULike�V�m�B���ո�ƶ��Ыص{���X
	}
	
	//���ͤ����w�D�D�P���ƶq���V�m��
	public static void topic_doc_generateTrainSet(String source_dir, String resultDir){
		random_topic_doc_generate(source_dir,resultDir,"training");
	}
	//���ͤ����w���ƶq���V�m��
	public static void topic_doc_generateTrainSet(String source_dir, String resultDir, String topic){
		System.out.println("�V�m�����w�D�D = "+topic);
		random_doc_generate(source_dir,resultDir,"training",topic);
	}
	//���ͤ����w�D�D���V�m��
	public static void topic_doc_generateTrainSet(String source_dir, String resultDir, int size){
		random_topic_generate(source_dir,resultDir,"training",size);
	}
	//���ͤ����w�D�D�P���ƶq�����ն�
	public static void topic_doc_generateTestSet(String source_dir, String resultDir){
		random_topic_doc_generate(source_dir,resultDir,"testing");
	}
	//���ͤ����w���ƶq�����ն�
	public static void topic_doc_generateTestSet(String source_dir, String resultDir, String topic){
		System.out.println("���ն����w�D�D = "+topic);
		random_doc_generate(source_dir,resultDir,"testing",topic);
	}
	//���ͤ����w�D�D�����ն�
	public static void topic_doc_generateTestSet(String source_dir, String resultDir, int size){
		random_topic_generate(source_dir,resultDir,"testing",size);
	}
	//���ͤ����w���ƶq���V�m�P���ն�
	public static void topic_doc_generateTrainTestSet(String source_dir, String resultDir, int day, String topic){
		System.out.println("�V�m�P���ն����w�D�D = "+topic);
		random_doc_generate(source_dir,resultDir,"training",topic);
	}
	
	//���w�D�D�A�H���D�D���ƶq
	public static void random_doc_generate(String source_dir, String resultDir, String Object, String topic){
		Random ran = new Random();
		int size = ran.nextInt(15)+1; //�H��1~15�g�峹
		System.out.println("�H���D�D���ƶq = "+size);
		point_topic_doc_generateSet(source_dir, resultDir, topic, size);
	}
	//�H���D�D�A���w�D�D���ƶq
	public static void random_topic_generate(String source_dir, String resultDir, String Object, int size){
		File cdir = new File(source_dir);
		ArrayList<File> list = new ArrayList<File>();
		for(File f : cdir.listFiles()){
			list.add(f);
		}
		Collections.shuffle(list);
		point_topic_doc_generateSet(source_dir, resultDir, list.get(1).getName(), size);
	}
	//n���H���D�D�Am�g�H���D�D���
	public static void random_topic_doc_generate(String source_dir, String resultDir, String Object){
		int topic_count;
		int size = 0;
		File cdir = new File(source_dir);
		ArrayList<File> list = new ArrayList<File>();
		ArrayList<File> topicList = new ArrayList<File>();
		Random ran = new Random();
		topic_count = ran.nextInt(7)+1; //�H��1~7�ӥD�D
		for(File f : cdir.listFiles()){
			list.add(f);
		}
		Collections.shuffle(list);
		for(int i=0; i<topic_count; i++){
			topicList.add(list.get(i));
		}
		for(File d: topicList){
			size = ran.nextInt(15)+1; //�H��1~15�g�峹
			point_topic_doc_generateSet(source_dir, resultDir, d.getName(), size);
		}
	}
	
	//���w�D�D�P�ƶq
	public static void point_topic_doc_generateSet(String source_dir, String resultDir, String topic, int size){
		point_topic_doc_generateSet(source_dir,resultDir,topic,size,0);
	}
	//�ѼƬ���ƨӷ���ƧX, �ؼи�ƧX, �ؼХD�D, �D�D���ƶq, �üƺؤl
	public static void point_topic_doc_generateSet(String source_dir, String resultDir, String topic, int size, int random_num){
		File cdir = new File(source_dir+"/"+topic);
		ArrayList<File> list = new ArrayList<File>();
		//System.out.println(cdir);
		for(File f : cdir.listFiles()){
			//System.out.println(f.getName());
			list.add(f);
		}
		if(random_num==0){
			Collections.shuffle(list); //�H���Ƨ�
		}else{
			Collections.shuffle(list,new Random(random_num)); //�H���Ƨ�
		}
		for(int i=0; i<size; i++){
			//System.out.println("�}�l�ƻs�� "+(i+1)+" �g = "+list.get(i).getName());
			copyfile(list.get(i), new File(resultDir + "/" + list.get(i).getName()));
		}
	}
	
	public static void copyfile(File srFile, File dtFile) {
		try {
			File f1 = srFile;
			File f2 = dtFile;
			InputStream in = new FileInputStream(f1);
			// For Append the file.
			// OutputStream out = new FileOutputStream(f2,true);
			// For Overwrite the file.
			OutputStream out = new FileOutputStream(f2);

			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
			//System.out.println("File copied.");
		} catch (FileNotFoundException ex) {
			System.out
					.println(ex.getMessage() + " in the specified directory.");
			System.exit(0);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	//�ѼƬ���ƨӷ���ƧX, �ؼи�ƧX, ��Ƭy��H, �V�m�Ѽ�, ���դѼ�
	public static int real_word_generateSet(String source_dir, String resultDir, String real_people, int train_days, int test_days){
		try {
			String line = "",article_id = "";
			BufferedReader br;
			ArrayList<String> list = new ArrayList<String>();
			int first_day_m=13, first_day_d=32, last_day_m=0, last_day_d=0; //������Ƭy���̪�P�̫�@��
			int train_start_m=0, train_start_d=0, test_start_m=0, test_start_d=0; //�}�l���ͰV�m�B���ն������
			int days_num = 0; //���v�������ɶ������e�סA�@�Ӥ��30��
			int max_data_gap = 0; //���v������ƶ��̤j���j
			int data_line=0, m=0, d=0, pre_m=0, pre_d=0;
			int train_num=0, test_num=0; //�V�m�P���դ��ƶq�p�ƾ�
			double gap_times=0, gap_sum_days=0;
			boolean long_enough=true; //�������Ƭy��������
			ArrayList<String> steam_list = new ArrayList<String>();
			steam_list.clear();
			new File(resultDir+"training").deleteOnExit();
			new File(resultDir+"testing").deleteOnExit();
			
			if(train_days==0){
				test_days=-1;
			}else if(train_days==-1){
				test_days=0;
			}
			
			if(test_days==0){
				train_days=-1;
			}else if(test_days==-1){
				train_days=0;
			}
			
			if(real_people=="anyone"){
				list.clear();
				br = new BufferedReader(new FileReader("citeulike/used_file_into.txt"));
				//System.out.println(f.getName());
				while((line=br.readLine())!=null){
					list.add(br.readLine().split(",")[0]);
				}
				Collections.shuffle(list); //�H���Ƨ�
				real_people=list.get(1).split("-")[0];
				br.close();
				list.clear();
				line="";
			}
			System.out.println("��Ƭy��: "+real_people);
			real_real_people = real_people;
			
			br = new BufferedReader(new FileReader("citeulike/CiteULike��ƶ�/2014/"+real_people+"-read2014.txt"));
			while((line=br.readLine())!=null){
				//System.out.println(line);
				article_id = line.split(",")[0]; //�峹id
				m = Integer.valueOf(line.split(",")[1].split(" ")[0].split("-")[1]); //�ݤ峹�����
				d = Integer.valueOf(line.split(",")[1].split(" ")[0].split("-")[2]); //�ݤ峹����
				data_line++;
				
				if(data_line==1){
					first_day_m = m;
					first_day_d = d;
				}
				steam_list.add(String.valueOf(m)+"-"+String.valueOf(d)+","+article_id);
			}
			br.close();
			//System.out.println("�̫�@����ƬO�b"+m+"��"+d+"��");
			days_num = (m - first_day_m)*30 + (d - first_day_d) + 1;
			//System.out.println("first_day_m="+first_day_m+" first_day_d="+first_day_d+" m="+m+" d="+d);
			data_line = steam_list.size();
			
			//�p��ӱq��Ƭy�����Ѷ}�l�гy���ն�
			if(test_days!=0 || test_days!=-1){
				while(test_days>d){
					if(m==1){
						System.out.println("��Ƭy���פ���");
						long_enough=false;
					}else if(m==3){ //2��u���28�ѵ�d
						d = d+28;
					}else if(m==2 || m==4 || m==6 || m==8 || m==9 || m==11){ //1 3 5 7 8 10����31��
						d = d+31;
					}else if(m==5 || m==7 || m==10 || m==12){ //4 6 9 11����30��
						d = d+30;
					}
					m--;
				}
				d = d - test_days + 1;
				test_start_m = m; test_start_d = d;
				//System.out.println("���ն}�l�ɶ��O"+test_start_m+"��"+test_start_d+"��A�@"+test_days+"��");
			}
			
			//�p��ӱq��Ƭy�����Ѷ}�l�гy�V�m��
			if(train_days!=0 || train_days!=-1){
				while(train_days>d){
					if(m==1){
						System.out.println("��Ƭy���פ���");
						long_enough=false;
					}else if(m==3){ //2��u���28�ѵ�d
						d = d+28;
					}else if(m==2 || m==4 || m==6 || m==8 || m==9 || m==11){ //1 3 5 7 8 10����31��
						d = d+31;
					}else if(m==5 || m==7 || m==10 || m==12){ //4 6 9 11����30��
						d = d+30;
					}
					m--;
				}
				d = d - train_days + 1;
				train_start_m = m; train_start_d = d;
				//System.out.println("�V�m�}�l�ɶ��O"+train_start_m+"��"+train_start_d+"��A�@"+train_days+"��");
			}
			
			//System.out.println("�@"+data_line+"�����");
			if(long_enough){
				int i,j,z;
				//String start_day = String.valueOf(m)+"-"+String.valueOf(d);
				String temp_resultDir;
				if(train_days!=0 && test_days!=0){
					for(j=0; j<data_line; j++){
						line = steam_list.get(j);
						if((m<=Integer.valueOf(line.split(",")[0].split("-")[0])) && (d<=Integer.valueOf(line.split(",")[0].split("-")[1]))){
							break;
						}
					}
				}else{
					j=0;
				}
				
				if(train_days==0){
					train_days=days_num;
					train_start_m=first_day_m;
					train_start_d=first_day_d;
				}
				//System.out.println("��"+j+"����ƶ}�l�V�m");
				//System.out.println("train_days="+train_days);
				
				//�}�l���ͰV�m��
				if(train_days!=-1){
					for(i=1;i<=train_days;i++){
						//System.out.println("�}�l����"+train_start_m+"��"+train_start_d+"��V�m�P���ն�");
						new File(resultDir+"training/"+"day_"+i).mkdirs(); //�гy�X����V�m����i�Ѹ�ƧX
						new File(resultDir+"testing/"+"day_"+i).mkdirs(); //�гy�X������ն���i�Ѹ�ƧX
						temp_resultDir = resultDir+"training/"+"day_"+i;
						list.clear();
						
						while(j<data_line){
							line = steam_list.get(j);
							if(train_start_m==Integer.valueOf(line.split(",")[0].split("-")[0]) && train_start_d==Integer.valueOf(line.split(",")[0].split("-")[1])){
								File f = new File(source_dir+"cite_0abciteulike"+line.split(",")[1]+"_concepts.txt");
								if(f.exists() && !list.contains(f.getName())){
									//System.out.println("�ƻs�ɮ�: "+temp_resultDir + "/" + f.getName());
									copyfile(f, new File(temp_resultDir + "/" + f.getName()));
									list.add(f.getName());
									train_num++;
									if(pre_m==0 || pre_d==0){
										pre_m = train_start_m;
										pre_d = train_start_d;
									}
									if(max_data_gap<((train_start_m-pre_m)*30+train_start_d-pre_d)){
										max_data_gap = (train_start_m-pre_m)*30+(train_start_d-pre_d);
									}else if(((train_start_m-pre_m)*30+train_start_d-pre_d)>0){
										gap_times++;
										gap_sum_days = gap_sum_days + ((train_start_m-pre_m)*30+train_start_d-pre_d);
									}
									pre_m = train_start_m;
									pre_d = train_start_d;
								}
								j++;
							}else{
								break;
							}
						}
						
						if(d_to_m(train_start_m,train_start_d)){
							train_start_d=1;
							train_start_m++;
						}else{
							train_start_d++;
						}
					}
					
				}else{
					i=2;
					train_days=0;
				}
				
				if(test_days==0){
					test_days=days_num;
					test_start_m=first_day_m;
					test_start_d=first_day_d;
				}
				//System.out.println("��"+j+"����ƶ}�l����");
				
				//�}�l���ʹ��ն�
				if(test_days!=-1){
					for(z=(i-1);z<=(train_days+test_days);z++){
						//System.out.println("�}�l����"+test_start_m+"��"+test_start_d+"��A�V�m�P���ն�");
						new File(resultDir+"training/"+"day_"+z).mkdirs(); //�гy�X����V�m����i�Ѹ�ƧX
						new File(resultDir+"testing/"+"day_"+z).mkdirs(); //�гy�X������ն���i�Ѹ�ƧX
						temp_resultDir = resultDir+"testing/"+"day_"+z;
						list.clear();
						
						while(j<data_line){
							line = steam_list.get(j);
							if(test_start_m==Integer.valueOf(line.split(",")[0].split("-")[0]) && test_start_d==Integer.valueOf(line.split(",")[0].split("-")[1])){
								File f = new File(source_dir+"cite_0abciteulike"+line.split(",")[1]+"_concepts.txt");
								if(f.exists() && !list.contains(f.getName())){
									//System.out.println("�ƻs�ɮ�: "+temp_resultDir + "/" + f.getName());
									copyfile(f, new File(temp_resultDir + "/" + f.getName()));
									list.add(f.getName());
									test_num++;
									if(pre_m==0 || pre_d==0){
										pre_m = test_start_m;
										pre_d = test_start_d;
									}
									if(max_data_gap<((test_start_m-pre_m)*30+test_start_d-pre_d)){
										max_data_gap = (test_start_m-pre_m)*30+(test_start_d-pre_d);
									}else if(((test_start_m-pre_m)*30+test_start_d-pre_d)>0){
										gap_times++;
										gap_sum_days = gap_sum_days + ((test_start_m-pre_m)*30+test_start_d-pre_d);
									}
									pre_m = test_start_m;
									pre_d = test_start_d;
								}
								j++;
							}else{
								break;
							}
						}
						
						if(d_to_m(test_start_m,test_start_d)){
							test_start_d=1;
							test_start_m++;
						}else{
							test_start_d++;
						}
					}
				}
				/*BufferedWriter bw = new BufferedWriter(new FileWriter("citeulike/30_train_10_test_gap14.txt",true));
				if(max_data_gap<=14){
					bw.write(real_people+","+train_num+","+test_num+","+max_data_gap+","+(gap_sum_days/gap_times));
					bw.newLine();
					bw.flush();
					bw.close();
				}*/
				System.out.println("�@����"+train_num+"�g�V�m���");
				System.out.println("�@����"+test_num+"�g���դ��");
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(train_days==-1){
			train_days=0;
		}
		if(test_days==-1){
			test_days=0;
		}
		return (train_days+test_days);
	}
	
	public String get_real_people(){
		return real_real_people;
	}
	
	//�ѼƬ���ƨӷ���ƧX, �ؼи�ƧX, ��Ƭy��H, �V�m�Ѽ�, ���դѼ�
	public static boolean d_to_m(int m,int d){
		if(d==28){
			if(m==2){
				return true;
			}else{
				return false;
			}
		}else if(d==30){
			if(m==4 || m==6 || m==9 || m==11){
				return true;
			}else{
				return false;
			}
		}else if(d==31){
			if(m==1 || m==3 || m==5 || m==7 || m==8 || m==10 || m==12){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
}
