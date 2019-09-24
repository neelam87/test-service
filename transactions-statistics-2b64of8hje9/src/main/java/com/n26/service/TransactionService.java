package com.n26.service;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.n26.config.Config;
import com.n26.exception.OutdatedTransactionException;
import com.n26.model.Transaction;
import com.n26.model.TransactionStatistics;
import com.n26.time.Time;

@Singleton
@Service
public class TransactionService
{
	private final Time time;

	private final TransactionStatistics[] transactionStatistics;
	private final Object[] locks;
	private final TransactionStatistics cachedTransactionStatistics;
	private final Object cachedTransactionStatisticsLock;

	@Autowired
	public TransactionService(final Time time) {
		this.time = time;
		transactionStatistics = new TransactionStatistics[Config.MILISECONDS_TO_KEEP_TRANSACTIONS / Config.MILISECONDS_TO_PACK_IN_ONE_LUMP];
		locks = new Object[transactionStatistics.length];
		cachedTransactionStatistics = TransactionStatistics.builder().time(time).build();
		cachedTransactionStatisticsLock = new Object();
		initializeLocks();
	}

	private void initializeLocks()
	{
		for (int i = 0; i < locks.length; i++) {
			locks[i] = new Object();
		}
	}

	public void addTransaction(final Transaction transaction) throws OutdatedTransactionException {
		assertValidTransaction(transaction);
		final int index = getTransactionStatisticsIndexForTimestamp(transaction.getTimestampOffset());
		synchronized (locks[index])
		{
			transactionStatistics[index] = new TransactionStatistics(time, transaction, transactionStatistics[index]);
		}
	}
	
	private int getTransactionStatisticsIndexForTimestamp(final long timestamp)
	{
		return (int) ((timestamp / Config.MILISECONDS_TO_PACK_IN_ONE_LUMP) % transactionStatistics.length);
	}

	private void assertValidTransaction(final Transaction transaction) throws OutdatedTransactionException
	{
		if (!time.isValidTransactionTimestamp(transaction.getTimestampOffset())) {
			throw new OutdatedTransactionException();
		}
	}

	public TransactionStatistics getTransactionStatistics() {
		final long timestamp = time.getCurrentTimestamp();
		if (cachedTransactionStatistics.getTimestamp() < timestamp) {
			synchronized (cachedTransactionStatisticsLock) {
				if (cachedTransactionStatistics.getTimestamp() < timestamp) {
					cachedTransactionStatistics.reset();
					for (int i = 0; i < transactionStatistics.length; i++) {
						cachedTransactionStatistics.mergeTransactionStatistics(transactionStatistics[i]);
					}
					cachedTransactionStatistics.setTimestamp(timestamp);
				}
			}
		}
		return cachedTransactionStatistics;
	}
}

