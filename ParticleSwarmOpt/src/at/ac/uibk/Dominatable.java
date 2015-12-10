package at.ac.uibk;

public interface Dominatable<T> {

	public DominationStatus dominateable(T compare);
}
