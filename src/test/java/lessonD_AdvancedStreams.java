import org.junit.Before;
import org.junit.Test;
import rx.Observable;
import rx.functions.Func1;
import rx.observables.GroupedObservable;
import rx.observables.MathObservable;
import rx.observers.TestSubscriber;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static util.LessonResources._____;


@SuppressWarnings({"Duplicates", "unused"})
public class lessonD_AdvancedStreams {

  private String evenNumbers = "";
  private String oddNumbers = "";
  private TestSubscriber<String> subscriber;

  @Before
  public void setup() {
    subscriber = new TestSubscriber<>();
  }

  /**
   * So far everything has been pretty linear. Our pipelines all took the form:
   * "do this, then do this, then do this, then end". In reality we can combine pipelines. We can take two streams
   * and turn them into a single stream.
   * <p>
   * Now its worth nothing this is different from what we did when we nested Observables. In that case we always had
   * one stream.
   * <p>
   * Lets take a stream of integers and a stream of strings and join them.
   */
  @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
  @Test
  public void _1_merging() {
    String received = "";

    Observable<String> you = Observable.just("1", "2", "3");
    Observable<String> me = Observable.just("A", "B", "C");

    you.mergeWith(me).subscribe(subscriber);

    assertThat(subscriber.getOnNextEvents()).isEqualTo(asList(_____));
  }

  /**
   * We can also split up a single stream into two streams. We are going to to use the groupBy() action.
   * This action can be a little tricky because it emits an observable of observables. So we need to subscribe to the
   * "parent" observable and each emitted observable.
   * <p>
   * We encourage you to read more from the wiki: http://reactivex.io/documentation/operators/groupby.html
   * <p>
   * Lets split up a single stream of integers into two streams: even and odd numbers.
   */
  @Test
  public void _2_splittingUp() {
    Observable.range(1, 9)
        .groupBy(integer -> _____)
        .subscribe(group -> group.subscribe(integer -> {
          String key = group.getKey();
          if ("even".equals(key)) {
            evenNumbers = evenNumbers + integer;
          } else if ("odd".equals(key)) {
            oddNumbers = oddNumbers + integer;
          }
        }));

    assertThat(evenNumbers).isEqualTo("2468");
    assertThat(oddNumbers).isEqualTo("13579");
  }

  /**
   * Lets take what we know now and do some cool stuff. We've setup an observable and a function for you. Lets combine
   * them together to average some numbers.
   * <p>
   * Also see that we need to subscribe first to the "parent" observable but that the pipeline still cold until we
   * subscribe to each subset observable. Don't forget to do that.
   */
  @Test
  public void _3_challenge_needToSubscribeImmediatelyWhenSplitting() {
    final double[] averages = {0, 0};
    Observable<Integer> numbers = Observable.just(22, 22, 99, 22, 101, 22);
    Func1<Integer, Integer> keySelector = integer -> integer % 2;
    Observable<GroupedObservable<Integer, Integer>> split = numbers.groupBy(keySelector);
    split.subscribe(
        group -> {
          Observable<Double> convertToDouble = group.map(integer -> (double) integer);
          Func1<Double, Double> insertIntoAveragesArray = aDouble -> averages[group.getKey()] = aDouble;
//        MathObservable.averageDouble(________).map(____________).______();
        }
    );

    assertThat(averages[0]).isEqualTo(22.0);
    assertThat(averages[1]).isEqualTo(100.0);
  }
}