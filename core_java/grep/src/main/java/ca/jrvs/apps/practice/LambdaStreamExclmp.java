package ca.jrvs.apps.practice;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class LambdaStreamExcImp implements LambdaStreamExc {

    /**
     * Create a String stream from array
     * <p>
     * note: arbitrary number of value will be stored in an array
     *
     * @param strings
     * @return
     */
    @Override
    public Stream<String> createStrStream(String... strings) {
        return Arrays.stream(strings);
    }

    /**
     * Convert all strings to uppercase
     * please use createStrStream
     *
     * @param strings
     * @return
     */
    @Override
    public Stream<String> toUpperCase(String... strings) {
        return createStrStream(strings).map(String::toUpperCase);
    }

    /**
     * filter strings that contains the pattern
     * e.g. filter(stringStream, "a") will return another stream which no element contains a
     *
     * @param stringStream
     * @param pattern
     * @return
     */
    @Override
    public Stream<String> filter(Stream<String> stringStream, String pattern) {
        return stringStream.filter(str -> !str.contains(pattern));
    }

    /**
     * Create a intStream from a arr[]
     *
     * @param arr
     * @return
     */
    @Override
    public IntStream createIntStream(int[] arr) {
        return Arrays.stream(arr);
    }

    /**
     * Convert a stream to list
     *
     * @param stream
     * @return
     */
    @Override
    public <E> List<E> toList(Stream<E> stream) {
        return stream.collect(Collectors.toList());
    }

    /**
     * Convert a intStream to list
     *
     * @param intStream
     * @return
     */
    @Override
    public List<Integer> toList(IntStream intStream) {
        return intStream.boxed().collect(Collectors.toList());
    }

    /**
     * Create a IntStream range from start to end inclusive
     *
     * @param start
     * @param end
     * @return
     */
    @Override
    public IntStream createIntStream(int start, int end) {
        return IntStream.rangeClosed(start, end);
    }

    /**
     * Convert a intStream to a doubleStream and compute square root of each element
     *
     * @param intStream
     * @return
     */
    @Override
    public DoubleStream squareRootIntStream(IntStream intStream) {
        return intStream.asDoubleStream().map(Math::sqrt);
    }

    /**
     * filter all even number and return odd numbers from a intStream
     *
     * @param intStream
     * @return
     */
    @Override
    public IntStream getOdd(IntStream intStream) {
        return intStream.filter(i -> i % 2 == 1);
    }

    /**
     * Return a lambda function that print a message with a prefix and suffix
     * This lambda can be useful to format logs
     * <p>
     * You will learn:
     * - functional interface http://bit.ly/2pTXRwM & http://bit.ly/33onFig
     * - lambda syntax
     * <p>
     * e.g.
     * LambdaStreamExc lse = new LambdaStreamImp();
     * Consumer<String> printer = lse.getLambdaPrinter("start>", "<end");
     * printer.accept("Message body");
     * <p>
     * sout: start>Message body<end
     *
     * @param prefix prefix str
     * @param suffix suffix str
     * @return
     */
    @Override
    public Consumer<String> getLambdaPrinter(String prefix, String suffix) {
        return msg -> System.out.println(prefix + msg + suffix);
    }

    /**
     * Print each message with a given printer
     * Please use `getLambdaPrinter` method
     * <p>
     * e.g.
     * String[] messages = {"a","b", "c"};
     * lse.printMessages(messages, lse.getLambdaPrinter("msg:", "!") );
     * <p>
     * sout:
     * msg:a!
     * msg:b!
     * msg:c!
     *
     * @param messages
     * @param printer
     */
    @Override
    public void printMessages(String[] messages, Consumer<String> printer) {
        Arrays.stream(messages).forEach(printer);
    }

    /**
     * Print all odd number from a intStream.
     * Please use `createIntStream` and `getLambdaPrinter` methods
     * <p>
     * e.g. lse.printOdd(lse.createIntStream(0, 5), lse.getLambdaPrinter("odd number:", "!"));
     * <p>
     * sout:
     * odd number:1!
     * odd number:3!
     * odd number:5!
     *
     * @param intStream
     * @param printer
     */
    @Override
    public void printOdd(IntStream intStream, Consumer<String> printer) {
        getOdd(intStream).forEach(i -> printer.accept(String.valueOf(i)));
    }

    /**
     * Square each number from the input.
     * Please write two solutions and compare difference
     * - using flatMap
     *
     * @param ints
     * @return
     */
    @Override
    public Stream<Integer> flatNestedInt(Stream<List<Integer>> ints) {
        return ints.flatMap(Collection::stream).map(i -> i * i);
    }
}