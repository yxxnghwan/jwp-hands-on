package reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        final Junit3Test junit3Test = clazz.getConstructor().newInstance();
        Arrays.stream(clazz.getMethods())
                .filter(method -> method.getName().startsWith("test"))
                .forEach(method -> ReflectionTestUtil.executeMethod(junit3Test, method));
    }
}
