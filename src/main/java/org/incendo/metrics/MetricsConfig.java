package org.incendo.metrics;

import com.intellectualsites.configurable.ConfigurationImplementation;
import com.intellectualsites.configurable.annotations.ConfigSection;
import com.intellectualsites.configurable.annotations.Configuration;

@Configuration(implementation = ConfigurationImplementation.JSON) public class MetricsConfig
{

	@ConfigSection public static class Cache
	{
		public static int cachedEntitiesApplication = 10_000;
	}

}
