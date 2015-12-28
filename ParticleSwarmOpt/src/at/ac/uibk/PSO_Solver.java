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

	/**
	 * This method contains the whole algorithm. A swarm and an archive are
	 * initialized, which are then used to calculate the best solution for our
	 * problem within a certain number of iterations. In order to do so we
	 * update the speed and the position of each particle in the swarm in every
	 * iteration, with the goal of getting better positions. Then the archive is
	 * updated with the updated particles. This goes on until we have reached
	 * the generation limit. Then the archive is returned.
	 * 
	 * 
	 * @param swarmSize
	 *            The size of the swarm
	 * @param archiveSize
	 *            The size of the archive
	 * @param generationLimit
	 *            The maximum number of generations of the swarm
	 * @param problem
	 *            The problem to solve
	 * @return The archive with the best particles found
	 */
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
				computeSpeed(p, selectLeader(p), gen);
				updatePosition(p);
			}
			updateArchive();

		}

		System.out.println(archive.toString());

		return archive.getParticles();
	}

	/**
	 * Every particle of the swarm has a chance to get inserted into the
	 * archive. Only particles, which are not dominated by existing members of
	 * the archive are inserted at last.
	 */
	private void updateArchive() {
		for (int i = 0; i < swarm.size(); i++) {
			archive.insertParticle(swarm.get(i));
		}
	}

	/**
	 * A leader (which is "followed" by other particles) gets selected by one of
	 * two methods (decided randomly).
	 * 
	 * @param p
	 *            The particle which searches a leader.
	 * @return The leader
	 */
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

	/**
	 * The archive is initialized with a certain limit of members. Then every
	 * particle of the swarm has a chance to get inserted into the archive. Only
	 * particles, which are not dominated by existing members of the archive are
	 * inserted at last.
	 * 
	 * @param limit
	 *            The limit
	 */
	private void initializeArchive(int limit) {
		archive = new PSO_Archive(limit);
		for (Particle p : swarm) {
			archive.insertParticle(p);
		}
	}

	/**
	 * The position of a particle is changed by moving it according to its speed.
	 * 
	 * @param p The particle
	 */
	private void updatePosition(Particle p) {
		p.move();
	}

	/**
	 * Now with simulated annealing. First iteration has a 1/8 chance of not
	 * updating the speed, ever decreasing.
	 * 
	 * @param p
	 * @param randomBest
	 * @param generation
	 */
	private void computeSpeed(Particle p, Particle randomBest, int generation) {
		double chance = sr.nextDouble() * 5;
		if (chance >= (1 - generation / MAX_GENERATION))
			p.updateSpeed(randomBest);
	}

	/**
	 * The swarm is initialized with a certain number of random particles.
	 * 
	 * @param size The number of particles in the swarm
	 * @param problem The problem to solve
	 */
	private void initializeSwarm(int size, ZDT problem) {
		swarm = new ArrayList<Particle>();
		for (int i = 0; i < size; i++) {
			Particle p = new Particle(problem);
			swarm.add(p);
		}
	}

}
