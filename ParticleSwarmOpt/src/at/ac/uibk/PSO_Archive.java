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

	public void insertParticle(Particle toInsert) {
		for (int i = 0; i < particles.size(); i++) {
			Particle p = particles.get(i);
			DominationStatus dom = toInsert.dominateable(p);

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

		particles.add(toInsert);

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
			double[] prev = particles.get(i - 1).getEval();
			double[] next = particles.get(i + 1).getEval();

			double x = next[0] - prev[0];
			double y = prev[1] - next[1];

			double dens = x * y;

			if (dens < maxDens) {
				maxDens = dens;
				toRemove = i;
			}
		}

		particles.remove(toRemove);
	}
}
