package at.ac.uibk;

import java.util.ArrayList;
import java.util.List;

public class PSO_Solver {

	private int MAX_GENERATION;
	private List<Particle> swarm;
	private PSO_Archive archive;

	public List<Particle> solve(int swarmSize, int archiveSize, int generationLimit) {

		// init stuff
		MAX_GENERATION = generationLimit;
		initializeSwarm(swarmSize);
		initializeArchive(archiveSize);
		System.out.println("Archive: " + archive.toString());

		for (int gen = 0; gen < MAX_GENERATION; gen++) {

			for (Particle p : swarm) {
				Particle randomBest = selectLeader();
				computeSpeed(p, randomBest);
				updatePosition(p);
			}
			updateArchive();
			System.out.println("Archive (" + (archive.getSize() + 1) + ") #" + (gen + 1) + ": "
					+ archive.toString());
		}
		return archive.getParticles();
	}

	private void updateArchive() {
		for (int i = 0; i < swarm.size(); i++) {
			archive.insertParticle(swarm.get(i));
		}
	}

	private Particle selectLeader() {
		return archive.getRandomBest();
	}

	private void initializeArchive(int limit) {
		archive = new PSO_Archive(limit);
		for (Particle p : swarm) {
			archive.insertParticle(p);
		}
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
			swarm.add(p);
		}
	}

}
