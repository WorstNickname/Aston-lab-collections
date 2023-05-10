package ru.aston.map;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CustomHashMapTest {

    @Test
    void putTest() {
        CustomMap<String, Integer> underTest = new CustomHashMap<>();
        underTest.put("Hello", 123);
        assertThat(underTest.size()).isEqualTo(1);
    }

    @Test
    void put_Null_Key() {
        CustomMap<String, Integer> underTest = new CustomHashMap<>();
        underTest.put(null, 456);
        assertThat(underTest.size()).isEqualTo(1);
    }

    @Test
    void put_When_Key_Already_Exists() {
        CustomMap<String, Integer> underTest = new CustomHashMap<>();
        underTest.put(null, 456);
        underTest.put(null, 1337);
        assertThat(underTest.size()).isEqualTo(1);
    }

    @Test
    void put_When_Collision_In_0_Bucket() {
        CustomMap<String, Integer> underTest = new CustomHashMap<>();
        underTest.put("H", 123);
        underTest.put(null, 1337);
        assertThat(underTest.size()).isEqualTo(2);
    }

    @Test
    void getTest() {
        String key = "Hello";
        int value = 123;
        CustomMap<String, Integer> underTest = new CustomHashMap<>();
        underTest.put(key, value);
        var result = underTest.get(key);
        assertThat(result).isEqualTo(value);
    }

    @Test
    void getTest_With_Collisions() {
        String firstKey = "Hello";
        int firstValue = 123;
        int secondValue = 456;

        class TestClass {
            @Override
            public int hashCode() {
                return firstKey.hashCode();
            }
        }

        TestClass secondKey = new TestClass();

        CustomMap<Object, Integer> underTest = new CustomHashMap<>();
        underTest.put(firstKey, firstValue);
        underTest.put(secondKey, secondValue);

        var result = underTest.get(firstKey);
        assertThat(result).isEqualTo(firstValue);
    }

    @Test
    void getTest_When_Key_Does_Not_Exists() {
        CustomMap<Object, Integer> underTest = new CustomHashMap<>();
        var key = underTest.get("key");
        assertThat(key).isNull();
    }

    @Test
    void resizeTest() {
        CustomMap<String, Integer> underTest = new CustomHashMap<>();
        underTest.put("test", 123);
        underTest.put("test", 456);
        underTest.put("test", 789);
        assertThat(underTest);
    }
}