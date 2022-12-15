package eean_games.main;

import java.util.function.Function;

@SuppressWarnings("rawtypes")
public class KeyExtractorAndSortType<T>
{
	public KeyExtractorAndSortType(Function<T, Comparable> _keyExtractor, eSortType _sortType)
	{
		keyExtractor = _keyExtractor;
		sortType = _sortType;
	}
	
	public Function<T, Comparable> keyExtractor;
	public eSortType sortType;
}
