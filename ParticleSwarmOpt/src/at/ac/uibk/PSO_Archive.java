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

	public PSO_Archive(int limit) {
		this.limit = limit;
		particles = new ArrayList<Particle>();
	}

	// add instead get best with farthest neighbors
	public Particle getRandomBest() {
		int rand = sr.nextInt(particles.size());
		return particles.get(rand);
	}

	public Particle getTournamentBest(int k) {
		// can't really use tournament selection when less then k elements are selectable
		if (particles.size() < k + 2){
			return particles.get(sr.nextInt(particles.size()));
		}
		
		Collections.sort(particles, comparator);

		// ignore first and last
		int bestPos = sr.nextInt(particles.size() - 2) + 1;
		double bestDens = calculateDensity(particles.get(bestPos - 1), particles.get(bestPos + 1));

		for (int i = 1; i < k; i++) {
			int randPos = sr.nextInt(particles.size() - 2) + 1;
			double randDens = calculateDensity(particles.get(bestPos - 1), particles.get(bestPos + 1));

			if (randDens > bestDens){
				bestDens = randDens;
				bestPos = randPos;
			}
		}

		return particles.get(bestPos);
	}

	public void insertParticle(Particle toInsert) {
		for (int i = 0; i < particles.size(); i++) {
			Particle p = particles.get(i);
			DominationStatus dom = toInsert.compareTo(p);

			// if new particle is dominated, abort
			if (dom.equals(DominationStatus.DOMINATED)) {
				return;
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
	}

	public String toString() {
		Collections.sort(particles, comparator);

		return particles.toString();
	}

	public int getSize() {
		return particles.size();
	}

	// sort particles by first eval
	private double calculateDensity(Particle prev, Particle next) {
		double dens = 1;

		for (int i = 0; i < prev.getEval().length; i++) {
			dens *= Math.abs((next.getEval()[i] - prev.getEval()[i]));
		}

		return dens;
	}

	// horrible code - needs to be written again
	public void removeParticleWithHighestDensity() {

		// sort by one eval value, since all are pareto optimal (ideally)
		// neighbors are automatically correct
		Collections.sort(particles, comparator);

		// max density on minimal rectangle size ~.~
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
