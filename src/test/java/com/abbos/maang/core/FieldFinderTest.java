package com.abbos.maang.core;

import com.abbos.maang.core.reflection.FieldFinder;
import com.abbos.maang.core.reflection.ReflectionException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.junit.jupiter.api.Test;

import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Aliabbos Ashurov
 * @since 2025-06-12
 */
public class FieldFinderTest {

    @Test
    void simple_varHandle() {
        VarHandle vh = FieldFinder.findVarHandle(User.class, "id", Long.class);
        assertNotNull(vh);
        assertEquals(Long.class, vh.varType());

        User user = User.getDefault();
        vh.set(user, 123L);
        assertEquals(123L, vh.get(user), "Field value should be 123L");
    }

    @Test
    void findVarHandle_shouldThrowReflectionException() {
        assertThrows(ReflectionException.class, () -> FieldFinder.findVarHandle(User.class, "atomic", Long.class));
    }

    @Test
    void simple_varHandleWithFilter() {
        VarHandle vh = FieldFinder.findVarHandle(User.class, "username", String.class,
                                                 (f) -> f.isAccessModeSupported(VarHandle.AccessMode.GET));
        assertNotNull(vh);
        assertEquals(String.class, vh.varType());
        User user = User.getDefault();
        vh.set(user, "newUsername");
        assertEquals("newUsername", vh.get(user), "Field value should be 'newUsername'");
    }

    @Test
    void simple_findField() {
        Field field = FieldFinder.findField(User.class, "id");

        assertNotNull(field, "Field should not be null");
        assertEquals("id", field.getName());
        assertEquals(Long.class, field.getType());

        User user = User.getDefault();
        try {
            field.set(user, 123L);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        assertEquals(123L, user.getId(), "Field value should be updated to 123L");
    }

    @Test
    void findField_withFilter() {
        Field field = FieldFinder.findField(User.class, "id", f -> f.getType().equals(Long.class));

        assertNotNull(field, "Field should not be null");
        assertEquals("id", field.getName());
        assertEquals(Long.class, field.getType());

        User user = User.getDefault();
        try {
            field.set(user, 123L);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        assertEquals(123L, user.getId(), "Field value should be updated to 123L");
    }


    @ToString
    @Getter
    @Setter
    private static class BaseDomain {
        private Long id;
        private boolean active;
    }

    @ToString(callSuper = true)
    @Getter
    @Setter
    @NoArgsConstructor
    private static final class User extends BaseDomain {
        private String username;
        private String mail;

        public <T> User(String username, String mail, T t) {
            this.username = username;
            this.mail = mail;
        }

        public static User getDefault() {
            return new <String>User("def-username", "def-mail", "something");
        }
    }
}
