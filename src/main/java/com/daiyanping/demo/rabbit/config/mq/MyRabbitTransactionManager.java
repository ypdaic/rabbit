package com.daiyanping.demo.rabbit.config.mq;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactoryUtils;
import org.springframework.amqp.rabbit.connection.RabbitResourceHolder;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.InvalidIsolationLevelException;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.SmartTransactionObject;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * 只是需要事务功能，而不是正在开启rabbit事务
 */
public class MyRabbitTransactionManager extends RabbitTransactionManager {

    public MyRabbitTransactionManager(ConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) {
        if (definition.getIsolationLevel() != TransactionDefinition.ISOLATION_DEFAULT) {
            throw new InvalidIsolationLevelException("AMQP does not support an isolation level concept");
        }
        RabbitTransactionObject txObject = (RabbitTransactionObject) transaction;
        RabbitResourceHolder resourceHolder = null;
        try {
            resourceHolder = ConnectionFactoryUtils.getTransactionalResourceHolder(getConnectionFactory(), false);
            if (logger.isDebugEnabled()) {
                logger.debug("Created AMQP transaction on channel [" + resourceHolder.getChannel() + "]");
            }
            // resourceHolder.declareTransactional();
            txObject.setResourceHolder(resourceHolder);
            txObject.getResourceHolder().setSynchronizedWithTransaction(true);
            int timeout = determineTimeout(definition);
            if (timeout != TransactionDefinition.TIMEOUT_DEFAULT) {
                txObject.getResourceHolder().setTimeoutInSeconds(timeout);
            }
            TransactionSynchronizationManager.bindResource(getConnectionFactory(), txObject.getResourceHolder());
        }
        catch (AmqpException ex) {
            if (resourceHolder != null) {
                ConnectionFactoryUtils.releaseResources(resourceHolder);
            }
            throw new CannotCreateTransactionException("Could not create AMQP transaction", ex);
        }
    }

    /**
     * Rabbit transaction object, representing a RabbitResourceHolder. Used as transaction object by
     * RabbitTransactionManager.
     * @see RabbitResourceHolder
     */
    private static class RabbitTransactionObject implements SmartTransactionObject {

        private RabbitResourceHolder resourceHolder;

        RabbitTransactionObject() {
            super();
        }

        public void setResourceHolder(RabbitResourceHolder resourceHolder) {
            this.resourceHolder = resourceHolder;
        }

        public RabbitResourceHolder getResourceHolder() {
            return this.resourceHolder;
        }

        @Override
        public boolean isRollbackOnly() {
            return this.resourceHolder.isRollbackOnly();
        }

        @Override
        public void flush() {
            // no-op
        }
    }
}
