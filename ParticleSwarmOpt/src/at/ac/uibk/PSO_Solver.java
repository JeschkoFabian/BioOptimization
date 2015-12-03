package at.ac.uibk;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class PSO_Solver {

	private int MAX_GENERATION;
	private List<Particle> swarm;
	private PSO_Archive archive;

	SecureRandom sr = new SecureRandom();

	public void solve(int swarmSize, int archiveSize, int generationLimit) {
		
		MAX_GENERATION = generationLimit;
		initializeSwarm(swarmSize);
		initializeArchive(archiveSize);
		System.out.println("Archive: " + archive.toString());
		int generation = 0;
		
		while (generation < MAX_GENERATION) {
			Particle randomBest = selectLeader();
			for (Particle p : swarm) {
				computeSpeed(p, randomBest);
				updatePosition(p);
				evaluate(p);
				updateLocalBest(p);
			}
			updateArchive();
			System.out.println("Archive #"+generation +": " + archive.toString());
			generation++;
		}
	}

	private void updateArchive() {
		for (int i = 0; i < swarm.size(); i++) {
			archive.insertParticle(swarm.get(i));
		}
		System.out.println(archive.getSize());

	}

	private void updateLocalBest(Particle p) {
		p.updateBestValue();

	}

	private Particle selectLeader() {
		return archive.getRandomBest();

	}

	private void evaluate(Particle p) {
		p.eval();

	}

	private void initializeArchive(int limit) {
		archive = new PSO_Archive(limit);
		for (int i = 0; i < swarm.size(); i++) {
			archive.insertParticle(swarm.get(i));
		}
		System.out.println(archive.getSize());
	}

	private void updatePosition(Particle p) {
		p.move();
	}

	private void computeSpeed(Particle p, Particle randomBest) {
		p.updateSpeed(randomBest.getValues());

	}

	private void initializeSwarm(int size) {
		swarm = new ArrayList<Particle>();
		for (int i = 0; i < size; i++) {
			Particle p = new Particle();
			p.eval();
			swarm.add(p);
		}
	}
	
	

}
