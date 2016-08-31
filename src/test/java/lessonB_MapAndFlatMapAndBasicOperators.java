import org.junit.Before;
import org.junit.Test;
import rx.Observable;
import rx.observers.TestSubscriber;
import util.LessonResources.CarnivalFood;
import util.LessonResources.ElevatorPassenger;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static util.LessonResources.____;
import static util.LessonResources._____;

@SuppressWarnings({"unused", "Duplicates"})
public class lessonB_MapAndFlatMapAndBasicOperators {

  private String string1;
  private String string2;
  private String string3;
  private TestSubscriber<String> subscriber;

  @Before
  public void setup() {
    subscriber = new TestSubscriber<>();
  }

  /**
   * The Map function transforms the items emitted by an Observable by applying a function to each, changing the
   * content.
   */
  @SuppressWarnings("SpellCheckingInspection")
  @Test
  public void _1_mapAppliesAFunctionToEachItemAndEmitsDataOnTheOtherSide() {
    Observable.from(Arrays.asList("kewl", "leet", "speak"))
        .map(word -> word.replace("e", "3"))
        .map(word -> word.replace("l", "1"))
        .subscribe(subscriber);

    assertThat(subscriber.getOnNextEvents()).contains(_____);
    assertThat(subscriber.getOnNextEvents()).contains(_____);
    assertThat(subscriber.getOnNextEvents()).contains(_____);
  }

  /**
   * Understanding what flatMap() does is a major awakening on the seeker's path to rx enlightenment.
   * We will use non-lambda syntax here to help illustrate what the return types are in this use case for flatmap.
   * For this experiment, we will be going to the carnival. Because we spent our money unwisely at the carnival
   * ($25 dollars on the Dunk Tank), we are left only with 5$.
   * <p>
   * We still need to eat though. Our goal - check the available food options and get a filtered list of things
   * under 5$.
   */
  @Test
  public void _2_A_flatMapUnwrapsOneLevelOfNestingInAnObservableStream() {
    TestSubscriber<Observable<CarnivalFood>> subscriber = new TestSubscriber<>();

    // The First Food cart's offerings:
    List<CarnivalFood> funnelCakeCart = Arrays.asList(
        new CarnivalFood("Cheese Pizza", 5.95),
        new CarnivalFood("Funnel Cake", 3.95),
        new CarnivalFood("Candied Apple", 1.50),
        new CarnivalFood("Jumbo Corn Dog", 2.25),
        new CarnivalFood("Deluxe Corned Beef Hoagie with Swiss Cheese", 6.75),
        new CarnivalFood("Faygo", 1.95));

    // The Second Food Cart's offerings:
    List<CarnivalFood> chineseFoodCart = Arrays.asList(
        new CarnivalFood("Duck Teriyaki Kabobs", 12.95),
        new CarnivalFood("Vegetable Dumplings", 2.50),
        new CarnivalFood("Poor Quality Shrimp Lo Mein", 4.75),
        new CarnivalFood("Green Tea Ice Cream", 3.95),
        new CarnivalFood("Basic Mandarin Chicken", 5.25));

    // Emit each foodCart list on a stream.
    Observable<List<CarnivalFood>> foodCartItemsObservable = Observable.just(funnelCakeCart, chineseFoodCart);

    // what do you think calling .map() on the foodCartItemsObservable will do?
    Observable<Observable<CarnivalFood>> map = foodCartItemsObservable.map(Observable::from);
    map.subscribe(subscriber);

    assertThat(subscriber.getOnNextEvents()).hasSize(____);
  }

  /**
   * Was the result above what you expected? A bit strange huh? You'd think that you'd get
   * a value matching the number of items of foods in each list at first glance.
   * The reason we get a different result is because of the difference between map(), and flatmap(), which we will
   * see next. map() will always keep the SAME NUMBER OF events/ data as the previous segment in the pipeline. It
   * can never change the number of items on the previous piece of the pipeline.
   * <p>
   * Next, we would like to begin filtering the list to match what we can afford to eat.
   * The problem now is that rather than Observable<Food> items, we are emitting Observable<Observable<Food>>s
   * instead. We can't filter these, because Observable has no price (its content does, but we cant access that).
   * <p>
   * This is where flatMap comes in!
   */
  @Test
  public void _2_B_flatMapUnwrapsOneLevelOfNestingInAnObservableStream() {
    TestSubscriber<CarnivalFood> subscriber = new TestSubscriber<>();

    // The First Food cart's offerings:
    List<CarnivalFood> funnelCakeCart = Arrays.asList(
        new CarnivalFood("Cheese Pizza", 5.95),
        new CarnivalFood("Funnel Cake", 3.95),
        new CarnivalFood("Candied Apple", 1.50),
        new CarnivalFood("Jumbo Corn Dog", 2.25),
        new CarnivalFood("Deluxe Corned Beef Hoagie with Swiss Cheese", 6.75),
        new CarnivalFood("Faygo", 1.95));

    // The Second Food Cart's offerings:
    List<CarnivalFood> chineseFoodCart = Arrays.asList(
        new CarnivalFood("Duck Teriyaki Kabobs", 12.95),
        new CarnivalFood("Vegetable Dumplings", 2.50),
        new CarnivalFood("Poor Quality Shrimp Lo Mein", 4.75),
        new CarnivalFood("Green Tea Ice Cream", 3.95),
        new CarnivalFood("Basic Mandarin Chicken", 5.25));

    // Emit each foodCart list on a stream.
    Observable<List<CarnivalFood>> foodCartItemsObservable = Observable.just(funnelCakeCart, chineseFoodCart);

    /*
      flatMap() transform the items emitted by an Observable into Observables, then flattens the emissions from those
      into a single Observable

      As martin fowler defines flatMap:
      Map a function over a collection and flatten the result by one-level. In this case, we will map a function over
      the list of List<Food>s and then flatten them into one list.
   */
    Observable<CarnivalFood> individualItemsObservable = foodCartItemsObservable.flatMap(Observable::from);
    individualItemsObservable.subscribe(subscriber);

    assertThat(subscriber.getOnNextEvents()).hasSize(____);
  }

