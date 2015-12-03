package at.ac.uibk;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class PSO_Archive {
	private final int SIZE = 100;
	private List<Particle> particles; 
	private SecureRandom sr = new SecureRandom();
	private ZDT1 problem = new ZDT1();
	
	
	public PSO_Archive(){
		particles = new ArrayList<Particle>();
	}
	
	// add instead get best with farthest neighbors
	public Particle getRandomBest(){
		int rand = sr.nextInt(particles.size());
		
		return particles.get(rand);
	}
	
	public void insertParticle(Particle toInsert){
		double[] insertEval = problem.evaluate(toInsert.getValues());
		for (Particle p : particles){
			double[] pEval = problem.evaluate(p.getValues());
			
			if (pEval[0] < insertEval[0] && pEval[1] < insertEval[1]){
				return;
			}
		}
	}
	
}
