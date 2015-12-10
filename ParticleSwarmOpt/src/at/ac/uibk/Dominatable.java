package at.ac.uibk;

public interface Dominatable<T> {

	public DominationStatus compareTo(T compare);
}
