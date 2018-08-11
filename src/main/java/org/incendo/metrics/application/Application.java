package org.incendo.metrics.application;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.incendo.metrics.metric.Metric;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@SuppressWarnings( { "WeakerAccess", "unused" } )@Entity public class Application
{

	/**
	 * Internal application ID
	 */
	@Id @Getter @Setter( AccessLevel.PACKAGE ) private int applicationId;

	/**
	 * Application display name
	 */
	@Getter @Setter( AccessLevel.PACKAGE ) private String applicationName;

	/**
	 * Public application token
	 */
	@Getter @Setter( AccessLevel.PACKAGE ) private String applicationToken;

	/**
	 * Public application categories
	 */
	@Getter private ApplicationCategory[] applicationCategories;

	private Collection<Metric> applicationSpecificMetrics = Collections.synchronizedList( new ArrayList<>() );

	public Collection<Metric> getApplicationSpecificMetrics()
	{
		return Collections.unmodifiableCollection( this.applicationSpecificMetrics );
	}

	void setApplicationCategories(@NonNull final ApplicationCategory[] applicationCategories)
	{
		this.applicationCategories = applicationCategories;
		for ( final ApplicationCategory category : applicationCategories )
		{
			category.addApplicationReference( this );
		}
	}
}