  /**
   * Now that the answer to the riddle of flatMap has been revealed to us, we may filter the stream of individual
   * carnival food items and eat what we can afford. to do that we can use the filter() operator.

   * public final Observable<T> filter(Func1<? super T,java.lang.Boolean> predicate)
   * if the predicate returns true, the data/event being evaluated in the predicate is passed on
   */
  @Test
  public void _2_C_flatMapUnwrapsOneLevelOfNestingInAnObservableStream() {
    TestSubscriber<CarnivalFood> subscriber = new TestSubscriber<>();

    // The First Food cart's offerings:
    List<CarnivalFood> funnelCakeCart = Arrays.asList(
        new CarnivalFood("Cheese Pizza", 5.95),
        new CarnivalFood("Funnel Cake", 3.95),
        new CarnivalFood("Candied Apple", 1.50),
        new CarnivalFood("Jumbo Corn Dog", 2.25),
        new CarnivalFood("Deluxe Corned Beef Hoagie with Swiss Cheese", 6.75),
        new CarnivalFood("Faygo", 1.95));

    // The Second Food Cart's offerings:
    List<CarnivalFood> chineseFoodCart = Arrays.asList(
        new CarnivalFood("Duck Teriyaki Kabobs", 12.95),
        new CarnivalFood("Vegetable Dumplings", 2.50),
        new CarnivalFood("Poor Quality Shrimp Lo Mein", 4.75),
        new CarnivalFood("Green Tea Ice Cream", 3.95),
        new CarnivalFood("Basic Mandarin Chicken", 5.25));

    Observable.just(funnelCakeCart, chineseFoodCart)
        .flatMap(Observable::from)
        .filter(food -> food.price < 5.00)
        .subscribe(subscriber);

    assertThat(subscriber.getOnNextEvents()).hasSize(____);
  }

  /**
   * Reduce is helpful for aggregating a set of data and emitting a final result
   */
  @Test
  public void _3_theReduceOperatorAccumulatesValuesAndEmitsTheResult() {

    TestSubscriber<Integer> testSubscriber = new TestSubscriber<>();

    List<ElevatorPassenger> elevatorPassengers = Arrays.asList(
        new ElevatorPassenger("Max", 150),
        new ElevatorPassenger("Mike", 200));

    Observable<ElevatorPassenger> elevatorPassengersObservable = Observable.from(elevatorPassengers);

    // http://reactivex.io/documentation/operators/reduce.html
    elevatorPassengersObservable.reduce(0, (accumulatedWeight, elevatorPassenger) ->
        elevatorPassenger.weightInPounds + accumulatedWeight)
        .subscribe(testSubscriber);

    assertThat(testSubscriber.getOnNextEvents().get(0)).isEqualTo(____);
  }

  /**
   * .repeat() creates an Observable that emits a particular item or sequence of items repeatedly
   */
  @Test
  public void _4_repeatOperatorRepeatsThePreviousOperationANumberOfTimes() {
    String weapon = "A Boomerang made of Pure Gold";
    TestSubscriber<Object> subscriber = new TestSubscriber<>();

    Observable<String> repeatingObservable = Observable.just(weapon).repeat(4);
    repeatingObservable.subscribe(subscriber);
    assertThat(subscriber.getOnNextEvents()).hasSize(____);

    subscriber = new TestSubscriber<>();

    // Challenge - what about this one?? Remember, .repeat() repeats the previous step in the pipeline
    Observable<String> challengeRepeatingObservable = repeatingObservable.repeat(4);
    challengeRepeatingObservable.subscribe(subscriber);

    assertThat(subscriber.getOnNextEvents()).hasSize(____);
  }

  /**
   * A great feature of RxJava is that we can chain actions together to achieve more functionality.
   * In this example we have one Observable and we perform two actions on the data it emits.
   * Lets build two Strings by concatenating some integers.
   */
  @Test
  public void _5_composableFunctions() {
    string1 = "";
    string2 = "";
    string3 = "";

    Observable.range(1, 6)
        .doOnNext(integer -> string1 += integer)
        .doOnNext(integer -> {
          if (integer % 2 == 0) {
            string2 += integer;
          }
        })
        .doOnNext(integer -> string3 += integer)
        .subscribe(integer -> string3 += integer);

    assertThat(string1).isEqualTo(_____);
    assertThat(string2).isEqualTo(_____);
    assertThat(string3).isEqualTo(_____);
  }

  /**
   * Instead of just using events as input to actions (for example summing them), we can transform the events
   * themselves.  We'll use the map() function for this. Lets take some text and map it to all lowercase. The key to
   * making this work is to return the same variable that comes into the action.
   */
  @Test
  public void _6_convertingEvents() {
    Observable.just("wE", "hOpe", "yOU", "aRe", "eNjOyInG", "thIS")
        .map(s -> _____)
        .reduce("", (sentence, word) -> sentence + word)
        .subscribe(subscriber);

    assertThat(subscriber.getOnNextEvents().get(0)).isEqualTo("we hope you are enjoying this ");
  }

}
