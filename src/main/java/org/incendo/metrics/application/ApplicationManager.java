package org.incendo.metrics.application;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import lombok.NonNull;

@SuppressWarnings( "unused" )
public final class ApplicationManager
{

	private static final Pattern CATEGORY_PATTERN = Pattern.compile( "^[a-z0-9_]+$" );

	private final Map<String, ApplicationCategory> applicationCategories = new ConcurrentHashMap<>();

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

	@SuppressWarnings( "WeakerAccess" )
	public static boolean validateApplicationCategoryString(@NonNull String string)
	{
		string = string.toLowerCase( Locale.ENGLISH );
		return CATEGORY_PATTERN.matcher( string ).matches();
	}

	public static final class ApplicationCategoryValidationException extends IllegalArgumentException
	{

		private ApplicationCategoryValidationException(@NonNull final String attemptedCategory, @NonNull final String reason)
		{
			super( String.format( "\"%s\" is not a valid application category. Reason: %s", attemptedCategory, reason ) );
		}

	}

}
