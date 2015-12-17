package at.ac.uibk;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PSO_Solver {
	private SecureRandom sr = new SecureRandom();

	private int MAX_GENERATION;
	private List<Particle> swarm;
	private PSO_Archive archive;

	public List<Particle> solve(int swarmSize, int archiveSize, int generationLimit, ZDT problem) {

		// init stuff
		MAX_GENERATION = generationLimit;
		initializeSwarm(swarmSize, problem);
		initializeArchive(archiveSize);

		// String line = "Archive (" + (archive.getSize() + 1) + ") #0: " +
		// archive.toString();
		// System.out.println(line);

		for (int gen = 0; gen < MAX_GENERATION; gen++) {
			Collections.sort(swarm, new Comparator<Particle>() {
				@Override
				public int compare(Particle o1, Particle o2) {
					double[] e1 = o1.getEval();
					double[] e2 = o2.getEval();

					if (e1[0] < e2[0])
						return -1;
					if (e2[0] < e1[0])
						return 1;

					return 0;
				}
			});

			for (Particle p : swarm) {
				Particle randomBest = selectLeader(p);
				computeSpeed(p, randomBest);
				updatePosition(p);
			}
			updateArchive();

			// removed due to bad performance and for mor readability
			// if (!line.endsWith(archive.toString())) {
			// line = "Archive (" + (archive.getSize() + 1) + ") #" + (gen + 1)
			// + ": " + archive.toString();
			// System.out.println(line);
			// }
		}

		String line = "Archive (" + (archive.getSize() + 1) + "): " + archive.toString();
		System.out.println(line);

		return archive.getParticles();
	}

	private void updateArchive() {
		for (int i = 0; i < swarm.size(); i++) {
			archive.insertParticle(swarm.get(i));
		}
	}

	private Particle selectLeader(Particle p) {
		if (sr.nextBoolean()) {
			return archive.getTournamentBest(5);
		} else {
			// funnily this improved the performance, however
			// both as 10 did not work that great
			// return archive.getTournamentBest(10);

			// not really a big upgrade
			return archive.getProximityBest(p, 10);
		}
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
		// if (sr.nextBoolean())
		p.updateSpeed(randomBest);
		// else
		// p.updateSpeed2(randomBest);
	}

	private void initializeSwarm(int size, ZDT problem) {
		swarm = new ArrayList<Particle>();
		for (int i = 0; i < size; i++) {
			Particle p = new Particle(problem);
			swarm.add(p);
		}
	}

}
