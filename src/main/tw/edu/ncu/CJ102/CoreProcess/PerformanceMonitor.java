package tw.edu.ncu.CJ102.CoreProcess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import tw.edu.ncu.CJ102.Data.TopicTermGraph;

public class PerformanceMonitor {
	public static double lamda = 0.15,sigma = -0.05;
	private double TP, TN, FP, FN;
	private ArrayList<Double> historyFMeasure  = new ArrayList<>();
	
	public PerformanceMonitor(){
		TP = 0;
		TN = 0;
		FP = 0;
		FN = 0;
	}

	/**
	 * @return the true Positive
	 */
	public double getTP() {
		return TP;
	}

	/**
	 * @return the true negative 
	 */
	public double getTN() {
		return TN;
	}

	/**
	 * @return the false Positive
	 */
	public double getFP() {
		return FP;
	}

	/**
	 * @return the false negative 
	 */
	public double getFN() {
		return FN;
	}


	/**
	 * record the performance in the monitor, different Type will add up into different data
	 * @param type
	 */
	public void set_EfficacyMeasure(PerformanceType type) {
		if (type == PerformanceType.TRUEPOSTIVE) {
			TP++;
		}else if (type == PerformanceType.TRUENEGATIVE) {
			TN++;
		}else if (type == PerformanceType.FALSEPOSTIVE) {
			FP++;
		}else if (type == PerformanceType.FALSENEGATIVE) {
			FN++;
		}else{
			throw new IllegalArgumentException("The type:"+type+" is not in any correct PerformanceType");
		}
	}
	/**
	 * record performance until now, and clear all the performance static 
	 * @return
	 */
	public Map<PerformanceType,Double> getResult() {
		Map<PerformanceType,Double> results = new LinkedHashMap<>();
		results.put(PerformanceType.TRUENEGATIVE, this.TN);
		results.put(PerformanceType.TRUEPOSTIVE, this.TP);
		results.put(PerformanceType.FALSENEGATIVE, this.FN);
		results.put(PerformanceType.FALSEPOSTIVE, this.FP);
		results.put(PerformanceType.FMEASURE, this.computeFmeasure());
		results.put(PerformanceType.ACCURACY, this.computeAccuracy());
		results.put(PerformanceType.PRECISION, this.computePrecision());
		results.put(PerformanceType.RECALL, this.computeRecall());
		results.put(PerformanceType.ERROR, this.computeError());
		return results;
	}
	/**
	 * mean a day have been passed. monitor should be saved
	 */
	public void saveRecord(){
		this.historyFMeasure.add(this.computeFmeasure());
		this.FN = 0;
		this.FP = 0;
		this.TP = 0;
		this.TN = 0;
	}

	public double computePrecision() {
			if(TP+FP==0){
				return 0;
			}else{
				return TP/(TP+FP);
			}
	}

	public double computeRecall() {
		if(TP+FN==0){
			return 0;
		}else{
			return TP / (TP + FN);
		}
	}

	public double computeFmeasure() {
		Double f = (2 * computePrecision() * computeRecall())
				/ (computePrecision() + computeRecall());
		if(f.equals(Double.NaN)){
			return 0;
		}
		return f;
	}

	public double computeAccuracy() {
			double acc = (TP + TN) / (TP + TN + FP + FN);
			if((TP + TN + FP + FN)==0){
				return 0;
			}else{
				return acc;
			}
		
	}

	public double computeError() {
		if((FP+FN)==0||(TP + TN + FP + FN)==0){
			return 0;
		}else{
			return (FP + FN) / (TP + TN + FP + FN);
		}
	}
	public String toString(){
		return "F-measure:"+this.computeFmeasure()+", Accuracy:"+this.computeAccuracy()+",Recall:"+this.computeRecall()+",Precision:"+this.computePrecision();
	}
	
	public void addUp(PerformanceMonitor anotherMonitor){
		this.TN += anotherMonitor.getTN();
		this.TP += anotherMonitor.getTP();
		this.FN += anotherMonitor.getFN();
		this.FP += anotherMonitor.getFP();
	}
	public boolean phTest(){
		boolean phFlag = false;
		if(historyFMeasure.size()<3){ // at least exist size day
			return phFlag;
		}
		double pHTest  = 0;
		double totalFmeasure = 0,mT = 100000,uT = 0;//mT:最小累積差;uT:累積差和
		for (double fMeasure:historyFMeasure){
			totalFmeasure += fMeasure;
		}
		double avgFmeasure = totalFmeasure / historyFMeasure.size();

		for (double fMeasure:historyFMeasure) {
			uT += (fMeasure - avgFmeasure - sigma);
			if(uT<mT){ // find the smallest possible mT
				mT = uT;
			}
		}
		pHTest = uT - mT;

		if(pHTest>lamda){
			phFlag = true;
		}
		return phFlag;
	}
}
enum PerformanceType{
	TRUENEGATIVE,TRUEPOSTIVE,FALSENEGATIVE,FALSEPOSTIVE,FMEASURE,ACCURACY,ERROR,PRECISION,RECALL;
	
}
