package apostov.collections;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.math3.util.Combinations;

import com.google.common.collect.ImmutableList;

public class ListCombinationIterable<T> implements Iterable<ImmutableList<T>> {

	private final Combinations apacheCombinations;
	private final List<T> list;

	public ListCombinationIterable(final int n, final int k, final List<T> list) {
		this.apacheCombinations = new Combinations(n, k);
		this.list = list;
	}
	
	public ListCombinationIterable(final int k, final List<T> list) {
		this(list.size(), k, list);
	}

	@Override
	public Iterator<ImmutableList<T>> iterator() {
		final Iterator<int[]> apacheIterator = apacheCombinations.iterator();
		
		return new Iterator<ImmutableList<T>>() {

			@Override
			public boolean hasNext() {
				return apacheIterator.hasNext();
			}

			@Override
			public ImmutableList<T> next() {
				final ImmutableList.Builder<T> builder = ImmutableList.builder();
				for (int index : apacheIterator.next()) {
					builder.add(list.get(index));
				}
				return builder.build();
			}
		};
	}

}
