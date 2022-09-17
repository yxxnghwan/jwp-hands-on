package reflection;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

class Junit4TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;

        final Junit4Test junit4Test = clazz.getConstructor().newInstance();

        // TODO Junit4Test에서 @MyTest 애노테이션이 있는 메소드 실행
        Arrays.stream(clazz.getMethods())
                .filter(method -> method.isAnnotationPresent(MyTest.class))
                .forEach(method -> ReflectionTestUtil.executeMethod(junit4Test, method));
    }
}
