package aop.stage1;

import aop.DataAccessException;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * 어드바이스(advice). 부가기능을 담고 있는 클래스
 */
public class TransactionAdvice  implements MethodInterceptor {

    private final PlatformTransactionManager transactionManager;

    public TransactionAdvice(final PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public Object invoke(final MethodInvocation invocation) throws Throwable {
        final TransactionStatus transaction = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            final Object result = invocation.proceed();
            transactionManager.commit(transaction);
            return result;
        } catch (Exception e) {
            transactionManager.rollback(transaction);
            throw new DataAccessException(e);
        }
    }
}
