package online.thinhtran.demo.proxy;

import online.thinhtran.demo.annotations.Transaction;
import online.thinhtran.demo.utils.DbConnect;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class TransactionProxy implements InvocationHandler {
    private final Object target;

    public TransactionProxy(Object target) {
        this.target = target;
    }

    public static <T> T createProxy(T target) {
        return (T) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new TransactionProxy(target)
        );
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.isAnnotationPresent(Transaction.class)) {
            DbConnect dbConnect = DbConnect.getInstance();
            try {
                dbConnect.getConnection().setAutoCommit(false); // Bắt đầu transaction
                Object result = method.invoke(target, args); // Gọi phương thức thực tế
                dbConnect.getConnection().commit(); // Commit nếu thành công
                return result;
            } catch (Exception e) {
                dbConnect.getConnection().rollback(); // Rollback nếu có lỗi
                throw new RuntimeException("Transaction failed", e);
            } finally {
                dbConnect.getConnection().setAutoCommit(true); // Khôi phục chế độ tự động commit
            }
        }
        return method.invoke(target, args); // Gọi phương thức nếu không có transaction
    }
}
