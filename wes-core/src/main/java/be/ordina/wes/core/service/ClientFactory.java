package be.ordina.wes.core.service;

import org.elasticsearch.client.Client;

/**
 * A client factory is used to construct an Elasticsearch Client instance. A client provides a one stop interface for performing actions/operations against the cluster.
 */
public interface ClientFactory {

	/**
	 * Returns the Elasticsearch Client instance
	 * @return Client instance
	 */
	Client getInstance();
	
	/**
	 * Destroys Client instance
	 */
	void destroyInstance();

}