package net.bytebuddy.asm;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.test.utility.ObjectPropertyAssertion;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static net.bytebuddy.matcher.ElementMatchers.named;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class MemberRemovalTest {

    private static final String FOO = "foo", BAR = "bar";

    @Test
    public void testFieldRemoval() throws Exception {
        Class<?> type = new ByteBuddy()
                .redefine(Sample.class)
                .visit(new MemberRemoval().stripFields(named(FOO)))
                .make()
                .load(ClassLoadingStrategy.BOOTSTRAP_LOADER, ClassLoadingStrategy.Default.WRAPPER)
                .getLoaded();
        try {
            type.getDeclaredField(FOO);
            fail();
        } catch (NoSuchFieldException ignored) {
        }
        assertThat(type.getDeclaredField(BAR), notNullValue(Field.class));
    }

    @Test
    public void testMethodRemoval() throws Exception {
        Class<?> type = new ByteBuddy()
                .redefine(Sample.class)
                .visit(new MemberRemoval().stripMethods(named(FOO)))
                .make()
                .load(ClassLoadingStrategy.BOOTSTRAP_LOADER, ClassLoadingStrategy.Default.WRAPPER)
                .getLoaded();
        try {
            type.getDeclaredMethod(FOO);
            fail();
        } catch (NoSuchMethodException ignored) {
        }
        assertThat(type.getDeclaredMethod(BAR), notNullValue(Method.class));
    }

    @Test
    public void testObjectProperties() throws Exception {
        ObjectPropertyAssertion.of(MemberRemoval.class).apply();
    }

    private static class Sample {

        Void foo;

        Void bar;

        void foo() {
            /* empty*/
        }

        void bar() {
            /* empty*/
        }
    }
}