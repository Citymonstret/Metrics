package org.incendo.metrics.application;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity
@EqualsAndHashCode
public final class ApplicationCategory
{

	@Id
	@Getter
	@Setter(AccessLevel.PACKAGE)
	private int id;

	@Getter
	@Setter( AccessLevel.PACKAGE )
	private String key;

	private final Collection<String> applications = Collections.synchronizedSet( new HashSet<>() );

	public ApplicationCategory()
	{
	}

	ApplicationCategory(@NonNull final String key)
	{
		this.key = key;
	}

	void addApplicationReference(@NonNull final Application application)
	{
		this.applications.add( application.getApplicationToken() );
	}

	public Collection<String> getApplications()
	{
		return Collections.unmodifiableCollection( this.applications );
	}
}
