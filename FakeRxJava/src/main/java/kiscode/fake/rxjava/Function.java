package kiscode.fake.rxjava;

/**
 * Description:
 * Author: KrisKeno
 * Date : 2021/1/9 9:13 PM
 **/
interface Function<T,R> {
    /**
     * Apply some calculation to the input value and return some other value.
     * @param t the input value
     * @return the output value
     * @throws Exception on error
     */
    R apply(T t) throws Exception;
}
