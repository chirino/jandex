package org.jboss.jandex.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationInstanceEquivalenceProxy;
import org.jboss.jandex.Index;
import org.jboss.jandex.test.util.IndexingUtil;
import org.junit.jupiter.api.Test;

public class AnnotationInstanceTest {
    @MyAnnotation("foo")
    static class Foo {
    }

    @MyAnnotation("foo")
    static class Foo2 {
    }

    @MyAnnotation("bar")
    static class Bar {
    }

    @Test
    public void equalityEquivalence() throws IOException {
        Index index = Index.of(Foo.class, Foo2.class, Bar.class);
        testEqualityEquivalence(index);
        testEqualityEquivalence(
                IndexingUtil.roundtrip(index, "e32820a690a531dae840621cd977e3b1c9f2edb116b8eefd93963612ab42e4e4"));
    }

    private void testEqualityEquivalence(Index index) {
        AnnotationInstance foo = index.getClassByName(Foo.class).declaredAnnotation(MyAnnotation.DOT_NAME);
        AnnotationInstance foo2 = index.getClassByName(Foo2.class).declaredAnnotation(MyAnnotation.DOT_NAME);
        AnnotationInstance bar = index.getClassByName(Bar.class).declaredAnnotation(MyAnnotation.DOT_NAME);

        assertNotNull(foo);
        assertNotNull(foo2);
        assertNotNull(bar);

        assertNotEquals(foo, foo2);
        assertNotEquals(foo, bar);
        assertNotEquals(foo2, bar);

        assertTrue(foo.equivalentTo(foo2));
        assertFalse(foo.equivalentTo(bar));
        assertFalse(foo2.equivalentTo(bar));

        assertEquals(foo.equivalenceHashCode(), foo2.equivalenceHashCode());

        AnnotationInstanceEquivalenceProxy fooEquiv = foo.createEquivalenceProxy();
        AnnotationInstanceEquivalenceProxy foo2Equiv = foo2.createEquivalenceProxy();
        AnnotationInstanceEquivalenceProxy barEquiv = bar.createEquivalenceProxy();

        assertNotNull(fooEquiv);
        assertNotNull(foo2Equiv);
        assertNotNull(barEquiv);

        assertEquals(fooEquiv, foo2Equiv);
        assertNotEquals(fooEquiv, barEquiv);
        assertNotEquals(foo2Equiv, barEquiv);

        assertEquals(fooEquiv.hashCode(), foo2Equiv.hashCode());
    }
}
