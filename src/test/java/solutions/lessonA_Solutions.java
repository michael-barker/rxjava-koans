package solutions;

import org.junit.Before;
import org.junit.Test;
import rx.Observable;
import rx.observers.TestSubscriber;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({"unused", "Duplicates"})
public class lessonA_Solutions {

  private TestSubscriber<String> subscriber;
  private TestSubscriber<Integer> intSubscriber;
  private int count1;
  private int count2;
  private int count3;

  @Before
  public void setup() {
    subscriber = new TestSubscriber<>();
    intSubscriber = new TestSubscriber<>();
  }

  /**
   * Observables are ultimately about handling "streams" of items (i.e. more than one item) in a "data pipeline".
   * Each item is called an "event" of "data". Here we have the creation of a new stream of data/events, called an
   * Observable. (http://reactivex.io/RxJava/javadoc/rx/Observable.html)
   * We also have a subscription, which finally takes the values from the pipeline and consumes them.
   * <p>
   * For our RxJava tests, we will be working with an object called TestSubscriber which the framework includes.
   * It gives us an easy way to check what was emitted on the pipeline.
   */
  @Test
  public void _1_anObservableStreamOfEventsAndDataEmitsEachItemInOrder() {
    Observable<String> pipelineOfData = Observable.just("Foo", "Bar");

    pipelineOfData.subscribe(subscriber);
    List<String> dataEmitted = subscriber.getOnNextEvents();

    assertThat(dataEmitted).hasSize(2);
    assertThat(dataEmitted).containsOnlyOnce("Foo");
    assertThat(dataEmitted).containsOnlyOnce("Bar");
  }

  /**
   * An observable stream calls 3 major lifecycle methods as it does it's work:
   * onNext(), onCompleted(), and onError().
   * <p>
   * onNext():
   * An Observable calls this method whenever the Observable emits an item.  This method takes as a parameter the
   * item emitted by the Observable.
   * <p>
   * onError():
   * An Observable calls this method to indicate that it has failed to generate the expected data or has encountered
   * some other error.  This stops the Observable and it will not make further calls to onNext or onCompleted.  The
   * onError method takes as its parameter an indication of what caused the error.
   * <p>
   * onCompleted():
   * An Observable calls this method after it has called onNext for the final time,
   * if it has not encountered any errors.
   */
  @Test
  public void _2_anObservableStreamEmitsThreeMajorEventTypes() {
    Observable<Integer> pipelineOfData = Observable.just(1, 2, 3, 4, 5);

    pipelineOfData
        .doOnNext(integer -> count1++)
        .doOnCompleted(() -> count2++)
        .doOnError(throwable -> count3++)
        .subscribe(intSubscriber);

    intSubscriber.awaitTerminalEvent();

    assertThat(count1).isEqualTo(5);
    assertThat(count2).isEqualTo(1);
    assertThat(count3).isEqualTo(0);
  }


  /**
   * In the test above, we saw Observable.just(), which takes one or several Java objects and converts them into an
   * Observable which emits those objects.
   * (http://reactivex.io/RxJava/javadoc/rx/Observable.html#just(T))
   * Let's build our own this time.
   */
  @Test
  public void _3_justCreatesAnObservableEmittingItsArguments() {
    String stoogeOne = "Larry";
    String stoogeTwo = "Moe";
    String stoogeThree = "Curly";

    Observable<String> stoogeDataObservable = Observable.just(stoogeOne, stoogeTwo, stoogeThree);
    stoogeDataObservable.subscribe(subscriber);
    /*
      As we've seen, the TestSubscriber's getOnNextEvents() method gives a list of all the events emitted by the
      observable stream in a blocking fashion.  This makes it possible for us to test what was emitted by the stream.
      Without the TestSubscriber, the events would have been emitted asynchronously and our assertion would have
      failed.
     */
    List<String> events = subscriber.getOnNextEvents();
    assertThat(events).containsOnlyOnce(stoogeOne);
    assertThat(events).containsOnlyOnce(stoogeTwo);
    assertThat(events).containsOnlyOnce(stoogeThree);
    assertThat(events).hasSize(3);
  }

  /**
   * Observable.from() is another way to create an Observable. It's different than .just() - it is specifically
   * designed to work with Collections. When just is given a collection, it converts it into an Observable that emits
   * each item from the list.
   * <p>
   * Let's understand how the two are different more clearly.
   */
  @Test
  public void _4_fromCreatesAnObservableThatEmitsEachElementFromAnIterable() {
    List<String> sandwichIngredients = Arrays.asList(
        "bread (one)",
        "bread (two)",
        "cheese",
        "mayo",
        "turkey",
        "lettuce",
        "pickles",
        "jalapenos",
        "Sriracha sauce");

    Observable<String> favoriteFoodsObservable = Observable.from(sandwichIngredients);

    TestSubscriber<Object> subscriber = new TestSubscriber<>();
    favoriteFoodsObservable.subscribe(subscriber);

    assertThat(subscriber.getOnNextEvents()).hasSize(9);
    assertThat(subscriber.getOnNextEvents()).containsAll(sandwichIngredients);

    subscriber = new TestSubscriber<>();
    Observable.just(sandwichIngredients).subscribe(subscriber);

    assertThat(subscriber.getOnNextEvents()).hasSize(1);
    assertThat(subscriber.getOnNextEvents()).contains(sandwichIngredients);
    // ^^  As you can see here, from() & just() do very different things!
  }

  /**
   * So far we've created observables and immediately "subscribed" to them. Its only when we subscribe to an
   * observable that it is fully wired up. This observable is now considered "hot". Until then it is "cold"
   * and doesn't really do anything, it won't emit any events.
   * <p>
   * So if we are going to build an observable and not subscribe to it until later on, how can we include the all
   * of the functionality as before? Do we have to put all the work inside subscribe() ? No we don't!
   * <p>
   * If we peek at the Observer interface we see it has three methods:
   * <p>
   * public interface Observer<T> {
   * void onCompleted();
   * void onError(Throwable var1);
   * void onNext(T var1);
   * <p>
   * When we subscribe to an Observable, the code we put inside subscribe() is getting handed off to the Observer's
   * onNext() method.  However, we can manually pass code right to onNext() ourselves with Observable.doOnNext()
   * <p>
   * Lets setup an Observable with all the functionality we need to sum a range of Integers. Then lets subscribe to it
   * later on.
   */
  @Test
  public void _5_nothingListensUntilYouSubscribe() {
    /*
      Observable.range() creates a sequential list from a starting number of a particular size.
      (http://reactivex.io/RxJava/javadoc/rx/Observable.html#range(int,%20int))

      We also haven't seen doOnNext() yet - its one way we can take action based on one of a series of Observable
      lifecycle events.
      http://reactivex.io/documentation/operators/do.html
     */
    Observable
        .range(1, 10)
        .reduce(0, (num, total) -> num + total)
        .subscribe(intSubscriber);
    //Hint: what would we need to do to get our Observable to start emitting things?

    assertThat(intSubscriber.getOnNextEvents()).hasSize(1);
    assertThat(intSubscriber.getOnNextEvents().get(0))
        .isEqualTo(1 + 2 + 3 + 4 + 5 + 6 + 7 + 8 + 9 + 10);
  }

}
