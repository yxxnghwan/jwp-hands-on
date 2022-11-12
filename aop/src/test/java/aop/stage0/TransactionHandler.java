package aop.stage0;

import aop.DataAccessException;
import aop.Transactional;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class TransactionHandler implements InvocationHandler {

    private final PlatformTransactionManager transactionManager;
    private final Object target;

    public TransactionHandler(final PlatformTransactionManager transactionManager, final Object target) {
        this.transactionManager = transactionManager;
        this.target = target;
    }

    /**
     * @Transactional 어노테이션이 존재하는 메서드만 트랜잭션 기능을 적용하도록 만들어보자.
     */
    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        final Method declaredMethod = target.getClass()
                .getDeclaredMethod(method.getName(), method.getParameterTypes());
        if (!declaredMethod.isAnnotationPresent(Transactional.class)) {
            return method.invoke(proxy, args);
        }

        return invokeInTransaction(method, args);
    }

    private Object invokeInTransaction(final Method method, final Object[] args) {
        final TransactionStatus transaction = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            final Object result = method.invoke(target, args);
            transactionManager.commit(transaction);
            return result;
        } catch (IllegalAccessException | RuntimeException | InvocationTargetException e) {
            transactionManager.rollback(transaction);
            throw new DataAccessException(e);
        }
    }
}
