package org.incendo.metrics;

import com.mongodb.MongoClient;
import java.io.File;
import java.util.Optional;
import lombok.Getter;
import org.mongodb.morphia.Datastore;
import xyz.kvantum.server.api.config.CoreConfig.Application;
import xyz.kvantum.server.api.core.Kvantum;
import xyz.kvantum.server.api.core.ServerImplementation;
import xyz.kvantum.server.api.logging.Logger;
import xyz.kvantum.server.api.util.InstanceFactory;
import xyz.kvantum.server.api.util.RequestManager;
import xyz.kvantum.server.api.views.Routes;
import xyz.kvantum.server.implementation.DefaultLogWrapper;
import xyz.kvantum.server.implementation.ServerContext;
import xyz.kvantum.server.implementation.StandaloneServer;
import xyz.kvantum.server.implementation.mongo.MongoAccountManager;

public class Metrics
{

	private static final Object[] STATIC_ROUTES = new Object[] {};

	@Getter
	private static Metrics instance;

	@Getter private final Kvantum kvantum;
	@Getter private final MongoAccountManager accountManager;
	@Getter private final Datastore datastore;
	@Getter private final MongoClient dbClient;

	private Metrics()
	{
		//
		// Setup the instance reference
		//
		InstanceFactory.setupInstanceAutomagic( this );

		final ServerContext serverContext = ServerContext.builder()
				.coreFolder( new File( "./kvantum" ) )
				.logWrapper( new DefaultLogWrapper() )
				.router( RequestManager.builder().build() )
				.standalone( true )
				.serverSupplier( StandaloneServer::new )
				.build();
		final Optional<Kvantum> kvantumOptional = serverContext.create();

		if ( !kvantumOptional.isPresent() )
		{
			System.err.println( "Failed to start Kvantum..." );
			System.exit( -1 );
			throw new IllegalStateException(); // Just here to make IDEA happy
		}

		Logger.info("Hello World ;)");

		//
		// Enforce MongoDB usage, so we can use Morphia
		//
		if ( !Application.databaseImplementation.equals( "mongo" ) )
		{
			throw new IllegalStateException( "Application must be using MongoDB..." );
		}

		//
		// Testing purposes only
		//
		Routes.get( "", (request, response) -> response.setContent( "Hello World!" ) );

		this.kvantum = ServerImplementation.getImplementation();
		this.accountManager = ( MongoAccountManager ) this.kvantum.getApplicationStructure().getAccountManager();
		this.dbClient = this.accountManager.getApplicationStructure().getMongoClient();
		this.datastore = this.accountManager.getApplicationStructure().getMorphiaDatastore();

		this.kvantum.start();
	}

	public static void main(final String[] args)
	{
		try
		{
			new Metrics();
		} catch ( final Throwable throwable )
		{
			if ( ServerImplementation.getImplementation() != null )
			{
				try
				{
					ServerImplementation.getImplementation().stopServer();
				} catch ( final Throwable ignore ) {}
			}
			System.exit( -1 );
		}
	}
}
