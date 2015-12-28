package at.ac.uibk;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PSO_Archive {
	private final int limit;
	private List<Particle> particles;
	private SecureRandom sr = new SecureRandom();

	// comparator
	private Comparator<Particle> comparator = new Comparator<Particle>() {
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
	};

	/**
	 * Initializes a new archive with a certain limit of particles.
	 * 
	 * @param limit
	 *            The limit
	 */
	public PSO_Archive(int limit) {
		this.limit = limit;
		particles = new ArrayList<Particle>();
	}

	/**
	 * Selects a random particle and returns it.
	 * 
	 * @return The random particle
	 */
	public Particle getRandomBest() {
		int rand = sr.nextInt(particles.size());
		return particles.get(rand);
	}


	/**
	 * Gets the x closest particles to a particle, and selects (and returns) one of them randomly.
	 * 
	 * @param p The initial particle
	 * @param x The number of particles considered
	 * @return A random particle out of x particles.
	 */
	public Particle getProximityBest(final Particle p, int x) {
		Collections.sort(particles, new Comparator<Particle>() {

			@Override
			public int compare(Particle o1, Particle o2) {
				double dist1 = 0;
				double dist2 = 0;

				for (int i = 0; i < p.getEval().length; i++) {
					dist1 += Math.abs(o1.getEval()[i] - p.getEval()[i]);
					dist2 += Math.abs(o2.getEval()[i] - p.getEval()[i]);
				}

				dist1 /= p.getEval().length;
				dist2 /= p.getEval().length;

				if (dist1 < dist2) {
					return -1;
				}
				if (dist2 < dist1) {
					return 1;
				}

				return 0;
			}
		});

		int rand = sr.nextInt(x);
		if (rand >= particles.size()) {
			rand = particles.size() - 1;
		}

		return particles.get(rand);
	}

	/**
	 * Searches the best particle (with lowest density) out of k particles and returns it.
	 * 
	 * @param k The number of participating particles in the tournament.
	 * @return The best particle found
	 */
	public Particle getTournamentBest(int k) {
		// can't really use tournament selection when less then k elements are
		// selectable
		if (particles.size() < k + 2) {
			return particles.get(sr.nextInt(particles.size()));
		}

		Collections.sort(particles, comparator);

		// ignore first and last
		int bestPos = sr.nextInt(particles.size() - 2) + 1;
		double bestDens = calculateDensity(particles.get(bestPos - 1), particles.get(bestPos + 1));

		for (int i = 1; i < k; i++) {
			int randPos = sr.nextInt(particles.size() - 2) + 1;
			double randDens = calculateDensity(particles.get(randPos - 1), particles.get(randPos + 1));

			if (randDens > bestDens) {
				bestDens = randDens;
				bestPos = randPos;
			}
		}

		return particles.get(bestPos);
	}

	/**
	 * Inserts a particle into the archive if it is not dominated by any
	 * existing member (All particles dominated by the new inserted particle are
	 * removed immediately). If the archive is full, then the particle with the
	 * highest density is removed.
	 * 
	 * @param toInsert The particle
	 * @return true if the particle was inserted; false if not.
	 */
	public boolean insertParticle(Particle toInsert) {
		for (int i = 0; i < particles.size(); i++) {
			Particle p = particles.get(i);
			DominationStatus dom = toInsert.compareTo(p);

			// if new particle is dominated, abort
			if (dom.equals(DominationStatus.DOMINATED)) {
				return false;
			}

			// if dominates, remove dominated
			if (dom.equals(DominationStatus.DOMINATES)) {
				particles.remove(i);
				i--;
			}
		}

		particles.add(new Particle(toInsert));

		if (particles.size() >= limit) {
			removeParticleWithHighestDensity();
		}

		return true;
	}

	public String toString() {
		Collections.sort(particles, comparator);

		String ret = String.format("Archive (%3d): " + particles.toString(), (particles.size() + 1));

		return ret;
	}

	public int getSize() {
		return particles.size();
	}

	/**
	 * Calculates the density for a particle with the help of the previous and
	 * the next particle.
	 * 
	 * @param prev
	 *            The previous particle
	 * @param next
	 *            The next particle
	 * @return The density
	 */
	private double calculateDensity(Particle prev, Particle next) {
		double dens = 1;

		for (int i = 0; i < prev.getEval().length; i++) {
			dens *= Math.abs((next.getEval()[i] - prev.getEval()[i]));
		}

		return dens;
	}

	/**
	 * Removes the particle with the highest density
	 */
	public void removeParticleWithHighestDensity() {

		// sort by one eval value, since all are pareto optimal (ideally)
		// neighbors are automatically correct
		Collections.sort(particles, comparator);

		// max density on minimal rectangle size
		double maxDens = Double.MAX_VALUE;
		int toRemove = -1;

		// smallest and biggest are an auto keep
		for (int i = 1; i < particles.size() - 1; i++) {
			Particle prev = particles.get(i - 1);
			Particle next = particles.get(i + 1);

			double dens = calculateDensity(prev, next);

			if (dens < maxDens) {
				maxDens = dens;
				toRemove = i;
			}
		}

		particles.remove(toRemove);
	}

	public List<Particle> getParticles() {
		return this.particles;
	}
}
