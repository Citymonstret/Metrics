package org.incendo.metrics.application;

import xyz.kvantum.server.api.repository.FieldComparator;
import xyz.kvantum.server.api.repository.Matcher;
import xyz.kvantum.server.api.repository.MatcherFactory;

/**
 * Factory class for {@link FieldComparator}
 */
@SuppressWarnings( "unused" )
public final class ApplicationMatcherFactory<A extends Application, B extends Application> implements MatcherFactory<A, B>
{

	@Override public Matcher<? extends A, ? super B> createMatcher(A a)
	{
		return new FieldComparator<>( a, true, true );
	}
}
