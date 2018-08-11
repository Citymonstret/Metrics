package org.incendo.metrics.application;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import lombok.NonNull;
import org.incendo.metrics.Metrics;
import org.incendo.metrics.MetricsConfig;
import org.mindrot.jbcrypt.BCrypt;
import xyz.kvantum.server.api.repository.KvantumRepository;
import xyz.kvantum.server.api.repository.Matcher;

@SuppressWarnings( "unused" )
public final class ApplicationManager implements KvantumRepository<Application, String>
{

	private static final Pattern CATEGORY_PATTERN = Pattern.compile( "^[a-z0-9_]+$" );

	//
	// Basically, the idea is that application tokens are always loaded
	// (all of them) as they are just simple strings. Not all applications
	// will be active, and thus we lazy load them and store them in a cache.
	//
	// Each category will also contain references to applications within that
	// category, which will be used by the front end. But every service
	// will request categories from the same cache. Applications that are
	// not in the cache will be loaded from the database and will be
	// added to the cache on load. This will be especially useful when
	// loading category listings, etc, using the front end.
	//

	private final Map<String, ApplicationCategory> applicationCategories = new ConcurrentHashMap<>();
	private final Cache<String, Application> applicationCache = Caffeine.newBuilder()
			.maximumSize( MetricsConfig.Cache.cachedEntitiesApplication )
			.build();
	private final Collection<String> applicationTokens = Collections
			.synchronizedList( new ArrayList<>( MetricsConfig.Cache.cachedEntitiesApplication ) );

	@SuppressWarnings( "WeakerAccess" )
	public static boolean validateApplicationCategoryString(@NonNull String string)
	{
		string = string.toLowerCase( Locale.ENGLISH );
		return CATEGORY_PATTERN.matcher( string ).matches();
	}

	private static String generateApplicationToken()
	{
		//
		// May be overkill, but UUIDs are a bit strange
		//
		return BCrypt.hashpw( UUID.randomUUID().toString(), BCrypt.gensalt() );
	}

	public ApplicationCategory getCategory(@NonNull final String name)
	{
		if ( !validateApplicationCategoryString( name ) )
		{
			throw new ApplicationCategoryValidationException( name, "Categories may only contain numbers, lower case letters and underscores" );
		}
		if ( !applicationCategories.containsKey( name ) )
		{
			applicationCategories.put( name, new ApplicationCategory( name.toLowerCase( Locale.ENGLISH ) ) );
		}
		return applicationCategories.get( name );
	}

	public Application createApplication(@NonNull final String name, @NonNull final ApplicationCategory[] categories)
	{
		final Application application = new Application();
		application.setApplicationToken( generateApplicationToken() );
		application.setApplicationName( name );
		application.setApplicationCategories( categories );
		Metrics.getInstance().getDatastore().save( application );
		return this.storeApplicationReference( application );
	}

	private Application storeApplicationReference(@NonNull final Application application)
	{
		applicationCache.put( application.getApplicationToken(), application );
		if ( !applicationTokens.contains( application.getApplicationToken() ) )
		{
			applicationTokens.add( application.getApplicationToken() );
		}
		return application;
	}

	@Nullable public Application getApplication(@NonNull final String token)
	{
		Application application = applicationCache.getIfPresent( token );
		if ( application != null )
		{
			return application;
		}
		application = Metrics.getInstance().getDatastore().createQuery( Application.class )
				.field( "token" ).equal( token ).get();
		if ( application == null )
		{
			return null;
		}
		return this.storeApplicationReference( application );
	}

	public Collection<String> getApplicationTokens()
	{
		return Collections.unmodifiableCollection( this.applicationTokens );
	}

	@Override public ImmutableList<? extends Application> findAll()
	{
		return null;
	}

	@Override public ImmutableList<? extends Application> findAllById(Collection<String> collection)
	{
		return null;
	}

	@Override public ImmutableList<? extends Application> findAllByQuery(Matcher<?, ? super Application> matcher)
	{
		return null;
	}

	@Override public ImmutableCollection<? extends Application> save(Collection<? extends Application> collection)
	{
		return null;
	}

	@Override public void delete(Collection<Application> collection)
	{
	}

	@Override public Optional<? extends Application> findSingle(String s)
	{
		return null;
	}

	@Override public ImmutableCollection<? extends Application> findAll(String s)
	{
		return null;
	}

	public static final class ApplicationCategoryValidationException extends IllegalArgumentException
	{

		private ApplicationCategoryValidationException(@NonNull final String attemptedCategory, @NonNull final String reason)
		{
			super( String.format( "\"%s\" is not a valid application category. Reason: %s", attemptedCategory, reason ) );
		}

	}

}
