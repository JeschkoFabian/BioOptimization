package at.ac.uibk;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class PSO_Archive {
	private int limit;
	private List<Particle> particles;
	private SecureRandom sr = new SecureRandom();

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
		for (Particle p : particles) {
			if (p.isDominantTo(toInsert)) {
				return;
			}
		}
		if (particles.size() < limit) {
			particles.add(toInsert);
		} else {
			if (this.removeParticleWithHighestDensity()) {
				particles.add(toInsert);
			}
		}
	}

	public String toString() {
		return particles.toString();
	}

	public int getSize() {
		return particles.size();
	}

	
	//horrible code - needs to be written again
	public boolean removeParticleWithHighestDensity() {
		double[] density = new double[particles.size()];
		for (int i = 0; i < particles.size(); i++) {
			Particle p = particles.get(i);
			double upperMinDist = Double.MAX_VALUE;
			double lowerMinDist = Double.MAX_VALUE;
			Particle upperNeighbor = null;
			Particle lowerNeighbor = null;
			for (Particle p2 : particles) {
				double dist = Math.sqrt((p2.getEval()[0] - p.getEval()[0]) + (p2.getEval()[1] - p.getEval()[1]));
				if (p2.getEval()[0] - p.getEval()[0] > 0 && p2.getEval()[1] - p.getEval()[1] > 0) {
					if (dist < upperMinDist) {
						upperMinDist = dist;
						upperNeighbor = p2;
					}
				}
				if (p2.getEval()[0] - p.getEval()[0] < 0 && p2.getEval()[1] - p.getEval()[1] < 0) {
					if (dist < lowerMinDist) {
						lowerMinDist = dist;
						lowerNeighbor = p2;
					}
				}
			}

			if (upperNeighbor != null && lowerNeighbor != null) {

				density[i] = (upperNeighbor.getEval()[0] - lowerNeighbor.getEval()[0])
						+ (upperNeighbor.getEval()[1] - lowerNeighbor.getEval()[1]);
			} else {
				System.out.println("Hallo");
				density[i] = Double.MAX_VALUE;
			}
		}

		double min = Double.MAX_VALUE;
		int index = -1;
		for (int i = 0; i < density.length; i++) {
			if (density[i] < min) {
				min = density[i];
				index = i;
			}
		}
		if (index == -1) {
			return false;
		} else {
			particles.remove(index);
			System.out.println("Removed element with index " + index);
		}
		return true;
	}

}
