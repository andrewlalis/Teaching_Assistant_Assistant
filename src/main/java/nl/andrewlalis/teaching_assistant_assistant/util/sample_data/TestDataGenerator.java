package nl.andrewlalis.teaching_assistant_assistant.util.sample_data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Generates random objects for testing.
 * @param <T> The class of objects this generator produces.
 */
public abstract class TestDataGenerator<T> {

    private Random random;
    private long seed;

    public TestDataGenerator() {
        this.random = new Random(0);
    }

    public TestDataGenerator(long seed) {
        this.seed = seed;
        this.random = new Random(seed);
    }

    public List<T> generateList(int count) {
        List<T> list = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            list.add(this.generate());
        }
        return list;
    }

    public abstract T generate();

    protected <C> C getRandomObjectFromArray(C[] array) {
        return array[this.random.nextInt(array.length)];
    }

    protected int getRandomInteger(int lower, int upper) {
        return this.random.nextInt(upper + lower) - lower;
    }

    protected Random getRandom() {
        return this.random;
    }

    protected long getSeed() {
        return this.seed;
    }

}
