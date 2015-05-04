package tw.edu.ncu.CJ102.algorithm.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import tw.edu.ncu.CJ102.NGD_calculate;
import tw.edu.ncu.CJ102.SolrSearcher;
import tw.edu.ncu.CJ102.Data.TermNode;
@RunWith(Parameterized.class)
public class testParallexWork {
	@Parameters
	public static Collection<Object[]> prepareData(){
		return Arrays.asList(new Object[][]{
				{10,1000},{10,1000},{50,1000},{50,1000},{100,1000},{100,10000},{300,10000}
		});
	}
	@Parameter
	public int threadNumber;
	
	@Parameter(value =1)
	public int round;
	
	public TermNode termA, termB;
	
	public double expecetedNumber;
	@Before
	public void setup() throws Exception{
		termA = new TermNode("Google");
		termB = new TermNode("Yahoo");
		double a = SolrSearcher.getHits("\"Google\"");
		double b = SolrSearcher.getHits("\"Yahoo\"");
		double mValue = SolrSearcher.getHits("+\"google\" +\"yahoo\"");
		double ngdDistance = NGD_calculate.NGD_cal(a, b, mValue);
		expecetedNumber = (1 - ngdDistance);
		
	}
	@Test
	public void testMultiThread(){
		ExecutorService executor = Executors.newFixedThreadPool(threadNumber);
		CompletionService<Double> cs = new ExecutorCompletionService<Double>(executor);
		List<Future<Double>> list = new ArrayList<>();
		for(int i=0;i<=round;i++){
				termA.termFreq = 1;
				termB.termFreq = 1;
				Future<Double> f = cs.submit(new NgdReverseTfComputingTask(termA,termB));
				list.add(f);
		}
		int errorCount = 0;
		double sum = 0;
		int count = 0;
		while (count < list.size()) {
			try {
				Future<Double> task = cs.take();
				if(!task.isDone()){
					errorCount++;
				}
				double ngdScore = task.get();
				assertEquals("Should return the same number",this.expecetedNumber,ngdScore,0);
				sum += ngdScore;
				count++;
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
				fail("System are not up, please check!");
				break;
			}
		}
		System.out.println(sum);
		assertEquals("Shoud not have any error",0,errorCount);

	}
	
	@Test
	public void testUnstableMultiThread(){
		ExecutorService executor = Executors.newFixedThreadPool(threadNumber);
		List<Future<Double>> list = new ArrayList<>();
		for(int i=0;i<=round;i++){
				termA.termFreq = 1;
				termB.termFreq = 1;
				Future<Double> f = executor.submit(new NgdReverseTfComputingTask(termA,termB));
				list.add(f);
		}
		double sum = 0;
		for(Future<Double> task:list){
			try {
				double ngdScore = task.get();
				assertEquals("Should return the same number",this.expecetedNumber,ngdScore,0);
				sum += ngdScore;
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		System.out.println(sum);
		
	}
	@Test
	public void testMultiThreadWithCachedPool(){
		ExecutorService executor = Executors.newCachedThreadPool();
		CompletionService<Double> cs = new ExecutorCompletionService<Double>(executor);
		List<Future<Double>> list = new ArrayList<>();
		for(int i=0;i<=round;i++){
				termA.termFreq = 1;
				termB.termFreq = 1;
				Future<Double> f = cs.submit(new NgdReverseTfComputingTask(termA,termB));
				list.add(f);
		}
		int errorCount = 0;
		double sum = 0;
		for(Future<Double> task:list){
			try {
				double ngdScore = task.get();
				assertEquals("Should return the same number",this.expecetedNumber,ngdScore,0);
				sum += ngdScore;
			} catch (InterruptedException | ExecutionException e) {
				errorCount++;
				e.printStackTrace();
			}
		}
		assertEquals("Shoud not have any error",0,errorCount);
		System.out.println(sum);
	}
	
	@Test
	public void testOneThread(){
		for(int i=0;i<=round;i++){
			double a = SolrSearcher.getHits("\"Google\"");
			double b = SolrSearcher.getHits("\"Yahoo\"");
			double mValue = SolrSearcher.getHits("+\"google\" +\"yahoo\"");
			double ngdDistance = NGD_calculate.NGD_cal(a, b, mValue);
			double ngdScore = (1 - ngdDistance);
			assertEquals("Should return the same number",this.expecetedNumber,ngdScore,0);
		}
	}

}
